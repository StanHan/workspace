package demo.mina;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import demo.mina.core.service.TcpPublishHandler;
import demo.mina.core.service.TcpServerHandleDemo1;
import demo.mina.core.service.TcpServerStringHandle;
import demo.mina.filter.codec.MyMinaDecoder;
import demo.mina.filter.codec.MyMinaEncoder;

/**
 * 
 * Apache MINA is a network application framework which helps users develop high performance and high scalability
 * network applications easily. It provides an abstract event-driven asynchronous API over various transports such as
 * TCP/IP and UDP/IP via Java NIO.
 * 
 * event-driven以及asynchronous。它们都是事件驱动、异步的网络编程框架。
 * 
 * Apache的Mina（Multipurpose Infrastructure Networked Applications）是一个网络应用框架，可以帮助用户开发高性能和高扩展性的网络应用程序；
 * 它提供了一个抽象的、事件驱动的异步API，使Java NIO在各种传输协议（如TCP/IP，UDP/IP协议等）下快速高效开发。
 * 
 * TCP是基于字节流的协议，它只能保证一方发送和另一方接收到的数据的字节顺序一致，但是，并不能保证一方每发送一条消息，另一方就能完整的接收到一条信息。有可能发送了两条对方将其合并成一条，也有可能发送了一条对方将其拆分成两条。
 * 对此，MINA的官方文档提供了以下几种解决方案：
 * <li>1、use fixed length messages。 使用固定长度的消息。比如每个长度4字节，那么接收的时候按每条4字节拆分就可以了。
 * <li>2、use a fixed length header that indicates the length of the body。
 * 使用固定长度的Header，Header中指定Body的长度（字节数），将信息的内容放在Body中。例如Header中指定的Body长度是100字节，那么Header之后的100字节就是Body，也就是信息的内容，100字节的Body后面就是下一条信息的Header了。
 * <li>3、using a delimiter; for example many text-based protocols append a newline (or CR LF pair) after every message。
 * 使用分隔符。例如许多文本内容的协议会在每条消息后面加上换行符（CR LF，即”\r\n”），也就是一行一条消息。当然也可以用其他特殊符号作为分隔符，例如逗号、分号等等。
 * 
 * 当然除了上面说到的3种方案，还有其他方案。有的协议也可能会同时用到上面多种方案。例如HTTP协议，Header部分用的是CR LF换行来区分每一条Header，而Header中用Content-Length来指定Body字节数。
 * 
 * <p>
 * 在MINA中，有三种非常重要的线程：Acceptor thread、Connector thread、I/O processor thread。 官方文档的介绍：In MINA, there are three kinds of
 * I/O worker threads in the NIO socket implementation.
 * <li>Acceptor thread accepts incoming connections, and forwards the connection to the I/O processor thread for read
 * and write operations. Each SocketAcceptor creates one acceptor thread. You can’t configure the number of the acceptor
 * threads.这个线程用于TCP服务器接收新的连接，并将连接分配到I/O processor thread，由I/O processor thread来处理IO操作。每个NioSocketAcceptor创建一个Acceptor
 * thread，线程数量不可配置。
 * 
 * <li>Connector thread attempts connections to a remote peer, and forwards the succeeded connection to the I/O
 * processor thread for read and write operations. Each SocketConnector creates one connector thread. You can’t
 * configure the number of the connector threads, either.用于处理TCP客户端连接到服务器，并将连接分配到I/O processor thread，由I/O processor
 * thread来处理IO操作。每个NioSocketConnector创建一个Connector thread，线程数量不可配置。
 * 
 * <li>I/O processor thread performs the actual read and write operation until the connection is closed. Each
 * SocketAcceptor or SocketConnector creates its own I/O processor thread(s). You can configure the number of the I/O
 * processor threads. The default maximum number of the I/O processor threads is the number of CPU cores + 1。
 * 用于处理TCP连接的I/O操作，如read、write。I/O processor thread的线程数量可通过NioSocketAcceptor或NioSocketConnector构造方法来配置，默认是CPU核心数+1。
 * <p>
 * 由于本文主要介绍TCP服务器的线程模型，所以就没有Connector thread什么事了。下面说下Acceptor thread和I/O processor thread处理TCP连接的流程：
 * MINA的TCP服务器包含一个Acceptor thread和多个I/O processor thread，当有新的客户端连接到服务器，首先会由Acceptor thread获取到这个连接， 同时将这个连接分配给多个I/O
 * processor thread中的一个线程，当客户端发送数据给服务器，对应的I/O processor thread负责读取这个数据，并执行IoFilterChain中的IoFilter以及IoHandle。 由于I/O
 * processor thread本身数量有限，通常就那么几个，但是又要处理成千上万个连接的IO操作，包括read、write、协议的编码解码、各种Filter以及IoHandle中的业务逻辑，
 * 特别是业务逻辑，比如IoHandle的messageReceived，如果有耗时、阻塞的任务，例如查询数据库，那么就会阻塞I/O processor thread，导致无法及时处理其他IO事件，服务器性能下降。
 * 针对这个问题，MINA中提供了一个ExecutorFilter，用于将需要执行很长时间的会阻塞I/O processor thread的业务逻辑放到另外的线程中，这样就不会阻塞I/O processor
 * thread，不会影响IO操作。 ExecutorFilter中包含一个线程池，默认是OrderedThreadPoolExecutor，这个线程池保证同一个连接的多个事件按顺序依次执行，
 * 另外还可以使用UnorderedThreadPoolExecutor，它不会保证同一连接的事件的执行顺序，并且可能会并发执行。二者之间可以根据需要来选择。
 *
 */
