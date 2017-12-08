package demo.java.nio;

import java.nio.charset.StandardCharsets;

/**
 * java.nio（java non-blocking IO）,java非阻塞式IO。 特 性 Channel,Buffer,Selector 目 的 提供非阻塞式的高伸缩性网络。
 * 
 * Channel 有点象流，数据可以从Channel读到Buffer中，也可以从Buffer 写到Channel中。
 * 
 * <li>关键的Buffer实现：ByteBuffer\CharBuffer\DoubleBuffer\FloatBuffer\IntBuffer\LongBuffer\ShortBuffer\MappedByteBuffer用于表示内存映射文件，
 * <li>主要Channel的实现：FileChannel\DatagramChannel(通过UDP读写网络中的数据)\SocketChannel(通过TCP读写网络中的数据)\ServerSocketChannel(监听新进来的TCP连接，对每一个新进来的连接都会创建一个SocketChannel)
 * <li>Selector允许单线程处理多个 Channel。如果你的应用打开了多个连接（通道），但每个连接的流量都很低，使用Selector就会很方便。
 * 
 * 要使用Selector，得向Selector注册Channel，然后调用它的select()方法。这个方法会一直阻塞到某个注册的通道有事件就绪。一旦这个方法返回，线程就可以处理这些事件，
 */
public class NonBlockingDemo {

    public static void main(String[] args) throws Exception {
        MappedFile file = new MappedFile("D:/logs/aa.txt");
        if (file.boundChannelToByteBuffer()) {
            for (int i = 0; i < 10_0000; i++) {
                file.appendData(
                        "java.nio（java non-blocking IO）,java非阻塞式IO。 特 性 Channel,Buffer,Selector 目 的 提供非阻塞式的高伸缩性网络。 Channel 有点象流， 数据可以从Channel读到Buffer中，也可以从Buffer 写到Channel中。\n"
                                .getBytes(StandardCharsets.UTF_8));
            }
        }
        ;
    }

}
