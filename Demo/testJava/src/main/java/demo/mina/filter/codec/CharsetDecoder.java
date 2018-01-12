package demo.mina.filter.codec;

import java.nio.charset.StandardCharsets;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符解码
 *
 */
public class CharsetDecoder implements ProtocolDecoder {

    private final static Logger log = LoggerFactory.getLogger(CharsetDecoder.class);

    /**
     * IoBuffer，MiNa中传输的所有二进制信息都存放在IoBuffer中，IoBuffer是对Java NIO中ByteBuffer的封装（Mina2.0以前版本这个接口也是ByteBuffer），
     * 提供了更多操作二进制数据，对象的方法，并且存储空间可以自增长，用起来非常方便；简单理解，它就是个可变长度的byte字节数组
     */
    private IoBuffer buff = IoBuffer.allocate(100).setAutoExpand(true);

    /**
     * 解码方法，它主要是把读取到数据中的换行符去掉。因为在mina通信协议中以换行符为结束符，如果不定义结束符那么程序会在那里一直等待下一条发送的数据。
     */
    @Override
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        log.info("#########decode#########");

        // 如果有消息
        while (in.hasRemaining()) {
            // 判断消息是否是结束符，不同平台的结束符也不一样；
            // windows换行符（\r\n）就认为是一个完整消息的结束符了； UNIX 是\n；MAC 是\r
            byte b = in.get();
            if (b == '\n') {
                buff.flip();
                byte[] bytes = new byte[buff.limit()];
                buff.get(bytes);
                String message = new String(bytes, StandardCharsets.UTF_8);

                buff = IoBuffer.allocate(100).setAutoExpand(true);

                // 如果结束了，就写入转码后的数据
                out.write(message);
                log.info("message: " + message);
            } else {
                buff.put(b);
            }
        }

    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
        log.info("#########完成解码#########");
    }

    @Override
    public void dispose(IoSession session) throws Exception {
        log.info("#########dispose#########");
        log.info(session.getCurrentWriteMessage().toString());
    }

}
