package demo.netty;

import java.io.File;

import javax.net.ssl.SSLException;

import demo.netty.channel.HttpServerHandler;
import demo.netty.channel.HttpsServerHandler;
import demo.netty.channel.PublishHandler;
import demo.netty.channel.TcpServerHandlerDemo1;
import demo.netty.channel.TcpServerStringHandler;
import demo.netty.handler.codec.MyNettyDecoder;
import demo.netty.handler.codec.MyNettyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Netty is an asynchronous event-driven network application framework for rapid development of maintainable high
 * performance protocol servers & clients.
 * 
 * event-driven以及asynchronous。它们都是事件驱动、异步的网络编程框架。
 * <P>
 * <h5>SESSION</h5>
 * <p>
 * Netty中分为两种情况，一种是针对每个TCP连接创建一个新的ChannelHandler实例，另一种是所有TCP连接共用一个ChannelHandler实例。
 * 这两种方式的区别在于ChannelPipeline的addLast方法中添加的是否是新的ChannelHandler实例。
 *
 */
public class NettyDemo {

    public static void main(String[] args) {

    }

    static void demoHttps() throws InterruptedException, SSLException {

        File certificate = new File("/Users/wucao/Desktop/https/1_gw2.vsgames.cn_bundle.crt"); // 证书
        File privateKey = new File("/Users/wucao/Desktop/https/private.pem"); // 私钥
        final SslContext sslContext = SslContext.newServerContext(certificate, privateKey);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 加入SslHandler实现HTTPS
                            SslHandler sslHandler = sslContext.newHandler(ch.alloc());
                            pipeline.addLast(sslHandler);

                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpsServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void demoSSL_TLS() throws InterruptedException, SSLException {

        File certificate = new File("/Users/wucao/Desktop/ssl/cert.crt"); // 证书
        File privateKey = new File("/Users/wucao/Desktop/ssl/private.pem"); // 私钥
        final SslContext sslContext = SslContext.newServerContext(certificate, privateKey);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // SslHandler要放在最前面
                            SslHandler sslHandler = sslContext.newHandler(ch.alloc());
                            pipeline.addLast(sslHandler);

                            pipeline.addLast(new LineBasedFrameDecoder(80));
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));

