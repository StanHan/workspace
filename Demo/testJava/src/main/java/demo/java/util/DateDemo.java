package demo.java.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import demo.java.text.DateFormatDemo;
import demo.java.time.TimeDemo;

public class DateDemo {

    public static void main(String[] args) throws ParseException {
        System.out.println(new Date(-9999999));
        Date date = CalendarDemo.parseDate(0000, 00, 01, 00, 00, 00);
        System.out.println(DateFormatDemo.formatDate(date, DateFormatDemo.G_Y_M_D_HMS));
        try {
            Date d2 = DateFormatDemo.parseStr2Date("0000-00-00", DateFormatDemo.Y_M_D);
            System.out.println(DateFormatDemo.formatDate(d2, DateFormatDemo.G_Y_M_D_HMS));
            Date d3 = DateFormatDemo.parseStr2Date("0001-01-01", DateFormatDemo.Y_M_D);
            System.out.println(DateFormatDemo.formatDate(d3, DateFormatDemo.G_Y_M_D_HMS));
            System.out.println(d3.getTime());
            Date d4 = new Date(-62135798400000L);
            System.out.println(DateFormatDemo.formatDate(d4, DateFormatDemo.G_Y_M_D_HMS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 时间戳的边界
     */
    static void boundaryTimestamp() {
        System.out.println(DateFormatDemo.formatDate(new Date(Long.MAX_VALUE), DateFormatDemo.G_Y_M_D_HMS));
        System.out.println(DateFormatDemo.formatDate(new Date(Long.MIN_VALUE), DateFormatDemo.G_Y_M_D_HMS));
    }

    /**
     * 距离现在多少天
     * 
     * @param date
     * @return
     */
    public static int howManyDaysFromNow(Date date) {
        long now = System.currentTimeMillis();
        long start = date.getTime();
        long days = (now - start) / TimeDemo.MILLIS_PER_DAY;
        return (int) days;
    }

    static void demoDateUtils() {
        Date now = new Date();
        System.out.println(DateUtils.truncate(now, Calendar.DAY_OF_MONTH));
        System.out.println(DateUtils.truncate(now, Calendar.MONTH));
        System.out.println(DateUtils.truncate(now, Calendar.DATE));
        System.out.println(DateUtils.truncate(now, Calendar.HOUR));
        System.out.println(removeHmsS2(new Date()));
        System.out.println(CalendarDemo.removeHmsS(new Date()));
        Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date tomorrow = DateUtils.addDays(today, 1);
        System.out.println(today + "    " + tomorrow);

    }

    

    static void demo1() throws ParseException {
        Date date2 = CalendarDemo.parseDate(2000, 6, 12, 13, 13, 13);
        System.out.println(date2);
    }

    /**
     * 计算两个日期之间相差多少天(endDate-startDate)
     * 
     * @param startDate
     *            小的日期
     * @param endDate
     *            大的日期
     * @return Integer
     */
    public static int subtractDays(Date startDate, Date endDate) {
        long increaseDate = (endDate.getTime() - startDate.getTime()) / TimeDemo.MILLIS_PER_DAY;
        return (int) increaseDate;
    }

    

    /**
     * 将带有分隔的时间数据转换成不带分隔符的时间字符串
     * 
     * @如：2014-06-12 11:26:21.143 => 20140612112621143
     * 
     * @Description:
     * @version 1.0 2014年7月25日 下午4:28:45 by bsgao create
     * @param srcDatetime
     * @return
     */
    public static String replaceDatetime(String srcDatetime) {
        return srcDatetime.replaceAll("\\s|:|-|\\.|\\/", "");
    }

    /**
     * 计算指定日期到现在日期的月数
     * 
     * @param dateString
     * @param format
     * @return
     * @throws ParseException
     */
    public static double processMonthsFromNow(String dateString, String format) throws ParseException {
        Date now = new Date();
        Date date = DateFormatDemo.parseStr2Date(dateString, format);
        long howlong = now.getTime() - date.getTime();// 毫秒
        return howlong / (1000 * 60 * 60 * 24 * 30);
    }

    /**
     * 计算指定日期到现在日期的月数
     * 
     * @param dateString
     * @param format
     * @return
     * @throws ParseException
     */
    public static double processMonths(String startDate, String endDate, String format) throws ParseException {
        Date start_date = DateFormatDemo.parseStr2Date(startDate, format);
        Date end_date = DateFormatDemo.parseStr2Date(endDate, format);
        long howlong = end_date.getTime() - start_date.getTime();// 毫秒
        return howlong / (1000 * 60 * 60 * 24 * 30);
    }

    /**
     * 得到指定年到月的天数
     */
    public static int getYearLastDay(int year, int month) {
        int maxDate = 0;
        for (int i = month; i >= 1; i--) {
            maxDate += CalendarDemo.getMonthLastDay(year, i);
        }
        return maxDate;
    }

    /**
     * 去除时分秒
     * 
     * @param date
     * @return
     */
    public static Date removeHmsS2(Date date) {
        return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }

}
