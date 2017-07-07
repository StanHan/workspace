package demo.hadoop.hdfs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class HdfsUrlRreader {

	public static String url = "hdfs://192.168.216.134/user/hadoop/input/test.txt";
	static{
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}
	
	public static void main(String[] args) {
		InputStream inputStream = null;
		System.out.println("------------------------------------");
		try {
			String hdfsFile = args[0];
			inputStream = new URL(hdfsFile).openStream();
			IOUtils.copyBytes(inputStream, System.out, 1024, false);
		} catch (Exception e) {
			IOUtils.closeStream(inputStream);
			System.out.println(e);
		}
	}
	
	public void createFile(String localPath,String hdfsPath)throws IOException{
		InputStream inputStream = null;
		try {
			Configuration conf = new Configuration();
			FileSystem fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
			FSDataOutputStream out = fileSystem.create(new Path(hdfsPath));
			inputStream = new BufferedInputStream(new FileInputStream(new File(localPath)));
			IOUtils.copyBytes(inputStream, out, 4096, false);
			out.hsync();
			out.close();
		} catch (Exception e) {
			IOUtils.closeStream(inputStream);
		}
	}

	public void getFile(String localPath,String hdfsPath,boolean print)throws IOException{
		Configuration conf = new Configuration();
		FileSystem fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
		FSDataInputStream in = null;
		OutputStream out = null;
		try {
			in = fileSystem.open(new Path(hdfsPath));
			out = new BufferedOutputStream(new FileOutputStream(new File(localPath)));
			IOUtils.copyBytes(in, out, 4096, !print);
			if(print){
				in.seek(0);
				IOUtils.copyBytes(in, out, 4096, true);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			IOUtils.closeStream(out);
		}
	}
	
	public void fileCopyWithProgress(String localSrc,String dst) throws IOException{
		InputStream in = new BufferedInputStream(new FileInputStream(new File(localSrc)));
		Configuration conf = new Configuration();
		FileSystem fileSystem = FileSystem.get(URI.create(dst), conf);
		OutputStream out = fileSystem.create(new Path(dst), new Progressable() {
			@Override
			public void progress() {
				System.out.println(".");
				
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
	}
}
