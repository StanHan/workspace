package demo.java.security;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * <h4>什么是SSL/TLS</h4>
 * <p>
 * 不使用SSL/TLS的网络通信，一般都是明文传输，网络传输内容在传输过程中很容易被窃听甚至篡改，非常不安全。SSL/TLS协议就是为了解决这些安全问题而设计的。
 * SSL/TLS协议位于TCP/IP协议之上，各个应用层协议之下，使网络传输的内容通过加密算法加密，并且只有服务器和客户端可以加密解密，中间人即使抓到数据包也无法解密获取传输的内容，从而避免安全问题。
 * 例如广泛使用的HTTPS协议即是在TCP协议和HTTP协议之间加了一层SSL/TLS协议，
 * <h4>相关术语</h4>
 * <p>
 * <li>对称加密：加密和解密都采用同一个密钥，常用的算法有DES、3DES、AES，相对于非对称加密算法更简单速度更快。
 * <li>非对称加密：和对称加密算法不同，非对称加密算法会有两个密钥：公钥（可以公开的）和私钥（私有的），例如客户端如果使用公钥加密，那么即时其他人有公钥也无法解密，只能通过服务器私有的私钥解密。RSA算法即是典型的非对称加密算法。
 * <li>数字证书：数字证书是一个包含公钥并且通过权威机构发行的一串数据，数字证书很多需要付费购买，也有免费的，另外也可以自己生成数字证书，本文中将会采用自签名的方式生成数字证书。
 * 
 * <h4>SSL/TLS流程</h4>
 * <p>
 * 使用SSL/TLS协议的服务器和客户端开始通信之前，会先进行一个握手阶段：
 * <ol>
 * <li>客户端发出请求：这一步客户端会生成一个随机数传给服务器；
 * <li>服务器回应：这一步服务器会返回给客户端一个服务器数字证书（证书中包含用于加密的公钥），另外服务器也会生成一个随机数给客户端；
 * <li>客户端回应：这一步客户端首先会校验数字证书的合法性，然后会再生成一个随机数，这个随机数会使用第2步中的公钥采用非对称加密算法（例如RSA算法）进行加密后传给服务器，密文只能通过服务器的私钥来解密。
 * <li>服务器最后回应：握手结束。
 * </ol>
 * 握手结束后，客户端和服务器都有上面握手阶段的三个随机数。客户端和服务器都通过这三个随机生成一个密钥，接下来所有的通信内容都使用这个密钥通过对称加密算法加密传输，服务器和客户端才开始进行安全的通信。
 * 如果看到这里还是一脸懵逼，可以参考SSL/TLS协议运行机制的概述更深入地了解SSL/TLS流程，本文不再过多介绍。
 * 
 * <h4>生成私钥和证书</h4>
 * <p>
 * 使用openssl来生成私钥和证书：
 * <li>openssl req -x509 -newkey rsa:2048 -nodes -days 365 -keyout private.pem -out cert.crt
 * <p>
 * 运行以上命令后，会在当前目录下生成一个私钥文件（private.pem）和一个证书文件（cert.crt）。
 * <p>
 * 生成的私钥和证书Twisted、Netty可以直接使用，然而MINA对私钥文件的格式的要求，要将pem格式转换成der格式，实际上就是将文本文件私钥转成二进制文件私钥。openssl将private.pem转成private.der私钥文件：
 * <li>openssl pkcs8 -topk8 -inform PEM -in private.pem -outform DER -nocrypt -out private.der
 * 
 * 
 */
public class SSL_TLS {

    public static void main(String[] args) {

    }

    static void demoClint() throws Exception {

        // 客户端信任改证书，将用于校验服务器传过来的证书的合法性
        String certPath = "/Users/wucao/Desktop/ssl/cert.crt";

        Certificate certificate = null;
        try (InputStream inStream = new FileInputStream(certPath);) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            certificate = cf.generateCertificate(inStream);
        }

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("cert", certificate);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();

        try (Socket socket = socketFactory.createSocket("localhost", 8080);
                OutputStream out = socket.getOutputStream();) {
            // 请求服务器
            String lines = "床前明月光\r\n疑是地上霜\r\n举头望明月\r\n低头思故乡\r\n";
            byte[] outputBytes = lines.getBytes("UTF-8");
            out.write(outputBytes);
            out.flush();
        }

    }
}
