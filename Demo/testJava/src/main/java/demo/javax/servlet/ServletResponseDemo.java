package demo.javax.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * <h1>response概述</h1>
 * <p>
 * response是Servlet.service方法的一个参数，类型为javax.servlet.http.HttpServletResponse。
 * 在客户端发出每个请求时，服务器都会创建一个response对象，并传入给Servlet.service()方法。response对象是用来对客户端进行响应的，这说明在service()方法中使用response对象可以完成对客户端的响应工作。
 * response对象的功能分为以下四种：
 * <li>设置响应头信息；
 * <li>发送状态码；
 * <li>设置响应正文；
 * <li>重定向；
 * <li>向客户端写入Cookie
 *
 * repsonse一共提供了两个响应流对象：
 * <li>获取字符流:PrintWriter out = response.getWriter()
 * <li>获取字节流LServletOutputStream out = response.getOutputStream()
 * <p>
 * 注意，在一个请求中，不能同时使用这两个流！也就是说，要么你使用repsonse.getWriter()，要么使用response.getOutputStream()，但不能同时使用这两个流。不然会抛出IllegalStateException异常。
 */
public class ServletResponseDemo {

    public static void main(String[] args) {
        ServletResponse sevletResponse = null;
        HttpServletResponse httpServletResponse = null;
    }

    static void demoResponse(HttpServletResponse response) throws IOException {
        // 将指定的Cookie加入到当前的响应中
        response.addCookie(new Cookie("a", "b"));
        // 将指定的名字和值加入到响应的头信息中
        response.addHeader("a", "b");
        // 返回一个布尔值，判断响应的头部是否被设置
        response.containsHeader("a");
        // 编码指定的URL
        response.encodeURL("url");
        // 使用指定状态码发送一个错误到客户端
        response.sendError(400);
        // 发送一个临时的响应到客户端
        response.sendRedirect("location");
        // 将给出的名字和日期设置响应的头部
        response.setDateHeader("head1", 123);
        // 将给出的名字和值设置响应的头部
        response.setHeader("a", "b");
        // 给当前响应设置状态码
        response.setStatus(200);
        // 设置响应的MIME类型
        response.setContentType("");
    }

    /**
     * 输出随机数字生成验证码图片
     * 
     * @param response
     * @throws IOException
     */
    static void demoPicture(HttpServletResponse response) throws IOException {
        int WIDTH = 500;
        int HEIGHT = 200;
        // 设置不要缓存(3种方式，建议三种都设置，防止浏览器不支持)
        response.addHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");

        // 1.内存图像 BufferedImage
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // 2.创建画笔
        Graphics g = image.getGraphics();

        // 2.1画边框
        g.setColor(Color.GRAY);// 设置边框颜色
        g.drawRect(0, 0, WIDTH, HEIGHT);// 画矩形边框
        // 2.2填充边框
        g.fillRect(1, 1, WIDTH - 1, HEIGHT - 1);

        // 2.3输出验证随机数字4个
        Random r = new Random();
        g.setColor(Color.BLUE);
        int x = 5;
        for (int i = 0; i < 4; i++) {
            g.setFont(new Font("宋体", Font.BOLD, 20));
            g.drawString(r.nextInt(10) + "", x, 20);
            x += 30;
        }

        // 2.4画干扰线
        g.setColor(Color.YELLOW);
        for (int i = 0; i < 9; i++) {

            g.drawLine(r.nextInt(WIDTH), r.nextInt(HEIGHT), r.nextInt(WIDTH), r.nextInt(HEIGHT));
        }

        // 3 利用response输出流输出image
        ImageIO.write(image, "jpeg", response.getOutputStream());
    }

