package demo.netty.channel;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

        if (msg instanceof HttpRequest) {

            // 请求，解码器将请求转换成HttpRequest对象
            HttpRequest request = (HttpRequest) msg;

            // 获取请求参数
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
            String name = "World";
            if (queryStringDecoder.parameters().get("name") != null) {
                name = queryStringDecoder.parameters().get("name").get(0);
            }

            // 响应HTML
            String responseHtml = "<html><body>Hello, " + name + "</body></html>";
            byte[] responseBytes = responseHtml.getBytes("UTF-8");
            int contentLength = responseBytes.length;

            // 构造FullHttpResponse对象，FullHttpResponse包含message body
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(responseBytes));
            response.headers().set("Content-Type", "text/html; charset=utf-8");
            response.headers().set("Content-Length", Integer.toString(contentLength));

            // 发送数据到客户端。异步网络编程中，由于IO操作是异步的，也就是一个IO操作不会阻塞去等待操作结果，程序就会继续向下执行。
            // 执行完write语句后并不表示数据已经发送出去，而仅仅是开始发送这个数据，程序并不阻塞等待发送完成，而是继续往下执行。
            // 所以如果有某些操作想在write完成后再执行，例如write完成后关闭连接，下面这些写法就有问题了，它可能会在数据write出去之前先关闭连接：
            // 那么如何在IO操作完成后再做一些其他操作？当异步IO操作完成后，无论成功或者失败，都会再通知程序。至于如何处理发送完成的通知，在MINA、Netty、Twisted中，都会有类似回调函数的实现方式。
            ChannelFuture future = ctx.writeAndFlush(response);

            future.addListener(new ChannelFutureListener() {

                // write操作完成后调用的回调函数
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) { // 是否成功
                        System.out.println("write操作成功");
                    } else {
                        System.out.println("write操作失败");
                    }
                    // close操作是在operationComplete中进行，这样可以保证不会在write完成之前close连接。注意close关闭连接同样是异步的，其返回值也是ChannelFuture。
                    ctx.close(); // 如果需要在write后关闭连接，close应该写在operationComplete中。注意close方法的返回值也是ChannelFuture
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
