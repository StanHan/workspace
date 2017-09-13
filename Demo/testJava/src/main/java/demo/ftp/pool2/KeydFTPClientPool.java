package demo.ftp.pool2;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import demo.ftp.pool.FTPClientConfigure;

/**
 * GenericKeyedObjectPool的对象池看作是一个map的GenericObjectPool，每个key对应一个GenericObjectPool。它用于区别不同类型的对象。
 * @author hanjy
 *
 */
public class KeydFTPClientPool extends GenericKeyedObjectPool<FTPClientConfigure, FTPClient> {

    public KeydFTPClientPool(KeyedPooledObjectFactory<FTPClientConfigure, FTPClient> factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config);
        
    }

    public KeydFTPClientPool(KeyedPooledObjectFactory<FTPClientConfigure, FTPClient> factory) {
        super(factory);
        // TODO Auto-generated constructor stub
    }

}
