package demo.java.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * 在进行网络编程时，我们常常见到同步(Sync)/异步(Async)，阻塞(Block)/非阻塞(Unblock)四种调用方式：
 * 
 * 同步/异步主要针对Client端:
 * <li>同步： 所谓同步，就是在c端发出一个功能调用时，在没有得到结果之前，该调用就不返回。也就是必须一件一件事做,等前一件做完了才能做下一件事。 例如普通B/S模式（同步）：提交请求->等待服务器处理->处理完毕返回
 * 这个期间客户端浏览器不能干任何事
 * <li>异步： 异步的概念和同步相对。当c端一个异步过程调用发出后，调用者不能立刻得到结果。实际处理这个调用的部件在完成后，通过状态、通知和回调来通知调用者。 例如 ajax请求（异步）:
 * 请求通过事件触发->服务器处理（这是浏览器仍然可以作其他事情）->处理完毕
 *
 * 阻塞/非阻塞主要针对S端:
 * <li>阻塞： 阻塞调用是指调用结果返回之前，当前线程会被挂起（线程进入非可执行状态，在这个状态下，cpu不会给线程分配时间片，即线程暂停运行）。函数只有在得到结果之后才会返回。
 * <li>非阻塞： 非阻塞和阻塞的概念相对应，指在不能立刻得到结果之前，该函数不会阻塞当前线程，而会立刻返回。
 * 
 * <li>1. 同步，就是我客户端（c端调用者）调用一个功能，该功能没有结束前，我（c端调用者）死等结果。
 * <li>2. 异步，就是我（c端调用者）调用一个功能，不需要知道该功能结果，该功能有结果后通知我（c端调用者）即回调通知。
 * <li>3. 阻塞， 就是调用我（s端被调用者，函数），我（s端被调用者，函数）没有接收完数据或者没有得到结果之前，我不会返回。
 * <li>4. 非阻塞， 就是调用我（s端被调用者，函数），我（s端被调用者，函数）立即返回，通过select通知调用者
 * 
 * 使用传统的BIO（Blocking IO/阻塞IO）进行网络编程时，进行网络IO读写时都会阻塞当前线程，如果实现一个TCP服务器，都需要对每个客户端连接开启一个线程，而很多线程可能都在傻傻的阻塞住等待读写数据，系统资源消耗大。
 * 
 * 而NIO（Non-Blocking IO/非阻塞IO）或AIO（Asynchronous
 * IO/异步IO）则是通过IO多路复用技术实现，不需要为每个连接创建一个线程，其底层实现是通过操作系统的一些特性如select、poll、epoll、iocp等
 * 
 * Java NIO（java non-blocking IO）,java非阻塞式IO。目 的 提供非阻塞式的高伸缩性网络。
 * 由以下几个核心部分组成：Channels、Buffers、Selectors。其它组件，如Pipe和FileLock，只不过是与三个核心组件共同使用的工具类。
 * 
 * Channel 有点象流，数据可以从Channel读到Buffer中，也可以从Buffer 写到Channel中。
 * 
 * <p>
 * 关键的Buffer实现：ByteBuffer\CharBuffer\DoubleBuffer\FloatBuffer\IntBuffer\LongBuffer\ShortBuffer\MappedByteBuffer(用于表示内存映射文件)
 * <p>
 * 主要Channel的实现：
 * <li>FileChannel\
 * <li>DatagramChannel(通过UDP读写网络中的数据)\
 * <li>SocketChannel(通过TCP读写网络中的数据)\
 * <li>ServerSocketChannel(监听新进来的TCP连接，对每一个新进来的连接都会创建一个SocketChannel)
 * <p>
 * Selector允许单线程处理多个 Channel。如果你的应用打开了多个连接（通道），但每个连接的流量都很低，使用Selector就会很方便。
 * 要使用Selector，得向Selector注册Channel，然后调用它的select()方法。这个方法会一直阻塞到某个注册的通道有事件就绪。一旦这个方法返回，线程就可以处理这些事件，
 * 
 * <p>
 * <h4>Java NIO和IO的主要区别</h4>
 * <li>Java NIO和IO之间第一个最大的区别是，IO是面向流的，NIO是面向缓冲区的。 Java IO面向流意味着每次从流中读一个或多个字节，直至读取所有字节，它们没有被缓存在任何地方。
 * 此外，它不能前后移动流中的数据。如果需要前后移动从流中读取的数据，需要先将它缓存到一个缓冲区。 Java NIO的缓冲导向方法略有不同。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动。
 * 这就增加了处理过程中的灵活性。但是，还需要检查是否该缓冲区中包含所有您需要处理的数据。而且，需确保当更多的数据读入缓冲区时，不要覆盖缓冲区里尚未处理的数据。
 * <li>Java IO的各种流是阻塞的。这意味着，当一个线程调用read() 或 write()时，该线程被阻塞，直到有一些数据被读取，或数据完全写入。该线程在此期间不能再干任何事情了。 Java
 * NIO的非阻塞模式，使一个线程从某通道发送请求读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取。而不是保持线程阻塞，所以直至数据变的可以读取之前，该线程可以继续做其他的事情。
 * 非阻塞写也是如此。一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。
 * 线程通常将非阻塞IO的空闲时间用于在其它通道上执行IO操作，所以一个单独的线程现在可以管理多个输入和输出通道（channel）。
 * <li>选择器（Selectors）：Java
 * NIO的选择器允许一个单独的线程来监视多个输入通道，你可以注册多个通道使用一个选择器，然后使用一个单独的线程来“选择”通道：这些通道里已经有可以处理的输入，或者选择已准备写入的通道。这种选择机制，使得一个单独的线程很容易来管理多个通道。
 * 
 * <h4>NIO和IO如何影响应用程序的设计：</h4>
 * <p>
 * NIO可让您只使用一个（或几个）单线程管理多个通道（网络连接或文件），但付出的代价是解析数据可能会比从一个阻塞流中读取数据更复杂。
 * 如果需要管理同时打开的成千上万个连接，这些连接每次只是发送少量的数据，例如聊天服务器，实现NIO的服务器可能是一个优势。同样，如果你需要维持许多打开的连接到其他计算机上，如P2P网络中，使用一个单独的线程来管理你所有出站连接，可能是一个优势。
 * 如果你有少量的连接使用非常高的带宽，一次发送大量的数据，也许典型的IO服务器实现可能非常契合。
 */
