package demo.java.lang.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.WeakHashMap;

public class ReferenceDemo {
	public static void main(String[] args) {

	}
}

/**
 * 软引用（soft reference）在强度上弱于强引用，通过类SoftReference来表示。它的作用是告诉垃圾回收器，程序中的哪些对象是不那么重要，
 * 当内存不足的时候是可以被暂时回收的。当JVM中的内存不足的时候，垃圾回收器会释放那些只被软引用所指向的对象。如果全部释放完这些对象之后，内存还不足，
 * 才会抛出OutOfMemory错误。软引用非常适合于创建缓存。当系统内存不足的时候，缓存中的内容是可以被释放的。比如考虑一个图像编辑器的程序。
 * 该程序会把图像文件的全部内容都读取到内存中，以方便进行处理。而用户也可以同时打开多个文件。当同时打开的文件过多的时候，就可能造成内存不足。
 * 如果使用软引用来指向图像文件内容的话，垃圾回收器就可以在必要的时候回收掉这些内存。
 *
 */
class ImageData {
	private String path;
	private SoftReference<byte[]> dataRef;

	public ImageData(String path) {
		this.path = path;
		dataRef = new SoftReference<byte[]>(new byte[0]);
	}

	private byte[] readImage() {
		return new byte[1024 * 1024]; // 省略了读取文件的操作
	}

	/**
	 * 由于软引用所指向的对象可能被回收掉，在通过get方法来获取软引用所实际指向的对象的时候，总是要检查该对象是否还存活。
	 * 
	 * @return
	 */
	public byte[] getData() {
		byte[] dataArray = dataRef.get();
		if (dataArray == null || dataArray.length == 0) {
			dataArray = readImage();
			dataRef = new SoftReference<byte[]>(dataArray);
		}
		return dataArray;
	}
}

/**
 * 弱引用（weak
 * reference）在强度上弱于软引用，通过类WeakReference来表示。它的作用是引用一个对象，但是并不阻止该对象被回收。如果使用一个强引用的话，
 * 只要该引用存在，那么被引用的对象是不能被回收的。弱引用则没有这个问题。在垃圾回收器运行的时候，如果一个对象的所有引用都是弱引用的话，该对象会被回收。
 * 弱引用的作用在于解决强引用所带来的对象之间在存活时间上的耦合关系。
 *
 */
class WeakReferenceDemo {
	WeakHashMap weakHashMap = new WeakHashMap();
}

/**
 * 
 *
 */
class PhantomBuffer {
	private byte[] data = new byte[0];
	private ReferenceQueue<byte[]> queue = new ReferenceQueue<byte[]>();
	private PhantomReference<byte[]> ref = new PhantomReference<byte[]>(data, queue);

	public byte[] get(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Wrong buffer size");
		}
		if (data.length < size) {
			data = null;
			System.gc(); // 强制运行垃圾回收器
			try {
				queue.remove(); // 该方法会阻塞直到队列非空
				ref.clear(); // 幽灵引用不会自动清空，要手动运行
				ref = null;
				data = new byte[size];
				ref = new PhantomReference<byte[]>(data, queue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
}
