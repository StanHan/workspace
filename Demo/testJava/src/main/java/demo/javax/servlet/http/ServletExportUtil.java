package demo.javax.servlet.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletExportUtil {
    private static Logger logger = LoggerFactory.getLogger(ServletExportUtil.class);

    /** CSV文件列分隔符 */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /** CSV文件列分隔符 */
    private static final String CSV_RN = "\r\n";

    public static void export(HttpServletResponse response, String fName) {
        try (final OutputStream os = response.getOutputStream()) {
            responseSetProperties(fName, response);
            String sTitle = "投资日期,订单号,姓名,购买产品,金额,状态";
            String mapKey = "createDate,no,realname,productName,money,state";
            doExport(new ArrayList<Map<String, Object>>(), sTitle, mapKey, os);
            return;

        } catch (Exception e) {
            logger.error("购买CSV失败", e);

        }
    }

    /**
     * 
     * @param dataList
     *            集合数据
     * @param colNames
     *            表头部数据
     * @param mapKey
     *            查找的对应数据
     * @param response
     *            返回结果
     */
    public static boolean doExport(List<Map<String, Object>> dataList, String colNames, String mapKey,
            OutputStream os) {
        try {
            StringBuffer buf = new StringBuffer();

            String[] colNamesArr = null;
            String[] mapKeyArr = null;

            colNamesArr = colNames.split(",");
            mapKeyArr = mapKey.split(",");

            // 完成数据csv文件的封装
            // 输出列头
            for (int i = 0; i < colNamesArr.length; i++) {
                buf.append(colNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);

            if (null != dataList) { // 输出数据
                for (int i = 0; i < dataList.size(); i++) {
                    for (int j = 0; j < mapKeyArr.length; j++) {
                        buf.append(dataList.get(i).get(mapKeyArr[j])).append(CSV_COLUMN_SEPARATOR);
                    }
                    buf.append(CSV_RN);
                }
            }
            // 写出响应
            os.write(buf.toString().getBytes("GBK"));
            os.flush();
            return true;
        } catch (Exception e) {
            logger.error("doExport错误...", e);
        }
        return false;
    }

    /**
     * @throws UnsupportedEncodingException
     * 
     *             setHeader
     */
    public static void responseSetProperties(String fileName, HttpServletResponse response)
            throws UnsupportedEncodingException {
        // 设置文件后缀
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = fileName + sdf.format(new Date()).toString() + ".csv";
        // 读取字符编码
        String utf = "UTF-8";

        // 设置响应
        response.setContentType("application/ms-txt.numberformat:@");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }

    /**
     * 导出CSV
     * 
     * @param response
     * @param head
     * @param body
     * @param fileName
     */
    public static void exportCSV(HttpServletResponse response, String[] head, List<Map<String, String>> body,
            String fileName) {
        if (response == null) {
            throw new NullPointerException("exportCSV failed.");
        }
        // 设置响应
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

        try (PrintWriter printWriter = response.getWriter();) {
            if (head != null && head.length > 0) {
                String headStr = Stream.of(head).peek(e -> {
                    if (e.contains(",")) {
                        throw new IllegalArgumentException("非法字符‘,’");
                    }
                }).collect(Collectors.joining(","));
                printWriter.print(headStr);
                printWriter.print("\r\n");
            }
            if (body != null && !body.isEmpty()) {
                String bodys = body.stream().map(m -> {
                    return parseValues(head, m);
                }).collect(Collectors.joining("\r\n"));
                printWriter.print(bodys);
            }
            printWriter.flush();
            response.flushBuffer();
        } catch (IOException e2) {
            logger.error(e2.getMessage(), e2);
        }

    }

    /**
     * 获取value的字符串
     * 
     * @param keys
     * @param map
     * @return
     */
    private static String parseValues(String[] keys, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String value = map.get(key);
            if (value == null) {
                sb.append("");
            } else {
                if (value.contains(",")) {
                    sb.append("\"").append(value).append("\"");
                } else {
                    sb.append(value);
                }
            }
            sb.append(",");
        }
        int length = sb.length();
        sb.deleteCharAt(length - 1);
        return sb.toString();
    }

}
