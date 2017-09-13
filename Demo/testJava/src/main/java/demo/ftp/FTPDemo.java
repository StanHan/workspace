package demo.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP是一个比较特殊的服务，它占用了20和21两个端口，21是命令端口，20是数据端口。
 * <p>
 * 主动FTP过程大致如下：
 * <ol>
 * <li>1、客户端启用端口N（N>1024,因为1024之前为特殊端口，不能手动占用，把N当作客户端的命令端口）和端口N+1（客户端的数据端口），从端口N向服务器的21端口发送PORT命令，其中PORT命令包含客户端IP和数据端口
 * <li>2、服务器接收到客户端的PORT命令后，并得知客户端用N+1端口监听数据。接着，服务器向客户端发送ACK应答（ACK与TCP通信中的连接握手一样）
 * <li>3、服务器用20端口再向客户端的N+1端口发送数据请求
 * <li>4、客户端向服务器端发送数据ACK应答
 * </ol>
 * 以上就是主动FTP的大致过程，但是数据请求的发起方是服务器，如果此时客户端的防火墙启用了高端端口的屏蔽的话，有可能会发生阻塞，所以主动FTP的情况下，客户端最好把防火墙关闭了。
 * 
 * 被动FTP过程大致如下：
 * <ol>
 * <li>1、客户端启用端口N（同样的N>1024）和N+1，N用作命令端口，N+1用作数据端口。然后客户端向服务器端发送PASV请求，告诉服务器端，这是被动FTP请求
 * <li>2、服务器端接收到PASV请求后，启动一个M（同样>1024）端口当作数据端，并发送PORT M到客户端
 * <li>3、客户端得到服务器端的数据端口后，再由端口N+1向服务器的M端口发起数据请求
 * <li>4、服务器端通过N端口向客户端的N+1端口发送ACK应答
 * </ol>
 * 以上是被动FTP的大致过程，与主动FTP请求不同，请求的发起方是客户端，这样客户端就不会为防火墙的问题感到烦恼，但是同样道理，服务器端的端口就会有了限制。
 * 
 * 所以，一般情况下。服务器端为了方便管理，一般采用被动FTP方式连接。当然客户端可以通过ftp -d host port命令向服务器发送请求，可以看出到底用的是主动FTP还是被动FTP。
 *
 */
public class FTPDemo {

    public static void main(String[] args) throws Exception {
    }

    void demo2() {
        FTPDemo ftp = new FTPDemo("10.3.15.1", 21, "ghips", "ghipsteam");
        ftp.ftpLogin();
        // 上传文件夹
        ftp.uploadDirectory("d://DataProtemp", "/home/data/");
        // 下载文件夹
        ftp.downLoadDirectory("d://tmp//", "/home/data/DataProtemp");
        ftp.ftpLogOut();
    }

    public void list(String directory) throws IOException {

        FTPListParseEngine engine = ftpClient.initiateListParsing(directory);

        while (engine.hasNext()) {
            FTPFile[] files = engine.getNext(25); // "page size" you want
            // do whatever you want with these files, display them, etc.
            // expensive FTPFile objects not created until needed.
        }
    }

