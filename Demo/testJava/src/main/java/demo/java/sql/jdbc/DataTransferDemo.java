package demo.java.sql.jdbc;

import java.sql.Connection;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import demo.javax.sql.DataSourceDemo;

/**
 * 数据迁移
 *
 */
public class DataTransferDemo {
    
    DataSource local = DataSourceDemo.local;

    public static void main(String[] args) {
        DataTransferUtils dataTransferUtils = new DataTransferUtils(DataSourceDemo.local, DataSourceDemo.local);
//        dataTransferUtils.transferAll(selectSql, insertSql);
        dataTransferUtils.transferAll(selectSql2, insertSql, 5_0000);
    }

    static String selectSql2 = "SELECT id, user_id, product_id, apply_id, send_at, create_at, update_at FROM cp_send where id>? and id<=? order by id";
    static String selectSql = "SELECT id, user_id, product_id, apply_id, send_at, create_at, update_at FROM cp_send limit ?,?";
    static String insertSql = "INSERT INTO cp_send_bak (id, user_id, product_id, apply_id, send_at, create_at, update_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public void multiThreadImportDemo(final int ThreadNum) {
        ExecutorService executorService = Executors.newFixedThreadPool(ThreadNum);
        final CountDownLatch cdl = new CountDownLatch(ThreadNum);
        long starttime = System.currentTimeMillis();
        for (int k = 1; k <= ThreadNum; k++) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try (Connection connection = local.getConnection();
                            Statement statement = connection.createStatement();) {

                        for (int i = 1; i <= 80000 / ThreadNum; i++) {
                            String uuid = UUID.randomUUID().toString();
                            statement.addBatch("insert into demo_table(a,b) values('" + uuid + "','" + uuid + "')");
                            if (i % 500 == 0) {
                                statement.executeBatch();
                            }
                        }
                        cdl.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        try {
            cdl.await();
            long spendtime = System.currentTimeMillis() - starttime;
            System.out.println(ThreadNum + "个线程花费时间:" + spendtime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
