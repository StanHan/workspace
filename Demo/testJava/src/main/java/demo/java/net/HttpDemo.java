package demo.java.net;

import java.net.HttpURLConnection;

/**
 * <H1>HTTP协议</H1>
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
 * <h2>长连接</h2>
 * <p>
 * HTTP1.1规定了默认保持长连接（HTTP persistent connection ，也有翻译为持久连接），数据传输完成了保持TCP连接不断开（不发RST包、不四次握手），等待在同域名下继续用这个通道传输数据；相反的就是短连接。
 * 如果HTTP1.1版本的HTTP请求报文不希望使用长连接，则要在HTTP请求报文首部加上Connection: close。
 * <h3>长连接的过期时间</h3><br>
 * 客户端的长连接不可能无限期的拿着，会有一个超时时间，服务器有时候会告诉客户端超时时间，
 * 譬如：Keep-Alive:timeout=20，表示这个TCP通道可以保持20秒。另外还可能有max=XXX，表示这个长连接最多接收XXX次请求就断开。
 * 对于客户端来说，如果服务器没有告诉客户端超时时间也没关系，服务端可能主动发起四次握手断开TCP连接，客户端能够知道该TCP连接已经无效；另外TCP还有心跳包来检测当前连接是否还活着，方法很多，避免浪费资源。
 * <h3>长连接的数据传输完成识别</h3>
 * <p>
 * 使用长连接之后，客户端、服务端怎么知道本次传输结束呢？两部分：
 * <li>1是判断传输数据是否达到了Content-Length指示的大小；
 * <li>2动态生成的文件没有Content-Length，它是分块传输（chunked），这时候就要根据chunked编码来判断，chunked编码的数据在最后有一个空chunked块，表明本次传输数据结束。
 * <h3>并发连接数的数量限制</h3>
 * <p>
 * 在web开发中需要关注浏览器并发连接的数量，RFC文档说，客户端与服务器最多就连上两通道，但服务器、个人客户端要不要这么做就随人意了，有些服务器就限制同时只能有1个TCP连接，导致客户端的多线程下载（客户端跟服务器连上多条TCP通道同时拉取数据）发挥不了威力，有些服务器则没有限制。
 * 浏览器客户端就比较规矩，知乎这里有分析，限制了同域名下能启动若干个并发的TCP连接去下载资源。 并发数量的限制也跟长连接有关联，打开一个网页，很多个资源的下载可能就只被放到了少数的几条TCP连接里，这就是TCP通道复用（长连接）。
 * 如果并发连接数少，意味着网页上所有资源下载完需要更长的时间（用户感觉页面打开卡了）；并发数多了，服务器可能会产生更高的资源消耗峰值。
 * 浏览器只对同域名下的并发连接做了限制，也就意味着，web开发者可以把资源放到不同域名下，同时也把这些资源放到不同的机器上，这样就完美解决了。
 * <h3>容易混淆的概念——TCP的keep alive和HTTP的Keep-alive</h3><br>
 * TCP的keep alive是检查当前TCP连接是否活着；HTTP的Keep-alive是要让一个TCP连接活久点。 它们是不同层次的概念。 TCP keep
 * alive的表现：当一个连接“一段时间”没有数据通讯时，一方会发出一个心跳包（Keep Alive包），如果对方有回包则表明当前连接有效，继续监控。这个“一段时间”可以设置。
 * 
 * <h3>HTTP 流水线技术</h3>
 * <p>
 * 使用了HTTP长连接（HTTP persistent connection ）之后的好处，包括可以使用HTTP 流水线技术（HTTP
 * pipelining，也有翻译为管道化连接），它是指，在一个TCP连接内，多个HTTP请求可以并行，下一个HTTP请求在上一个HTTP请求的应答完成之前就发起。
 */
public class HttpDemo {

    public static void main(String[] args) {
        HttpURLConnection httpConnection;
    }

}
