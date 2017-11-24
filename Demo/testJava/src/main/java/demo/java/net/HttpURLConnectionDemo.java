package demo.java.net;

import java.net.HttpURLConnection;

/**
 * 在提出持久连接之前，每获取一个URL都有创建一个单独的TCP连接,不断的加重HTTP服务器的负担并导致网络的拥塞。
 * 
 * 持久HTTP连接具有一下优势：
 * <li>通过打卡和关闭更少的TCP连接，节省了路由和主机的CPU耗时，以及TCP协议控制阻塞使用的主机内存。
 * <li>HTTP请求和响应可以在一个连接的基础上管道化。管道技术允许客户端发送多个请求而不用等待响应，使得TCP连接更加高效地使用从而更少的浪费时间。
 * <li>通过减少TCP打开导致的包的消息来减少网络拥塞，通过给TCP充分的时间来确定网络的拥塞状态。
 * <li>HTTP可以进化的更加优美，因为错误可以被报告而不用直接关闭TCP连接。使用高HTTP版本的客户端可能尝试一些新的功能，但是如果与旧版本服务端通信时在有错误报告后用要重试旧的语义。
 * 
 * HTTP实现应该实现持久连接。
 * 
 * HTTP/1.1和早期HTTP版本最大的不同是持久连接是HTTP连接的默认行为。也就是说，除非有其他的标识，客户端应该假设服务器会持有一个持久连接，即使服务端返回错误的响应。
 * 持久化连接提供了一个机制可以让客户端和服务端给出TCP连接关闭的信号。信号用Connection报头域给出。一旦关闭信号发出，客户端就不能再通过那个连接发送任何请求。
 *
 */
public class HttpURLConnectionDemo {

    public static void main(String[] args) {
        HttpURLConnection httpConnection;
    }

}
