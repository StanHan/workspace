package demo.java.security;

/**
 * <h2>PKCS</h2>The Public-Key Cryptography Standards
 * <P>
 * 公钥密码学标准（PKCS）是由RSA实验室与一个非正式联盟合作共同开发的一套公钥密码学的标准，这个非正式联盟最初包括Apple, Microsoft, DEC, Lotus, Sun 和 MIT。
 * PKCS已经被OIW（OSI标准实现研讨会）作为一个OSI标准实现。PKCS是基于二进制和ASCII编码来设计的，也兼容 ITU-T X.509 标准。
 * 其中包括证书申请、证书更新、证书作废表发布、扩展证书内容以及数字签名、数字信封的格式等方面的一系列相关协议。
 * 
 * PKCS包括算法指定（algorithm-specific）和算法独立（algorithm-independent）两种实现标准。多种算法被支持，包括RSA算法和 Diffie-Hellman
 * 密钥交换算法，然而只有后两种才特别详尽。PKCS也为数字签名、数字信封、可扩展证书 定义了一种算法独立（algorithm-independent）的语法；这就意味着任何加密算法都可以实现这套标准的语法，并且因此获得互操作性。
 * 
 * 以下是公钥加密标准（PKCS）：
 * 
 * <li>PKCS #1 定义了基于RSA公钥密码系统 加密和签名数据的机制
 * <li>PKCS #3 定义了 Diffie-Hellman 密钥协商协议
 * <li>PKCS #5 描述了一种 通过从密码衍生出密钥来加密字符串的方法
 * <li>PKCS #6 被逐步淘汰，取而代之的是X.509的第三版本
 * <li>PKCS #7 为信息定义了大体语法，包括加密增强功能产生的信息，如数字签名和加密
 * <li>PKCS #8 描述了私钥信息的格式，这个信息包括某些公钥算法的私钥，和一些可选的属性
 * <li>PKCS #9 定义了在其他的PKCS标准中可使用的选定的属性类型
 * <li>PKCS #10 描述了认证请求的语法
 * <li>PKCS #11 为加密设备定义了一个技术独立的（technology-independent ）编程接口，称之为 Cryptoki。比如智能卡、PCMCIA卡这种加密设备
 * <li>PKCS #12 为 存储或传输用户的私钥、证书、各种秘密等的过程 指定了一种可移植的格式
 * <li>PKCS #13 的目的是为了定义使用椭圆曲线加密和签名数据加密机制
 * <li>PKCS #14 正在酝酿中，涵盖了伪随机数生成
 * <li>PKCS #15 是一个PKCS # 11的补充，给出了一个存储在加密令牌上的加密证书的格式的标准
 * <p>
 * RSA实验室的意图就是要时不时修改PKCS文档以跟得上密码学和数据安全领域的新发展，以及当机会出现时转变文档成开放标准的研制计划。详细的PKCS标准可以在RSA安全的web服务器上获取到，可以访问网站 或者直接匿名访问FTP。
 * https://www.rsa.com/en-us
 * 
 * https://link.jianshu.com/?t=ftp://ftp.rsasecurity.com/pub/pkcs/doc/
 * 
 * 作者：乔伯 链接：https://www.jianshu.com/p/b98b12de85f3 來源：简书 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 */
public interface PKCS {

    /**
     * PFX证书：由Public Key Cryptography Standards #12，PKCS#12标准定义，包含了公钥和私钥的二进制格式的证书形式，以pfx作为证书文件后缀名。
     */
    void pfx证书();

    /**
     * 
     */
    void cer证书();

}
