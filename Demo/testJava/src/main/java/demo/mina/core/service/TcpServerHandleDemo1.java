package demo.mina.core.service;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class TcpServerHandleDemo1 extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    // 接收到新的数据
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        // 接收客户端的数据
        IoBuffer ioBuffer = (IoBuffer) message;
        byte[] byteArray = new byte[ioBuffer.limit()];
        ioBuffer.get(byteArray, 0, ioBuffer.limit());
        System.out.println("messageReceived:" + new String(byteArray, "UTF-8"));

        // 发送到客户端
        byte[] responseByteArray = "你好".getBytes("UTF-8");
        IoBuffer responseIoBuffer = IoBuffer.allocate(responseByteArray.length);
        responseIoBuffer.put(responseByteArray);
        responseIoBuffer.flip();
        // 发送数据到客户端。session.write返回值是WriteFuture类型。WriteFuture也可以通过addListener方法添加IoFutureListener监听器。
        // IoFutureListener接口的抽象方法operationComplete会在write完成（无论成功或失败）时被调用。如果需要在write完成后进行一些操作，只需实现operationComplete方法。
        WriteFuture future = session.write(responseIoBuffer);
        future.addListener(new IoFutureListener<WriteFuture>() {

            // write操作完成后调用的回调函数
            @Override
            public void operationComplete(WriteFuture future) {
                if (future.isWritten()) {
                    System.out.println("write操作成功");
                } else {
                    System.out.println("write操作失败");
                }
            }
        });
        // MINA和Netty不同在于关闭连接的close方法。使用带一个boolean类型参数的close(boolean immediately)方法。
        // immediately为true则直接关闭连接，为false则是等待所有的异步write完成后再关闭连接。
        session.close(false); // 虽然write是异步的，但是immediately参数为false会等待write完成后再关闭连接

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("sessionCreated");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed");
    }
}
