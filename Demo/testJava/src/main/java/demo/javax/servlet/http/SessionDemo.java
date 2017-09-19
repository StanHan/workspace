package demo.javax.servlet.http;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Session是服务器端使用的一种记录客户端状态的机制，使用上比Cookie简单一些，相应的也增加了服务器的存储压力。
 * 
 * Session是另一种记录客户状态的机制，不同的是Cookie保存在客户端浏览器中，而Session保存在服务器上。客户端浏览器访问服务器的时候，服务器把客户端信息以某种形式记录在服务器上。这就是Session。客户端浏览器再次访问时只需要从该Session中查找该客户的状态就可以了。
 * 如果说Cookie机制是通过检查客户身上的“通行证”来确定客户身份的话，那么Session机制就是通过检查服务器上的“客户明细表”来确认客户身份。Session相当于程序在服务器上建立的一份客户档案，客户来访的时候只需要查询客户档案表就可以了。
 * 
 * Session对象是在客户端第一次请求服务器的时候创建的。Session也是一种key-value的属性对。
 * 
 * 服务器创建session出来后，会把session的id号，以cookie的形式回写给客户机， 这样，只要客户机的浏览器不关，再去访问服务器时，都会带着session的id号去，服务器发现客户机浏览器带session
 * id过来了，就会使用内存中与之对应的session为之服务。 该Cookie为服务器自动生成的，它的maxAge属性一般为–1，表示仅当前浏览器内有效，并且各浏览器窗口间不共享，关闭浏览器就会失效。
 * 因此同一机器的两个浏览器窗口访问服务器时，会生成两个不同的Session。但是由浏览器窗口内的链接、脚本等打开的新窗口，这类子窗口会共享父窗口的Cookie，因此会共享一个Session。
 * 新开的浏览器窗口会生成新的Session，但子窗口除外。子窗口会共用父窗口的Session。
 * 
 * Servlet中必须使用request来编程式获取HttpSession对象，而JSP中内置了Session隐藏对象，可以直接使用。如果使用声明了<%@page session="false" %>，则Session隐藏对象不可用。
 * 
 * 当多个客户端执行程序时，服务器会保存多个客户端的Session。获取Session的时候也不需要声明获取谁的Session。Session机制决定了当前客户只会获取到自己的Session，而不会获取到别人的Session。各客户的Session也彼此独立，互不可见。
 * 
 * Session在用户第一次访问服务器的时候自动创建。需要注意只有访问JSP、Servlet等程序时才会创建Session，只访问HTML、IMAGE等静态资源并不会创建Session。
 * Session生成后，只要用户继续访问，服务器就会更新Session的最后访问时间，并维护该Session。用户每访问服务器一次，无论是否读写Session，服务器都认为该用户的Session“活跃（active）”了一次。
 * 
 * URL地址重写是对客户端不支持Cookie的解决方案。URL地址重写的原理是将该用户Session的id信息重写到URL地址中。服务器能够解析重写后的URL获取Session的id。这样即使客户端不支持Cookie，也可以使用Session来记录用户状态。
 * HttpServletResponse类提供了encodeURL(Stringurl)实现URL地址重写.
 * 
 * 该方法会自动判断客户端是否支持Cookie。如果客户端支持Cookie，会将URL原封不动地输出来。如果客户端不支持Cookie，则会将用户Session的id重写到URL中。
 * 
 * 例如：<a href="<%=response.encodeURL("index.jsp?c=1&wd=Java") %>"> Homepage</a>重写后的输出可能是这样的：
 * <a href="index.jsp;jsessionid=0CCD096E7F8D97B0BE608AFDC3E1931E?c=1&wd=Java">Homepage</a>
 * 
 * 注意：TOMCAT判断客户端浏览器是否支持Cookie的依据是请求中是否含有Cookie。
 * 尽管客户端可能会支持Cookie，但是由于第一次请求时不会携带任何Cookie（因为并无任何Cookie可以携带），URL地址重写后的地址中仍然会带有jsessionid。
 * 当第二次访问时服务器已经在浏览器中写入Cookie了，因此URL地址重写后的地址中就不会带有jsessionid了。
 * 
 * Session中禁止使用Cookie。Java Web规范支持通过配置的方式禁用Cookie。<Context cookies="false"></Context>
 * 该配置只是禁止Session使用Cookie作为识别标志，并不能阻止其他的Cookie读写。也就是说服务器不会自动维护名为JSESSIONID的Cookie了，但是程序中仍然可以读写其他的Cookie。
 */
public class SessionDemo extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF=8");
        response.setContentType("text/html;charset=UTF-8");
        // 使用request对象的getSession()获取session，如果session不存在则创建一个
        HttpSession session = request.getSession(false);
        // 将数据存储到session中
        session.setAttribute("data", "孤傲苍狼");
        // 如果超过了超时时间没访问过服务器，Session就自动失效了。Session的超时时间也可以在web.xml中修改。注意：<session-timeout>参数的单位为分钟，而setMaxInactiveInterval(int
        // s)单位为秒。
        session.setMaxInactiveInterval(20);
        // 使Session失效。
        session.invalidate();
        // 返回Session中存在的属性名
        Enumeration<String> attributeNames = session.getAttributeNames();
        // 获取session的Id
        String sessionId = session.getId();
        // 如果客户端支持Cookie，会将URL原封不动地输出来。如果客户端不支持Cookie，则会将用户Session的id重写到URL中。
        response.encodeURL("index.jsp?c=1&wd=Java");
        // 判断session是不是新创建的
        if (session.isNew()) {
            response.getWriter().print("session创建成功，session的id是：" + sessionId);
        } else {
            response.getWriter().print("服务器已经存在该session了，session的id是：" + sessionId);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
