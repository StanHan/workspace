package demo.db.dynamic;

/**
 * 用来切换数据库
 * @author hanjy
 *
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDbType() {
        return ((String) contextHolder.get());
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}
