package demo.java.security;

import java.security.Provider;
import java.security.Security;

/**
 * 
 * bouncy castle（轻量级密码术包）是一种用于 Java 平台的开放源码的轻量级密码术包；它支持大量的密码术算法，并提供JCE 1.2.1的实现。
 * <p>
 * Bouncy Castle的Java JCE安装方法
 * <li>1、去官方站点下载Bouncy Castle的JCE Provider包 bcprov-ext-jdk15-145.jar
 * <li>2、把jar文件复制到 $JAVA_HOME$\jre\lib\ext 目录下面
 * <li>3、修改配置文件\jre\lib\security\java.security 尾部加上这一行即可
 * security.provider.7=org.bouncycastle.jce.provider.BouncyCastleProvider
 *
 */
public class BouncyCastleDemo {

    public static void main(String[] args) {
        System.out.println("-------列出加密服务提供者-----");
        Provider[] pro = Security.getProviders();
        for (Provider p : pro) {
            System.out.println("Provider:" + p.getName() + " - version:" + p.getVersion());
            System.out.println(p.getInfo());
        }
        System.out.println("");
        System.out.println("-------列出系统支持的消息摘要算法：");
        for (String s : Security.getAlgorithms("MessageDigest")) {
            System.out.println(s);
        }
        System.out.println("-------列出系统支持的生成公钥和私钥对的算法：");
        for (String s : Security.getAlgorithms("KeyPairGenerator")) {
            System.out.println(s);
        }
    }
}
