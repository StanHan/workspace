package demo.ftp.pool;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoolDemo {
    
    private static Logger logger = LoggerFactory.getLogger(PoolDemo.class);

    public static void main(String[] args) throws Throwable {
        demoPool();
    }
    
    static void demoPool() throws Exception {
        FTPClientConfigure configure = new FTPClientConfigure();
        configure.setHost("221.228.75.188");
        configure.setUsername("test_user_resource_opt1");
        configure.setPassword("dGVzdF91c2VyX3Jlc291cmNlX29wdDFAMjM0NS5jb20K");
        configure.setPort(21);
        configure.setClientTimeout(30_000);
        configure.setPassiveMode("true");
        configure.setTransferFileType(FTPClient.BINARY_FILE_TYPE);
        configure.setEncoding("GBK");

        FtpClientFactory factory = new FtpClientFactory(configure);
        FTPClientPool pool = new FTPClientPool(2, factory);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 50; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    FTPClient ftpClient = null;
                    try {
                        ftpClient = pool.borrowObject();
                        long t = System.currentTimeMillis();
                        String localFileName = "G:/logs/" + t + ".jpg";
                        File file = new File(localFileName);
                        try (FileOutputStream fos = new FileOutputStream(file);) {
                            boolean ok = ftpClient.retrieveFile(
                                    "/2017/06/17/SHB90001/4897b5b823bf746423c0/70/70-1--20170904000439.jpg", fos);
                            fos.flush();
                            long t1 = System.currentTimeMillis();
                            logger.info("download {},take {}ms.",ok,(t1-t));
                        } catch (Exception e) {
                            // invalidate the object
                            pool.invalidateObject(ftpClient);
                            // do not return the object to the pool twice
                            ftpClient = null;
                        } finally {
                            // make sure the object is returned to the pool
                            if (null != ftpClient) {
                                pool.returnObject(ftpClient);
                            }
                        }
                    } catch (Exception e) {
                        // failed to borrow an object
                    }
                }
            };
            executor.submit(runnable);
        }
        executor.shutdown();
        while(!executor.isTerminated()) {
            logger.info("executor is not Terminated");
            Thread.sleep(100);
        }
        pool.close();

    }

}
