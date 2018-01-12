package demo.java.net;

import java.net.HttpURLConnection;

/**
 * <H4>HTTP协议</H4>
 * <p>
 * HTTP协议是请求/响应式的协议，客户端需要发送一个请求，服务器才会返回响应内容。 HTTP请求有很多种method，最常用的就是GET和POST，每种method的请求之间会有细微的区别。下面分别分析一下GET和POST请求。
 * <p>
 * Get请求:包含request line和header两部分。其中request line中包含method（例如GET、POST）、request uri和protocol version三部分，三个部分之间以空格分开。
 * request line和每个header各占一行，以换行符CRLF（即\r\n）分割。
 * <p>
 * POST请求:请求包含三个部分：request line、header、message，比之前的GET请求多了一个message body，其中header和message
 * body之间用一个空行分割。POST请求的参数不在URL中，而是在message body中，header中多了一项Content-Length用于表示message
 * body的字节数，这样服务器才能知道请求是否发送结束。这也就是GET请求和POST请求的主要区别。
 * <p>
 * HTTP响应 :HTTP响应和HTTP请求非常相似，HTTP响应包含三个部分：status line、header、massage body。
 * <li>其中status line包含protocol version、状态码（status code）、reason
 * phrase三部分。状态码用于描述HTTP响应的状态，例如200表示成功，404表示资源未找到，500表示服务器出错。
 * <li>Header中的Content-Length同样用于表示message body的字节数。Content-Type表示message body的类型，通常浏览网页其类型是HTML，当然还会有其他类型，比如图片、视频等。
 * 
 * 在提出持久连接之前，每获取一个URL都有创建一个单独的TCP连接,不断的加重HTTP服务器的负担并导致网络的拥塞。持久HTTP连接具有一下优势：
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
public class HttpDemo {

    public static void main(String[] args) {
        HttpURLConnection httpConnection;
    }

}
