package demo.java.nio.channels;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SocketChannelClientDemo {

    public static void main(String args[]) {
        new SocketChannelClientDemo().start();
    }

    public void start() {
        SocketAddress address = new InetSocketAddress("localhost", 5555);
        try (SocketChannel client = SocketChannel.open(address);) {
            client.configureBlocking(false);
            String a = "asdasdasdasddffasfas";
            ByteBuffer buffer = ByteBuffer.allocate(a.length());
            buffer.put(a.getBytes(StandardCharsets.UTF_8));
            buffer.clear();
            int d = client.write(buffer);
            System.out.println("发送数据: " + new String(buffer.array()));
            while (true) {
                buffer.flip();
                int i = client.read(buffer);
                if (i > 0) {
                    byte[] b = buffer.array();
                    System.out.println("接收数据: " + new String(b, StandardCharsets.UTF_8));
                    client.close();
                    System.out.println("连接关闭!");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
