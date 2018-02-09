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

    public static void main(String[] args) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

}
