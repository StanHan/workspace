package demo.mina.core.service;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 每当一个客户端连接到服务器，就会创建一个新的IoSession，直到客户端断开连接才会销毁。 IoSession可以用setAttribute和getAttribute来存储和获取一个TCP连接的相关信息。
 * <p>
 * The Session is at the heart of MINA : every time a client connects to the server, a new session is created, and will
 * be kept in memory until the client is disconnected. A session is used to store persistent informations about the
 * connection, plus any kind of information the server might need to use during the request processing, and eventually
 * during the whole session life.
 */
public class TcpServerStringHandle extends IoHandlerAdapter {
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    // 接收到新的数据
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        // 接收客户端的数据，这里接收到的不再是IoBuffer类型，而是字符串
        String line = (String) message;
        System.out.println("messageReceived:" + line);

        int counter = 1;
        // 第一次请求，创建session中的counter
        if (session.getAttribute("counter") == null) {
            session.setAttribute("counter", 1);
        } else {
            // 获取session中的counter，加1后再存入session
            counter = (Integer) session.getAttribute("counter");
            counter++;
            session.setAttribute("counter", counter);
        }

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("sessionCreated");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed");
    }
}