package demo.java.time.format;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

public class DateTimeFormatterDemo {
    
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        
    }
    
    static void demo() {
        System.out.println(dateTimeFormatter.toString());

        LocalTime localTime = LocalTime.parse("2018-02-08 12:00:00", dateTimeFormatter);
        System.out.println(localTime);

        long a = localTime.getLong(ChronoField.SECOND_OF_DAY);
        System.out.println(a);

        LocalDate localDate = LocalDate.parse("2011-12-03T10:15:30", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println(localDate);

        LocalDateTime localDateTime = LocalDateTime.parse("2011-12-03T10:15:30", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println(localDateTime);
        System.out.println(localDateTime.toEpochSecond(ZoneOffset.of("+8")));
        Long newSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(newSecond);
        long tmp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(tmp);
        System.out.println(new Date(tmp * 1000));
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
    
}
