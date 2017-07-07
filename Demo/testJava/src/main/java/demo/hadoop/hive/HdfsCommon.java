package demo.hadoop.hive;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import test.hadoop.hdfs.GcsLogBean;

public class HdfsCommon implements Runnable{
	private Configuration configuration;
	private FileSystem fileSystem;
	private FSDataOutputStream fsDataOutputStream;
	private Path filePath;

	/**
	 * @param hdfsFile 文件路径
	 * @throws IOException
	 */
	public HdfsCommon(String hdfsFile) throws IOException {
		configuration = new Configuration();
		fileSystem = FileSystem.get(configuration);
		filePath = new Path(hdfsFile);
		if(!fileSystem.exists(filePath)){
			fileSystem.createNewFile(filePath);
		}
		FileStatus fileStatus = fileSystem.getFileStatus(filePath);
		if(fileStatus.isDirectory()){//参数不能是文件夹
			throw new IllegalArgumentException("argument should not be a directory,but a file path");
		}
		fsDataOutputStream = fileSystem.append(filePath);
	}

	/**向HDFS写入数据
	 * @param strToWrite
	 * @throws IOException
	 */
	public void writeFile(String strToWrite) throws IOException{
		strToWrite = strToWrite + "\n";
		byte[] readBuf = strToWrite.getBytes("UTF-8");
		fsDataOutputStream.write(readBuf, 0, readBuf.length);
	}
	
	/**向HDFS写入数据
	 * @param GcsLogBean
	 * @throws IOException
	 */
	public synchronized void writeFile(GcsLogBean bean) throws IOException{
		String strToWrite = bean.toBeSavedString("\u007F") + "\n";
		byte[] readBuf = strToWrite.getBytes("UTF-8");
		fsDataOutputStream.write(readBuf, 0, readBuf.length);
	}
	
	public void close() throws IOException{
		fsDataOutputStream.close();
		fileSystem.close();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
