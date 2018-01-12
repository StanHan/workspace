package demo.netty.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 当有新的客户端连接到服务器，将对应的Channel加入到一个ChannelGroup中，当发布者发布消息时，服务器可以将消息通过ChannelGroup写入到所有客户端
 *
 */
public class PublishHandler extends ChannelInboundHandlerAdapter {

    // ChannelGroup用于保存所有连接的客户端，注意要用static来保证只有一个ChannelGroup实例，否则每new一个TcpServerHandler都会创建一个ChannelGroup
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channels.add(ctx.channel()); // 将新的连接加入到ChannelGroup，当连接断开ChannelGroup会自动移除对应的Channel
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        channels.writeAndFlush(msg + "\r\n"); // 接收到消息后，将消息发送到ChannelGroup中的所有客户端
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // cause.printStackTrace(); 暂时把异常打印注释掉，因为PublishClient发布一条消息后会立即断开连接，而服务器也会向PublishClient发送消息，所以会抛出异常
        ctx.close();
    }
}
