package demo.netty.channel;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 所有TCP连接共用一个ChannelHandler实例。 在这种情况下，就不能把连接相关的信息放在ChannelHandler实现类的成员变量中了，否则这些信息会被其他连接共用。
 * 这里就要使用到ChannelHandlerContext的Attribute了。
 * <p>
 * Netty文档节选： Although it’s recommended to use member variables to store the state of a handler, for some reason you
 * might not want to create many handler instances. In such a case, you can use AttributeKeys which is provided by
 * ChannelHandlerContext.
 * 
 * <p>
 * 多个连接使用同一个ChannelHandler，要加上@Sharable注解
 */
//@Sharable
public class TcpServerHandlerDemo1 extends ChannelInboundHandlerAdapter {

    private AttributeKey<Integer> attributeKey = AttributeKey.valueOf("counter");

    // 接收到新的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

        Attribute<Integer> attribute = ctx.attr(attributeKey);

        int counter = 1;

        if (attribute.get() == null) {
            attribute.set(1);
        } else {
            counter = attribute.get();
            counter++;
            attribute.set(counter);
        }

        try {
            // 接收客户端的数据
            ByteBuf in = (ByteBuf) msg;
            System.out.println("channelRead:" + in.toString(CharsetUtil.UTF_8));
            // 发送到客户端
            byte[] responseByteArray = "你好".getBytes("UTF-8");
            ByteBuf out = ctx.alloc().buffer(responseByteArray.length);
            out.writeBytes(responseByteArray);
            ctx.writeAndFlush(out);
        } finally {
            ReferenceCountUtil.release(msg);
        }
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
