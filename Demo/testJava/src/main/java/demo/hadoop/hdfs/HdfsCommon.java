package demo.hadoop.hdfs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.util.Progressable;

public class HdfsCommon {
	public static final String FieldDelimit = "\u007F";//字段分隔符，007F是unicode的127，表示删除符
	private Configuration configuration;
	private FileSystem fileSystem;

	public HdfsCommon() throws IOException {
		configuration = new Configuration();
		fileSystem = FileSystem.get(configuration);
	}

	public static void main(String[] args) throws IOException {
		System.out.println("hadoop jar Test.jar test.hadoop.hdfs.HdfsCommon");
		System.out.println("-----------------------Command List-----------------------");
		System.out.println("upFile");
		System.out.println("downFile");
		System.out.println("appendFile");
		System.out.println("delFile");
		System.out.println("writeFile");
		System.out.println("writeFile2");
		System.out.println("uncompress");
		System.out.println("----------------------------------------------");
		if(args.length < 1){//无参数
			System.exit(-1);
		}
		GcsLogBean bean = new GcsLogBean();
		bean.setTradeDate("2015-10-19");//交易日期	
		bean.setTradeTime("23:47:45");//交易时间	
		bean.setTradeApplication("FUNDS.TRANSFER");//交易APPLICATION	
		bean.setTradeVersion("ST-FT-STD-AAA");//交易VERSION	
		bean.setTradeCode("ST-FT-STD-AAA");//交易代码	
		bean.setChannelId("S72");//CHANNEL.ID	
		bean.setFbId("179");//FB.ID	
		bean.setFtTxnType("CBIB0001");//本地交易类型	
		bean.setProcessingTime("1198");//处理时间	
		bean.setResultFlag("1");//交易成功/失败标志,1表示成功，否则为失败	
		bean.setRspCode("0000");//报错码,0000表示成功	
		bean.setRspMsg("SUCC");//错误信息	
		bean.setIsReferMoney("1");//是否涉及账务	
		bean.setAmount("123098");//交易金额
		bean.setBfeTraceNo("5f026593-b1cb-4ab8-bbb5-8acf7d8a8d3b");//交易流水
		bean.setAdapter("BFEII.TXN");//核心ADAPTER
		String aa = bean.toBeSavedString(FieldDelimit)+"\n";
		System.out.println(aa);
		
		for (int i = 0; i < args.length; i++) {
			System.out.println("args["+i+"] = "+args[i]);
		}
		HdfsCommon hdfsCommon=new HdfsCommon();
		String type = args[0];
		
		if("upFile".equals(type)){
			System.out.println("-----------------------Command example-----------------------");
			System.out.println("hadoop jar Test.jar test.hadoop.hdfs.HdfsCommon "
					+ "upFile <localFile> <hdfsFile,i.e /user/hdper/tmp/aa.txt>");
			if(args.length !=3){
				System.exit(-1);
			}
			String localFile = args[1];
			String hdfsFile = args[2];
			hdfsCommon.upFile(localFile, hdfsFile);
		}
		if("downFile".equals(type)){
			System.out.println("-----------------------Command example-----------------------");
			System.out.println("hadoop jar Test.jar test.hadoop.hdfs.HdfsCommon "
					+ "downFile <hdfsFile,i.e /user/hdper/tmp/aa.txt> <localFile>");
			if(args.length !=3){
				System.exit(-1);
			}
			String hdfsFile = args[1];
			String localFile = args[2];
			hdfsCommon.downFile(hdfsFile, localFile);
		}
		if("appendFile".equals(type)){
			System.out.println("-----------------------Command example-----------------------");
			System.out.println("hadoop jar Test.jar test.hadoop.hdfs.HdfsCommon "
					+ "appendFile <localFile> <hdfsFile,i.e /user/hdper/tmp/aa.txt>");
			if(args.length !=3){
				System.exit(-1);
			}
			String localFile = args[1];
			String hdfsFile = args[2];
			hdfsCommon.appendFile(localFile, hdfsFile);
		}
		if("delFile".equals(type)){
			System.out.println("-----------------------Command example-----------------------");
			System.out.println("hadoop jar Test.jar test.hadoop.hdfs.HdfsCommon "
					+ "delFile <hdfsFile,i.e /user/hdper/tmp/aa.txt>");
			if(args.length !=2){
				System.exit(-1);
			}
			String hdfsFile = args[1];
			hdfsCommon.delFile(hdfsFile);
		}
		if("writeFile".equals(type)){
			System.out.println("-----------------------Command example-----------------------");
			System.out.println("hadoop jar Test.jar test.hadoop.hdfs.HdfsCommon "
					+ "writeFile <hdfsFile,i.e /user/hdper/tmp/aa.txt>");
			if(args.length !=2){
				System.exit(-1);
			}
			String hdfsFile = args[1];
			hdfsCommon.writeFile(bean.toBeSavedString(FieldDelimit),hdfsFile);
		}
		if("writeFile2".equals(type)){
			System.out.println("-----------------------Command example-----------------------");
			System.out.println("hadoop jar Test.jar test.hadoop.hdfs.HdfsCommon "
					+ "writeFile2 <hdfsFile,i.e /user/hdper/tmp/aa.txt>");
			if(args.length !=2){
				System.exit(-1);
			}
			String hdfsFile = args[1];
			hdfsCommon.writeFile(bean.toString(),hdfsFile);
		}
		if("uncompress".equals(type)){
			System.out.println("-----------------------Command example-----------------------");
			System.out.println("hadoop jar Test.jar test.hadoop.hdfs.HdfsCommon "
								+ "uncompress <hdfsFile,i.e /user/hdper/tmp/aa.tar.gz>");
			if(args.length !=2){
				System.exit(-1);
			}
			String inputFile = args[1];
			uncompress(inputFile);
		}
	}
	
