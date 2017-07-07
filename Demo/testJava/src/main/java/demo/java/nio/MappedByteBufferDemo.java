package demo.java.nio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedAction;

import sun.misc.Cleaner;

public class MappedByteBufferDemo {

	public static void main(String[] args) {
		// test2();
		// test();
		// moveFile("生活大爆炸第七季第01集[中英双字][电影天堂www.dy2018.com].mkv",
		// "D:\\Videos\\The Big Bang Theory Season", "D:\\cache");
		long start = System.currentTimeMillis();
		copyFile_IO("生活大爆炸第七季第01集[中英双字][电影天堂www.dy2018.com].mkv", "D:\\Videos\\The Big Bang Theory Season",
				"D:\\cache\\aa");
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		copyFile_NIO("生活大爆炸第七季第01集[中英双字][电影天堂www.dy2018.com].mkv", "D:\\Videos\\The Big Bang Theory Season",
				"D:\\cache\\bb");

		long end2 = System.currentTimeMillis();
		System.out.println(end2 - end);
	}

	public static void test() {
		File file_in = new File("D:\\Videos\\The Big Bang Theory Season\\生活大爆炸第七季第01集[中英双字][电影天堂www.dy2018.com].mkv");
		File file_out = new File("D:\\cache\\test2.txt");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 14 * 1024);

		try (FileInputStream fileInputStream = new FileInputStream(file_in);
				FileOutputStream fileOutputSteam = new FileOutputStream(file_out);
				FileChannel fileChannel = fileInputStream.getChannel();) {
			long start = System.currentTimeMillis();
			final MappedByteBuffer mappedByteBuffer = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
			System.out.println("file size of " + file_in.getName() + " is" + fileChannel.size() / 1024 + "k");
			long end = System.currentTimeMillis();
			System.out.println("Read time:" + (end - start) + "ms.");

			mappedByteBuffer.flip();
			long end2 = System.currentTimeMillis();
			System.out.println("Write time:" + (end2 - end) + "ms.");

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public static void test2() {
		File file_in = new File("D:\\Videos\\The Big Bang Theory Season\\生活大爆炸第七季第01集[中英双字][电影天堂www.dy2018.com].mkv");
		File file_out = new File("D:\\cache\\test2.txt");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 14 * 1024);
		byte[] bytes = new byte[1024 * 14 * 1024];

		try (FileInputStream fileInputStream = new FileInputStream(file_in);
				FileOutputStream fileOutputSteam = new FileOutputStream(file_out);
				FileChannel fileChannel = fileInputStream.getChannel();) {
			long start = System.currentTimeMillis();

			fileChannel.read(byteBuffer);
			System.out.println("file size of " + file_in.getName() + " is" + fileChannel.size() / 1024 + "k");
			long end = System.currentTimeMillis();
			System.out.println("Read time:" + (end - start) + "ms.");
			fileOutputSteam.write(bytes);
			fileOutputSteam.flush();
			long end2 = System.currentTimeMillis();
			System.out.println("Write time:" + (end2 - end) + "ms.");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// 文件复制
	public static void copyFile_IO(String filename, String srcpath, String destpath) {
		File source = new File(srcpath + "/" + filename);
		File dest = new File(destpath + "/" + filename);
		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(source));
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dest))) {
			byte[] buffer = new byte[1024 * 1024];
			while (bufferedInputStream.read(buffer) != -1) {
				bufferedOutputStream.write(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 文件复制
	public static void copyFile_NIO(String filename, String srcpath, String destpath) {
		File source = new File(srcpath + "/" + filename);
		File dest = new File(destpath + "/" + filename);
		try (FileChannel in = new FileInputStream(source).getChannel();
				FileChannel out = new FileOutputStream(dest).getChannel();) {
			long size = in.size();
			MappedByteBuffer mappedByteBuffer = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
			out.write(mappedByteBuffer);
			clean(mappedByteBuffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param filename
	 * @param srcpath
	 * @param destpath
	 */
	public static void moveFile(String filename, String srcpath, String destpath) {
		File source = new File(srcpath + "/" + filename);
		File dest = new File(destpath + "/" + filename);
		try (FileChannel in = new FileInputStream(source).getChannel();
				FileChannel out = new FileOutputStream(dest).getChannel();) {
			long size = in.size();
			MappedByteBuffer mappedByteBuffer = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
			out.write(mappedByteBuffer);
			clean(mappedByteBuffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean isDeleted = source.delete();// 文件复制完成后，删除源文件
		if (isDeleted) {
			System.out.println("move file success");
		} else {
			// 删除失败，主要原因是变量mappedByteBuffer仍然有源文件的句柄，文件处于不可删除状态。可以调用clean()方法解决
			System.err.println("move file failed");
		}
	}

	public static void clean(final Object buffer) throws Exception {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					Method method_cleaner = buffer.getClass().getMethod("cleaner", new Class[0]);
					method_cleaner.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner) method_cleaner.invoke(buffer, new Object[0]);
					cleaner.clean();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	public static ByteBuffer getBytes(String str) {// 将字符转为字节(编码)
		Charset charset = Charset.forName("GBK");
		ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes(charset));
		return byteBuffer;
	}

	public static String getChars(ByteBuffer byteBuffer) {// 将字节转为字符(解码)
		Charset charset = Charset.forName("GBK");
		byteBuffer.flip();
		CharBuffer charBuffer = charset.decode(byteBuffer);

		return charBuffer.toString();
	}
}
