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
 * <h4>SSL</h4>
 * <p>
 * SSL 是指安全套接字（Secure Socket Layer），是应用最为广泛的安全协议。它在 TCP/IP
 * 协议之上提供了一条安全通道，可以保证在不安全网络环境下的数据安全。它支持各种加密算法、数字签名、数字证书，可以防御常见的网络攻击。
 * <p>
 * 网络攻击类型及应对策略:根据攻击对消息产生的影响，主要可以划分为三类：窃听、篡改、仿冒。这三种攻击方式，分别会破坏消息的私密性、完整性和通信双方的互信。针对这些攻击的特点，SSL 提供了可靠的防御策略。
 * <p>
 * <li>1)窃听与加密：
 * <p>
 * 为了避免消息被别人窃听，就需要对消息进行加密。加密即按照一定的规则（加密算法）将原消息（明文）转化为对窃听者不可读的密文。
 * 通信双方可以共享一个相同的密钥，用于加密和解密。这称为对称加密或者秘密密钥加密。这种方法的优势是速度快，劣势是难以安全的传递密钥。
 * 为了解决这个问题，就出现了不对称加密或者公共密钥加密。使用这种方法，每一个通信对象都有一个密钥对：公钥和私钥。发送者使用对方的公钥加密，接收者使用自己的私钥解密。
 * 因为公钥是公开的，这就解决了密钥传递的问题。但是，也造成了一些性能的损失。 在实际的应用中，为了避免密钥传递问题同时保证性能，通常使用不对称加密来交换密钥，然后使用对称加密传输数据。
 * 
 * <li>2)篡改与消息摘要：
 * <p>
 * 篡改是指攻击者拦截并修改消息的内容。加密不能避免消息被修改，接收者也无法判断消息是否被修改过。这里，就需要用到消息摘要（Message Digest）技术。
 * 消息摘要是一个表示消息特征的定长字符串。它有两个重要的特征：不可逆性和抗冲突性。不可逆性是指从消息可以计算出消息概要，但是从消息摘要基本不可能得到消息；抗冲突性是指要想产生具有相同摘要值的两条消息是相当困难的。
 * 这两个特性，使消息摘要可以用来验证消息的完整性。发送者使用摘要算法计算出消息摘要，并随同消息发送给对方；接收者收到消息以后，同样会根据消息计算出消息摘要 , 并和收到的摘要对比。如果两者相同，则表示消息没有被篡改。
 * 在实际应用中，消息摘要很少被单独使用，通常用于数字签名和消息认证码（MAC）。 消息认证码是基于消息和密钥生成的消息摘要。它在验证完整性同时，可以在一定程度上完成用户验证功能。数字签名是经过加密的消息摘要。
 * 
 * <li>3)仿冒与数字证书
 * <p>
 * 仿冒即假冒别人进行通信。 消息认证码可以用来验证消息的发送者。数字签名则更进一步，具有不可抵赖性。因为数字签名是使用发送者私钥加密的消息摘要，只有发送者才能产生。
 * 这一切似乎都很完美，使用加密防止窃听，使用摘要防止篡改，使用数字签名防止仿冒，似乎不要什么数字证书。
 * <p>
 * <b>中间人攻击（man-in-the-middle attack）</b>在通信双方交换公钥的过程中，攻击者拦截双方的公钥，并将自己的密钥发送给通信双方。这样，通信双方就会使用攻击者的密钥加密。
 * <p>
 * 要防范中间人攻击，就需要使用数字证书，这里，数字证书主要指由专门的证书颁发机构（CA）授权的证书，称为 CA 证书。CA 证书将证书属主信息和公钥绑定到一起。证书颁发机构负责验证证书申请者的身份信息。
 * 通过可信的第三方（CA），通信方可以向任何人证明其拥有的公钥，其他人也可以通过 CA 验证其证书真伪，这就阻止了中间人攻击。 CA 证书一般包含所有者的公钥 , 所有者的专有名称 , 签发证书的 CA 的专有名称 ,
 * 证书开始生效的日期 , 证书过期日期等信息。与 CA 证书相对应的，还有自签署证书。自签署证书一般用在测试环境中。
 * 
 * <h4>SSL 握手</h4>
 * <p>
 * SSL 连接实现主要分为两部分：SSL 握手和数据传输。前者使用 SSL 握手协议建立连接，后者使用记录协议传输数据。握手是 SSL 客户端和 SSL 服务器端完成认证，并确定用于数据传输的加密密钥的过程。这里，SSL
 * 客户端是指主动发起连接的一端。
 * <p>
 * SSL 握手具体流程如下：
 * <ol>
 * <li>SSL 客户端发送“Hello”消息给 SSL 服务器端，并列出客户端所支持的 SSL
 * 版本、加密算法和摘要算法。在实际应用中，一般将加密算法和摘要算法结合到一起，组成一个加密套件。同时，客户端还会发送一个随机数，用于以后产生加密密钥。
 * <li>SSL 服务器端对客户端的连接请求作出回应。从客户端提供的加密套件列表中，选择要使用的加密套件，并产生另外一个随机数。同时，服务器端会发送自己的数字证书给客户端，里面包含服务器端的认证信息及公共密钥。
 * <li>如果服务器端需要验证客户端，它还会在消息中包含要求客户端提供数字证书的请求。
 * <li>客户端收到服务器端的回应。它首先使用 CA 证书验证服务器端证书的有效性。在此过程中，客户端会检查证书的签名、证书认证路径、有效日期、证书状态等。验证通过后，抽取出其中的公钥。
 * 客户端产生另外一个随机数，并使用服务器端的公钥加密后发送给服务器端。
 * <li>如果服务器端要求客户端提供证书，客户端会将随机数用私钥加密，连同证书发送给服务器端。服务器端对客户端的证书进行验证，并使用客户端的公钥解密收到的随机数。
 * <li>服务器端和客户端基于前面提供的随机数，计算出用于数据加密的共享密钥。
 * <li>客户端将所有握手消息的 MAC 值发送给服务器端，服务器端也将所有握手消息的 MAC 值发送给客户端。这么做是为了防止攻击者在握手过程中篡改了消息内容。
 * <li>客户端和服务器端使用握手过程中产生的加密密钥交换握手结束消息。握手结束，SSL 连接建立。在 SSL 连接建立后，将开始遵循记录协议对数据进行传输。
 * </ol>
 * 在 SSL 连接建立后，将开始遵循记录协议对数据进行传输。其中SSL握手就是使用的HMAC-MD5，俗称challenge协议。
 * HMAC-MD5就可以用一把发送方和接收方都有的key进行计算，而没有这把key的第三方是无法计算出正确的散列值的，这样就可以防止数据被篡改。
 * 
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
