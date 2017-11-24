package demo.java.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;

//G	 Era designator Text 
//y	 Year Year 
//M	 Month in year Month 
//w	 Week in year Number 
//W	 Week in month Number 
//D	 Day in year Number 
//d	 Day in month Number 
//F	 Day of week in month Number 
//E	 Day in week Text 
//a	 Am/pm marker Text 
//H	 Hour in day (0-23) Number 
//k	 Hour in day (1-24) Number 
//K	 Hour in am/pm (0-11) Number 
//h	 Hour in am/pm (1-12) Number 
//m	 Minute in hour Number 
//s	 Second in minute Number 
//S	 Millisecond Number 
//z	 Time zone General time zone 
//Z	 Time zone RFC 822 time zone 
public class DateDemo {
	
	public static void main(String[] args) throws ParseException {
//	    testCalendar();
//	    demoDateUtils();
	    System.out.println((new Date().getTime()));
	}
	
	static void demoDateUtils() {
	    Date now = new Date();
	    System.out.println(DateUtils.truncate(now, Calendar.DAY_OF_MONTH));
	    System.out.println(DateUtils.truncate(now, Calendar.MONTH));
	    System.out.println(DateUtils.truncate(now, Calendar.DATE));
	    System.out.println(DateUtils.truncate(now, Calendar.HOUR));
	    System.out.println(removeHmsS2(new Date()));
        System.out.println(removeHmsS(new Date()));
        Date lastDate = new Date(Long.MAX_VALUE);
        System.out.println(getDate(lastDate) + " "+ getTime(lastDate));
        Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date tomorrow = DateUtils.addDays(today, 1);
        System.out.println(today + "    "+ tomorrow);
	}
	
	static void testCalendar() {
	    Calendar calendar = Calendar.getInstance();
	    System.out.println(calendar.toString());
	    System.out.println(calendar.getTime());
	    calendar.set(Calendar.DAY_OF_MONTH,1);
	    System.out.println(calendar.toString());
	    System.out.println(calendar.getTime());
	}
	
	static void demo1() throws ParseException {
	    SimpleDateFormat sdf = new SimpleDateFormat("",Locale.SIMPLIFIED_CHINESE); 
        sdf.applyPattern("yyyy年MM月dd日_HH时mm分ss秒"); 

        Date date = sdf.parse("2006年07月01日_14时00分00秒"); 
        System.out.println(date);
        Date date2= parseDate(2000, 6, 12, 13, 13, 13);
        System.out.println(date2);
	}

	public final static long DAY_MS_RATE = 1000 * 60 * 60 * 24L;

