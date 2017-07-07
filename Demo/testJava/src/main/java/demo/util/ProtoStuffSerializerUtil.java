package demo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * @author zhoujunyang
 * @version 创建时间：2016年2月24日  下午8:45:22
 * java对象序列化和反序列化公共类
 */
public class ProtoStuffSerializerUtil {
	
	/**
	 * @author zhoujunyang
	 * @version 创建时间：2016年2月25日  上午9:55:44
	 * @param obj 需要序列化的对象
	 * @return 序列化的字节码
	 * 序列化对象
	 */
	public static <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new RuntimeException("Serializer(" + obj + ") error! parameter obj is null");
        }
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protostuff = null;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new RuntimeException("Serializer(" + obj.getClass() + ") object(" + obj + ") exception!", e);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    /**
     * @author zhoujunyang
     * @version 创建时间：2016年2月25日  上午9:56:24
     * @param paramArrayOfByte 需要反序列化的字节码
     * @param targetClass 需要反序列化成为的对象
     * @return 序列化后的对象
     * 将字节数组反序列化为对象
     */
    public static <T> T deserialize(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            throw new RuntimeException("deserialize exception,byte is null!");
        }
        T instance = null;
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("deserialize exception targetClass is not match", e);
        }
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        ProtostuffIOUtil.mergeFrom(paramArrayOfByte, instance, schema);
        return instance;
    }
    
    /**
     * @author zhoujunyang
     * @version 创建时间：2016年2月25日  上午9:57:35
     * @param objList 需要序列化的list对象
     * @return 序列化的字节数组
	 * 序列化list对象
     */
    public static <T> byte[] serializeList(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            throw new RuntimeException("Serializer object List(" + objList + ") parameter objList is null");
        }
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(objList.get(0).getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protostuff = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
            protostuff = bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Serializer object List(" + objList + ") exception!", e);
        } finally {
            buffer.clear();
            try {
                if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return protostuff;
    }
    
    /**
     * @author zhoujunyang
     * @version 创建时间：2016年2月25日  上午9:58:35
     * @param paramArrayOfByte  需要反序列化的字节码
     * @param targetClass  需要反序列化成为的对象
     * @return  反序列化后的list对象
     * 反序列化后的list对象
     */
    public static <T> List<T> deserializeList(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            throw new RuntimeException("deserialize exception,parameter byte is null!");
        }
        
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        List<T> result = null;
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(paramArrayOfByte), schema);
        } catch (IOException e) {
            throw new RuntimeException("deserialize exception!",e);
        }
        return result;
    }

}
