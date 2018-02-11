package demo.javax.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

/**
 * HttpServletRequest接口最常用的方法就是获得请求中的参数，这些参数一般是客户端表单中的数据。
 * 同时，HttpServletRequest接口可以获取由客户端传送的名称，也可以获取产生请求并且接收请求的服务器端主机名及IP地址，还可以获取客户端正在使用的通信协议等信息。
 *
 */
public class ServletRequestDemo {

    public static void main(String[] args) {
        HttpServletRequest httpServletRequest = null;
        ServletRequest servletRequest = null;
    }

    /**
     * 接口HttpServletRequest的常用方法
     * 
     * @param request
     * @throws IOException
     */
    static void demo3(HttpServletRequest request) throws IOException {
        // 返回当前请求的所有属性的名字集合
        request.getAttributeNames();

        // 返回name指定的属性值
        request.getAttribute("name");

        // 返回客户端发送的Cookie
        request.getCookies();

        // 返回和客户端相关的session，如果没有给客户端分配session，则返回null
        request.getSession();

        // 返回和客户端相关的session，如果没有给客户端分配session，则创建一个session并返回
        request.getSession(true);

        // 获取请求中的参数，该参数是由name指定的
        request.getParameter("name");

        // 返回请求中的参数值，该参数值是由name指定的
        request.getParameterValues("name");

        // 返回请求的字符编码方式
        request.getCharacterEncoding();

        // 返回请求体的有效长度
        request.getContentLength();

        // 获取请求的输入流中的数据
        request.getInputStream();

        // 获取发送请求的方式，如get、post
        request.getMethod();

        // 获取请求中所有参数的名字
        request.getParameterNames();

        // 获取请求所使用的协议名称
        request.getProtocol();

        // 获取请求体的数据流
        request.getReader();

        // 获取客户端的IP地址
        request.getRemoteAddr();

        // 获取客户端的名字
        request.getRemoteHost();

        // 返回接受请求的服务器的名字
        request.getServerName();

        // 获取请求的文件的路径
        request.getServletPath();
        // 传输协议
        String locale = request.getLocalName();
        // 请求的地址
        String url = request.getRequestURL().toString();
        // 没有主机名的地址
        String uri = request.getRequestURI();
        // 客户端端口号
        String port = request.getRemotePort() + "";
        // 获取服务器地址
        String localAddr = request.getLocalAddr();
        // 地址后面?请求的参数
        String username = request.getParameter("username");
        // 服务器端口号
        String serverPort = request.getServerPort() + "";
    }

    static void demo(HttpServletRequest request) {

    }

    static void demoHeader(HttpServletRequest request) {
        // 获取单个请求头的值
        request.getHeader("Accept-Language");
        // 获取单个请求头多个值
        Enumeration<String> headers = request.getHeaders("user-agent");
        while (headers.hasMoreElements()) {
        }
        // 获取所有的请求头名
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) { // 根据请求头名得到对应的值
            String name = names.nextElement();
        }
    }

    /**
     * 包含include，返回两个servlet的内容
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    static void demoInclude(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/servlet/RequestDemo9");
        rd.include(request, response);
    }

    /**
     * 请求转发 forward
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    static void demoForward(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("path");// 得到转发器，path可以是相对路径或者绝对路径（因为是服务器发起的）
        rd.forward(request, response);
    }

    /**
     * get方式：先按照原编码得到原始字节码，然后再重新编码。如：name = new String(name.getBytes("ISO-8859-1"),"UTF-8");
     * 
     * post方式：客户端是什么编码，发送的请求数据就是什么编码，根据客户端的编码，告知服务器编码方式。
     * 
     * 如：request.setCharacterEncoding("UTF-8");// 只适合POST请求方式
     * 
     * @param request
     * @throws UnsupportedEncodingException
     */
    static void demoEncoding(HttpServletRequest request) throws UnsupportedEncodingException {
        // get方式
        String name = request.getParameter("name");
        name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
        // Post方式
        request.setCharacterEncoding("UTF-8");// 只适合POST请求方式
    }

    /**
     * 得到请求正文
     * 
     * @throws IOException
     */
    static void demo2(HttpServletRequest request) throws IOException {
        // 获取请求正文
        InputStream in = request.getInputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = in.read(buf)) != -1) {
            System.out.println(new String(buf, 0, len));
        }
    }

    /**
     * 获取参数
     * 
     * @param request
     */
    static void demoParameter(HttpServletRequest request) {
        // 参数不存在为null
        String usernameValue = request.getParameter("username");
        String[] values = request.getParameterValues("username");
        for (String name : values) {
            System.out.println(name);//// 得到某个请求参数的所有值

        }
        // 获取所有的请求参数名
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) { // 根据参数名得到值
            String name = (String) names.nextElement();
            System.out.println(name + "------->" + request.getParameter(name));
        }
        // 利用BeanUtils和 getParameterMap()将对象封装到bean
        Map map = request.getParameterMap();
        Student s = new Student();
        try {
            BeanUtils.populate(s, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] userNames = s.getUsername();
        System.out.println(userNames[0]);// aa
        System.out.println(userNames[1]);// bb
        System.out.println(s.getPassword());// 123
    }

}

class Student {
    private String[] username;
    private String password;

    public String[] getUsername() {
        return username;
    }

    public void setUsername(String[] username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
