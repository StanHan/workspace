package com.daikuan.platform.ma.task;

import java.time.Clock;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.daikuan.platform.ma.dao.AUserProductAuditStatusMapper;
import com.daikuan.platform.ma.redis.RedisUtil;
import com.daikuan.platform.ma.util.Constants;
import com.daikuan.platform.ma.util.RedisKey;

public class InitTask implements Runnable {

    private static Logger log = LoggerFactory.getLogger(InitTask.class);

    private AUserProductAuditStatusMapper userProductAuditStatusMapper;

    private Clock clock = Clock.systemUTC();

    private RedisUtil redisUtil;

    public InitTask(AUserProductAuditStatusMapper userProductAuditStatusMapper, RedisUtil redisUtil) {
        this.userProductAuditStatusMapper = userProductAuditStatusMapper;
        this.redisUtil = redisUtil;
    }

    private int defaultSize = 500;// 混合集合的默认大小

    @Override
    public void run() {
        long start = clock.millis();
        StringRedisTemplate stringRedisTemplate = redisUtil.getStringRedisTemplate();
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();

        // 读取REDIS比例配置并解析，
        String tmp = valueOps.get(RedisKey.KEY_PRODUCT_PROPORTION);
        if (tmp == null || tmp.isEmpty()) {
            parseProportion(Constants.DEFAULT_PRODUCTS_PROPORTION);
            log.info("默认分配比例：" + Constants.DEFAULT_PRODUCTS_PROPORTION);
        } else {
            log.info("REDIS分配比例：" + tmp);
            boolean isSucs = parseProportion(tmp);
            if (!isSucs) {
                parseProportion(Constants.DEFAULT_PRODUCTS_PROPORTION);
            }
        }

        SetOperations<String, String> setOp = stringRedisTemplate.opsForSet();
        Long mixedSize = setOp.size(RedisKey.SET_MIXED);// 按比例混合
        // 检查上银、中银、小贷现有数量
        for (int productType : products) {
            Long size = setOp.size(RedisKey.SET_PRODUCT_ + productType);
            log.info("the size of {} is {},", RedisKey.SET_PRODUCT_ + productType, size);
            if (size < defaultSize) {
                // 如果现有数量小于一定值则从数据库拉数据填充进去
                List<Long> list = userProductAuditStatusMapper.selectCertainId(Constants.AUDIT_UNDECIDED, productType,
                        0, 10 * defaultSize);
                if (list != null && list.size() > 0) {
                    String[] array = list.stream().map(String::valueOf).toArray(String[]::new);
                    Long addCount = setOp.add(RedisKey.SET_PRODUCT_ + productType, array);
                    log.info("add {} records to {}", addCount, (RedisKey.SET_PRODUCT_ + productType));
                    Set<String> set = setOp.members(RedisKey.SET_PRODUCT_ + productType);
                    log.info("now the members of {} is :"+set,RedisKey.SET_PRODUCT_ + productType);
                }
            }
        }

        // 按照比例将一定量的数据移动到混合集合中
        if (mixedSize < defaultSize/5) {
            long empty = defaultSize - mixedSize;

            long denominator = Arrays.stream(proportion).sum();
            if (denominator <= 0) {
                log.error("分流比例参数错误：" + proportion);
                return;
            }
            for (int i = 0; i < proportion.length; i++) {
                long need = proportion[0] * empty / denominator;
                if (need > 0) {
                    long count = redisUtil.move(RedisKey.SET_PRODUCT_ + products[i], need, RedisKey.SET_MIXED);
                    log.info("move {} records from {} to mix set.", count, (RedisKey.SET_PRODUCT_ + products[i]));
                }
            }
        }
        Set<String> set = setOp.members(RedisKey.SET_MIXED);
        log.info("mix set members:" + set);
        log.info("InitTask take {} millisecond", (clock.millis() - start));
    }

    /**
     * 解析产品比例
     * 
     * @param proportion
     * @return
     */
    public boolean parseProportion(String productsProportion) {
        if (productsProportion == null || productsProportion.isEmpty()) {
            return false;
        }
        String[] tmp = productsProportion.split("=");
        if (tmp.length != 2) {
            return false;
        }
        String[] productArray = tmp[0].split(":");
        String[] proportionArray = tmp[1].split(":");
        if (productArray.length == proportionArray.length) {
            try {
                // 解析产品
                products = new int[productArray.length];
                for (int i = 0; i < productArray.length; i++) {
                    products[i] = Integer.valueOf(productArray[i]);
                }
                // 解析比例
                proportion = new long[proportionArray.length];
                for (int i = 0; i < proportionArray.length; i++) {
                    proportion[i] = Long.valueOf(proportionArray[i]);
                }
                return true;
            } catch (Exception e) {
                log.error("解析产品比例 {} 失败.", productsProportion);
                return false;
            }
        } else {
            return false;
        }
    }

    private long[] proportion;// 按比例分配（上银：中银：小贷）
    private int[] products;// （上银1,中银5,小贷6）
}
