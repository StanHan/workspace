package demo.util;

/**
 * Created by qiuxinjie on 14-11-11.
 */
public class VersionUtils {

    /**
     * 比较两个版本大小
     * 
     * @param version1
     * @param version2
     * @return
     *         <li>0：相等，
     *         <li>1：v1>v2,
     *         <li>-1： v1 < v2
     */
    public static int compare(String version1, String version2) {

        if (version1 == null || version1.isEmpty() || version2 == null || version2.isEmpty()) {
            throw new IllegalArgumentException("Invalid parameter!");
        }
        if (version1.equals(version2)) {
            return 0;
        }
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int len1 = v1.length;
        int len2 = v2.length;
        int lim = Math.min(len1, len2);

        int k = 0;
        while (k < lim) {
            String c1 = v1[k];
            String c2 = v2[k];
            if (!c1.equals(c2)) {
                return c1.compareTo(c2);
            }
            k++;
        }
        int diff = Math.abs(len1 - len2);
        if (len1 > lim) {
            for (int i = lim; i < lim + diff; i++) {
                int t = Integer.valueOf(v1[i]);
                if (t != 0) {
                    return 1;
                }
            }
            return 0;
        }

        if (len2 > lim) {
            for (int i = lim; i < lim + diff; i++) {
                int t = Integer.valueOf(v2[i]);
                if (t != 0) {
                    return -1;
                }
            }
            return 0;
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(compare("5.3.0", "5.3.0"));
        System.out.println(compare("5.3.0", "5.2.1"));
        System.out.println(compare("5.8.0", "5.2.1"));
        System.out.println(compare("5.3.0", "5.4.0"));
        System.out.println(compare("5.3.0", "5.3"));
        System.out.println(compare("5.3", "5.3.00.00.00"));
    }
}