    static void demo() {
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        // config.setXXX(YYY); // change required options
        ftp.setControlKeepAliveTimeout(300); // set timeout to 5 minutes
        // for example config.setServerTimeZoneId("Pacific/Pitcairn")
        ftp.configure(config);
        boolean error = false;
        try {
            String server = "ftp.example.com";
            ftp.connect(server);
            System.out.println("Connected to " + server + ".");
            System.out.print(ftp.getReplyString());

            // After connection attempt, you should check the reply code to verify success.
            int reply = ftp.getReplyCode();
            String[] replyStrings = ftp.getReplyStrings();
            if (replyStrings != null && replyStrings.length > 0) {
                System.out.println(Arrays.toString(replyStrings));
            }

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }

            ftp.sendNoOp();
            // transfer files
            ftp.logout();
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
            System.exit(error ? 1 : 0);
        }

    }

    private FTPClient ftpClient;
    private String server;
    private int port;
    private String user;
    private String password;

    private static Logger logger = LoggerFactory.getLogger(FTPDemo.class);

    public FTPDemo(String strIp, int intPort, String user, String Password) {
        this.server = strIp;
        this.port = intPort;
        this.user = user;
        this.password = Password;
        this.ftpClient = new FTPClient();
    }

    /**
     * @return 判断是否登入成功
     */
    public boolean ftpLogin() {
        boolean isLogin = false;
        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
        this.ftpClient.setControlEncoding("GBK");
        this.ftpClient.configure(ftpClientConfig);
        try {
            if (this.port > 0) {
                this.ftpClient.connect(this.server, this.port);
            } else {
                this.ftpClient.connect(this.server);
            }
            // FTP服务器连接回答
            int reply = this.ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.ftpClient.disconnect();
                logger.error("登录FTP服务失败！");
                return isLogin;
            }
            this.ftpClient.login(this.user, this.password);
            // 设置传输协议
            this.ftpClient.enterLocalPassiveMode();
            this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            logger.info("恭喜" + this.user + "成功登陆FTP服务器");
            isLogin = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(this.user + "登录FTP服务失败！" + e.getMessage());
        }
        this.ftpClient.setBufferSize(1024 * 2);
        this.ftpClient.setDataTimeout(30 * 1000);
        return isLogin;
    }

    /**
     * @退出关闭服务器链接
     */
    public void ftpLogOut() {
        if (null != this.ftpClient && this.ftpClient.isConnected()) {
            try {
                boolean reuslt = this.ftpClient.logout();// 退出FTP服务器
                if (reuslt) {
                    logger.info("成功退出服务器");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.warn("退出FTP服务器异常！" + e.getMessage());
            } finally {
                try {
                    this.ftpClient.disconnect();// 关闭FTP服务器的连接
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.warn("关闭FTP服务器的连接异常！");
                }
            }
        }
    }

    /***
     * 上传Ftp文件
     * 
     * @param localFile
     *            当地文件
     * @param romotUpLoadePath上传服务器路径
     *            - 应该以/结束
     */
    public boolean uploadFile(File localFile, String romotUpLoadePath) {
        BufferedInputStream inStream = null;
        boolean success = false;
        try {
            this.ftpClient.changeWorkingDirectory(romotUpLoadePath);// 改变工作路径
            inStream = new BufferedInputStream(new FileInputStream(localFile));
            logger.info(localFile.getName() + "开始上传.....");
            success = this.ftpClient.storeFile(localFile.getName(), inStream);
            if (success == true) {
                logger.info(localFile.getName() + "上传成功");
                return success;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(localFile + "未找到");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /***
     * 下载文件
     * 
     * @param remoteFileName
     *            待下载文件名称
     * @param localDires
     *            下载到当地那个路径下
     * @param remoteDownLoadPath
     *            remoteFileName所在的路径
     */

    public boolean downloadFile(String remoteFileName, String localDires, String remoteDownLoadPath) {
        String strFilePath = localDires + remoteFileName;
        BufferedOutputStream outStream = null;
        boolean success = false;
        try {
            this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
            outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
            logger.info(remoteFileName + "开始下载....");
            success = this.ftpClient.retrieveFile(remoteFileName, outStream);
            if (success == true) {
                logger.info(remoteFileName + "成功下载到" + strFilePath);
                return success;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(remoteFileName + "下载失败");
        } finally {
            if (null != outStream) {
                try {
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (success == false) {
            logger.error(remoteFileName + "下载失败!!!");
        }
        return success;
    }

    /***
     * @上传文件夹
     * @param localDirectory
     *            当地文件夹
     * @param remoteDirectoryPath
     *            Ftp 服务器路径 以目录"/"结束
     */
    public boolean uploadDirectory(String localDirectory, String remoteDirectoryPath) {
        File src = new File(localDirectory);
        try {
            remoteDirectoryPath = remoteDirectoryPath + src.getName() + "/";
            this.ftpClient.makeDirectory(remoteDirectoryPath);
            // ftpClient.listDirectories();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(remoteDirectoryPath + "目录创建失败");
        }
        File[] allFile = src.listFiles();
        for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
            if (!allFile[currentFile].isDirectory()) {
                String srcName = allFile[currentFile].getPath().toString();
                uploadFile(new File(srcName), remoteDirectoryPath);
            }
        }
        for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
            if (allFile[currentFile].isDirectory()) {
                // 递归
                uploadDirectory(allFile[currentFile].getPath().toString(), remoteDirectoryPath);
            }
        }
        return true;
    }

    /***
     * 下载文件夹
     * 
     * @param localDirectoryPath本地地址
     * @param remoteDirectory
     *            远程文件夹
     */
    public boolean downLoadDirectory(String localDirectoryPath, String remoteDirectory) {
        try {
            String fileName = new File(remoteDirectory).getName();
            localDirectoryPath = localDirectoryPath + fileName + "//";
            new File(localDirectoryPath).mkdirs();
            FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (!allFile[currentFile].isDirectory()) {
                    downloadFile(allFile[currentFile].getName(), localDirectoryPath, remoteDirectory);
                }
            }
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (allFile[currentFile].isDirectory()) {
                    String strremoteDirectoryPath = remoteDirectory + "/" + allFile[currentFile].getName();
                    downLoadDirectory(localDirectoryPath, strremoteDirectoryPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("下载文件夹失败");
            return false;
        }
        return true;
    }

    // FtpClient的Set 和 Get 函数
    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

}
