package demo.java.time;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Date;

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
 */
public class TimeDemo {

    public static void main(String[] args) throws Exception {
        demoInstance();

    }

    /**
     * 计算两个日期之间包含多少天、周、月、年。可以用java.time.Period类完成该功能。
     */
    static void demoPeriod() {
        LocalDate birthday_nini = LocalDate.of(2016, 10, 1);
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthday_nini, now);
        System.out.printf("%s 和 %s 相差 %d 年 %d 月  %d 天。", birthday_nini, now, period.getYears(), period.getMonths(),
                period.getDays());
    }

    /**
     * 在java8中如何检查重复事件，比如生日？
     * 使用MonthDay类。这个类由月日组合，不包含年信息，可以用来代表每年重复出现的一些日期或其他组合。他和新的日期库中的其他类一样也都是不可变且线程安全的，并且它还是一个值类（value class）
     */
    static void demoMonthDay() {
        MonthDay monthDay = MonthDay.now();
        System.out.println(monthDay);
        LocalDate birthday_nini = LocalDate.of(2016, 10, 1);
        System.out.println("Nini was born at " + birthday_nini);
        MonthDay birthday = MonthDay.of(birthday_nini.getMonth(), birthday_nini.getDayOfMonth());
        System.out.println("Nini's birthday is " + birthday);
        System.out.println("is today Nini's birthday ? " + birthday.equals(monthDay));
    }

    static void demoYearMonth() {
        YearMonth yearMonth = YearMonth.of(2018, Month.FEBRUARY);
        System.out.println(yearMonth);

        YearMonth now = YearMonth.now();
        System.out.printf("%s 这个月有%d天.", now.toString(), now.lengthOfMonth());
    }

    /**
     * Epoch指的是一个特定的时间：1970-01-01 00:00:00 UTC。
     * 
     * 时间戳。 java8获取时间戳特别简单。Instant类由一个静态的工厂方法now()可以返回当前时间戳。 当前时间戳是包含日期和时间的，与java.util.Date很类似，事实上Instant就是java8以前的Date，
     * 可以使用这个两个类中的方法在这两个类型之间进行转换，比如Date.from(Instant)就是用来把Instant转换成java.util.date的，而Date。 toInstant()就是将Date转换成Instant的
     * 
     * @throws InterruptedException
     */
    static void demoInstant() throws InterruptedException {
        // 方法 一
        long timestamp = System.currentTimeMillis();
        Instant t1 = Instant.ofEpochMilli(timestamp);
        System.out.println(t1);
        // 方法 二
        Calendar.getInstance().getTimeInMillis();
        // 方法 三
        new Date().getTime();

        System.out.println(Instant.now());
        Thread.sleep(500);
        System.out.println(Instant.now());
        Date now = Date.from(Instant.now());
        System.out.println(now);
        Instant instant = now.toInstant();
        System.out.println(instant);

    }

    /**
     * 在java8之前，时间日期的格式化非常麻烦，经常使用SimpleDateFormat来进行格式化，但是SimpleDateFormat并不是线程安全的。
     * 在java8中，引入了一个全新的线程安全的日期与时间格式器。并且预定义好了格式。在DateTimeFormatter中还有很多定义好的格式。
     */
    static void demoDateTimeFormatter() {
        String formatNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println(formatNow);
        LocalDate localDate = LocalDate.parse("20180101", DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println(localDate);
        LocalDate localDate2 = LocalDate.parse("20180101", DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(localDate2);
        ;
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
     * Clock 时钟，类似于钟表的概念，提供了如系统时钟、固定时钟、特定时区的时钟。
     * 可以用来获取某个时区下（所以对时区是敏感的）当前的瞬时时间、日期。用来代替System.currentTimelnMillis()与TimeZone.getDefault()方法 时钟提供给我们用于访问某个特定 时区的
     * 瞬时时间、日期 和 时间的。
     */
    static void demoClock() throws InterruptedException {
        // 系统默认UTC时钟（当前瞬时时间 System.currentTimeMillis()）
        Clock clock_SystemUTC = Clock.systemUTC();
        long start = clock_SystemUTC.millis();
        System.out.println("当前时间：" + new Date(start));
        Thread.sleep(100);
        // 每次调用将返回当前瞬时时间（UTC）
        System.out.println("耗时：" + (clock_SystemUTC.millis() - start));
        // 系统默认时区时钟（当前瞬时时间）
        Clock clock_systemDefaultZone = Clock.systemDefaultZone();
        System.out.println("系统默认时区时钟：" + clock_systemDefaultZone);
        // 巴黎时区
        Clock paris_clock = Clock.system(ZoneId.of("Europe/Paris"));
        // 每次调用将返回当前瞬时时间（UTC）
        System.out.printf("巴黎时钟：%s,时间戳：%d,转为Date：%s%n", paris_clock, paris_clock.millis(),
                new Date(paris_clock.millis()));
        // 上海时区
        Clock clock_shanghai = Clock.system(ZoneId.of("Asia/Shanghai"));
        // 每次调用将返回当前瞬时时间（UTC）
        System.out.printf("上海时钟：%s,时间戳：%d,转为Date：%s%n", clock_shanghai, clock_shanghai.millis(),
                new Date(clock_shanghai.millis()));
        // 固定上海时区时钟
        Clock clock_fixed = Clock.fixed(Instant.now(), ZoneId.of("Asia/Shanghai"));
        System.out.printf("固定了的时钟：%s,时间戳：%d %n", clock_fixed, clock_fixed.millis());
        Thread.sleep(1000);
        // 不变, 即时钟时钟在那一个点不动
        System.out.printf("固定了的时钟一秒后：%s,时间戳：%d %n", clock_fixed, clock_fixed.millis());
        // 相对于系统默认时钟两秒的时钟
        Clock clock_offset = Clock.offset(clock_SystemUTC, Duration.ofSeconds(10));

        System.out.printf("当前时钟时间戳：%d,相对于当前时钟10秒后的时间戳：%d %n", clock_SystemUTC.millis(), clock_offset.millis());
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

        Instant current = Clock.system(ZoneId.of("Asia/Shanghai")).instant();
        System.out.println(current.toEpochMilli() + "      " + current);
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
     * ZonedDateTime 带有时区的date-time 存储纳秒、时区和时差（避免与本地date-time歧义）；
     * API和LocalDateTime类似，只是多了时差(如2013-12-20T10:35:50.711+08:00[Asia/Shanghai])
     * ZonId代表的是某个特定时区，ZonedDateTime代表带时区的时间，等同于以前的GregorianCalendar类。使用该类，可以将本地时间转换成另一个时区中的对应时间。
     */
    static void demoZone() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前时间：" + now);

        ZoneId paris = ZoneId.of(ZoneId.SHORT_IDS.get("ECT"));
        ZonedDateTime paris_zonedDateTime = ZonedDateTime.of(now, paris);
        System.out.println("对应巴黎当前时间：" + paris_zonedDateTime);

        ZonedDateTime defaultZone = ZonedDateTime.now();
        System.out.println("默认时区：" + defaultZone);

        ZonedDateTime zonedDateTime_paris = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        System.out.println("巴黎时区时间：" + zonedDateTime_paris);

        // 其他的用法也是类似的 就不介绍了
        ZonedDateTime z1 = ZonedDateTime.parse("2013-12-31T23:59:59Z[Europe/Paris]");
        System.out.println(z1);
    }

    /**
     * 用ZoneOffset来代表某个时区，可以使用它的静态方法ZoneOffset.of()方法来获取对应的时区，只要获得了这个偏移量，就可以用这个偏移量和LocalDateTime创建一个新的OffsetDateTime
     */
    static void demoZoneOffset() {
        LocalDateTime now = LocalDateTime.now();
        ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(5, 30);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(now, zoneOffset);
        System.out.println(offsetDateTime);
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

    /**
     * Hours per day.
     */
    public static final int HOURS_PER_DAY = 24;
    /**
     * Minutes per hour.
     */
    public static final int MINUTES_PER_HOUR = 60;
    /**
     * Minutes per day.
     */
    public static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;
    /**
     * Seconds per minute.
     */
    public static final int SECONDS_PER_MINUTE = 60;
    /**
     * Seconds per hour.
     */
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    /**
     * Seconds per day.
     */
    public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
    /**
     * Milliseconds per day.
     */
    public static final long MILLIS_PER_DAY = SECONDS_PER_DAY * 1000L;
    /**
     * Microseconds per day.
     */
    public static final long MICROS_PER_DAY = SECONDS_PER_DAY * 1000_000L;
    /**
     * Nanos per second.
     */
    public static final long NANOS_PER_SECOND = 1000_000_000L;
    /**
     * Nanos per minute.
     */
    public static final long NANOS_PER_MINUTE = NANOS_PER_SECOND * SECONDS_PER_MINUTE;
    /**
     * Nanos per hour.
     */
    public static final long NANOS_PER_HOUR = NANOS_PER_MINUTE * MINUTES_PER_HOUR;
    /**
     * Nanos per day.
     */
    public static final long NANOS_PER_DAY = NANOS_PER_HOUR * HOURS_PER_DAY;
}
