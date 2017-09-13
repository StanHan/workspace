package demo.ftp.pool2;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class FTPClientPool extends GenericObjectPool<FTPClient> {

    public FTPClientPool(PooledObjectFactory<FTPClient> factory, GenericObjectPoolConfig config,
            AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }

    public FTPClientPool(PooledObjectFactory<FTPClient> factory, GenericObjectPoolConfig config) {
        super(factory, config);
        // TODO Auto-generated constructor stub
    }

    public FTPClientPool(PooledObjectFactory<FTPClient> factory) {
        super(factory);
        // TODO Auto-generated constructor stub
    }

}