public class MinaDemo {
    public static void main(String[] args) throws IOException {

    }

    public static void demoHttps() throws Exception {

        String certPath = "/Users/wucao/Desktop/https/1_gw2.vsgames.cn_bundle.crt"; // 证书
        String privateKeyPath = "/Users/wucao/Desktop/https/private.der"; // 私钥

        // 证书
        // https://docs.oracle.com/javase/7/docs/api/java/security/cert/X509Certificate.html
        InputStream inStream = null;
        Certificate certificate = null;
        try {
            inStream = new FileInputStream(certPath);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            certificate = cf.generateCertificate(inStream);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }

        // 私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Files.readAllBytes(new File(privateKeyPath).toPath()));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        Certificate[] certificates = { certificate };
        ks.setKeyEntry("key", privateKey, "".toCharArray(), certificates);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, "".toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        IoAcceptor acceptor = new NioSocketAcceptor();
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("ssl", new SslFilter(sslContext)); // SslFilter + HttpServerCodec实现HTTPS
//        chain.addLast("codec", new HttpServerCodec());
//        acceptor.setHandler(new HttpServerHandle());
        acceptor.bind(new InetSocketAddress(8080));
    }

    /**
     * MINA对私钥文件的格式的要求，要将pem格式转换成der格式，实际上就是将文本文件私钥转成二进制文件私钥。openssl将private.pem转成private.der私钥文件：
     * <li>openssl pkcs8 -topk8 -inform PEM -in private.pem -outform DER -nocrypt -out private.der
     * 
     */
    public static void demoSSL_TLS() throws Exception {

        String certPath = "/Users/wucao/Desktop/ssl/cert.crt"; // 证书
        String privateKeyPath = "/Users/wucao/Desktop/ssl/private.der"; // 私钥

        // 证书
        // https://docs.oracle.com/javase/7/docs/api/java/security/cert/X509Certificate.html

        Certificate certificate = null;
        try (InputStream inStream = new FileInputStream(certPath);) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            certificate = cf.generateCertificate(inStream);
        }

