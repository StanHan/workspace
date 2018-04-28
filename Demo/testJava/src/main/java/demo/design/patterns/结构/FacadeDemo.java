package demo.design.patterns.结构;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h1>设计模式之Facade(外观 总管 Manager)</h1> Facade模式的定义: 为子系统中的一组接口提供一个一致的界面.
 * 
 *
 * 
 * @author hanjy
 *
 */
public class FacadeDemo {

    /**
     * Facade一个典型应用就是数据库JDBC的应用, 在应用中,经常需要对数据库操作,每次都写上述一段代码肯定比较麻烦,需要将其中不变的部分提炼出来,做成一个接口,这就引入了facade外观对象.
     * 如果以后我们更换Class.forName中的<driver>也非常方便,比如从Mysql数据库换到Oracle数据库,只要更换facade接口中的driver就可以.
     */
    static void demo1() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement prep = null;
        ResultSet rset = null;
        try {
            Class.forName("<driver>").newInstance();
            conn = DriverManager.getConnection("<database>");

            String sql = "SELECT * FROM <table> WHERE <column name> = ?";
            prep = conn.prepareStatement(sql);
            prep.setString(1, "<column value>");
            rset = prep.executeQuery();
            if (rset.next()) {
                System.out.println(rset.getString("<column name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rset.close();
            prep.close();
            conn.close();
        }
    }
}
