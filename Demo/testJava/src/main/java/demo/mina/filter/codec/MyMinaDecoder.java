package demo.mina.filter.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import demo.java.lang.PrimitiveData;

/**
 * 用固定字节数的Header来指定Body字节数的消息分割方案，其中Header部分是常规的大字节序（Big-Endian）的4字节整数。
 * 本文中对这个方案稍作修改，将固定字节数的Header改为小字节序（Little-Endian）的4字节整数。
 *
 */
public class MyMinaDecoder extends CumulativeProtocolDecoder {

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        // 如果没有接收完Header部分（4字节），直接返回false
        if (in.remaining() < 4) {
            return false;
        } else {
            // 标记开始位置，如果一条消息没传输完成则返回到这个位置
            in.mark();

            byte[] bytes = new byte[4];
            in.get(bytes); // 读取4字节的Header

            int bodyLength = PrimitiveData.getLittleEndianInt(bytes); // 按小字节序转int

            // 如果body没有接收完整，直接返回false
            if (in.remaining() < bodyLength) {
                in.reset(); // IoBuffer position回到原来标记的地方
                return false;
            } else {
                byte[] bodyBytes = new byte[bodyLength];
                in.get(bodyBytes);
                String body = new String(bodyBytes, "UTF-8");
                out.write(body); // 解析出一条消息
                return true;
            }
        }
    }
}