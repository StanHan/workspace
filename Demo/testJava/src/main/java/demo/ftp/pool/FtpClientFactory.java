package demo.ftp.pool;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool.PoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.ftp.FTPException;

/**
 * FTPClient工厂类，通过FTPClient工厂提供FTPClient实例的创建和销毁
 * 
 * @author hanjy
 */
public class FtpClientFactory implements PoolableObjectFactory<FTPClient> {
    private static Logger logger = LoggerFactory.getLogger(FtpClientFactory.class);
    private FTPClientConfigure config;

    // 给工厂传入一个参数对象，方便配置FTPClient的相关参数
    public FtpClientFactory(FTPClientConfigure config) {
        this.config = config;
    }

    @Override
    public FTPClient makeObject() throws Exception {
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
        return ftpClient;
    }

    @Override
    public void destroyObject(FTPClient ftpClient) throws Exception {
        long t0 = System.currentTimeMillis();
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
    public boolean validateObject(FTPClient ftpClient) {
        try {
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            throw new RuntimeException("Failed to validate client: " + e, e);
        }
    }

    @Override
    public void activateObject(FTPClient ftpClient) throws Exception {
    }

    @Override
    public void passivateObject(FTPClient ftpClient) throws Exception {

    }
}