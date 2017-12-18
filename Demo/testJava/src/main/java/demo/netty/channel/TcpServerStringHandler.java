package demo.netty.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 针对每个TCP连接创建一个新的ChannelHandler实例是最常用的一种方式。这种情况非常简单，直接在ChannelHandler的实现类中加入一个成员变量即可保存连接相关的信息。
 *
 */
public class TcpServerStringHandler extends ChannelInboundHandlerAdapter {

    // 连接相关的信息直接保存在TcpServerHandler的成员变量中
    private int counter = 0;

    // 接收到新的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        // msg经过StringDecoder后类型不再是ByteBuf而是String
        String line = (String) msg;
        System.out.println("channelRead:" + line);

        counter++;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
