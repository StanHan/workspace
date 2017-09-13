package demo.java.time;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SQLDate {

    public static void main(String[] args) throws Exception {
        demo();
    }

    static void demo() throws ParseException {
        Date sqlDate = new Date((new java.util.Date()).getTime());
        System.out.println(sqlDate);
        System.out.println(sqlDate.getTime());
        System.out.println((new java.util.Date()).getTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = simpleDateFormat.parse(sqlDate.toString());
        long timeStemp = date.getTime();
        System.out.println(timeStemp);
        System.out.println(DateDemo.removeHmsS(new java.util.Date()).getTime());
        
        Date today = new Date(System.currentTimeMillis());
        System.out.println(today);
        Date yestoday = new Date(DateDemo.moveDays(today, -1, false).getTime());
        System.out.println(yestoday);
    }

}
