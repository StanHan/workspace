package demo.java.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;

/**
 * java8引入了一套全新的时间日期API，java。time包中的是类是不可变且线程安全的。 这与之前的Date与Calendar
 * API中的恰好相反，那里面像java.util.Date以及SimpleDateFormat这些关键的类都不是线程安全的。
 * <li>Instant——它代表的是时间戳
 * <li>LocalDate——不包含具体时间的日期，比如2014-01-14。它可以用来存储生日，周年纪念日，入职日期等。
 * <li>LocalTime——它代表的是不含日期的时间
 * <li>LocalDateTime——它包含了日期及时间，不过还是没有偏移信息或者说时区。
 * <li>ZonedDateTime——这是一个包含时区的完整的日期时间，偏移量是以UTC/格林威治时间为基准的。
 * <li>javax.time.ZoneId用来处理时区。
 * <li>新的时间与日期API中很重要的一点是它定义清楚了基本的时间与日期的概念，比方说，瞬时时间，持续时间，日期，时间，时区以及时间段。它们都是基于ISO日历体系的。
 * <li>时区指的是地球上共享同一标准时间的地区。每个时区都有一个唯一标识符，同时还有一个地区/城市(Asia/Tokyo)的格式以及从格林威治时间开始的一个偏移时间。比如说，东京的偏移时间就是+09:00。
 * <li>OffsetDateTime类实际上包含了LocalDateTime与ZoneOffset。它用来表示一个包含格林威治时间偏移量（+/-小时：分，比如+06:00或者
 * -08：00）的完整的日期（年月日）及时间（时分秒，纳秒）。
 * <li>DateTimeFormatter类用于在Java中进行日期的格式化与解析。与SimpleDateFormat不同，它是不可变且线程安全的，如果需要的话，可以赋值给一个静态变量。
 * <p>
 * 最新JDBC映射将把数据库的日期类型和Java 8的新类型关联起来
 * <li>SQL -> Java
 * <li>--------------------------
 * <li>date -> LocalDate
 * <li>time -> LocalTime
 * <li>timestamp -> LocalDateTime
 * 
 */
public class LocalDateTimeDemo {

    public static void main(String[] args) {
        LocalDateTime localDateTime = toLocalDateTime(System.currentTimeMillis());
        System.out.println(localDateTime);
        LocalDate localDate = LocalDate.parse("2018-02-24", DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(untilByMonths(localDate, localDateTime.toLocalDate()));
        System.out.println(localDate.until(localDateTime, ChronoUnit.DAYS));

        // 20171202和20171206
        LocalDateTime t1 = LocalDateTime.of(2017, 12, 02, 23, 59, 59);
        LocalDateTime t2 = LocalDateTime.of(2017, 12, 06, 0, 0, 0);
        long dif = t2.toInstant(ZoneOffset.of("+8")).toEpochMilli() - t1.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(dif / (24 * 3600 * 1000));
    }

    static void demo() {
        LocalDate localDate = LocalDate.parse("2018-02-20", DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(localDate);
        System.out.println(localDate.until(LocalDate.now(), ChronoUnit.DAYS));
        System.out.println(untilNowByDay(localDate));
    }

    /**
     * LocalDateTime、LocalDate、LocalTime 提供了对java.util.Date的替代，另外还提供了新的DateTimeFormatter用于对格式化/解析的支持
     */
    static void demoLocalDateTime() {
        // 使用默认时区时钟瞬时时间创建 Clock.systemDefaultZone() -->即相对于 ZoneId.systemDefault()默认时区
        LocalDateTime now = LocalDateTime.now();
        LocalDate _now = LocalDate.now();
        System.out.println(now + "是闰年吗？" + _now.isLeapYear());

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
        System.out.println(d1);

        // 年月日 时分秒 纳秒
        LocalDateTime d2 = LocalDateTime.of(2013, 12, 31, 23, 59, 59, 11);
        System.out.println(d2);

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
        LocalDate today = LocalDate.now();
        System.out.println(today);// yyyy-MM-dd
        LocalDate yesterday = LocalDate.now().minusDays(1);
        System.out.println(yesterday);// yyyy-MM-dd
        // LocalTime即时分秒 无年月日
        // API和LocalDateTime类似就不演示了
    }

    /**
     * 时间戳转时间
     * 
     * @param timestamp
     * @return
     */
    public static LocalDateTime toLocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.of("+8"));
        return offsetDateTime.toLocalDateTime();
    }

    /**
     * 相差多少月
     * 
     * @param start
     * @param end
     * @return
     */
    public static long untilByMonths(LocalDate start, LocalDate end) {
        return start.until(end, ChronoUnit.MONTHS);
    }

    /**
     * 相差多少天
     * 
     * @param start
     * @param end
     * @return
     */
    public static long untilByDays(LocalDate start, LocalDate end) {
        return start.until(end, ChronoUnit.DAYS);
    }

    /**
     * 到现在相差多少天
     * 
     * @param localDate
     * @return
     */
    public static long untilNowByDay(LocalDate localDate) {
        return untilByDays(localDate, LocalDate.now());
    }

}