	public final static String YM = "yyyyMM";
	public final static String YMD = "yyyyMMdd";
	public final static String Y_M = "yyyy-MM";
	public final static String Y_M_D = "yyyy-MM-dd";
	public final static String Y_M_D_HMS = "yyyy-MM-dd HH:mm:ss";
	public final static String TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	public static String getDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(date);
	}

	public static String getTime(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
		return formatter.format(date);
	}

	/**
	 * Date字符串转换为Date
	 * 
	 * @param dateStr
	 *            Date字符串
	 * @param format
	 *            时间格式
	 * @return Date
	 * @throws Exception
	 */
	public static Date toDate(String dateStr, String format) {
		Date date = null;
		if (_isEmpty(dateStr)) {
			return date;
		}

		try {
			date = new SimpleDateFormat(format).parse(dateStr);
		} catch (Exception e) {
			System.err.println("[parseToDateFormat Exception] dateStr=" + dateStr + ",format=" + format + ",e=" + e);
		}
		return date;
	}

	/**
	 * Date(年月日)字符串转换为Date
	 * 
	 * @param dateStr
	 *            Date字符串
	 * @return Date
	 */
	public static Date toDate(String dateStr) {
		return toDate(dateStr, Y_M_D);
	}

	/**
	 * Date(年月日,时分秒)字符串转换为Date
	 * 
	 * @param dateStr
	 *            Date字符串
	 * @return Date
	 */
	public static Date toDateTime(String dateStr) {
		return toDate(dateStr, Y_M_D_HMS);
	}

	/**
	 * Date转换为字符串(年月日)
	 * 
	 * @param obj
	 *            Object
	 * @param format
	 *            时间格式
	 * @return String
	 */
	public static String dateToStr(Object obj, String format) {
		String dateStr = "";
		if (obj == null) {
			return dateStr;
		}

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			dateStr = simpleDateFormat.format(obj);
		} catch (Exception e) {
			System.err.println("[dateToString Exception] obj=" + obj + ",format=" + format + ",e=" + e);
		}
		return dateStr;
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
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(startDate);
		calendar2.setTime(endDate);
		long increaseDate = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / DAY_MS_RATE;
		if (((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) % DAY_MS_RATE) > 0) {
			increaseDate = increaseDate + 1;
		}
		return (int) increaseDate;
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
	public static int howLong(Date startDate, Date endDate) {

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(startDate);
		calendar2.setTime(endDate);
		int distanceHour = calendar2.get(Calendar.HOUR_OF_DAY) - calendar1.get(Calendar.HOUR_OF_DAY);

		int distanceMin = calendar2.get(Calendar.MINUTE) - calendar1.get(Calendar.MINUTE);

		long increaseDate = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / DAY_MS_RATE;
		if (((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) % DAY_MS_RATE) > 0) {
			increaseDate = increaseDate + 1;
		}
		return (int) increaseDate;
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
	 * 获取日期所在当月的最后一个日期
	 * 
	 * @param date
	 *            日期
	 * @param inDateFormat
	 *            入参日期格式
	 * @param outDateFormat
	 *            出参日期格式
	 * @return
	 */
	public static String getMouthLastDay(String date, String inDateFormat, String outDateFormat) {
		String outDate = "";
		try {
			outDate = new SimpleDateFormat(outDateFormat)
					.format(getMouthLastDay(new SimpleDateFormat(inDateFormat).parse(date)));
		} catch (Exception e) {
			System.err.println("[getMouthLastDay Exception] date=" + date + ",inDateFormat=" + inDateFormat
					+ ",outDateFormat=" + outDateFormat + ",e=" + e);
		}
		return outDate;
	}

	/************************ private method ********************************/
	private static boolean _isEmpty(String str) {
		boolean flg = false;
		if (str == null || "".equals(str)) {
			flg = true;
		}
		return flg;
	}

	public static String addMDate(String date, String inDateFormat, int m) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(inDateFormat);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(format.parse(date));
			calendar.add(Calendar.MONTH, m);
			// calendar.set(Calendar.MONTH, 1);
			return getMouthLastDay(format.format(calendar.getTime()), inDateFormat, inDateFormat);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return null;
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
	 */
	public static double processMonthsFromNow(String dateString, String format) {
		Date now = new Date();
		Date date = DateDemo.toDate(dateString, format);
		long howlong = now.getTime() - date.getTime();// 毫秒
		return howlong / (1000 * 60 * 60 * 24 * 30);
	}

	/**
	 * 计算指定日期到现在日期的月数
	 * 
	 * @param dateString
	 * @param format
	 * @return
	 */
	public static double processMonths(String startDate, String endDate, String format) {
		Date start_date = DateDemo.toDate(startDate, format);
		Date end_date = DateDemo.toDate(endDate, format);
		long howlong = end_date.getTime() - start_date.getTime();// 毫秒
		return howlong / (1000 * 60 * 60 * 24 * 30);
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
	 * 得到指定年到月的天数
	 */
	public static int getYearLastDay(int year, int month) {
		int maxDate = 0;
		for (int i = month; i >= 1; i--) {
			maxDate += getMonthLastDay(year, i);
		}
		return maxDate;
	}

	public static String getPreviousMonth() {
		String res = "";
		Calendar a = Calendar.getInstance();
		int month = a.get(Calendar.MONTH);
		if (month == 0) {
			res = a.get(Calendar.YEAR) - 1 + "" + 12;
		} else if (month >= 10) {
			res = a.get(Calendar.YEAR) + "" + month;
		} else {
			res = a.get(Calendar.YEAR) + "0" + month;
		}
		return res;
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
     * 去除时分秒
     * 
     * @param date
     * @return
     */
    public static Date removeHmsS2(Date date) {
        return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }
    
    public static final int oneDayInMilliseconds = 24 * 60 * 60 * 1000;
}