        // 私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Files.readAllBytes(new File(privateKeyPath).toPath()));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        Certificate[] certificates = { certificate };
        ks.setKeyEntry("key", privateKey, "".toCharArray(), certificates);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, "".toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        IoAcceptor acceptor = new NioSocketAcceptor();
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("ssl", new SslFilter(sslContext)); // SslFilter需要放在最前面
        chain.addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), "\r\n", "\r\n")));
        acceptor.setHandler(new TcpServerStringHandle());
        acceptor.bind(new InetSocketAddress(8080));
    }

    /**
     * MINA中提供了一个ExecutorFilter，用于将需要执行很长时间的会阻塞I/O processor thread的业务逻辑放到另外的线程中，这样就不会阻塞I/O processor thread，不会影响IO操作。
     */
    static void demoExecutor() throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor(4); // 配置I/O processor thread线程数量
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
        acceptor.getFilterChain().addLast("executor", new ExecutorFilter()); // 将TcpServerHandle中的业务逻辑拿到ExecutorFilter的线程池中执行
        acceptor.setHandler(new TcpServerStringHandle());
        acceptor.bind(new InetSocketAddress(8080));
    }

    /**
     * 发布/订阅（Publish/Subscribe）是一种服务器主动发送消息到客户端的消息传递方式。
     * 订阅者Subscriber连接到服务器客户端后，相当于开始订阅发布者Publisher发布的消息，当发布者发布了一条消息后，所有订阅者都会接收到这条消息。
     * 网络聊天室一般就是基于发布/订阅模式来实现。例如加入一个QQ群，就相当于订阅了这个群的所有消息，当有新的消息，服务器会主动将消息发送给所有的客户端。只不过聊天室里的所有人既是发布者又是订阅者。
     * 
     * 简单的发布/订阅模式的服务器程序，连接到服务器的所有客户端都是订阅者，当发布者发布一条消息后，服务器会将消息转发给所有客户端。
     */
    static void demo5() throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();

        acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), "\r\n", "\r\n")));

        acceptor.setHandler(new TcpPublishHandler());
        acceptor.bind(new InetSocketAddress(8080));
    }

    /**
     * 用一个固定为4字节的前缀Header来指定Body的字节数的一种消息分割方式
     */
    static void demo4() throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();

        // 指定编码解码器
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyMinaEncoder(), new MyMinaDecoder()));

        acceptor.setHandler(new TcpServerStringHandle());
        acceptor.bind(new InetSocketAddress(8080));
    }

    /**
     * use a fixed length header that indicates the length of the body，用一个固定字节数的Header前缀来指定Body的字节数，以此来分割消息。
     * Header固定为4字节，Header中保存的是一个4字节（32位）的整数，例如12即为0x0000000C，这个整数用来指定Body的长度（字节数）。当读完这么多字节的Body之后，又是下一条消息的Header。
     */
    static void demo3() throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();

        // 4字节的Header指定Body的字节数，对这种消息的处理
        acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));

        acceptor.setHandler(new TcpServerStringHandle());
        acceptor.bind(new InetSocketAddress(8080));
    }

    /**
     * TCP guarantess delivery of all packets in the correct order. But there is no guarantee that one write operation
     * on the sender-side will result in one read event on the receiving side. One call of IoSession.write(Object
     * message) by the sender can result in multiple messageReceived(IoSession session, Object message) events on the
     * receiver; and multiple calls of IoSession.write(Object message) can lead to a single messageReceived event.
     * <p>
     * MINA可以使用ProtocolCodecFilter来对发送和接收的二进制数据进行加工，如何加工取决于ProtocolCodecFactory或ProtocolEncoder、ProtocolDecoder，
     * 加工后在IoHandler中messageReceived事件函数获取的message就不再是IoBuffer了，而是你想要的其他类型，可以是字符串，Java对象。
     * 这里可以使用TextLineCodecFactory（ProtocolCodecFactory的一个实现类）实现CR LF分割消息。
     */
    static void demoTextLineCodec() throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();

        // 添加一个Filter，用于接收、发送的内容按照"\r\n"分割
        acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), "\r\n", "\r\n")));

        acceptor.setHandler(new TcpServerStringHandle());
        acceptor.bind(new InetSocketAddress(8080));
    }

    /**
     * 最简单的TCP服务器。当接收到客户端发过来的字符串后，向客户端回写一个字符串作为响应。 这个框架实现的TCP服务器，在连接建立、接收到客户端传来的数据、连接关闭时，都会触发某个事件。
     * 例如接收到客户端传来的数据时，MINA会触发事件调用messageReceived，Netty会调用channelRead，Twisted会调用dataReceived。
     * 编写代码时，只需要继承一个类并重写响应的方法即可。这就是event-driven事件驱动。
     */
    static void demo1() throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(new TcpServerHandleDemo1());
        acceptor.bind(new InetSocketAddress(8080));
    }
}
