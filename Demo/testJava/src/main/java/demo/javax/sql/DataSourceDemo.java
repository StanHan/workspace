package demo.javax.sql;

import javax.sql.DataSource;

import demo.java.sql.jdbc.pool.TomcatDataSource;

public class DataSourceDemo {

    public static String url = "jdbc:mysql://180.101.195.217:5026/xinyongjin", user = "daikuanwang",
            pass = "ZGFpa3VhbndhbmcK";

    public static String url2 = "jdbc:mysql://180.101.195.217:5022", user2 = "biz_data",
            pass2 = "C55DE2E68AA14F4B23528417E9A050FD";

    public static String localUrl = "jdbc:mysql://127.0.0.1:3306/test", localUser = "root", localPass = "root";

    public static DataSource xyj = TomcatDataSource.getMySqlDataSource(url, user, pass);

    public static DataSource mycat = TomcatDataSource.getMySqlDataSource(url2, user2, pass2);

    public static DataSource local = TomcatDataSource.getMySqlDataSource(localUrl, localUser, localPass);

}
