package demo.java.text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <li>G : Era designator Text
 * <li>y : Year Year
 * <li>M : Month in year Month
 * <li>w : Week in year Number
 * <li>W : Week in month Number
 * <li>D : Day in year Number
 * <li>d : Day in month Number
 * <li>F : Day of week in month Number
 * <li>E : Day in week Text
 * <li>a : Am/pm marker Text
 * <li>H : Hour in day (0-23) Number
 * <li>k : Hour in day (1-24) Number
 * <li>K : Hour in am/pm (0-11) Number
 * <li>h : Hour in am/pm (1-12) Number
 * <li>m : Minute in hour Number
 * <li>s : Second in minute Number
 * <li>S : Millisecond Number
 * <li>z : Time zone General time zone
 * <li>Z : Time zone RFC 822 time zone
 */
public class DateFormatDemo {

    public final static String Y_M_D = "yyyy-MM-dd";
    public final static String G_Y_M_D_HMS = "G yyyy-MM-dd HH:mm:ss";
    public final static String Y_M_D_HMS = "yyyy-MM-dd HH:mm:ss";
    public final static String TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static void main(String[] args) throws ParseException {
        demo();

    }

    /**
     * <h2>SimpleDateFormat的线程安全问题与解决方案</h2>
     * SimpleDateFormat类内部有一个Calendar对象引用,它用来储存和这个SimpleDateFormat相关的日期信息。相关参数都是交友Calendar引用来储存的。
     * 这样就会导致一个问题,如果你的SimpleDateFormat是个static的, 那么多个thread 之间就会共享这个SimpleDateFormat, 同时也是共享这个Calendar引用。
     * 
     * <h3>解决方案</h3>
     * <li>最简单的解决方案我们可以把static去掉,这样每个新的线程都会有一个自己的sdf实例,从而避免线程安全的问题。 然而,使用这种方法,在高并发的情况下会大量的new sdf以及销毁sdf,这样是非常耗费资源的。
     * <li>将SimpleDateFormat进行同步使用，在每次执行时都对其加锁，这样也会影响性能，想要调用此方法的线程就需要block，当多线程并发量比较大时会对性能产生一定影响；
     * <li>使用ThreadLocal变量，用空间换时间，这样每个线程就会独立享有一个本地的SimpleDateFormat变量；
     */
    static void demo() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Y_M_D_HMS);
        String now = simpleDateFormat.format(new Date());
        System.out.println(now);
        Date date = simpleDateFormat.parse(now);
        System.out.println(date);
    }

    static void demo1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern("yyyy年MM月dd日_HH时mm分ss秒");
        Date date = sdf.parse("2006年07月01日_14时00分00秒");
        System.out.println(date);
    }

    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(Y_M_D_HMS);
        }
    };

    public static Date parse(String dateStr) throws ParseException {
        return threadLocal.get().parse(dateStr);
    }

    /**
     * Date字符串转换为Date
     * 
     * @param dateStr
     *            Date字符串
     * @param format
     *            时间格式
     * @return Date
     * @throws ParseException
     * @throws Exception
     */
    public static Date parseStr2Date(String dateStr, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = simpleDateFormat.parse(dateStr);
        return date;
    }

    /**
     * Date转换为字符串(年月日)
     * 
     * @param date
     *            Object
     * @param format
     *            时间格式
     * @return String
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

}