	public void writeFile(String strToWrite,String hdfsFile) throws IOException{
		strToWrite = strToWrite + "\n";
		Path path = new Path(hdfsFile);
		// writing
		if(fileSystem.isDirectory(path)){
			throw new IOException("data can't be write to a directory,should be write to a file.");
		}
		if(!fileSystem.exists(path)){
			fileSystem.createNewFile(path);
		}
		FSDataOutputStream fsDataOutputStream = fileSystem.append(path);
		byte[] readBuf = strToWrite.getBytes("UTF-8");
		fsDataOutputStream.write(readBuf, 0, readBuf.length);
		fsDataOutputStream.close();
		fileSystem.close();
	}
	/**
	 * 上传文件，
	 * @param localFile 
	 * @param hdfsPath  
	 * @throws IOException
	 */
	public void upFile(String localFile, String hdfsPath) throws IOException {
		Path hdfsFile = new Path(hdfsPath);
		FileStatus fileStatus = fileSystem.getFileStatus(hdfsFile);
		if(fileStatus.isDirectory()){
			File loaclFile = new File(localFile);
			String fileName = loaclFile.getName();
			hdfsPath = hdfsPath+fileName;
			hdfsFile = new Path(hdfsPath);
		}
		InputStream in = new BufferedInputStream(new FileInputStream(localFile));
		OutputStream out = fileSystem.create(hdfsFile);
		IOUtils.copyBytes(in, out, configuration);
	}

	/**
	 * 附加文件
	 * @param localFile
	 * @param hdfsPath
	 * @throws IOException
	 */
	public void appendFile(String localFile, String hdfsPath) throws IOException {
		InputStream in = new FileInputStream(localFile);
		OutputStream out = fileSystem.append(new Path(hdfsPath));
		IOUtils.copyBytes(in, out, configuration);
	}

	/**
	 * 下载文件
	 * @param hdfsPath
	 * @param localPath
	 * @throws IOException
	 */
	public void downFile(String hdfsPath, String localPath) throws IOException {
		InputStream in = fileSystem.open(new Path(hdfsPath));
		OutputStream out = new FileOutputStream(localPath);
		IOUtils.copyBytes(in, out, configuration);
	}

	/**
	 * 删除文件或目录
	 * @param hdfsPath
	 * @throws IOException
	 */
	public void delFile(String hdfsPath) throws IOException {
		fileSystem.delete(new Path(hdfsPath), true);
	}
	
    /**解压缩
     * @param uri
     * @throws IOException
     */
    public static void uncompress(String uri) throws IOException{
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        
        Path inputPath = new Path(uri);
        CompressionCodecFactory factory = new CompressionCodecFactory(conf);
        //使用文件扩展名来推断来的codec来对文件进行解压缩
        CompressionCodec codec = factory.getCodec(inputPath);
        if(codec == null){
            System.out.println("no codec found for " + uri);
            System.exit(1);
        }
        String outputUri = CompressionCodecFactory.removeSuffix(uri, codec.getDefaultExtension());
        InputStream in = null;
        OutputStream out = null;
        try {
            in = codec.createInputStream(fs.open(inputPath));
            out = fs.create(new Path(outputUri));
            IOUtils.copyBytes(in, out, conf);
        } finally{
            IOUtils.closeStream(out);
            IOUtils.closeStream(in);
        }
    }
}
