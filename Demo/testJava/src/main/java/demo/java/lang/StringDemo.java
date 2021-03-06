package demo.java.lang;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class StringDemo {

    public static void main(String[] args) throws IOException {
        byte[] a = "".getBytes();
        System.out.println(a.length);
        System.out.println(String.format("%08d", 1));
        System.out.println(String.format("%s", 1));
        String key = String.format("mx.migrate:%d.%s", 1, "a");
        System.out.println(key);
    }

    /**
     * String有个常量池，
     */
    static void testString() {
        String a = "hello";
        String b = "hello world";
        String c = " world";
        String d = "hello";
        System.out.println((a + c));
        System.out.println(b == (a + c));
        System.out.println(a == d);
    }

    /**
     * StringIndexOutOfBoundsException异常
     */
    static void testSubstr() {
        String a = "18217".substring(0, 7);
        System.out.println(a);
    }

    static void testStringBuilder() {
        StringBuilder sb = new StringBuilder("你好,");
        System.out.println(sb.toString());
        int length = sb.length();
        System.out.println("length:" + length);
        char idx0 = sb.charAt(0);
        System.out.println("charAt(0) :" + idx0);
        sb.deleteCharAt(length - 1);
        System.out.println(sb.toString());
    }

    /**
     * <li>replace的参数是char和CharSequence，即可以支持字符的替换，也支持字符串的替换
     * <li>replaceAll的参数是regex，即基于规则表达式的替换，比如，可以通过replaceAll("\\d", "*")把一个字符串所有的数字字符都换成星号;
     * <p>
     * 相同点：都是全部替换，即把源字符串中的某一字符或字符串全部换成指定的字符或字符串。
     * 如果只想替换第一次出现的，可以使用replaceFirst()，这个方法也是基于规则表达式的替换，但与replaceAll()不同的是，只替换第一次出现的字符串；
     * 另外，如果replaceAll()和replaceFirst()所用的参数据不是基于规则表达式的，则与replace()替换字符串的效果是一样的，即这两者也支持字符串的操作；
     * 还有一点注意:：执行了替换操作后,源字符串的内容是没有发生改变的。
     */
    @Test
    public void replaceDemo() {
        String name = "韩**";
        String tmp = name.replace("*", "");
        System.out.println(tmp);
        String tmp2 = name.replaceAll("*", "");
        System.out.println(tmp2);
        String s = "{用户姓名}你好！{银行开户行}，借款金额{借款金额}，{逾期天数}，{平台服务费}，{平台服务费}，{到期日},{到期日}";
        s = s.replace("{到期日}", "2017-09-08");
        s = s.replaceAll("{平台服务费}", "{002}");
        System.out.println(s);
        String a = "hello,%s";
        System.out.println(String.format(a, 123));
    }

    static void testMatch() {
        String a = ".*你好.*";
        System.out.println("老王，你好吗".matches(a));
    }

    /**
     * <li>%s 字符串类型
     * <li>%c 字符类型
     * <li>%b 布尔类型
     * <li>%d 整数类型（十进制）
     * <li>%x 整数类型（十六进制）
     * <li>%o 整数类型（八进制）
     * <li>%f 浮点类型
     * <li>%a 十六进制浮点类型
     * <li>%e 指数类型
     * <li>%g 通用浮点类型（f和e类型中较短的）
     * <li>%h 散列码
     * <li>%% 百分比类型
     * <li>%n 换行符
     * <li>%tx 日期与时间类型（x代表不同的日期与时间转换符
     */
    static void testFormat() {
        System.out.println(String.format("Hi,%s", "王力"));
        System.out.println(String.format("Hi,%s:%s.%s", "王南", "王力", "王张"));
        System.out.printf("字母a的大写是：%c %n", 'A');
        System.out.printf("3>7的结果是：%b %n", 3 > 7);
        System.out.printf("100的一半是：%d %n", 100 / 2);
        System.out.printf("100的16进制数是：%x %n", 100);
        System.out.printf("100的8进制数是：%o %n", 100);
        System.out.printf("50元的书打8.5折扣是：%f 元%n", 50 * 0.85);
        System.out.printf("上面价格的16进制数是：%a %n", 50 * 0.85);
        System.out.printf("上面价格的指数表示：%e %n", 50 * 0.85);
        System.out.printf("上面价格的指数和浮点数结果的长度较短的是：%g %n", 50 * 0.85);
        System.out.printf("上面的折扣是%d%% %n", 85);
        System.out.printf("字母A的散列码是：%h %n", 'A');

        // $使用
        System.out.println(String.format("格式参数$的使用：%1$d,%2$s", 99, "abc"));
        // +使用
        System.out.printf("显示正负数的符号：%+d与%d%n", 99, -99);
        // 补O使用
        System.out.printf("最牛的编号是：%03d%n", 7);
        // 空格使用
        System.out.printf("Tab键的效果是：% 8d%n", 7);
        // .使用
        System.out.printf("整数分组的效果是：%,d%n", 9989997);
        // 空格和小数点后面个数
        System.out.printf("一本书的价格是：% 50.5f元%n", 49.8);

        Date date = new Date();
        // c的使用
        System.out.printf("全部日期和时间信息：%tc%n", date);
        // f的使用
        System.out.printf("年-月-日格式：%tF%n", date);
        // d的使用
        System.out.printf("月/日/年格式：%tD%n", date);
        // r的使用
        System.out.printf("HH:MM:SS PM格式（12时制）：%tr%n", date);
        // t的使用
        System.out.printf("HH:MM:SS格式（24时制）：%tT%n", date);
        // R的使用
        System.out.printf("HH:MM格式（24时制）：%tR", date);

        // b的使用，月份简称
        System.out.println(String.format(Locale.US, "英文月份简称：%tb", date));
        System.out.printf("本地月份简称：%tb%n", date);
        // B的使用，月份全称
        System.out.println(String.format(Locale.US, "英文月份全称：%tB", date));
        System.out.printf("本地月份全称：%tB%n", date);
        // a的使用，星期简称
        System.out.println(String.format(Locale.US, "英文星期的简称：%ta", date));
        // A的使用，星期全称
        System.out.printf("本地星期的简称：%tA%n", date);
        // C的使用，年前两位
        System.out.printf("年的前两位数字（不足两位前面补0）：%tC%n", date);
        // y的使用，年后两位
        System.out.printf("年的后两位数字（不足两位前面补0）：%ty%n", date);
        // j的使用，一年的天数
        System.out.printf("一年中的天数（即年的第几天）：%tj%n", date);
        // m的使用，月份
        System.out.printf("两位数字的月份（不足两位前面补0）：%tm%n", date);
        // d的使用，日（二位，不够补零）
        System.out.printf("两位数字的日（不足两位前面补0）：%td%n", date);
        // e的使用，日（一位不补零）
        System.out.printf("月份的日（前面不补0）：%te", date);

        // H的使用
        System.out.printf("2位数字24时制的小时（不足2位前面补0）:%tH%n", date);
        // I的使用
        System.out.printf("2位数字12时制的小时（不足2位前面补0）:%tI%n", date);
        // k的使用
        System.out.printf("2位数字24时制的小时（前面不补0）:%tk%n", date);
        // l的使用
        System.out.printf("2位数字12时制的小时（前面不补0）:%tl%n", date);
        // M的使用
        System.out.printf("2位数字的分钟（不足2位前面补0）:%tM%n", date);
        // S的使用
        System.out.printf("2位数字的秒（不足2位前面补0）:%tS%n", date);
        // L的使用
        System.out.printf("3位数字的毫秒（不足3位前面补0）:%tL%n", date);
        // N的使用
        System.out.printf("9位数字的毫秒数（不足9位前面补0）:%tN%n", date);
        // p的使用
        System.out.println(String.format(Locale.US, "小写字母的上午或下午标记(英)：%tp", date));
        System.out.printf("小写字母的上午或下午标记（中）：%tp%n", date);
        // z的使用
        System.out.printf("相对于GMT的RFC822时区的偏移量:%tz%n", date);
        // Z的使用
        System.out.printf("时区缩写字符串:%tZ%n", date);
        // s的使用
        System.out.printf("1970-1-1 00:00:00 到现在所经过的秒数：%ts%n", date);
        // Q的使用
        System.out.printf("1970-1-1 00:00:00 到现在所经过的毫秒数：%tQ%n", date);

    }

    static void regionMatchesDemo() {
        String searchMe = "Green Eggs and Ham";
        String findMe = "Eggs";
        boolean foundIt = false;
        for (int i = 0; i <= (searchMe.length() - findMe.length()); i++) {
            if (searchMe.regionMatches(i, findMe, 0, findMe.length())) {
                foundIt = true;
                System.out.println(searchMe.substring(i, i + findMe.length()));
                break;
            }
        }
        if (!foundIt) {
            System.out.println("No match found.");
        }
    }

    static void trigonometricDemo() {
        double degrees = 45.0;
        double radians = Math.toRadians(degrees);

        System.out.format("The value of pi " + "is %.4f%n", Math.PI);

        System.out.format("The sine of %.1f " + "degrees is %.4f%n", degrees, Math.sin(radians));

        System.out.format("The cosine of %.1f " + "degrees is %.4f%n", degrees, Math.cos(radians));

        System.out.format("The tangent of %.1f " + "degrees is %.4f%n", degrees, Math.tan(radians));

        System.out.format("The arcsine of %.4f " + "is %.4f degrees %n", Math.sin(radians),
                Math.toDegrees(Math.asin(Math.sin(radians))));

        System.out.format("The arccosine of %.4f " + "is %.4f degrees %n", Math.cos(radians),
                Math.toDegrees(Math.acos(Math.cos(radians))));

        System.out.format("The arctangent of %.4f " + "is %.4f degrees %n", Math.tan(radians),
                Math.toDegrees(Math.atan(Math.tan(radians))));
    }

    static void exponentialDemo() {
        double x = 11.635;
        double y = 2.76;

        System.out.printf("The value of " + "e is %.4f%n", Math.E);

        System.out.printf("exp(%.3f) " + "is %.3f%n", x, Math.exp(x));

        System.out.printf("log(%.3f) is " + "%.3f%n", x, Math.log(x));

        System.out.printf("pow(%.3f, %.3f) " + "is %.3f%n", x, y, Math.pow(x, y));

        System.out.printf("sqrt(%.3f) is " + "%.3f%n", x, Math.sqrt(x));
    }

    static void basicMathDemo() {
        double a = -191.635;
        double b = 43.74;
        int c = 16, d = 45;

        System.out.printf("The absolute value " + "of %.3f is %.3f%n", a, Math.abs(a));

        System.out.printf("The ceiling of " + "%.2f is %.0f%n", b, Math.ceil(b));

        System.out.printf("The floor of " + "%.2f is %.0f%n", b, Math.floor(b));

        System.out.printf("The rint of %.2f " + "is %.0f%n", b, Math.rint(b));

        System.out.printf("The max of %d and " + "%d is %d%n", c, d, Math.max(c, d));

        System.out.printf("The min of of %d " + "and %d is %d%n", c, d, Math.min(c, d));
    }

    public static void customFormat(String pattern, double value) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        System.out.println(value + "  " + pattern + "  " + output);
    }

    static void decimalFormatDemo() {
        customFormat("###,###.###", 123456.789);
        customFormat("###.##", 123456.789);
        customFormat("000000.000", 123.78);
        customFormat("$###,###.###", 12345.67);
    }

    static void testFormat2() {

        long n = 461012;
        System.out.format("%d%n", n); // --> "461012"
        System.out.format("%08d%n", n); // --> "00461012"
        System.out.format("%+8d%n", n); // --> " +461012"
        System.out.format("%,8d%n", n); // --> " 461,012"
        System.out.format("%+,8d%n%n", n); // --> "+461,012"

        double pi = Math.PI;

        System.out.format("%f%n", pi); // --> "3.141593"
        System.out.format("%.3f%n", pi); // --> "3.142"
        System.out.format("%10.3f%n", pi); // --> " 3.142"
        System.out.format("%-10.3f%n", pi); // --> "3.142"
        System.out.format(Locale.FRANCE, "%-10.4f%n%n", pi); // --> "3,1416"

        Calendar c = Calendar.getInstance();
        System.out.format("%tB %te, %tY%n", c, c, c); // --> "May 29, 2006"

        System.out.format("%tl:%tM %tp%n", c, c, c); // --> "2:34 am"

        System.out.format("%tD%n", c); // --> "05/29/06"
    }

    /*
     * public static void test(){ StringBuilder sb = new StringBuilder(); // Send all output to the Appendable object sb
     * Formatter formatter = new Formatter(sb, Locale.US);
     * 
     * // Explicit argument indices may be used to re-order output. formatter.format("%4$2s %3$2s %2$2s %1$2s", "a",
     * "b", "c", "d") // -> " d  c  b  a"
     * 
     * // Optional locale as the first argument can be used to get // locale-specific formatting of numbers. The
     * precision and width can be // given to round and align the value. formatter.format(Locale.FRANCE, "e = %+10.4f",
     * Math.E); // -> "e =    +2,7183"
     * 
     * // The '(' numeric flag may be used to format negative numbers with // parentheses rather than a minus sign.
     * Group separators are // automatically inserted. formatter.format(
     * "Amount gained or lost since last statement: $ %(,.2f",balanceDelta); // ->
     * "Amount gained or lost since last statement: $ (6,217.58)" // Writes a formatted string to System.out.
     * System.out.format("Local time: %tT", Calendar.getInstance()); // -> "Local time: 13:34:18"
     * 
     * // Writes formatted output to System.err. System.err.printf( "Unable to open file '%1$s': %2$s", fileName,
     * exception.getMessage()); // -> "Unable to open file 'food': No such file or directory"
     * 
     * Calendar c = new GregorianCalendar(1995, MAY, 23); String s = String.format("Duke's Birthday: %1$tm %1$te,%1$tY",
     * c); // -> s == "Duke's Birthday: May 23, 1995" }
     */

    public static void testASCII() {
        String tmp = "abc";
        StringBuilder stringBuilder = new StringBuilder();
        char del = 127;// ASCII码删除符
        System.out.println(del);
        for (int i = 0; i < 9; i++) {
            stringBuilder.append(tmp + i).append(del);
        }
        System.out.println(stringBuilder.toString());

        System.out.println("\u007F");
        System.out.println('a');
        String[] array = stringBuilder.toString().split("\u007F");
        for (String aa : array) {
            System.out.println(aa);
        }
    }

    public static void demo() throws UnsupportedEncodingException {
        String line = "DT2015113012623392         10100              511381198206141936                                          YCL李志金                                                                                                                          上海东振环保工程技术有限公司                                                                                                                          浦东新区龙东大道3000号5号楼501室                                                                                                                                                                                                                CN                                      201203                                                                                                 ";
        System.out.println(line);
        System.out.println(line.length());

        byte[] gbk_array = line.getBytes("GBK");
        System.out.println(gbk_array.length);
        // String tmp = new String(gbk_array, offset, count);
        String stmt_yyyymm2 = new String(gbk_array, 2, 6, "GBK");// 账单月
        String cust_id2 = new String(gbk_array, 8, 10, "GBK");// 客户号
        String id_type2 = new String(gbk_array, 18, 19, "GBK");// 证件类型
        String id_no2 = new String(gbk_array, 37, 60, "GBK");// 证件号
        String cardHolder_name2 = new String(gbk_array, 100, 128, "GBK");// 持卡人姓名
        String bill_addr2 = new String(gbk_array, 378, 200, "GBK");// 账单寄送地址
        String addr_city2 = new String(gbk_array, 578, 20, "GBK");// 地址所在城市
        String addr_state2 = new String(gbk_array, 598, 20, "GBK");// 地址所在省份
        String addr_cny2 = new String(gbk_array, 618, 40, "GBK");// 地址国家代码/地址国家描述
        String addr_zip2 = new String(gbk_array, 658, 20, "GBK");// 地址邮政编码

        String line2 = "CH000002310107195301072514       陆家伟                                                                          梅川路1500弄59号702室                                                                                                                                                                                   上海                上海市              中国                                    200333    201510022015110120151126     90,0000090000008000030138                                                                                                                                                                                                                                                                                                                                                          ";
        System.out.println(line2);
        byte[] array_gbk = line2.getBytes("GBK");
        String bill_addr3 = new String(array_gbk, 113, 200, "GBK");
        String addr_city3 = new String(array_gbk, 313, 20, "GBK");
        String addr_state3 = new String(array_gbk, 333, 20, "GBK");
        String addr_cny3 = new String(array_gbk, 353, 40, "GBK");
        String addr_zip3 = new String(array_gbk, 393, 10, "GBK");

        System.out.println(line2);
        String line3 = line2.replaceAll(" ", "0").replace("0", "_");
        System.out.println(line2);
        System.out.println(line3);

        System.out.println(new String(gbk_array, "GBK"));
        System.out.println(new String(array_gbk, "GBK"));
        System.arraycopy(gbk_array, 378, array_gbk, 113, 280);
        System.out.println(new String(array_gbk, "GBK"));

    }

    /**
     * 获取<aaa>中的aaa
     * 
     * @param string
     * @return
     */
    public static String getElementName(String s) {
        if (s == null || "".equals(s)) {
            return "";
        }
        int begin = s.indexOf("<");
        int tmp = s.indexOf("</");
        if (begin == tmp) {
            return "";
        }
        int end = s.indexOf(">");
        String result = s.substring(begin + 1, end);
        return result;
    }

    /**
     * 首字母大写
     * 
     * @param string
     * @return
     */
    public static String upperCase1stLetter(String string) {
        if (null == string) {
            return "";
        }
        // string = string.substring(0, 1).toUpperCase() + string.substring(1);
        // return string;
        char[] cs = string.toCharArray();
        if (97 <= cs[0] && cs[0] <= 122) {// 小写字母ASCII码区间
            cs[0] -= 32;
            return String.valueOf(cs);
        }
        return string;
    }

    /**
     * 首字母小写
     * 
     * @param string
     * @return
     */
    public static String lowerCase1stLetter(String string) {
        if (null == string) {
            return "";
        }
        char[] cs = string.toCharArray();
        if (65 <= cs[0] && cs[0] <= 90) {// 大写字母ASCII码区间
            cs[0] += 32;
            return String.valueOf(cs);
        }
        return string;
    }

    public static String getDefineString(String type, String property) {
        StringBuffer buffer = new StringBuffer("");
        buffer.append("private " + type + " " + property + ";").append("\n");

        return buffer.toString();
    }

    /**
     * 标准的Java命名规范（驼峰形式）
     * 
     * @param name
     * @return
     */
    public static String javaNamingConvention(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        String[] array = name.split("_");
        StringBuilder convention = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            String tmp = array[i];
            if (i == 0) {
                convention.append(lowerCase1stLetter(tmp));
            } else {
                convention.append(upperCase1stLetter(tmp));
            }

        }
        return convention.toString();
    }

    /**
     * 
     * @Description: 将日期转换为字符串匹配格式 @Version1.0 2014年7月23日 下午4:50:09 by 张仁华（renhua.zhang@pactera.com）创建
     * @param filePath
     * @param dateTime
     * @return
     */
    public static String parseDateToStr(String regex, Date dateTime) {

        // 根据时间获取日历
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);

        // 获取该日历中对应的 年月日时分秒
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", calendar.get(Calendar.DATE));
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
        String second = String.format("%02d", calendar.get(Calendar.SECOND));
        String milltsecond = String.valueOf(calendar.get(Calendar.MILLISECOND));

        // 将字符串中的%表达式替换成对应的日期
        return regex.replace("%yyyy", year).replace("%MM", month).replace("%dd", day).replace("%HH", hour)
                .replace("%mm", minute).replace("%ss", second).replace("%S", milltsecond);
    }

}
