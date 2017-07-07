package demo.java.net;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class URLDemo {

    public static void main(String[] args) throws Exception {
        String url = "http://127.0.0.1:8080/as/image/banner/2/5.2.1.0";
        printURL(new URL(url));
//        demo1();
    }

    /**
     * 通过URL打开链接并写道本地文件
     * 
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void demo1() throws URISyntaxException, IOException {
        URL url = new URL("http://downloaddaikuan.2345.com/images/banner_list/1491569234558.jpg.aa");
        printURL(url);
        downloadFileFromURL2(url, "G:/aa.jpg");
    }

    /**
     * 下载文件
     * 
     * @param url
     * @param localPath
     * @throws IOException
     *             文件不可访问报错
     */
    public static void downloadFileFromURL(URL url, String localPath) throws IOException {
        try (InputStream inputStream = url.openStream();
                FileOutputStream fileOutputStream = new FileOutputStream(localPath);) {
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            int contentLength = httpconn.getContentLength();// 获取文件长度
            System.out.println("ContentLength = " + contentLength);
            int sum = 0;
            int bytesRead = 0;
            byte[] buffer = new byte[8192];

            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                sum += bytesRead;
            }
            System.out.println("下载文件大小：" + sum);
        }
    }

    /**
     * 下载文件
     * 
     * @param url
     * @param localPath
     * @throws IOException
     *             文件不可访问报错
     */
    public static void downloadFileFromURL2(URL url, String localPath) throws IOException {
        URLConnection httpconn = url.openConnection();
        try (InputStream inputStream = httpconn.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(localPath);) {
            int contentLength = httpconn.getContentLength();// 获取文件长度
            System.out.println("ContentLength = " + contentLength);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            httpconn.getInputStream();
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * 打印URL相关信息
     * 
     * @param url
     * @throws URISyntaxException
     */
    public static void printURL(URL url) throws URISyntaxException {
        String protocol = url.getProtocol();
        String file = url.getFile();
        String host = url.getHost();
        String path = url.getPath();
        int port = url.getPort();
        String query = url.getQuery();
        String userInfo = url.getUserInfo();
        String ref = url.getRef();
        String authority = url.getAuthority();

        System.out.println("url.toString() = " + url.toString());
        System.out.println(url.toURI().toString());
        System.out.println(url.toExternalForm());
        System.out.println("protocol = " + protocol);
        System.out.println("file = " + file);
        System.out.println("host = " + host);
        System.out.println("path = " + path);
        System.out.println("port = " + port);
        System.out.println("query = " + query);
        System.out.println("userInfo = " + userInfo);
        System.out.println("ref = " + ref);
        System.out.println("authority = " + authority);

    }

}
