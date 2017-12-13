package demo.java.nio.channels;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * java NIO 服务器端例子
 * 
 * @author user
 *
 */
public class ServerSocketChannelDemo {

    public static void main(String args[]) {
        new ServerSocketChannelDemo().start();
    }

    public void start() {

        try (
                /** 异步IO的核心对象名selector 具有事件侦听的效果,selector就是您注册对各种io事件的兴趣的地方 而且当那些事件发生时 就是这个对象告诉您所发生的事情 */
                Selector selector = Selector.open();
                /** 打开一个serversocketchannel通道 */
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {
            // 设为异步
            serverSocketChannel.configureBlocking(false);
            // 绑定端口
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(5555);
            serverSocket.bind(address);
            /** 注册事件 regisiter的第一个参数总是selector 第二个总是op_accept 这里他指定我们要监听accept事件,也就是当有新的链接进来是发生的事件 */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("端口注册完成");
            while (true) {
                /** select()这个方法会阻塞 直到有一个已注册的事件发生,当一个或者更多的事件注册进来的时候 这个会返回事件的数量 */
                int count = selector.select();
                /** 调用selectedKeys()会返回事件对象集合 */
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 然后我们迭代处理每一个事件
                ByteBuffer byteBuffer = ByteBuffer.allocate(20);
                SocketChannel socketChannel;
                for (SelectionKey selectionKey : selectionKeys) {
                    // 判断事件类型
                    if ((selectionKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                        ServerSocketChannel nssc = (ServerSocketChannel) selectionKey.channel();
                        socketChannel = nssc.accept();
                        // 设为非阻塞
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("有新的链接" + socketChannel);
                    } else if ((selectionKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        socketChannel = (SocketChannel) selectionKey.channel();
                        while (true) {
                            byteBuffer.clear();
                            int a = socketChannel.read(byteBuffer);
                            if (a == -1) {
                                break;
                            }
                            if (a > 0) {
                                byte[] b = byteBuffer.array();
                                System.out.println("接收数据: " + new String(b));
                                byteBuffer.flip();
                                socketChannel.write(byteBuffer);
                                System.out.println("返回数据: " + new String(b));
                            }
                        }
                        socketChannel.close();
                        System.out.println("连接结束");
                        System.out.println("=============================");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
