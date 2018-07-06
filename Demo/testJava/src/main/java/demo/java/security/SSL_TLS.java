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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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

    static void demoClint() throws Exception {

        // 客户端信任改证书，将用于校验服务器传过来的证书的合法性
        String certPath = "/Users/wucao/Desktop/ssl/cert.crt";

        Certificate certificate = null;
        try (InputStream inStream = new FileInputStream(certPath);) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            certificate = cf.generateCertificate(inStream);
        }

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("cert", certificate);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
        tmf.init(keyStore);

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

    /**
     * <h2>X.509</h2>
     * <p>
     * 服务器SSL数字证书和客户端数字证书的格式遵循 X.509标准。 X.509 是由国际电信联盟（ITU-T）制定的数字证书标准。 为了提供公用网络用户目录信息服务， ITU于 1988年制定了X.500系列标准。
     * 其中X.500 和 X.509 是安全认证系统的核心， X.500定义了一种区别命名规则，以命名树来确保用户名称的唯一性；
     * X.509则为X.500用户名称提供了通信实体鉴别机制，并规定了实体鉴别过程中广泛适用的证书语法和数据接口， X.509称之为证书。
     * <p>
     * X.509 给出的鉴别框架是一种基于公开密钥体制的鉴别业务密钥管理。 一个用户有两把密钥：一把是用户的专用密钥(简称为：私钥)，另一把是其他用户都可得到和利用的公共密钥(简称为：公钥) ，通常称之为密钥对，公钥加密，私钥解密。
     * 用户可用常规加密算法（如DES）为信息加密，然后再用接收者的公共密钥对 DES进行加密并将之附于信息之上，这样接收者可用对应的专用密钥打开 DES密锁，并对信息解密。
     * 该鉴别框架允许用户将其公开密钥存放在CA的目录项中。一个用户如果想与另一个用户交换秘密信息，就可以直接从对方的目录项中获得相应的公开密钥，用于各种安全服务。
     * <p>
     * 本质上， X.509 证书由用户公共密钥与用户标识符组成，此外还包括版本号、证书序列号、CA 标识符、签名算法标识、签发者名称、证书有效期等。
     * <p>
     * 用户可通过安全可靠的方式向 CA 提供其公共密钥以获得证书，这样用户就可公开其证书，而任何需要此用户的公共密钥者都能得到此证书，并通过 CA 检验密钥是否正确。
     * 这一标准的最新版本--X.509版本3是针对包含扩展信息的数字证书，提供一个扩展字段，以提供更多的灵活性及特殊环境下所需的信息传送。 为了进行身份认证， X.509
     * 标准及公共密钥加密系统提供了一个称作数字签名的方案。用户可生成一段信息及其摘要（亦称作信息“指纹”）。
     * 用户用专用密钥对摘要加密以形成签名，接收者用发送者的公共密钥对签名解密，并将之与收到的信息“指纹”进行比较，以确定其真实性。
     * <p>
     * 目前， X.509标准已在编排公共密钥格式方面被广泛接受，已用于许多网络安全应用程序，其中包括 IP安全（ Ipsec ）、安全套接层（ SSL ）、安全电子交易（ SET ）、安全多媒体 INTERNET 邮件扩展（
     * S/MIME ）等。
     * <p>
     * 数字证书有多种文件编码格式，主要包含CER编码，DER编码等。 CER(Canonical Encoding Rules, 规范编码格式)，DER(Distinguished Encoding Rules
     * 卓越编码格式)，两者的区别是前者是变长模式，后者是定长模式。 所有证书都符合公钥基础设施(PKI, Public Key Infrastructure)制定的ITU-T X509国际标准(X.509标准)。
     * 
     * <h2>模型分析</h2> 在实际应用中，很多数字证书都属于自签名证书，即证书申请者为自己的证书签名。这类证书通常应用于软件厂商内部发放的产品中，或约定使用该证书的数据交互双方。
     * 数字证书完全充当加密算法的载体，为必要数据做加密解密和签名验签等操作。在我司的开发过程中，数字证书更多是用来做加密和解密。
     * 
     * <h2>CA</h2>
     * <p>
     * CA中心又称CA机构，即证书授权中心(Certificate Authority )，或称证书授权机构，作为电子商务交易中受信任的第三方，承担公钥体系中公钥的合法性检验的责任。
     */
    void x509() {

    }

    /**
     * <h2>数字证书简介</h2>
     * <p>
     * 数字证书具备常规加密解密必要的信息，包含签名算法，可用于网络数据加密解密交互，标识网络用户（计算机）身份。数字证书为发布公钥提供了一种简便的途径，其数字证书则成为加密算法以及公钥的载体。
     * 依靠数字证书，我们可以构建一个简单的加密网络应用平台。数字证书类似于个人身份证，由数字证书颁发认证机构（Certificate Authority, CA）签发。
     * 只有经过CA签发的证书在网络中才具备可认证性。CA颁发给自己的证书叫根证书。 VeriSign,GeoTrust和Thawte是国际权威数字证书颁发认证机构的三巨头。其中应用最广泛的是VeriSign签发的电子商务用数字证书。
     * 最为常用的非对称加密算法是RSA，与之配套的签名算法是SHA1withRSA，最常用的消息摘要算法是SHA1. 除了RSA，还可以使用DSA算法。只是使用DSA算法无法完成加密解密实现，即这样的证书不包括加密解密功能。
     * 数字证书有多种文件编码格式，主要包含CER编码，DER编码等。 CER(Canonical Encoding Rules, 规范编码格式)，DER(Distinguished Encoding Rules
     * 卓越编码格式)，两者的区别是前者是变长模式，后者是定长模式。 所有证书都符合公钥基础设施(PKI, Public Key Infrastructure)制定的ITU-T X509国际标准(X.509标准)。
     * 
     * <h2>模型分析</h2>
     * <p>
     * 在实际应用中，很多数字证书都属于自签名证书，即证书申请者为自己的证书签名。这类证书通常应用于软件厂商内部发放的产品中，或约定使用该证书的数据交互双方。
     * 数字证书完全充当加密算法的载体，为必要数据做加密解密和签名验签等操作。在我司的开发过程中，数字证书更多是用来做加密和解密。
     * <p>
     * <h3>加密交互</h3> 当客户端获取到服务器下发的数字证书后，就可以进行加密交互了。具体做法是： 客户端使用公钥，加密后发送给服务端，服务端用私钥进行解密验证。 服务端使用私钥进行加密和数字签名。
     * 
     * <h2>KeyTool 管理证书</h2>
     * <p>
     * KeyTool与本地密钥库相关联，将私钥存于密钥库，公钥则以数字证书输出。KeyTool位于JDK目录下的bin目录中，需要通过命令行进行相应的操作。
     * 
     * <h3>1）构建自签名证书</h3> 申请数字证书之前，需要在密钥库中以别名的方式生成本地数字证书，建立相应的加密算法，密钥，有效期等信息。
     * 
     * <pre>
     * keytool -genkeypair -keyalg RSA -keysize 2048 -sigalg SHA1withRSA -validity 3600 -alias myCertificate -keystore myKeystore.keystore
     * </pre>
     * <p>
     * 各参数含义如下：
     * <li>-genkeypair 表示生成密钥对
     * <li>-keyalg 指定密钥算法，这里是RSA
     * <li>-keysize 指定密钥长度，默认1024，这里指定2048
     * <li>-sigal 指定签名算法，这里是SHA1withRSA
     * <li>-validity 指定有效期，单位为天
     * <li>-alias 指定别名
     * <li>-keystore 指定密钥库存储位置
     * <p>
     * 这里我输入参数Changeme123作为密钥库的密码，也可通过参数-storepass指定密码。可以用-dname "CN=xxx...."这样的形式，避免更多交互。
     * 注意：一个keystore应该是可以存储多套<私钥-数字证书>的信息，通过别名来区分。通过实践，调用上述命令两次（别名不同），生成同一个keystore，用不同别名进行加密解密和签名验签，没有任何问题。
     * 经过上述操作后，密钥库中已经创建了数字证书。虽然这时的数字证书并没有经过CA认证，但并不影响我们使用。我们仍可将证书导出，发送给合作伙伴进行加密交互。
     * 
     * <pre>
    keytool -exportcert -alias myCertificate -keystore myKeystore.keystore -file myCer.cer -rfc
     * </pre>
     * 
     * 各参数含义如下：
     * <li>-exportcert 表示证书导出操作
     * <li>-alias 指定别名
     * <li>-keystore 指定密钥库文件
     * <li>-file 指定导出证书的文件路径
     * <li>-rfc 指定以Base64编码格式输出
     * <p>
     * 打印证书
     * 
     * <pre>
    keytool -printcert -file myCer.cer
     * </pre>
     * 
     * <h3>2）构建CA签发证书</h3>
     * <p>
     * 如果要获取CA机构谁的数字证书，需要将数字证书签发申请(CSR)导出，经由CA机构认证并颁发，将认证后的证书导入本地密钥库和信息库。
     * 
     * <pre>
    keytool -certreq -alias myCertificate -keystore myKeystore.keystore -file myCsr.csr -v
     * </pre>
     * 
     * 各参数含义如下：
     * <li>-certreq 表示数字证书申请操作
     * <li>-alias 指定别名
     * <li>-keystore 指定密钥库文件路径
     * <li>-file 指定导出申请的路径
     * <li>-v 详细信息
     * <p>
     * 获得签发的数字证书后，需要将其导入信任库。
     * 
     * <pre>
    keytool -importcert -trustcacerts -alias myCertificate -file myCer.cer -keystore myKeystore.keystore
     * </pre>
     * 
     * 查看证书
     * 
     * <pre>
    keytool -list -alias myCertificate -keystore myKeystore.keystore
     * </pre>
     * 
     * <h2>证书使用</h2>
     * <p>
     * 终于到了激动人心的时刻，可以用代码通过keystore进行加解密操作了！ Java 6提供了完善的数字证书管理实现，我们几乎无需关注，仅通过操作密钥库和数字证书就可完成相应的加密解密和签名验签过程。
     * 密钥库管理私钥，数字证书管理公钥，公钥和私钥分属消息传递双方，进行加密消息传递。
     */
    void 数字证书() {

    }

    private static final String STORE_PASS = "Changeme123";
    private static final String ALIAS = "myCertificate";
    private static final String KEYSTORE_PATH = "D:\\JavaDemo\\Certifacate\\myKeystore.keystore";
    private static final String CERT_PATH = "D:\\JavaDemo\\Certifacate\\myCer.cer";
    private static final String PLAIN_TEXT = "MANUTD is the most greatest club in the world.";
    /** JDK6只支持X.509标准的证书 */
    private static final String CERT_TYPE = "X.509";

    public static void main(String[] args) throws IOException {
        /**
         * 假设现在有这样一个场景 。A机器上的数据，需要加密导出，然后将导出文件放到B机器上导入。 在这个场景中，A相当于服务器，B相当于客户端
         */

        /** A */
        KeyStore keyStore = getKeyStore(STORE_PASS, KEYSTORE_PATH);
        PrivateKey privateKey = getPrivateKey(keyStore, ALIAS, STORE_PASS);
        X509Certificate certificate = getCertificateByKeystore(keyStore, ALIAS);

        /** 加密和签名 */
        byte[] encodedText = encode(PLAIN_TEXT.getBytes(), privateKey);
        byte[] signature = sign(certificate, privateKey, PLAIN_TEXT.getBytes());

        /** 现在B收到了A的密文和签名，以及A的可信任证书 */
        X509Certificate receivedCertificate = getCertificateByCertPath(CERT_PATH, CERT_TYPE);
        PublicKey publicKey = getPublicKey(receivedCertificate);
        byte[] decodedText = decode(encodedText, publicKey);
        System.out.println("Decoded Text : " + new String(decodedText));
        System.out.println("Signature is : " + verify(receivedCertificate, decodedText, signature));
    }

    /**
     * 加载密钥库，与Properties文件的加载类似，都是使用load方法
     * 
     * @throws IOException
     */
    public static KeyStore getKeyStore(String storepass, String keystorePath) throws IOException {
        InputStream inputStream = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            inputStream = new FileInputStream(keystorePath);
            keyStore.load(inputStream, storepass.toCharArray());
            return keyStore;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return null;
    }

    /**
     * 获取私钥
     * 
     * @param keyStore
     * @param alias
     * @param password
     * @return
     */
    public static PrivateKey getPrivateKey(KeyStore keyStore, String alias, String password) {
        try {
            return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取公钥
     * 
     * @param certificate
     * @return
     */
    public static PublicKey getPublicKey(Certificate certificate) {
        return certificate.getPublicKey();
    }

    /**
     * 通过密钥库获取数字证书，不需要密码，因为获取到Keystore实例
     * 
     * @param keyStore
     * @param alias
     * @return
     */
    public static X509Certificate getCertificateByKeystore(KeyStore keyStore, String alias) {
        try {
            return (X509Certificate) keyStore.getCertificate(alias);
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过证书路径生成证书，与加载密钥库差不多，都要用到流。
     * 
     * @param path
     * @param certType
     * @return
     * @throws IOException
     */
    public static X509Certificate getCertificateByCertPath(String path, String certType) throws IOException {
        InputStream inputStream = null;
        try {
            // 实例化证书工厂
            CertificateFactory factory = CertificateFactory.getInstance(certType);
            // 取得证书文件流
            inputStream = new FileInputStream(path);
            // 生成证书
            Certificate certificate = factory.generateCertificate(inputStream);

            return (X509Certificate) certificate;
        } catch (CertificateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return null;

    }

    /**
     * 从证书中获取加密算法，进行签名
     * 
     * @param certificate
     * @param privateKey
     * @param plainText
     * @return
     */
    public static byte[] sign(X509Certificate certificate, PrivateKey privateKey, byte[] plainText) {
        /** 如果要从密钥库获取签名算法的名称，只能将其强制转换成X509标准，JDK 6只支持X.509类型的证书 */
        try {
            Signature signature = Signature.getInstance(certificate.getSigAlgName());
            signature.initSign(privateKey);
            signature.update(plainText);
            return signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 验签，公钥包含在证书里面
     * 
     * @param certificate
     * @param decodedText
     * @param receivedignature
     * @return
     */
    public static boolean verify(X509Certificate certificate, byte[] decodedText, final byte[] receivedignature) {
        try {
            Signature signature = Signature.getInstance(certificate.getSigAlgName());
            /** 注意这里用到的是证书，实际上用到的也是证书里面的公钥 */
            signature.initVerify(certificate);
            signature.update(decodedText);
            return signature.verify(receivedignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 加密。注意密钥是可以获取到它适用的算法的。
     * 
     * @param plainText
     * @param privateKey
     * @return
     */
    public static byte[] encode(byte[] plainText, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(plainText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 解密，注意密钥是可以获取它适用的算法的。
     * 
     * @param encodedText
     * @param publicKey
     * @return
     */
    public static byte[] decode(byte[] encodedText, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(encodedText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
