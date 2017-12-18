package demo.mina.core.service;

import java.util.Collection;

import org.apache.mina.core.IoUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 简单的发布/订阅模式的服务器程序，连接到服务器的所有客户端都是订阅者，当发布者发布一条消息后，服务器会将消息转发给所有客户端。
 * <p>
 * 通过IoService的getManagedSessions()方法可以获取这个IoService当前管理的所有IoSession，即所有连接到服务器的客户端集合。
 * 当服务器接收到发布者发布的消息后，可以通过IoService的getManagedSessions()方法获取到所有客户端对应的IoSession并将消息发送到这些客户端。
 */
public class TcpPublishHandler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        // 获取所有正在连接的IoSession
        Collection<IoSession> sessions = session.getService().getManagedSessions().values();

        // 将消息写到所有IoSession
        IoUtil.broadcast(message, sessions);
    }
}