package demo.mina.client;

import java.net.InetSocketAddress;
import java.util.Scanner;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import demo.mina.core.service.ClientMessageHandlerAdapter;
import demo.mina.filter.codec.CharsetCodecFactory;

/**
 * mina客户端
 *
 */
public class MinaClient {

    public static void main(String[] args) {
        MinaClient client = new MinaClient();
        if (client.connect()) {
            client.send("连接服务器成功！");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                client.send(scanner.next());
            }
        }
    }

    private SocketConnector socketConnector;
    private ConnectFuture connectFuture;
    private IoSession ioSession;

    public boolean connect() {

        // 创建一个socket连接
        socketConnector = new NioSocketConnector();
        // 设置链接超时时间
        socketConnector.setConnectTimeoutMillis(3000);
        // 获取过滤器链
        DefaultIoFilterChainBuilder filterChain = socketConnector.getFilterChain();
        // 添加编码过滤器 处理乱码、编码问题
        filterChain.addLast("codec", new ProtocolCodecFilter(new CharsetCodecFactory()));

        // 日志
        LoggingFilter loggingFilter = new LoggingFilter();
        loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
        loggingFilter.setMessageSentLogLevel(LogLevel.INFO);
        filterChain.addLast("loger", loggingFilter);

        // 消息核心处理器
        socketConnector.setHandler(new ClientMessageHandlerAdapter());

        // 连接服务器，知道端口、地址
        connectFuture = socketConnector.connect(new InetSocketAddress(3456));
        // 等待连接创建完成
        connectFuture.awaitUninterruptibly();
        // 获取当前session
        ioSession = connectFuture.getSession();
        return true;
    }

    public void setAttribute(Object key, Object value) {
        ioSession.setAttribute(key, value);
    }

    public void send(String message) {
        ioSession.write(message);
    }

    public boolean close() {
        CloseFuture future = ioSession.getCloseFuture();
        future.awaitUninterruptibly(1000);
        socketConnector.dispose();
        return true;
    }

    public SocketConnector getConnector() {
        return socketConnector;
    }

    public IoSession getSession() {
        return ioSession;
    }
}
