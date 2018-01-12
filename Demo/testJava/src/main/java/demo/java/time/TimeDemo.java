package demo.java.time;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class TimeDemo {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private static LocalDate date;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime time;

    public static void main(String[] args) throws Exception {
//      testLocalDateTime();
      Timestamp t = new Timestamp(100, 0, 1, 15, 45, 29, 987245732);
      System.out.println(t);
    }

    static void testDateTimeFormatter() {
        LocalDate now = LocalDate.now();
        System.out.println(now);// yyyy-MM-dd
        LocalDate yesterday = LocalDate.now().minusDays(1);
        System.out.println(yesterday);// yyyy-MM-dd
        String formatNow = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println(formatNow);
        LocalDate localDate = LocalDate.parse("20180101", DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * LocalDate转Date
     */
    static void test2() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);

        Date date = Date.from(zdt.toInstant());

        System.out.println("LocalDate = " + localDate);
        System.out.println("Date = " + date);

    }

    /**
     * Date转LocalDate
     */
    static void test1() {
        Date date = new Date();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        System.out.println("Date = " + date);
        System.out.println("LocalDate = " + localDate);
    }

    /**
     * Clock 时钟，类似于钟表的概念，提供了如系统时钟、固定时钟、特定时区的时钟
     */
    static void demoClock() throws InterruptedException {
        // 时钟提供给我们用于访问某个特定 时区的 瞬时时间、日期 和 时间的。
        Clock c1 = Clock.systemUTC(); // 系统默认UTC时钟（当前瞬时时间 System.currentTimeMillis()）
        long start = c1.millis();
        System.out.println(c1.millis()); // 每次调用将返回当前瞬时时间（UTC）
        System.out.println("耗时：" + (c1.millis() - start));

        Clock c2 = Clock.systemDefaultZone(); // 系统默认时区时钟（当前瞬时时间）

        Clock c31 = Clock.system(ZoneId.of("Europe/Paris")); // 巴黎时区
        System.out.println(c31.millis()); // 每次调用将返回当前瞬时时间（UTC）

        Clock c32 = Clock.system(ZoneId.of("Asia/Shanghai"));// 上海时区
        System.out.println(c32.millis());// 每次调用将返回当前瞬时时间（UTC）

        Clock c4 = Clock.fixed(Instant.now(), ZoneId.of("Asia/Shanghai"));// 固定上海时区时钟
        System.out.println(c4.millis());
        Thread.sleep(1000);
        System.out.println(c4.millis()); // 不变 即时钟时钟在那一个点不动

        Clock c5 = Clock.offset(c1, Duration.ofSeconds(2)); // 相对于系统默认时钟两秒的时钟
        System.out.println(c1.millis());
        System.out.println(c5.millis());
    }

    /**
     * Instant 瞬时时间，等价于以前的System.currentTimeMillis()
     */
    static void demoInstance() {
        // 瞬时时间 相当于以前的System.currentTimeMillis()
        Instant instant1 = Instant.now();
        System.out.println(instant1.getEpochSecond());// 精确到秒 得到相对于1970-01-01 00:00:00 UTC的一个时间
        System.out.println(instant1.toEpochMilli()); // 精确到毫秒

        Clock clock1 = Clock.systemUTC(); // 获取系统UTC默认时钟
        Instant instant2 = Instant.now(clock1);// 得到时钟的瞬时时间
        System.out.println(instant2.toEpochMilli());

        Clock clock2 = Clock.fixed(instant1, ZoneId.systemDefault()); // 固定瞬时时间时钟
        Instant instant3 = Instant.now(clock2);// 得到时钟的瞬时时间
        System.out.println(instant3.toEpochMilli());// equals instant1
    }

    /**
     * LocalDateTime、LocalDate、LocalTime 提供了对java.util.Date的替代，另外还提供了新的DateTimeFormatter用于对格式化/解析的支持
     */
    static void testLocalDateTime() {
        // 使用默认时区时钟瞬时时间创建 Clock.systemDefaultZone() -->即相对于 ZoneId.systemDefault()默认时区
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        // 自定义时区
        LocalDateTime now2 = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        System.out.println(now2);// 会以相应的时区显示日期

        // 自定义时钟
        Clock clock = Clock.system(ZoneId.of("Asia/Dhaka"));
        LocalDateTime now3 = LocalDateTime.now(clock);
        System.out.println(now3);// 会以相应的时区显示日期

        // 不需要写什么相对时间 如java.util.Date 年是相对于1900 月是从0开始
        // 2013-12-31 23:59
        LocalDateTime d1 = LocalDateTime.of(2013, 12, 31, 23, 59);

        // 年月日 时分秒 纳秒
        LocalDateTime d2 = LocalDateTime.of(2013, 12, 31, 23, 59, 59, 11);

        // 使用瞬时时间 + 时区
        Instant instant = Instant.now();
        LocalDateTime d3 = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        System.out.println(d3);

        // 解析String--->LocalDateTime
        LocalDateTime d4 = LocalDateTime.parse("2013-12-31T23:59");
        System.out.println(d4);

        LocalDateTime d5 = LocalDateTime.parse("2013-12-31T23:59:59.999");// 999毫秒 等价于999000000纳秒
        System.out.println(d5);

        // 使用DateTimeFormatter API 解析 和 格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime d6 = LocalDateTime.parse("2013/12/31 23:59:59", formatter);
        System.out.println(formatter.format(d6));

        // 时间获取
        System.out.println(d6.getYear());
        System.out.println(d6.getMonth());
        System.out.println(d6.getDayOfYear());
        System.out.println(d6.getDayOfMonth());
        System.out.println(d6.getDayOfWeek());
        System.out.println(d6.getHour());
        System.out.println(d6.getMinute());
        System.out.println(d6.getSecond());
        System.out.println(d6.getNano());
        // 时间增减
        LocalDateTime d7 = d6.minusDays(1);
        LocalDateTime d8 = d7.plus(1, IsoFields.QUARTER_YEARS);

        // LocalDate 即年月日 无时分秒
        // LocalTime即时分秒 无年月日
        // API和LocalDateTime类似就不演示了
    }

    /**
     * ZonedDateTime 带有时区的date-time
     * 存储纳秒、时区和时差（避免与本地date-time歧义）；API和LocalDateTime类似，只是多了时差(如2013-12-20T10:35:50.711+08:00[Asia/Shanghai])
     */
    static void demoZone() {
        // 即带有时区的date-time 存储纳秒、时区和时差（避免与本地date-time歧义）。
        // API和LocalDateTime类似，只是多了时差(如2013-12-20T10:35:50.711+08:00[Asia/Shanghai])
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(now);

        ZonedDateTime now2 = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        System.out.println(now2);

        // 其他的用法也是类似的 就不介绍了

        ZonedDateTime z1 = ZonedDateTime.parse("2013-12-31T23:59:59Z[Europe/Paris]");
        System.out.println(z1);
    }

    /**
     * Duration 表示两个瞬时时间的时间段
     * 
     */
    static void demoDuration() {
        // 表示两个瞬时时间的时间段
        Duration d1 = Duration.between(Instant.ofEpochMilli(System.currentTimeMillis() - 12323123), Instant.now());
        // 得到相应的时差
        System.out.println(d1.toDays());
        System.out.println(d1.toHours());
        System.out.println(d1.toMinutes());
        System.out.println(d1.toMillis());
        System.out.println(d1.toNanos());

        // 1天时差 类似的还有如ofHours()
        Duration d2 = Duration.ofDays(1);
        System.out.println(d2.toDays());
    }

    /**
     * Chronology 用于对年历系统的支持，是java.util.Calendar的替代者
     */
    static void demoChronology() {
        // 提供对java.util.Calendar的替换，提供对年历系统的支持
        Chronology c = HijrahChronology.INSTANCE;
        ChronoLocalDateTime d = c.localDateTime(LocalDateTime.now());
        System.out.println(d);
    }

    /**
     * 如果提供了年、年月、月日、周期的API支持
     */
    static void demoYYYYMMDD() {
        Year year = Year.now();
        YearMonth yearMonth = YearMonth.now();
        MonthDay monthDay = MonthDay.now();

        System.out.println(year);// 年
        System.out.println(yearMonth); // 年-月
        System.out.println(monthDay); // 月-日

        // 周期，如表示10天前 3年5个月钱
        Period period1 = Period.ofDays(10);
        System.out.println(period1);
        Period period2 = Period.of(3, 5, 0);
        System.out.println(period2);
    }
}
