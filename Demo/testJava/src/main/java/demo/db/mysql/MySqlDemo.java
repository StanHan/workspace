package demo.db.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * @author hanjy
 *
 */
public class MySqlDemo {

    static void command() {

    }

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    /** 查看当前事务的隔离级别 */
    public static final String tx_isolation = "select @@tx_isolation";

    /**
     * 设置事务的隔离 级别：
     * <li>set [glogal | session] transaction isolation level 隔离级别名称;
     * <li>set tx_isolation=’隔离级别名称’;
     * <p>
     * 如：
     * <li>set transaction isolation level repeatable read
     * <li>set tx_isolation='read-uncommitted'
     * <p>
     * 设置数据库的隔离级别一定要是在开启事务之前。隔离级别的设置只对当前链接有效。对于使用MySQL命令窗口而言，一个窗口就相当于一个链接，当前窗口设置的隔离级别只对当前窗口中的事务有效；
     * 对于JDBC操作数据库来说，一个Connection对象相当于一个链接，而对于Connection对象设置的隔离级别只对该Connection对象有效，与其他链接Connection对象无关。
     */
    static void setTxIsolation(Connection connection) throws SQLException {
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("");
        connection.commit();
    }

}