public class NIODemo {
    public static void main(String[] args) throws Exception {
        test1();
    }

    /**
     * Java NIO 管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取。
     */
    static void pipe() throws IOException {
        // 通过Pipe.open()方法打开管道
        Pipe pipe = Pipe.open();
        // 要向管道写数据，需要访问sink通道。
        Pipe.SinkChannel sinkChannel = pipe.sink();
        // 通过调用SinkChannel的write()方法，将数据写入SinkChannel,
        String newData = "New String to write to file..." + System.currentTimeMillis();
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();

        while (buf.hasRemaining()) {
            sinkChannel.write(buf);
        }
        // 从读取管道的数据，需要访问source通道，
        Pipe.SourceChannel sourceChannel = pipe.source();
        // 调用source通道的read()方法来读取数据,read()方法返回的int值会告诉我们多少字节被读进了缓冲区。
        int bytesRead = sourceChannel.read(buf);
    }

    /**
     * Java NIO中的DatagramChannel是一个能收发UDP包的通道。因为UDP是无连接的网络协议，所以不能像其它通道那样读取和写入。它发送和接收的是数据包。
     * 
     */
    static void DatagramChannel() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress(9999));
        // 接收数据
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        // receive()方法会将接收到的数据包内容复制到指定的Buffer. 如果Buffer容不下收到的数据，多出的数据将被丢弃。
        datagramChannel.receive(buf);

        String newData = "New String to write to file..." + System.currentTimeMillis();
        // 通过send()方法从DatagramChannel发送数据
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();
        // 一串字符到”jenkov.com”服务器的UDP端口80。 因为服务端并没有监控这个端口，所以什么也不会发生。也不会通知你发出的数据包是否已收到，因为UDP在数据传送方面没有任何保证。
        int bytesSent = datagramChannel.send(buf, new InetSocketAddress("jenkov.com", 80));

        /**
         * 连接到特定的地址:可以将DatagramChannel“连接”到网络中的特定地址的。由于UDP是无连接的，连接到特定地址并不会像TCP通道那样创建一个真正的连接。而是锁住DatagramChannel
         * ，让其只能从特定地址收发数据。当连接后，也可以使用read()和write()方法，就像在用传统的通道一样。只是在数据传送方面没有任何保证。
         */
        datagramChannel.connect(new InetSocketAddress("jenkov.com", 80));
    }

    /**
     * Java NIO中的 ServerSocketChannel 是一个可以监听新进来的TCP连接的通道, 就像标准IO中的ServerSocket一样
     */
    static void demoServerSocketChannel() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        while (true) {
            // accept()方法会一直阻塞到有新连接到达。
            SocketChannel socketChannel = serverSocketChannel.accept();

            // do something with socketChannel...
        }
    }

    /**
     * 非阻塞模式:
     * 
     * ServerSocketChannel可以设置成非阻塞模式。在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null。 因此，需要检查返回的SocketChannel是否是null.
     * 
     * @throws IOException
     */
    static void nonBlockingServerSocketChannel() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            if (socketChannel != null) {
                // do something with socketChannel...
            }
        }
    }

    /**
     * 非阻塞模式:可以设置 SocketChannel 为非阻塞模式（non-blocking mode）.设置之后，就可以在异步模式下调用connect(), read() 和write()了。
     * 
     * <li>如果SocketChannel在非阻塞模式下，此时调用connect()，该方法可能在连接建立之前就返回了。为了确定连接是否建立，可以调用finishConnect()的方法。
     * <li>非阻塞模式下，write()方法在尚未写出任何内容时可能就返回了。所以需要在循环中调用write()。
     * <li>非阻塞模式下,read()方法在尚未读取到任何数据时可能就返回了。所以需要关注它的int返回值，它会告诉你读取了多少字节。
     * <li>非阻塞模式与选择器搭配会工作的更好，通过将一或多个SocketChannel注册到Selector，可以询问选择器哪个通道已经准备好了读取，写入等。
     */
    static void nonBlockingSocketChannelDemo() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));
        while (!socketChannel.finishConnect()) {
            // wait, or do something else...
        }
    }

    /**
     * Java NIO中的SocketChannel是一个连接到TCP网络套接字的通道。可以通过以下2种方式创建SocketChannel：
     * 
     * <li>打开一个SocketChannel并连接到互联网上的某台服务器。
     * <li>一个新连接到达ServerSocketChannel时，会创建一个SocketChannel。
     */
    static void socketChannelDemo() throws IOException {
        // 打开 SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));

        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = socketChannel.read(buf);

        // 关闭 SocketChannel
        socketChannel.close();

        // 写入 SocketChannel
        String newData = "New String to write to file..." + System.currentTimeMillis();

        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();

        while (buf.hasRemaining()) {
            socketChannel.write(buf);
        }

    }

    /**
     * Java NIO中的FileChannel是一个连接到文件的通道。可以通过文件通道读写文件。 FileChannel无法设置为非阻塞模式，它总是运行在阻塞模式下。
     */
    static void fileChannelDemo() throws IOException {
        /**
         * 打开FileChannel.在使用FileChannel之前，必须先打开它。但是，我们无法直接打开一个FileChannel，需要通过使用一个InputStream、OutputStream或RandomAccessFile来获取一个FileChannel实例。
         */
        RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel channel = aFile.getChannel();
        /** 从FileChannel读取数据.调用多个read()方法之一从FileChannel中读取数据。 首先，分配一个Buffer。从FileChannel中读取的数据将被读到Buffer中。 */
        ByteBuffer buf = ByteBuffer.allocate(48);
        /** 然后，调用FileChannel.read()方法。该方法将数据从FileChannel读取到Buffer中。read()方法返回的int值表示了有多少字节被读到了Buffer中。如果返回-1，表示到了文件末尾 */
        int bytesRead = channel.read(buf);
        /** 向FileChannel写数据.使用FileChannel.write()方法向FileChannel写数据，该方法的参数是一个Buffer。 */
        String newData = "New String to write to file..." + System.currentTimeMillis();

        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();
        /**
         * 注意FileChannel.write()是在while循环中调用的。因为无法保证write()方法一次能向FileChannel写入多少字节，因此需要重复调用write()方法，直到Buffer中已经没有尚未写入通道的字节。
         */
        while (buf.hasRemaining()) {
            channel.write(buf);
        }
        /** 关闭FileChannel. 用完FileChannel后必须将其关闭。 */
        channel.close();
        /**
         * 有时可能需要在FileChannel的某个特定位置进行数据的读/写操作。可以通过调用position()方法获取FileChannel的当前位置。 也可以通过调用position(long
         * pos)方法设置FileChannel的当前位置。如果将位置设置在文件结束符之后，然后试图从文件通道中读取数据，读方法将返回-1 —— 文件结束标志。
         * 如果将位置设置在文件结束符之后，然后向通道中写数据，文件将撑大到当前位置并写入数据。这可能导致“文件空洞”，磁盘上物理文件中写入的数据间有空隙。
         */
        long pos = channel.position();
        channel.position(pos + 123);
        /** FileChannel实例的size()方法将返回该实例所关联文件的大小 */
        long fileSize = channel.size();
        /** 可以使用FileChannel.truncate()方法截取一个文件。截取文件时，文件将中指定长度后面的部分将被删除。 */
        channel.truncate(1024);
        /**
         * FileChannel.force()方法将通道里尚未写入磁盘的数据强制写到磁盘上。出于性能方面的考虑，操作系统会将数据缓存在内存中，所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。要保证这一点，
         * 需要调用force()方法。force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上。
         */
        channel.force(true);
    }

    /**
     * Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。
     * 
     */
    static void selectorDemo(SocketChannel socketChannel) throws IOException {
        /** Selector的创建 */
        Selector selector = Selector.open();
        /**
         * 向Selector注册通道:与Selector一起使用时，Channel必须处于非阻塞模式下。这意味着不能将FileChannel与Selector一起使用，因为FileChannel不能切换到非阻塞模式。而套接字通道都可以。
         */
        socketChannel.configureBlocking(false);
        /**
         * 注意register()方法的第二个参数。这是一个“interest集合”，意思是在通过Selector监听Channel时对什么事件感兴趣。
         * 可以监听四种不同类型的事件：Connect\Accept\Read\Write。 通道触发了一个事件意思是该事件已经就绪。 所以，某个channel成功连接到另一个服务器称为“连接就绪”。 一个server
         * socket channel准备好接收新进入的连接称为“接收就绪”。 一个有数据可读的通道可以说是“读就绪”。等待写数据的通道可以说是“写就绪”。
         * 
         * 这四种事件用SelectionKey的四个常量来表示：SelectionKey.OP_CONNECT\SelectionKey.OP_ACCEPT\SelectionKey.OP_READ\SelectionKey.OP_WRITE。
         * 如果你对不止一种事件感兴趣，那么可以用“位或”操作符将常量连接起来，如下：int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
         * 
         */
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
        /**
         * SelectionKey:当向Selector注册Channel时，register()方法会返回一个SelectionKey对象。这个对象包含了一些你感兴趣的属性：
         * 
         * <li>interest集合: 就像向Selector注册通道一节中所描述的，interest集合是你所选择的感兴趣的事件集合。可以通过SelectionKey读写interest集合，像这样： <pre>
        int interestSet = selectionKey.interestOps();
        boolean isInterestedInAccept  = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT；
        boolean isInterestedInConnect = interestSet & SelectionKey.OP_CONNECT;
        boolean isInterestedInRead    = interestSet & SelectionKey.OP_READ;
        boolean isInterestedInWrite   = interestSet & SelectionKey.OP_WRITE;
        </pre> 可以看到，用“位与”操作interest 集合和给定的SelectionKey常量，可以确定某个确定的事件是否在interest 集合中。
         * <li>ready集合: ready 集合是通道已经准备就绪的操作的集合。在一次选择(Selection)之后，你会首先访问这个ready set。 int readySet =
         * selectionKey.readyOps(); 可以用像检测interest集合那样的方法，来检测channel中什么事件或操作已经就绪。但是，也可以使用以下四个方法，它们都会返回一个布尔类型： <pre>
        selectionKey.isAcceptable();
        selectionKey.isConnectable();
        selectionKey.isReadable();
        selectionKey.isWritable();
        </pre>
         * 
         * <li>Channel:Channel channel = selectionKey.channel();
         * <li>Selector:Selector selector = selectionKey.selector();
         * <li>附加的对象（可选） 可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道。例如，可以附加 与通道一起使用的Buffer，或是包含聚集数据的某个对象。使用方法如下：
         * <pre>selectionKey.attach(theObject);Object attachedObj = selectionKey.attachment();</pre>
         * 还可以在用register()方法向Selector注册Channel的时候附加对象。如：
         * <pre>SelectionKey key = channel.register(selector,SelectionKey.OP_READ, theObject);</pre>
         */
        int interestSet = selectionKey.interestOps();
        int readyOps = selectionKey.readyOps();
        Channel channel = selectionKey.channel();
        Object attachedObj = selectionKey.attachment();
        /**
         * 通过Selector选择通道：一旦向Selector注册了一或多个通道，就可以调用几个重载的select()方法。这些方法返回你所感兴趣的事件（如连接、接受、读或写）已经准备就绪的那些通道。
         * 换句话说，如果你对“读就绪”的通道感兴趣，select()方法会返回读事件已经就绪的那些通道。 下面是select()方法：
         * <li>int select() : 阻塞到至少有一个通道在你注册的事件上就绪了。
         * <li>int select(long timeout) :和select()一样，除了最长会阻塞timeout毫秒(参数)。
         * <li>int selectNow(): 不会阻塞，不管什么通道就绪都立刻返回 select()方法返回的int值表示有多少通道已经就绪。亦即，自上次调用select()方法后有多少通道变成就绪状态。
         * 如果调用select()方法，因为有一个通道变成就绪状态，返回了1，若再次调用select()方法，如果另一个通道就绪了，它会再次返回1。
         * 如果对第一个就绪的channel没有做任何操作，现在就有两个就绪的通道，但在每次select()方法调用之间，只有一个通道就绪了。
         */

        int count = selector.selectNow();
        if (count > 0) {
            /**
             * 一旦调用了select()方法，并且返回值表明有一个或更多个通道就绪了，然后可以通过调用selector的selectedKeys()方法，访问“已选择键集（selected key set）”中的就绪通道。
             * 当像Selector注册Channel时，Channel.register()方法会返回一个SelectionKey
             * 对象。这个对象代表了注册到该Selector的通道。可以通过SelectionKey的selectedKeySet()方法访问这些对象
             */
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            /** 可以遍历这个已选择的键集合来访问就绪的通道。 */
            for (SelectionKey key : selectedKeys) {
                if (key.isAcceptable()) {
                    SelectableChannel selectableChannel = key.channel();
                    // a connection was accepted by a ServerSocketChannel.
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                } else if (key.isReadable()) {
                    // a channel is ready for reading
                } else if (key.isWritable()) {
                    // a channel is ready for writing
                }
                selectedKeys.remove(key);
            }
        }
        /**
         * 某个线程调用select()方法后阻塞了，即使没有通道已经就绪，也有办法让其从select()方法返回。只要让其它线程在第一个线程调用select()方法的那个对象上调用Selector.wakeup()方法即可。
         * 阻塞在select()方法上的线程会立马返回。如果有其它线程调用了wakeup()方法，但当前没有线程阻塞在select()方法上，下个调用select()方法的线程会立即“醒来（wake up）”。
         */
        selector.wakeup();

        /** 用完Selector后调用其close()方法会关闭该Selector，且使注册到该Selector上的所有SelectionKey实例无效。通道本身并不会关闭。 */
        selector.close();

    }

    /**
     * 有BUG，有机会再调
     */
    static void test1() throws Exception {
        MappedFile file = new MappedFile("D:/logs/aa.txt");
        if (file.boundChannelToByteBuffer()) {
            for (int i = 0; i < 10_0000; i++) {
                file.appendData("Selector 目 的 提供非阻塞式的高伸缩性网络\n".getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    /**
     * 分散（scatter）从Channel中读取是指在读操作时将读取的数据写入多个buffer中。因此，Channel将从Channel中读取的数据“分散（scatter）”到多个Buffer中。
     * read()方法按照buffer在数组中的顺序将从channel中读取的数据写入到buffer，当一个buffer被写满后，channel紧接着向另一个buffer中写。 Scattering
     * Reads在移动下一个buffer前，必须填满当前的buffer，这也意味着它不适用于动态消息(译者注：消息大小不固定)。 换句话说，如果存在消息头和消息体，消息头必须完成填充（例如 128byte），Scattering
     * Reads才能正常工作。
     */
    static void scatterReadDemo(FileChannel channel) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);
        ByteBuffer[] bufferArray = { header, body };
        channel.read(bufferArray);
    }

    /**
     * 聚集（gather）写入Channel是指在写操作时将多个buffer的数据写入同一个Channel，因此，Channel 将多个Buffer中的数据“聚集（gather）”后发送到Channel。
     * buffers数组是write()方法的入参，write()方法会按照buffer在数组中的顺序，将数据写入到channel，注意只有position和limit之间的数据才会被写入。
     * 因此，如果一个buffer的容量为128byte，但是仅仅包含58byte的数据，那么这58byte的数据将被写入到channel中。因此与Scattering Reads相反，Gathering
     * Writes能较好的处理动态消息。
     */
    static void gatherWriteDemo(FileChannel channel) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);
        // write data into buffers
        ByteBuffer[] bufferArray = { header, body };
        channel.write(bufferArray);
    }

    /**
     * 通道之间的数据传输
     */
    static void testFileChannelTransfer() {
        try (RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
                FileChannel fromChannel = fromFile.getChannel();
                RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
                FileChannel toChannel = toFile.getChannel();) {

            long position = 0;
            long count = fromChannel.size();
            /**
             * FileChannel的transferFrom()方法可以将数据从源通道传输到FileChannel中。 position表示从position处开始向目标文件写入数据，count表示最多传输的字节数。
             * 如果源通道的剩余空间小于 count 个字节，则所传输的字节数要小于请求的字节数。 此外要注意，在SoketChannel的实现中，SocketChannel只会传输此刻准备好的数据（可能不足count字节）。
             * 因此，SocketChannel可能不会将请求的所有数据(count个字节)全部传输到FileChannel中。
             */
            toChannel.transferFrom(fromChannel, position, count);
            /**
             * transferTo()方法将数据从FileChannel传输到其他的channel中。
             */
            fromChannel.transferTo(position, count, toChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
