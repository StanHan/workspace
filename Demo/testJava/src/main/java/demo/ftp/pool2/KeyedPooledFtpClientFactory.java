package demo.ftp.pool2;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.ftp.FTPException;
import demo.ftp.pool.FTPClientConfigure;

public class KeyedPooledFtpClientFactory implements KeyedPooledObjectFactory<FTPClientConfigure, FTPClient> {

    private static Logger logger = LoggerFactory.getLogger(KeyedPooledFtpClientFactory.class);
    
    @Override
    public PooledObject<FTPClient> makeObject(FTPClientConfigure key) throws Exception {
        long t0 = System.currentTimeMillis();
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(key.getClientTimeout());
        try {
            ftpClient.connect(key.getHost(), key.getPort());
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.warn("FTPServer refused connection");
                return null;
            }
            boolean result = ftpClient.login(key.getUsername(), key.getPassword());
            if (!result) {
                throw new FTPException(
                        "ftpClient登陆失败! userName:" + key.getUsername() + " ; password:" + key.getPassword());
            }
            ftpClient.setFileType(key.getTransferFileType());
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding(key.getEncoding());
            if (key.getPassiveMode().equals("true")) {
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
    public void destroyObject(FTPClientConfigure key, PooledObject<FTPClient> p) throws Exception {
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
    public boolean validateObject(FTPClientConfigure key, PooledObject<FTPClient> p) {
        FTPClient ftpClient = p.getObject();
        try {
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            throw new RuntimeException("Failed to validate client: " + e, e);
        }
    }

    @Override
    public void activateObject(FTPClientConfigure key, PooledObject<FTPClient> p) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void passivateObject(FTPClientConfigure key, PooledObject<FTPClient> p) throws Exception {
        // TODO Auto-generated method stub
        
    }
    

}
