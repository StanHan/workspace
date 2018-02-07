package demo.java.designPattern.proxy;

/**
 * <h1>RPC(Remote Procedure Call)远程过程调用</h1>
 * 
 * 什么是RPC？ 由于各服务部署在不同的机器，服务间的调用免不了网络通信过程，服务消费方没调用一个服务都要写一坨网络通信相关的代码，不仅负责而且容易出错。 RPC(Remote Procedure
 * Call)远程过程调用能让我们像调用本地服务一样调用远程服务，而让调用方对网络通信这些细节透明。
 * 
 * 常见的RPC框架：
 * 
 * <li>阿里巴巴的hsf、dubbo
 * <li>Facebook的thrift
 * <li>Google的grpc
 * <li>Twitter的finagle等
 * 
 * <h2>RPC调用过程及涉及到的通信细节</h2>
 * <ol>
 * <li>服务消费方（client）调用以本地调用方式调用服务
 * <li>client stub接收到调用后负责将方法、参数等组装成能够进行网络传输的消息体
 * <li>client stub找到服务地址，并将消息发送到服务端
 * <li>server stub收到消息后进行解码
 * <li>server stub根据解码结果调用本地服务
 * <li>本地服务执行并将结果返回给server stub
 * <li>server stub将返回结果打包成消息并发送至消费方
 * <li>client stub接收到消息，并进行解码
 * <li>服务消费方得到最终结果
 * </ol>
 * RPC的目标就是要将2-8这些步骤都封装起来，让用户对这些细节透明
 * 
 * <h2>如何做到透明化远程服务调用</h2>
 * 
 * 怎么封装通信细节才能让用户像以本地调用方式调用远程服务呢？对JAVA来说就是使用代理！ Java代理的两种方式：
 * <li>JDK动态代理
 * <li>CGLIB字节码生成
 *
 */
public class RpcDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
