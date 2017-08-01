package demo.java.sql.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import demo.javax.sql.DataSourceDemo;

public class BatchInsertDemo {
    
    static DataSource localDataSource = DataSourceDemo.local;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        generalInsert();
        batchInsert();
    }

    static void generalInsert() throws ClassNotFoundException, SQLException {
        long start = System.currentTimeMillis();
        Class.forName("com.mysql.jdbc.Driver");
//        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kxh", "root", "root");
        Connection connection = localDataSource.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement cmd = connection.prepareStatement("insert into test values(?,?)");

        for (int i = 0; i < 1000000; i++) {
            cmd.setInt(1, i);
            cmd.setString(2, "test");
            cmd.executeUpdate();
        }
        connection.commit();

        cmd.close();
        connection.close();

        long end = System.currentTimeMillis();
        System.out.println(end - start);// 158918毫秒
    }

    static void batchInsert() throws ClassNotFoundException, SQLException {
        long start = System.currentTimeMillis();
        Class.forName("com.mysql.jdbc.Driver");
//        Connection connection = DriverManager.getConnection(
//                "jdbc:mysql://127.0.0.1:3306/kxh?useServerPrepStmts=false&rewriteBatchedStatements=true", "root",
//                "root");
        
        Connection connection = localDataSource.getConnection();

        connection.setAutoCommit(false);
        PreparedStatement cmd = connection.prepareStatement("insert into test1 values(?,?)");

        for (int i = 0; i < 1000000; i++) {// 100万条数据
            cmd.setInt(1, i);
            cmd.setString(2, "test");
            cmd.addBatch();
            if (i % 1000 == 0) {
                cmd.executeBatch();
            }
        }
        cmd.executeBatch();
        connection.commit();

        cmd.close();
        connection.close();

        long end = System.currentTimeMillis();
        System.out.println("批量插入需要时间:" + (end - start)); // 批量插入需要时间:24675
    }

}
