package demo.java.net;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * URL: Uniform Resource Locator
 * 
 * 为什么要进行url encode？
 * 
 * 因为一些历史的原因URL设计者使用US-ASCII字符集表示URL。（原因比如ASCII比较简单；所有的系统都支持ASCII）。
 * 为了满足URL的以上特性，设计者就将转义序列移植了进去，来实现通过ASCII字符集的有限子集对任意字符或数据进行编码。 现在URL转义表示法比较常用的有两个：
 * <li>RFC 2396 - Uniform Resource Identifiers (URI): Generic Syntax
 * <li>RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax
 * 
 * 
 * .URL访问过程：
 * <p>
 * <ol>
 * <li>第一步：客户机提出域名解析请求,并将该请求发送给本地的域名服务器。
 * <li>第二步：当本地的域名服务器收到请求后,就先查询本地的缓存,如果有该纪录项,则本地的域名服务器就直接把查询的结果返回。
 * <li>第三步：如果本地的缓存中没有该纪录,则本地域名服务器就直接把请求发给根域名服务器,然后根域名服务器再返回给本地域名服务器一个所查询域(根的子域)的主域名服务器的地址。
 * <li>第四步：本地服务器再向上一步返回的域名服务器发送请求,然后接受请求的服务器查询自己的缓存,如果没有该纪录,则返回相关的下级的域名服务器的地址。
 * <li>第五步：重复第四步,直到找到正确的纪录。
 * </ol>
 * <p>
 * <ol>
 * <li>1.enter the url to the address bar
 * <li>2.a request will be sent to the DNS server based on your network configuration
 * <li>3.DNS will route you to the real IP of the domain name
 * <li>4.a request(with complete Http header) will be sent to the server(with 3's IP to identify)'s 80 port(suppose we
 * don't specify another port)
 * <li>5.server will search the listening ports and forward the request to the app which is listening to 80 port(let's
 * say nginx here) or to another server(then 3's server will be like a load balancer)
 * <li>6.nginx will try to match the url to its configuration and serve as an static page directly, or invoke the
 * corresponding script intepreter(e.g PHP/Python) or other app to get the dynamic content(with DB query, or other
 * logics)
 * <li>7.a html will be sent back to browser with a complete Http response header
 * <li>8.browser will parse the DOM of html using its parser
 * <li>9.external resources(JS/CSS/images/flash/videos..) will be requested in sequence(or not?)
 * <li>10.for JS, it will be executed by JS engine
 * <li>11.for CSS, it will be rendered by CSS engine and HTML's display will be adjusted based on the CSS(also in
 * sequence or not?)
 * <li>12.if there's an iframe in the DOM, then a separate same process will be executed from step 1-12
 * </ol>
 * <p>
 * 
 * <ol>
 * <li>1.browser checks cache; if requested object is in cache and is fresh, skip to #9
 * <li>2.browser asks OS for server's IP address
 * <li>3.OS makes a DNS lookup and replies the IP address to the browser
 * <li>4.browser opens a TCP connection to server (this step is much more complex with HTTPS)
 * <li>5.browser sends the HTTP request through TCP connection
 * <li>6.browser receives HTTP response and may close the TCP connection, or reuse it for another request
 * <li>7.browser checks if the response is a redirect (3xx result status codes), authorization request (401), error (4xx
 * and 5xx), etc.; these are handled differently from normal responses (2xx)
 * <li>8.if cacheable, response is stored in cache
 * <li>9.browser decodes response (e.g. if it's gzipped)
 * <li>10.browser determines what to do with response (e.g. is it a HTML page, is it an image, is it a sound clip?)
 * <li>11.browser renders response, or offers a download dialog for unrecognized types
 * </ol>
 * <p>
 * <ol>
 * <li>1. You enter a URL into the browser（输入一个url地址）
 * <li>2.The browser looks up the IP address for the domain name（浏览器查找域名的ip地址）
 * <li>3. The browser sends a HTTP request to the web server(浏览器给web服务器发送一个HTTP请求)
 * <li>4. The web server responds with a permanent redirect (WEB服务的永久重定向响应)
 * <li>5. The browser follows the redirect(浏览器跟踪重定向地址)
 * <li>6. The server ‘handles’ the request(服务器“处理”请求)
 * <li>7. The server sends back a HTML response(服务器发回一个HTML响应)
 * <li>8. The browser begins rendering the HTML(浏览器开始显示HTML)
 * <li>9. The browser sends requests for objects embedded in HTML(浏览器发送获取嵌入在HTML中的对象)
 * <li>10. The browser sends further asynchronous (AJAX) requests(浏览器发送异步（AJAX）请求)
 * </ol>
 * 
 * DNS查找过程如下：
 * <ol>
 * <li>浏览器缓存 – 浏览器会缓存DNS记录一段时间。 有趣的是，操作系统没有告诉浏览器储存DNS记录的时间，这样不同浏览器会储存个自固定的一个时间（2分钟到30分钟不等）。
 * <li>系统缓存 – 如果在浏览器缓存里没有找到需要的记录，浏览器会做一个系统调用（windows里是gethostbyname）。这样便可获得系统缓存中的记录。
 * <li>路由器缓存 – 接着，前面的查询请求发向路由器，它一般会有自己的DNS缓存。
 * <li>ISP DNS 缓存 – 接下来要check的就是ISP缓存DNS的服务器。在这一般都能找到相应的缓存记录。
 * <li>递归搜索 – 你的ISP的DNS服务器从跟域名服务器开始进行递归搜索，从.com顶级域名服务器到Facebook的域名服务器。一般DNS服务器的缓存中会有.com域名服务器中的域名，所以到顶级服务器的匹配过程不是那么必要了。
 * </ol>
 * 
 * 1、本地过程
 * 
 * @author hanjy
 *
 */
public class URLDemo {

    public static void main(String[] args) throws Exception {
    }

    /**
     * web 开发中通过问号（？）方式在浏览器地址栏中传值时。浏览器是通过“&”来区分问号后的参数个数的。
     * 如果出现传值参数中带有“&”时，在接受页面就会出现错误，类似如下请求路径：/next.jsp?param1=hendhs89&furej & param2=sss 参数param1中含有转义字符“&”
     * ，这样会导致被请求页的参数接收错误。 在传值前 通过 java.net.URLEncoder.encode(param1) 编码处理后，可将转义字符转为16进制;
     * 
     * <li>1. + URL 中+号表示空格 %2B
     * <li>2. 空格 URL中的空格可以用+号或者编码 %20
     * <li>3. / 分隔目录和子目录 %2F
     * <li>4. ? 分隔实际的 URL 和参数 %3F
     * <li>5. % 指定特殊字符 %25
     * <li>6. # 表示书签 %23
     * <li>7. & URL中指定的参数间的分隔符%26
     * <li>8. = URL中指定参数的值 %3D
     * <li>9. ! URL中指定参数的值 %21
     */
    @Test
    public void urlEncodeDemo() throws UnsupportedEncodingException {
        String message = "aX92rOy5T06FkRP03I3lV1RjCTLndx1XfVZjXxgK65ECACT482pWTidyJmQuSLnGWYNM8PyU96lg86gWWX/y/g9pjJKbj5BxkkRx1wbtYUdq3M3W+ciRcEcduSxM3zeeAqwhrx2x3CIV3uxwkgbVJamApLVkjgMRAvXThLetFnq3pjF58QGByIrbMuwkVt5n";

        String r = URLEncoder.encode(message, "UTF8");
        System.out.println(r);

        String a = URLDecoder.decode("%E9%BE%9A%E6%9D%A5%E5%AF%8C", "UTF8");
        System.out.println(a);
        
        System.out.println(URLEncoder.encode("QQ==", "UTF8"));
    }

    static void demo3() {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = "http://www.baidu.com";
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally { // 使用finally块来关闭输入流
            // 关闭流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void demo2() throws MalformedURLException, URISyntaxException {
        String url = "http://localhost:8080/as/image/banner/2/5.2.1.0?id=123#99";
        printURL(new URL(url));
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