                            pipeline.addLast(new TcpServerStringHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * Netty的TCP服务器启动时，会创建两个NioEventLoopGroup，一个boss，一个worker：
     * NioEventLoopGroup实际上是一个线程组，可以通过构造方法设置线程数量，默认为CPU核心数*2。boss用于服务器接收新的TCP连接，boss线程接收到新的连接后将连接注册到worker线程。
     * worker线程用于处理IO操作，例如read、write。 Netty中的boss线程类似于MINA的Acceptor thread，work线程和MINA的I/O processor
     * thread类似。不同的一点是MINA的Acceptor thread是单个线程，而Netty的boss是一个线程组。
     * 实际上Netty的ServerBootstrap可以监听多个端口号，如果只监听一个端口号，那么只需要一个boss线程即可，推荐将bossGroup的线程数量设置成1。 Netty中的boss线程类似于MINA的Acceptor
     * thread，work线程和MINA的I/O processor thread类似。不同的一点是MINA的Acceptor thread是单个线程，而Netty的boss是一个线程组。
     * 实际上Netty的ServerBootstrap可以监听多个端口号，如果只监听一个端口号，那么只需要一个boss线程即可，推荐将bossGroup的线程数量设置成1。
     * 
     * 和MINA的I/O processor thread 类似，Netty的worker线程本身数量不多，而且要实时处理IO事件，如果有耗时的业务逻辑阻塞住worker线程，
     * 例如在channelRead中执行一个耗时的数据库查询，会导致IO操作无法进行，服务器整体性能就会下降。Netty 4中可以使用EventExecutorGroup来处理耗时的业务逻辑：
     */
    static void demoThreadModel() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 服务器监听一个端口号，boss线程数建议设置成1
        EventLoopGroup workerGroup = new NioEventLoopGroup(4); // worker线程数设置成4
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        // 创建一个16个线程的线程组来处理耗时的业务逻辑
                        private EventExecutorGroup group = new DefaultEventExecutorGroup(16);

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LineBasedFrameDecoder(80));
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));

                            // 将TcpServerHandler中的业务逻辑放到EventExecutorGroup线程组中执行
                            pipeline.addLast(group, new TcpServerStringHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * Netty实现一个简单的HTTP服务器。
     */
    static void demoHttpServer() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * Netty提供了ChannelGroup来用于保存Channel组，ChannelGroup是一个线程安全的Channel集合，它提供了一些列Channel批量操作。
     * 当一个TCP连接关闭后，对应的Channel会自动从ChannelGroup移除，所以不用手动去移除关闭的Channel。
     * <p>
     * Netty文档关于ChannelGroup的解释： A thread-safe Set that contains open Channels and provides various bulk operations on
     * them. Using ChannelGroup, you can categorize Channels into a meaningful group (e.g. on a per-service or per-state
     * basis.) A closed Channel is automatically removed from the collection, so that you don’t need to worry about the
     * life cycle of the added Channel. A Channel can belong to more than one ChannelGroup.
     */
    static void demo5() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LineBasedFrameDecoder(80));
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new PublishHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * 用一个固定为4字节的前缀Header来指定Body的字节数的一种消息分割方式。
     */
    static void demo4() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 加上自己的Encoder和Decoder
                            pipeline.addLast(new MyNettyDecoder());
                            pipeline.addLast(new MyNettyEncoder());
                            // 针对每个TCP连接创建一个新的ChannelHandler实例。这也是Netty官方文档中推荐的一种方式，不过要保证针对每个连接创建新的ChannelHandler实例：
                            pipeline.addLast(new TcpServerStringHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * Netty使用LengthFieldBasedFrameDecoder来处理这种消息。 下面代码中的new LengthFieldBasedFrameDecoder(80, 0, 4, 0, 4)中包含。 5个参数：
     * <li>maxFrameLength为消息的最大长度，
     * <li>lengthFieldOffset为Header的位置，
     * <li>lengthFieldLength为Header的长度，
     * <li>lengthAdjustment为长度调整（默认Header中的值表示Body的长度，并不包含Header自己），
     * <li>initialBytesToStrip为去掉字节数（默认解码后返回Header+Body的全部内容，这里设为4表示去掉4字节的Header，只留下Body）。
     * 
     * @throws InterruptedException
     */
    static void demo3() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // LengthFieldBasedFrameDecoder按行分割消息，取出body。
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(80, 0, 4, 0, 4));
                            // 再按UTF-8编码转成字符串
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));

                            pipeline.addLast(new TcpServerStringHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * In a stream-based transport such as TCP/IP, received data is stored into a socket receive buffer. Unfortunately,
     * the buffer of a stream-based transport is not a queue of packets but a queue of bytes. It means, even if you sent
     * two messages as two independent packets, an operating system will not treat them as two messages but as just a
     * bunch of bytes. Therefore, there is no guarantee that what you read is exactly what your remote peer wrote.
     * <p>
     * Netty设计上和MINA类似，需要在ChannelPipeline加上一些ChannelHandler用来对原始数据进行处理。
     * 这里用LineBasedFrameDecoder将接收到的数据按行分割，StringDecoder再将数据由字节码转成字符串。
     * 同样，接收到的数据进过加工后，在channelRead事件函数中，msg参数不再是ByteBuf而是String。
     * 
     */
    static void demo2() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // LineBasedFrameDecoder按行分割消息
                            pipeline.addLast(new LineBasedFrameDecoder(800));
                            // 再按UTF-8编码转成字符串
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));

                            pipeline.addLast(new TcpServerStringHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    static void demo1() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TcpServerHandlerDemo1());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
