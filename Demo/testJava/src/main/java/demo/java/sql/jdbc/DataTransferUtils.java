package demo.java.sql.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import demo.java.sql.ResultSetUtils;

/**
 * 数据迁移工具类
 * 
 * @author hanjy
 *
 */
@Component
public class DataTransferUtils {

    static Clock clock = Clock.systemDefaultZone();

    private static Logger log = LoggerFactory.getLogger(DataTransferUtils.class);

    private DataSource srcDataSource;

    private DataSource tarDataSource;

    public DataTransferUtils(DataSource srcDataSource, DataSource tarDataSource) {
        this.srcDataSource = srcDataSource;
        this.tarDataSource = tarDataSource;
    }

    public void transferByMultiThread(String selectSql, String insertSql) {

    }

    /**
     * 全量迁移。限制条件：select出来的字段应与insert的参数字段对应
     * 
     * @param srcDataSource
     *            源数据源
     * @param tarDataSource
     *            目标数据源
     * @param selectSql
     *            SELECT a,b,c FROM TABLE WHERE * limit ?,?
     * @param insertSql
     *            INSERT INTO TABLE(a,b,c) VALUES (?,?,?);
     */
    public void transferAll(String selectSql, String insertSql) {
        try (Connection srcConnection = srcDataSource.getConnection();
                PreparedStatement srcStatement = srcConnection.prepareStatement(selectSql);) {

            int offset = 0;
            int batch = 5_0000;
            List<Object[]> list = null;
            do {
                srcStatement.setInt(1, offset);
                srcStatement.setInt(2, batch);
                log.info("SELECT * FROM s_user_campaign_url limit {},{}", offset, batch);
                long start = clock.millis();
                ResultSet srcRS = srcStatement.executeQuery();
                long end1 = clock.millis();
                log.info("executeQuery 耗时(ms): " + (end1 - start));
                list = ResultSetUtils.buildResultSet(srcRS, batch);
                long end2 = clock.millis();
                log.info("ResultSet 转  List 耗时(ms): " + (end2 - end1));
                srcRS.close();
                offset += batch;
                batchInsert(insertSql, list);
                long end3 = clock.millis();
                log.info("batchInsert 耗时(ms): " + (end3 - end2));
            } while (list.size() == batch);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 全量迁移。限制条件：select出来的字段应与insert的参数字段对应
     * 
     * @param srcDataSource
     *            源数据源
     * @param tarDataSource
     *            目标数据源
     * @param selectSql
     *            SELECT a,b,c FROM TABLE WHERE id >? and id<=? order by id
     * @param insertSql
     *            INSERT INTO TABLE(a,b,c) VALUES (?,?,?);
     */
    public void transferAll(String selectSql, String insertSql,final int batch) {
        try (Connection srcConnection = srcDataSource.getConnection();
                PreparedStatement srcStatement = srcConnection.prepareStatement(selectSql);) {

            String sqlLog = selectSql.replaceAll("\\?", "{}");

            int startId = 0;
            List<Object[]> list = null;
            do {
                srcStatement.setInt(1, startId);
                srcStatement.setInt(2, batch);
                log.info(sqlLog, startId, batch);
                long start = clock.millis();
                ResultSet srcRS = srcStatement.executeQuery();
                long end1 = clock.millis();
                log.info("executeQuery 耗时(ms): " + (end1 - start));
                list = ResultSetUtils.buildResultSet(srcRS, batch);
                long end2 = clock.millis();
                log.info("ResultSet 转  List 耗时(ms): " + (end2 - end1));
                srcRS.close();
                startId += batch;
                batchInsert(insertSql, list);
                long end3 = clock.millis();
                log.info("batchInsert 耗时(ms): " + (end3 - end2));
            } while (list.size() == batch);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public static void download(DataSource srcDataSource,String selectSql,String path,final int batch) {
        try (Connection srcConnection = srcDataSource.getConnection();
                PreparedStatement srcStatement = srcConnection.prepareStatement(selectSql);) {

            String sqlLog = selectSql.replaceAll("\\?", "{}");

            int startId = 0;
            List<Object[]> list = null;
            do {
                srcStatement.setInt(1, startId);
                srcStatement.setInt(2, batch);
                log.info(sqlLog, startId, batch);
                long t1 = clock.millis();
                ResultSet srcRS = srcStatement.executeQuery();
                long t2 = clock.millis();
                log.info("executeQuery 耗时(ms): " + (t2 - t1));
                list = ResultSetUtils.buildResultSet(srcRS, batch);
                long end2 = clock.millis();
                log.info("ResultSet 转  List 耗时(ms): " + (end2 - t2));
                srcRS.close();
                startId += batch;
//                batchInsert(insertSql, list);
                long end3 = clock.millis();
                log.info("batchInsert 耗时(ms): " + (end3 - end2));
            } while (list.size() == batch);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 批量插入
     * 
     * @param tarDataSource
     *            目标数据源
     * @param tarInsert
     *            INSERT INTO TABLE(a,b,c) VALUES (?,?,?);
     * @param list
     *            需要插入的数据
     * @return
     */
    public int batchInsert(String tarInsert, List<Object[]> list) {
        int counter = 0;
        try (Connection connection = tarDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(tarInsert);) {
            log.info("list.size = " + list.size());
            connection.setAutoCommit(false);
            for (Object[] array : list) {
                for (int i = 0; i < array.length; i++) {
                    statement.setObject(i + 1, array[i]);
                }
                statement.addBatch();
                counter++;
                if (counter % 5000 == 0) {
                    statement.executeBatch();
                    connection.commit();
                }
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return counter;
    }

    /**
     * 清表
     * 
     * @param tableName
     */
    public static void truncateTable(DataSource dataSource, String tableName) {
        String sql = " TRUNCATE TABLE " + tableName;
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
            log.info(sql);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 全量迁移数据，适用于10W条之内的数据，耗时20秒左右
     * 
     * @param sourceSelect
     *            源表的查询语句
     * @param targetSelect
     *            目标表的查询语句
     * @param map
     *            原表和目标表的字段对应关系
     */
    public void insertBySelectSQL(String sourceSelect, String targetSelect, Map<String, String> map) {
        log.info("source sql:" + sourceSelect);
        log.info("target sql:" + targetSelect);
        log.info("字段对应表:" + map);
        try (Connection connect_xyj = srcDataSource.getConnection();
                Connection connect_cp = tarDataSource.getConnection();) {
            connect_xyj.setReadOnly(true);
            connect_cp.setAutoCommit(false);

            Statement statement_XYJ = connect_xyj.createStatement();
            ResultSet sourceRS = statement_XYJ.executeQuery(sourceSelect);

            Statement statement_CP = connect_cp.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE,
                    ResultSet.HOLD_CURSORS_OVER_COMMIT);
            ResultSet targetRS = statement_CP.executeQuery(targetSelect);
            int count = 0;
            while (sourceRS.next()) {
                count++;
                if (count == 1000) {
                    connect_cp.commit();
                    count = 0;
                }
                targetRS.moveToInsertRow();
                map.forEach((k, v) -> {
                    try {
                        Object o = sourceRS.getObject(k);
                        targetRS.updateObject(v, o);
                    } catch (SQLException e) {
                        log.error(e.getMessage(), e);
                    }
                });
                targetRS.insertRow();
            }
            connect_cp.commit();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 全量的数据校验
     * <p>
     * <li>如果数据为空，返回false。
     * <li>如果数据总数不一致返回false。
     * <li>逐个比较非忽略字段，如果不一致则返回false。
     * 
     * @param sourceSelect
     *            源表的查询语句
     * @param targetSelect
     *            目标表的查询语句
     * @param map
     *            原表和目标表的字段对应关系
     * @param set
     *            不做比较的源表字段
     */
    public boolean verifyAllData(String sourceSelect, String targetSelect, Map<String, String> map, Set<String> set) {
        log.info("source sql:" + sourceSelect);
        log.info("target sql:" + targetSelect);
        log.info("字段对应表:" + map);
        log.info("忽略字段:" + set);
        try (Connection connect_xyj = srcDataSource.getConnection();
                Connection connect_cp = tarDataSource.getConnection();) {
            connect_xyj.setReadOnly(true);
            Statement statement_XYJ = connect_xyj.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet sourceRS = statement_XYJ.executeQuery(sourceSelect);

            Statement statement_CP = connect_cp.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet targetRS = statement_CP.executeQuery(targetSelect);
            int sourceCount = 0;// 原表 总数
            if (sourceRS.last()) {
                sourceCount = sourceRS.getRow();
                log.info("the count of source is:" + sourceCount);
            } else {
                log.warn("there are no rows in the source result set");
                return false;
            }
            int targetCount = 0;
            if (targetRS.last()) {
                targetCount = targetRS.getRow();
                log.info("the count of target is:" + targetCount);

            } else {
                log.warn("there are no rows in the target result set");
                return false;
            }
            if (sourceCount != targetCount) {// 总数不等
                log.warn("sourceCount != targetCount");
                return false;
            }
            sourceRS.beforeFirst();
            targetRS.beforeFirst();

            while (sourceRS.next() && targetRS.next()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (!set.contains(entry.getKey())) {
                        try {
                            Object sourceValue = sourceRS.getObject(entry.getKey());
                            Object targetValue = targetRS.getObject(entry.getValue());
                            if (sourceValue == null && targetValue == null) {// 空值不做比较
                                continue;
                            }
                            // 考虑到字段类型不同，统一转成字符串来比较
                            if (!String.valueOf(sourceValue).equals(String.valueOf(targetValue))) {
                                log.warn("source {} = {},target {} = {}", entry.getKey(), sourceValue, entry.getValue(),
                                        targetValue);
                                return false;
                            }
                        } catch (SQLException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 随机的数据校验
     * <li>随机一个起始位置，检查1000条数据是否一致
     * 
     * @param tarCountSql
     *            目标表总数查询
     * @param sourceSelect
     *            源表的查询语句
     * @param targetSelect
     *            目标表的查询语句
     * @param map
     *            目标表和源表的字段对应关系,K目标表字段，V源表字段
     * @param set
     *            不做比较的目标表字段
     */
    public boolean verifyDataByRandom(String tarCountSql, String sourceSelect, String targetSelect,
            Map<String, String> map, Set<String> set) {
        log.info("source sql:" + sourceSelect);
        log.info("target sql:" + targetSelect);
        log.info("目标表和源表的字段对应关系:" + map);
        log.info("忽略字段:" + set);
        boolean result = true;
        try (Connection srcConnection = srcDataSource.getConnection();
                Connection tarConnection = tarDataSource.getConnection();
                Statement srcStatement = srcConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                Statement tarStatement = tarConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);) {

            ResultSet tarCountRS = tarStatement.executeQuery(tarCountSql);
            int tarCount = 0;
            if (tarCountRS.next()) {
                tarCount = tarCountRS.getInt(1);
                log.info("target table count sql {} return {}", tarCountSql, tarCount);
            }
            tarCountRS.close();

            Random random = new Random();
            int offset = random.nextInt(tarCount);
            int rows = 100;

            PreparedStatement srcPStatement = srcConnection.prepareStatement(sourceSelect);
            srcPStatement.setInt(1, offset);
            srcPStatement.setInt(2, rows);
            ResultSet sourceRS = srcPStatement.executeQuery();
            Set<String> valueSet = map.values().stream().collect(Collectors.toSet());
            List<Map<String, Object>> srcList = ResultSetUtils.buildResultSet(sourceRS, valueSet);
            sourceRS.close();

            PreparedStatement tarPStatement = tarConnection.prepareStatement(targetSelect);
            tarPStatement.setInt(1, offset);
            tarPStatement.setInt(2, rows);
            ResultSet targetRS = tarPStatement.executeQuery();
            List<Map<String, Object>> tarList = ResultSetUtils.buildResultSet(targetRS, map.keySet());
            targetRS.close();

            for (int i = 0; i < tarList.size() && i < srcList.size(); i++) {
                Map<String, Object> tarMap = tarList.get(i);
                Map<String, Object> srcMap = srcList.get(i);
                Object srcID = srcMap.get("id");
                StringBuilder sb = new StringBuilder("verify id ").append(srcID).append(":");
                boolean isSame = true;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (!set.contains(entry.getKey())) {// 如果不是忽略字段，则比较
                        Object srcValue = srcMap.get(entry.getValue());
                        Object tarValue = tarMap.get(entry.getKey());
                        if (srcValue == null && tarValue == null) {// 空值不做比较
                            continue;
                        }

                        // 考虑到字段类型不同，统一转成字符串来比较
                        if (!String.valueOf(srcValue).equals(String.valueOf(tarValue))) {
                            sb.append("src.").append(entry.getValue()).append("=").append(srcValue).append(",");
                            sb.append("tar.").append(entry.getKey()).append("=").append(tarValue).append(";");
                            isSame = false;
                        }
                    }
                }
                if (!isSame) {
                    result = false;
                    log.warn(sb.toString());
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 总数校验
     * 
     * @param srcCountSql
     * @param tarCountSql
     * @return
     */
    public boolean verifyCount(String srcCountSql, String tarCountSql) {
        boolean result = true;
        try (Connection srcConnection = srcDataSource.getConnection();
                Connection tarConnection = tarDataSource.getConnection();
                Statement srcStatement = srcConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                Statement tarStatement = tarConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);) {

            ResultSet srcCountRS = srcStatement.executeQuery(srcCountSql);
            ResultSet tarCountRS = tarStatement.executeQuery(tarCountSql);
            int srcCount = 0;
            if (srcCountRS.next()) {
                srcCount = srcCountRS.getInt(1);
                log.info("source: {} = {}", srcCountSql, srcCount);
                srcCountRS.close();
            }

            int tarCount = 0;
            if (tarCountRS.next()) {
                tarCount = tarCountRS.getInt(1);
                log.info("target: {} count = {}", tarCountSql, tarCount);
                tarCountRS.close();
            }

            if (srcCount != tarCount) {
                log.warn("source sql {} return {}, target sql {} return {}.", srcCountSql, srcCount, tarCountSql,
                        tarCount);
                result = false;
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 全量的数据修复并打印修复记录
     * <p>
     * <ol>
     * <li>如果数据为空，停止修复。
     * <li>如果数据总数不一致返回，停止修复。
     * <li>逐个比较非忽略字段，如果不一致则修复目标表数据，并打印。
     * </ol>
     * 
     * @param sourceSelect
     *            源表的查询语句
     * @param targetSelect
     *            目标表的查询语句
     * @param map
     *            原表和目标表的字段对应关系
     * @param set
     *            不做比较的源表字段
     */
    public void fixData(String sourceSelect, String targetSelect, Map<String, String> map, Set<String> set) {
        log.info("source sql:" + sourceSelect);
        log.info("target sql:" + targetSelect);
        log.info("字段对应表:" + map);
        log.info("忽略字段:" + set);
        try (Connection srcConnection = srcDataSource.getConnection();
                Connection tarConnection = tarDataSource.getConnection();) {
            srcConnection.setReadOnly(true);
            Statement srcStatement = srcConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet srcResultSet = srcStatement.executeQuery(sourceSelect);

            Statement tarStatement = tarConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet tarResultSet = tarStatement.executeQuery(targetSelect);
            int sourceCount = 0;// 原表 总数
            if (srcResultSet.last()) {
                sourceCount = srcResultSet.getRow();
                log.info("the count of source is:" + sourceCount);
            } else {
                log.warn("there are no rows in the source result set");
                return;
            }
            int targetCount = 0;
            if (tarResultSet.last()) {
                targetCount = tarResultSet.getRow();
                log.info("the count of target is:" + targetCount);
            } else {
                log.warn("there are no rows in the target result set");
                return;
            }
            if (sourceCount != targetCount) {// 总数不等
                log.warn("sourceCount != targetCount");
                return;
            }
            srcResultSet.beforeFirst();
            tarResultSet.beforeFirst();

            while (srcResultSet.next() && tarResultSet.next()) {
                boolean needUpdate = false;
                Object srcID = srcResultSet.getObject(1);
                StringBuilder sb = new StringBuilder("source_").append(srcID).append(":");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (!set.contains(entry.getKey())) {
                        try {
                            Object sourceValue = srcResultSet.getObject(entry.getKey());
                            Object targetValue = tarResultSet.getObject(entry.getValue());
                            if (sourceValue == null && targetValue == null) {// 空值不做比较
                                continue;
                            }
                            // 考虑到字段类型不同，统一转成字符串来比较
                            if (!String.valueOf(sourceValue).equals(String.valueOf(targetValue))) {
                                sb.append(entry.getKey()).append("=").append(sourceValue).append(",");
                                sb.append(entry.getValue()).append("=").append(targetValue).append(";");
                                tarResultSet.updateObject(entry.getValue(), sourceValue);
                                needUpdate = true;
                                continue;
                            }
                        } catch (SQLException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
                if (needUpdate) {
                    log.info(sb.toString());
                    tarResultSet.updateRow();
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 全量的数据校验
     * <p>
     * <li>如果数据为空，返回false。
     * <li>如果数据总数不一致返回false。
     * <li>逐个比较非忽略字段，如果不一致则返回false。
     * 
     * @param sourceSelect
     *            源表的查询语句
     * @param targetSelect
     *            目标表的查询语句
     * @param map
     *            原表和目标表的字段对应关系
     * @param set
     *            不做比较的源表字段
     */
    public boolean verifyAllData(String sourceSelect, String targetSelect, Map<String, String> map, Set<String> set,
            int batchRows) {
        log.info("source sql:" + sourceSelect);
        log.info("target sql:" + targetSelect);
        log.info("字段对应表:" + map);
        log.info("忽略字段:" + set);
        try (Connection srcConnection = srcDataSource.getConnection();
                Connection tarConnection = tarDataSource.getConnection();
                PreparedStatement srcStatement = srcConnection.prepareStatement(sourceSelect,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                PreparedStatement tarPStatement = tarConnection.prepareStatement(targetSelect,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
            int offset = 0;
            List<Map<String, Object>> srcList = null;
            List<Map<String, Object>> tarList = null;
            do {
                SelectSqlTask srcTask = new SelectSqlTask(srcStatement, sourceSelect, offset, batchRows);
                FutureTask<ResultSet> srcFutureTask = new FutureTask<>(srcTask);
                new Thread(srcFutureTask).start();

                SelectSqlTask tarTask = new SelectSqlTask(tarPStatement, targetSelect, offset, batchRows);
                FutureTask<ResultSet> tarFutureTask = new FutureTask<>(tarTask);
                new Thread(tarFutureTask).start();

                while (!srcFutureTask.isDone() || !tarFutureTask.isDone()) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                try {
                    ResultSet sourceRS = srcFutureTask.get();
                    Set<String> valueSet = map.values().stream().collect(Collectors.toSet());
                    srcList = ResultSetUtils.buildResultSet(sourceRS, valueSet);
                    sourceRS.close();

                    ResultSet targetRS = tarFutureTask.get();
                    tarList = ResultSetUtils.buildResultSet(targetRS, map.keySet());
                    targetRS.close();

                    campareResultList(srcList, tarList, map, set);
                } catch (InterruptedException | ExecutionException e) {
                    log.error(e.getMessage(), e);
                }
                offset += batchRows;
            } while (srcList != null && !srcList.isEmpty());

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    public static void campareResultList(List<Map<String, Object>> srcList, List<Map<String, Object>> tarList,
            Map<String, String> map, Set<String> set) {
        for (int i = 0; i < tarList.size() && i < srcList.size(); i++) {
            Map<String, Object> tarMap = tarList.get(i);
            Map<String, Object> srcMap = srcList.get(i);
            Object srcID = srcMap.get("id");
            StringBuilder sb = new StringBuilder("verify id ").append(srcID).append(":");
            boolean isSame = true;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (set.contains(entry.getKey())) {
                    continue;
                }
                Object srcValue = srcMap.get(entry.getValue());
                Object tarValue = tarMap.get(entry.getKey());
                if (srcValue == null && tarValue == null) {// 空值不做比较
                    continue;
                }

                // 考虑到字段类型不同，统一转成字符串来比较
                if (!String.valueOf(srcValue).equals(String.valueOf(tarValue))) {
                    sb.append("src.").append(entry.getValue()).append("=").append(srcValue).append(",");
                    sb.append("tar.").append(entry.getKey()).append("=").append(tarValue).append(";");
                    isSame = false;
                }
            }
            if (!isSame) {
                log.warn(sb.toString());
            }
        }
    }

    /**
     * 执行SELECT 语句，并返回结果
     * 
     * @author hanjy
     *
     */
    public static class SelectSqlTask implements Callable<ResultSet> {

        private PreparedStatement preparedStatment;
        private Object[] parameters;
        private String sql;

        public SelectSqlTask(PreparedStatement preparedStatment, String sql, Object... objects) {
            super();
            this.preparedStatment = preparedStatment;
            this.sql = sql;
            this.parameters = objects;
        }

        @Override
        public ResultSet call() throws Exception {
            if (parameters != null && parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    preparedStatment.setObject(i + 1, parameters[i]);
                }
            }
            String sqlLog = sql.replaceAll("\\?", "{}");
            long t1 = clock.millis();
            ResultSet resultSet = preparedStatment.executeQuery();
            long t2 = clock.millis();
            log.info("execute " + sqlLog + " 耗时(ms)" + (t2 - t1), parameters);

            return resultSet;
        }

    }

}
