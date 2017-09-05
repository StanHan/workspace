package com.daikuan.platform.ma.util;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommonUtils {

    private final static Charset UTF8 = Charset.forName("UTF8");

    // public static void main(String[] args) {
    // String[] a = {"abcdefg","aa","b","1"};
    // byte[][] b = toArray(a);
    // for (byte[] cs : b) {
    // System.out.println(new String(cs));
    // }
    //
    // String[] c = toArray(b);
    // for (String string : c) {
    // System.out.println(string);
    // }
    // }

    /**
     * 一维字符串数组转二维字节数组
     * 
     * @param strings
     * @return
     */
    public static byte[][] toArray(String[] strings) {
        byte[][] array = new byte[strings.length][];
        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            array[i] = s.getBytes(UTF8);
        }
        return array;
    }

    /**
     * 二维字节数组转一维字符串数组
     * 
     * @param cs
     * @return
     */
    public static String[] toArray(byte[][] cs) {
        String[] s = new String[cs.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = new String(cs[i]);
        }
        return s;
    }

    /**
     * Long型的List转String型的List
     * 
     * @param list
     * @return
     */
    public static List<String> transfer(List<Long> list) {
        return list.stream().map(s -> String.valueOf(s)).collect(Collectors.toList());
    }

    /**
     * 把字符串格式的日期转成date
     * @param date
     * @param partten
     * @return
     */
    public static Date stringToDate(String date, String partten) {
        if (date == null || date.equals("")) {
            return null;
        }

        Date dateFormat = null;
        try {
            dateFormat = new SimpleDateFormat(partten).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat;
    }

    /**
     * DATE 转 String
     * @param date
     * @param partten
     * @return
     */
    public static String dateToString(Date date, String partten) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(partten);
        return sdf.format(date);
    }

    /**
     * 把date转成当天特定小时
     * @param date
     * @param time
     * @return
     */
    public static Date convertSpecialTimeDate(Date date, int time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, time > -1 && time < 23 ? time : 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前date的下一天
     * @param date
     * @return
     */
    public static Date getNextDay(Date date) {
        return new Date(date.getTime() + 24 * 60 * 60 * 1000);
    }
}
