package demo.ftp.pool2;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.ftp.FTPException;
import demo.ftp.pool.FTPClientConfigure;

public class PooledFtpClientFactory implements PooledObjectFactory<FTPClient> {

    private FTPClientConfigure config;
    
    private static Logger logger = LoggerFactory.getLogger(PooledFtpClientFactory.class);

    // 给工厂传入一个参数对象，方便配置FTPClient的相关参数
    public PooledFtpClientFactory(FTPClientConfigure config) {
        this.config = config;
    }
    
    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        long t0 = System.currentTimeMillis();
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(config.getClientTimeout());
        try {
            ftpClient.connect(config.getHost(), config.getPort());
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.warn("FTPServer refused connection");
                return null;
            }
            boolean result = ftpClient.login(config.getUsername(), config.getPassword());
            if (!result) {
                throw new FTPException(
                        "ftpClient登陆失败! userName:" + config.getUsername() + " ; password:" + config.getPassword());
            }
            ftpClient.setFileType(config.getTransferFileType());
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding(config.getEncoding());
            if (config.getPassiveMode().equals("true")) {
                // 这个方法的意思就是每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据。
                ftpClient.enterLocalPassiveMode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        }
        long t1 = System.currentTimeMillis();
        logger.info("获取FTP连接耗时{}ms.",(t1-t0));
        return new DefaultPooledObject<FTPClient>(ftpClient);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws Exception {
        long t0 = System.currentTimeMillis();
        FTPClient ftpClient = p.getObject();
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            // 注意,一定要在finally代码中断开连接，否则会导致占用ftp连接情况
            try {
                ftpClient.disconnect();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        long t1 = System.currentTimeMillis();
        logger.info("断开FTP连接耗时{}ms.",(t1-t0));
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        FTPClient ftpClient = p.getObject();
        try {
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            throw new RuntimeException("Failed to validate client: " + e, e);
        }
    }

    // 激活一个对象或者说启动对象的某些操作。比如，如果对象是socket，如果socket没有连接，或意外断开了，可以在这里启动socket的连接。
    // 它会在检测空闲对象的时候，如果设置了测试空闲对象是否可以用，就会调用这个方法，在borrowObject的时候也会调用。
    // 另外，如果对象是一个包含参数的对象，可以在这里进行初始化。让使用者感觉这是一个新创建的对象一样。
    @Override
    public void activateObject(PooledObject<FTPClient> p) throws Exception {
        // TODO Auto-generated method stub

    }

    // 钝化一个对象。在向对象池归还一个对象是会调用这个方法。这里可以对对象做一些清理操作。比如清理掉过期的数据，下次获得对象时，不受旧数据的影响。
    @Override
    public void passivateObject(PooledObject<FTPClient> p) throws Exception {
        // TODO Auto-generated method stub

    }

}
