package demo.java.text;

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

    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = null;

    }

    /**
     * <h2>SimpleDateFormat的线程安全问题与解决方案</h2>
     * 
     * SimpleDateFormat类内部有一个Calendar对象引用,它用来储存和这个SimpleDateFormat相关的日期信息。相关参数都是交友Calendar引用来储存的。
     * 这样就会导致一个问题,如果你的SimpleDateFormat是个static的, 那么多个thread 之间就会共享这个SimpleDateFormat, 同时也是共享这个Calendar引用,
     */
    static void demoSimpleDateFormat() {
    }

    static void demo1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern("yyyy年MM月dd日_HH时mm分ss秒");
        Date date = sdf.parse("2006年07月01日_14时00分00秒");
        System.out.println(date);
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
