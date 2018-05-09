package demo.javax.sql;

import javax.sql.DataSource;

public class DataSourceDemo {

    public static String url = "jdbc:mysql://180.101.195.217:5026/xinyongjin", user = "daikuanwang",
            pass = "ZGFpa3VhbndhbmcK";

    public static String url2 = "jdbc:mysql://180.101.195.217:5022", user2 = "biz_data",
            pass2 = "C55DE2E68AA14F4B23528417E9A050FD";

    public static String localUrl = "jdbc:mysql://127.0.0.1:3306/test", localUser = "root", localPass = "root";
    
    public static final String localJDBCURL = "jdbc:mysql://127.0.0.1:3306/testdb?user=root&password=123456&useUnicode=true&characterEncoding=utf8";

    public static DataSource xyj = TomcatDataSourceDemo.initMySqlDataSource(url, user, pass);

    public static DataSource mycat = TomcatDataSourceDemo.initMySqlDataSource(url2, user2, pass2);

    public static DataSource local = TomcatDataSourceDemo.initMySqlDataSource(localUrl, localUser, localPass);

}
