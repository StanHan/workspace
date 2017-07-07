package demo.java.nio;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * Compute 16-bit checksums for a list of files, in the style of the 
 * BSD "sum" command.  Uses NIO mapped byte buffers for speed.
 *
 * 
 */
public class Sum {

	public static void main(String[] args) {
//		test(args);
		String path = "D:/JavaLibrary/JDK1.7API/docs/technotes/tools/index.html";
		File file = new File(path);
		try {
			sum(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: java Sum file...");
			return;
		}
		for (int i = 0; i < args.length; i++) {
			File f = new File(args[i]);
			try {
				sum(f);
			} catch (IOException x) {
				System.err.println(f + ": " + x);
			}
		}
	}
	
	// Compute a 16-bit checksum for all the remaining bytes
	// in the given byte buffer
	//
	private static int sum(ByteBuffer byteBuffer) {
		int sum = 0;
		while (byteBuffer.hasRemaining()) {
			if ((sum & 1) != 0)
				sum = (sum >> 1) + 0x8000;
			else
				sum >>= 1;
			sum += byteBuffer.get() & 0xff;
			sum &= 0xffff;
		}
		return sum;
	}

	// Compute and print a checksum for the given file
	//
	private static void sum(File file) throws IOException {

		// Open the file and then get a channel from the stream
		FileInputStream fileInputStream = new FileInputStream(file);
		FileChannel fileChannel = fileInputStream.getChannel();

		// Get the file's size and then map it into memory
		int size = (int) fileChannel.size();
		MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);

		// Compute and print the checksum
		int sum = sum(mappedByteBuffer);
		int kb = (size + 1023) / 1024;
		String s = Integer.toString(sum);
		System.out.println(s + "\t" + kb + "\t" + file);

		// Close the channel and the stream
		fileChannel.close();
		fileInputStream.close();
	}

	

}
