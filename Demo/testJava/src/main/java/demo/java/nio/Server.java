package demo.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * java NIO 服务器端例子
 * @author user
 *
 */
public class Server {
	    //服务器端通道
	    ServerSocketChannel ssc;
	    
	    public void start(){
	        try {
	            //异步IO的核心对象名selector 具有事件侦听的效果
	            //selector就是您注册对各种io事件的兴趣的地方 而且当那些事件发生时 就是这个对象告诉您所发生的事情
	            Selector selector=Selector.open();
	            //打开一个serversocketchannel通道
	            ServerSocketChannel ssc=ServerSocketChannel.open();
	            //设为异步
	            ssc.configureBlocking(false);
	            //绑定端口
	            ServerSocket ss=ssc.socket();
	            InetSocketAddress address=new InetSocketAddress(5555);
	            ss.bind(address);
	            //注册事件 regisiter的第一个参数总是selector 第二个总是op_accept 这里他指定我们要监听accept事件
	            //也就是当有新的链接进来是发生的事件
	            ssc.register(selector,SelectionKey.OP_ACCEPT);
	            System.out.println("端口注册完成");
	            while(true){
	                //select()这个方法会阻塞 直到有一个已注册的事件发生 当一个或者更多的事件注册进来的时候 这个会返回事件的数量
	                selector.select();
	                //调用selectedKeys()会返回事件对象集合
	                Set<SelectionKey> selectionKeys=selector.selectedKeys();
	                //然后我们迭代处理每一个事件
	                Iterator<SelectionKey> iter=selectionKeys.iterator();
	                ByteBuffer echoBuffer=ByteBuffer.allocate(20); 
	                SocketChannel sc;
	                while(iter.hasNext()){
	                    SelectionKey key=iter.next();
	                    //判断事件类型
	                    if((key.readyOps()&SelectionKey.OP_ACCEPT)==SelectionKey.OP_ACCEPT) 
	                    { 
	                        ServerSocketChannel nssc=(ServerSocketChannel)key.channel();
	                        sc=nssc.accept();
	                        //设为非阻塞
	                        sc.configureBlocking(false);
	                        sc.register(selector, SelectionKey.OP_READ);
	                        iter.remove();
	                        System.out.println("有新的链接"+sc);
	                    }else if((key.readyOps()&SelectionKey.OP_READ)==SelectionKey.OP_READ) {
	                        sc=(SocketChannel)key.channel();
	                        while(true){
	                            echoBuffer.clear();
	                            int a=sc.read(echoBuffer);
	                            
	                            if(a==-1)
	                                break;
	                            if(a>0){
	                                byte[] b=echoBuffer.array();
	                                System.out.println("接收数据: "+new String(b)); 
	                                echoBuffer.flip(); 
	                                sc.write(echoBuffer); 
	                                System.out.println("返回数据: "+new String(b)); 
	                            }
	                        }
	                        sc.close();
	                        System.out.println("连接结束"); 
	                        System.out.println("============================="); 
	                        iter.remove(); 
	                    }
	                }
	            }
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    
	    public static void main(String args[]){
	        new Server().start();
	    }
}

class Client {
    public void start() {
        try {
            SocketAddress address = new InetSocketAddress("localhost", 5555);
            SocketChannel client = SocketChannel.open(address);
            client.configureBlocking(false);
            String a = "asdasdasdasddffasfas";
            ByteBuffer buffer = ByteBuffer.allocate(20);
            buffer.put(a.getBytes());
            buffer.clear();
            int d = client.write(buffer);
            System.out.println("发送数据: " + new String(buffer.array()));
            while (true) {
                buffer.flip();
                int i = client.read(buffer);
                if (i > 0) {
                    byte[] b = buffer.array();
                    System.out.println("接收数据: " + new String(b));
                    client.close();
                    System.out.println("连接关闭!");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]){
        new Client().start();
    }
}
