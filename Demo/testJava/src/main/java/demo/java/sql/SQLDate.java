package demo.java.sql;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SQLDate {

    public static void main(String[] args) throws ParseException {
        Date data = new Date((new java.util.Date()).getTime());
        System.out.println(data);
        System.out.println(data.getTime());
        System.out.println((new java.util.Date()).getTime());
        
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date=simpleDateFormat .parse(data.toString());
        long timeStemp = date.getTime();
        System.out.println(timeStemp );
        System.out.println(removeWhenSeconds(new java.util.Date()).getTime());
    }
    
    /**
     *  去除时分秒
     * @param date
     * @return
     */
    public static java.util.Date removeWhenSeconds(java.util.Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
