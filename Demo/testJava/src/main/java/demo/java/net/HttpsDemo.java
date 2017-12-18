package demo.java.net;

/**
 * HTTPS协议是由HTTP协议加上SSL/TLS协议组合而成，我们平时接触最多的SSL/TLS协议的应用就是HTTPS协议了。 HTTPS即HTTP over
 * SSL，实际上就是在原来HTTP协议的底层加入了SSL/TLS协议层，使得客户端（例如浏览器）与服务器之间的通信加密传输，攻击者无法窃听和篡改。相对而言HTTP协议则是明文传输，安全性并不高。
 * <p>
 * <h5>HTTPS主要可以避免以下几个安全问题：</h5>
 * <li>窃听隐私：使用明文传输的HTTP协议，传输过程中的信息都可能会被攻击者窃取到，例如你登录网站的用户名和密码、在电商的购买记录、搜索记录等，这就会造成例如账号被盗、各种隐私泄漏的风险。
 * 而使用HTTPS对通信内容加密过后，即使被攻击者窃取到也无法破解其中的内容。
 * 
 * <li>篡改内容：HTTP使用明文传输，不但消息会被窃取，还可能被篡改，例如常见的运营HTTP商劫持。你是否曾经浏览http协议的百度时，时不时会在页面下方弹出小广告，这些小广告并不是百度放上去的，而是电信网通等运营商干的，
 * 运营商通过篡改服务器返回的页面内容，加入一段HTML代码就可以轻松实现小广告。而使用HTTPS的百度，就不再会出现这样的小广告，因为攻击者无法对传输内容解密和加密，就无法篡改。
 * 
 * <li>冒充：例如DNS劫持，当你输入一个http网址在浏览器打开时，有可能打开的是一个假的网站，连的并不是真网站的服务器，假的网站可能给你弹出广告，还可能让你输入用户名密码来盗取账户。
 * 使用HTTPS的话，服务器都会有数字证书和私钥，数字证书公开的，私钥是网站服务器私密的，假网站如果使用假的证书，浏览器会拦截并提示，如果使用真的证书，由于没有私钥也无法建立连接。
 *
 * <h5>生成私钥和证书</h5>
 * <p>
 * 浏览器信任的证书一般是CA机构（证书授权中心）颁发的，证书有收费的也有免费的，本文使用免费证书用于测试。
 * 可以在腾讯云https://www.qcloud.com/product/ssl申请一个免费证书，申请证书前需要提供一个域名，即该证书作用的域名。
 * 证书生成好下载后包含一个私钥文件(.key)和一个证书文件(.crt)，腾讯云生成的证书可以在Nginx目录下找到这两个文件。
 * 这两个文件在Twisted中可以直接使用，但是Java只能使用PKCS#8私钥文件，需要对上面的.key文件用openssl进行转换。
 * <p>
 * 转换成DER二进制格式私钥文件，供MINA使用：
 * <li>openssl pkcs8 -topk8 -inform PEM -in 2_gw2.vsgames.cn.key -outform DER -nocrypt -out private.der
 * <p>
 * 转换成PEM文本格式私钥文件，供Netty使用：
 * <li>openssl pkcs8 -topk8 -inform PEM -in 2_gw2.vsgames.cn.key -outform PEM -nocrypt -out private.pem
 * <p>
 * 除了在CA机构申请证书，还可以通过自签名的方式生成私钥和证书。不过由于自签名的证书不是CA机构颁发，不受浏览器信任，在浏览器打开HTTPS地址时会有安全提示，测试时可以忽略提示。
 */
public class HttpsDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