    /**
     * 让客户端以下载方式打开文件，解决中文文件名乱码问题(URLEncoder.encode(name, "UTF-8"))
     * 
     * @throws IOException
     */
    static void demoDownload(HttpServletResponse response, String realPath) throws IOException {
        // 获取文件名
        String name = realPath.substring(realPath.lastIndexOf("\\"));

        // 设置响应头，通知客户端以下载的方式打开文件
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));

        // 构建输入流
        InputStream inputStream = new FileInputStream(realPath);
        // 输出到客户端的流
        OutputStream outputStream = response.getOutputStream();
        int len = -1;
        byte buf[] = new byte[1024];
        while ((len = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
        }
        inputStream.close();
    }

    /**
     * <h2>重定向</h2> 重定向是服务器通知浏览器去访问另一个地址，即再发出另一个请求。 响应码为200表示响应成功，而响应码为302表示重定向。所以完成重定向的第一步就是设置响应码为302。
     * 因为重定向是通知浏览器再第二个请求，所以浏览器需要知道第二个请求的URL，所以完成重定向的第二步是设置Location头，指定第二个请求的URL地址。
     * <li>重定向是两次请求；
     * <li>重定向的URL可以是其他应用，不局限于当前应用；
     * <li>重定向的响应头为302，并且必须要有Location响应头；
     * <li>重定向就不要再使用response.getWriter()或response.getOutputStream()输出数据，不然可能会出现异常；
     * 
     * @param response
     * @throws IOException
     */
    static void demoRedirect(HttpServletResponse response) throws IOException {
        response.setStatus(302);
        response.setHeader("Location", "http://www.itcast.cn");
        // 便捷的重定向方式:sendRedirect()方法会设置响应头为302，以设置Location响应头。
        response.sendRedirect("http://www.itcast.cn");
        // 如果要重定向的URL是在同一个服务器内，那么可以使用相对路径，
        response.sendRedirect("/hello/BServlet");
    }

    /**
     * 设置状态码及其他方法
     * 
     * @param response
     * @throws IOException
     */
    static void demo(HttpServletResponse response) throws IOException {
        // 设置content-type响应头，该头的作用是告诉浏览器响应内容为html类型，编码为utf-8。而且同时会设置response的字符流编码为utf-8
        response.setContentType("text/html;charset=utf-8");
        // 等同与调用
        response.setHeader("content-type", "text/html;charset=utf-8");
        // 设置字符响应流的字符编码为utf-8；
        response.setCharacterEncoding("utf-8");
        // 设置状态码；
        response.setStatus(200);
        // 当发送错误状态码时，Tomcat会跳转到固定的错误页面去，但可以显示错误信息。
        response.sendError(404, "您要查找的资源不存在");
        // 5秒后自动跳转
        response.setHeader("Refresh", "5; URL=http://www.itcast.cn");
        // 控制客户端刷新时间
        response.getOutputStream()
                .write("<meta http-equiv=\"Refresh\" content=\"3;URL=/day05/login.html\">".getBytes());
        // 控制客户端缓存时间
        response.setDateHeader("Expires", System.currentTimeMillis() + 10 * 24 * 1000 * 60 * 60);
    }

    /**
     * 在使用response.getWriter()时需要注意默认字符编码为ISO-8859-1，可以使用response.setCharaceterEncoding(“utf-8”)来设置。这样可以保证输出给客户端的字符都是使用UTF-8编码的！
     * 但客户端浏览器并不知道响应数据是什么编码的！如果希望通知客户端使用UTF-8来解读响应数据，那么还是使用response.setContentType("text/html;charset=utf-8")方法比较好，
     * 因为这个方法不只会调用response.setCharaceterEncoding(“utf-8”)，还会设置content-type响应头，客户端浏览器会使用content-type头来解读响应数据。
     * response.getWriter()是PrintWriter类型，所以它有缓冲区，缓冲区的默认大小为8KB。也就是说，在响应数据没有输出8KB之前，数据都是存放在缓冲区中，而不会立刻发送到客户端。
     * 当Servlet执行结束后，服务器才会去刷新流，使缓冲区中的数据发送到客户端。如果希望响应数据马上发送给客户端：
     * <li>向流中写入大于8KB的数据；
     * <li>调用response.flushBuffer()方法来手动刷新缓冲区；
     * 
     * 
     * @param response
     * @throws IOException
     */
    static void demoPrintWriter(HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        // 设置字符流的字符编码为utf-8
        response.setCharacterEncoding("UTF-8");

        response.setContentType("text/html;charset=utf-8");
    }

}
