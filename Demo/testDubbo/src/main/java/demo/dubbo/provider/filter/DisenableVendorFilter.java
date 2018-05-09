package demo.dubbo.provider.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.loanking.galaxy.domain.base.BaseResponse;

/**
 * 供应商服务不可用时，通过APOLLO的热发布功能，配置相应的接口，使得该接口返回默认值
 * 
 * @author hanjy
 *
 */
@Activate(group = Constants.PROVIDER)
public class DisenableVendorFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(DisenableVendorFilter.class);

    private String config;

    // dubbo通过setter方式自动注入
    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        logger.info("调用dubbo供应商拦截器开始执行......");
        Set<String> set = disenbaledMothods();
        String methodName = invocation.getMethodName();
        Class<?> invokerInterface = invoker.getInterface();
        String tmp = String.format("%s.%s", invokerInterface.getName(), methodName);
        logger.info("interface :{}", tmp);
        if (set != null && set.contains(tmp)) {
            BaseResponse<?> baseResponse = new BaseResponse<>();
            baseResponse.setCode(BaseResponse.ERROR);
            baseResponse.setMessage("该供应商暂不可用");
            RpcResult result = new RpcResult();
            result.setValue(baseResponse);
            return result;
        } else {
            return invoker.invoke(invocation);
        }
    }

    /**
     * 
     * @return
     */
    public Set<String> disenbaledMothods() {
        String disenbaledMothods = config;
        logger.info("disenbaledMothods :{}", disenbaledMothods);
        if (disenbaledMothods == null || disenbaledMothods.isEmpty()) {
            return Collections.emptySet();
        } else {
            return new HashSet<>(Arrays.asList(disenbaledMothods.split(",")));
        }
    }

}