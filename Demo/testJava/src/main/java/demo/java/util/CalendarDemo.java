package demo.java.util;

import java.util.Calendar;
import java.util.Date;

public class CalendarDemo {

    public static void main(String[] args) {

    }
    
    static void testCalendar() {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.toString());
        System.out.println(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(calendar.toString());
        System.out.println(calendar.getTime());
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    
    /**
     * 去除时分秒
     * 
     * @param date
     * @return
     */
    public static Date removeHmsS(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * 设置日期
     * 
     * @param yyyy
     * @param MM
     * @param dd
     * @param HH
     * @param mm
     * @param ss
     * @return
     */
    public static Date parseDate(int yyyy, int MM, int dd, int HH, int mm, int ss) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yyyy);
        calendar.set(Calendar.MONTH, MM);
        calendar.set(Calendar.DAY_OF_MONTH, dd);
        calendar.set(Calendar.HOUR_OF_DAY, HH);
        calendar.set(Calendar.MINUTE, mm);
        calendar.set(Calendar.SECOND, ss);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 从指定日期移动一定的天数
     * 
     * @param date
     *            日期
     * @param day
     *            天数
     * @param iszero
     *            时分秒毫秒是否置0 true:是 ,flase:否
     * @return Date
     */
    public static Date moveDays(Date date, int day, boolean iszero) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (iszero) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * 从指定日期移动一定的秒数
     * 
     * @param date
     *            日期
     * @param seconds
     *            秒数
     * @return Date
     */
    public static Date moveSeconds(Date date, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    /**
     * 获取日期所在当月的最后一个日期
     * 
     * @param date
     *            日期
     * @return
     */
    public static Date getMouthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 
     * @Description:日期减天数 @Version1.0 2014年8月8日 上午9:28:00 by
     * @param date
     * @param day
     * @return
     */
    public static Date subtractDayDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, day * -1);
        return calendar.getTime();
    }
}
