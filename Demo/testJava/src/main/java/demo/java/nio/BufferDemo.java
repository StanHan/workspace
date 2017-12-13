package demo.java.nio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Buffer有四个基本属性：
 * <li>capacity : 容量，buffer能够容纳的最大元素数目，在Buffer创建时设定并不能更改
 * 
 * <li>position : 下一个读或者写的位置。 当你写数据到Buffer中时，position表示当前的位置。初始的position值为0.当一个byte、long等数据写到Buffer后，
 * position会向前移动到下一个可插入数据的Buffer单元。position最大可为capacity – 1。 当读取数据时，也是从某个特定位置读。当将Buffer从写模式切换到读模式，position会被重置为0.
 * 当从Buffer的position处读取数据时，position向前移动到下一个可读的位置。
 * 
 * <li>limit : buffer中有效位置数目，不能对超过limit中的区域进行读写。 在写模式下，Buffer的limit表示你最多能往Buffer里写多少数据。 写模式下，limit等于Buffer的capacity。
 * 当切换Buffer到读模式时，limit表示你最多能读到多少数据。因此，当切换Buffer到读模式时，limit会被设置成写模式下的position值。
 * 换句话说，你能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position）
 * 
 * <li>mark : 用于记忆的标志位，配合reset()使用，初始值未设定，调用mark后将当前position设为值。
 * <p>
 * 
 * Java NIO中的Buffer用于和NIO通道进行交互。如你所知，数据是从通道读入缓冲区，从缓冲区写入到通道中的。
 * 
 * 使用Buffer读写数据一般遵循以下步骤：
 * <ol>
 * <li>写入数据到Buffer ,向Buffer中写数据，写数据到Buffer有两种方式：1、从Channel写到Buffer。2、通过Buffer的put()方法写到Buffer里。
 * <li>调用flip()方法.flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值。
 * <li>从Buffer中读取数据。从Buffer中读取数据有两种方式：1、从Buffer读取数据到Channel。2、使用get()方法从Buffer中读取数据。
 * <li>rewind()方法: 将position设回0，所以你可以重读Buffer中的所有数据。limit保持不变，仍然表示能从Buffer中读取多少个元素（byte、char等）。
 * <li>调用clear()方法或者compact()方法：当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。
 * 在读模式下，可以读取之前写入到buffer的所有数据。一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。有两种方式能清空缓冲区：调用clear()或compact()方法。
 * 如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值。换句话说，Buffer 被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
 * 如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有。
 * 如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先先写些数据，那么使用compact()方法。
 * compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。
 * <li>mark()与reset()方法:mark()方法，可以标记Buffer中的一个特定position。之后可以通过调用Buffer.reset()方法恢复到这个position。
 * <li>equals()与compareTo()方法：equals()，当满足下列条件时，表示两个Buffer相等： 1、有相同的类型（byte、char、int等）。 2、Buffer中剩余的byte、char等的个数相等。
 * 3、Buffer中所有剩余的byte、char等都相同。 如你所见，equals只是比较Buffer的一部分，不是每一个在它里面的元素都比较。实际上，它只比较Buffer中的剩余元素。
 * compareTo()方法，compareTo()方法比较两个Buffer的剩余元素(byte、char等)， 如果满足下列条件，则认为一个Buffer“小于”另一个Buffer：
 * 1、第一个不相等的元素小于另一个Buffer中对应的元素 。 2、所有元素都相等，但第一个Buffer比另一个先耗尽(第一个Buffer的元素个数比另一个少)。
 * </ol>
 * 
 * <p>
 * 直接与非直接缓冲区:如果为直接字节缓冲区，则Java虚拟机会尽最大努力直接在此缓冲区上执行本机 I/O 操作。直接字节缓冲区可以通过调用此类的allocateDireact()工厂方法来创建。
 * 此方法返回的缓冲区进行分配和取消分配所需成本通常高于非直接缓冲区。直接缓冲区的内容可以驻留在常规的垃圾回收堆之外，因此，它们对应用程序的内存需求量造成的影响可能并不明显。
 * 所以，建议将直接缓冲区主要分配给那些易受基础系统的本机I/O操作影响的大型、持久的缓冲区。一般情况下，最好仅在直接缓冲区能在程序性能方面带来明显好处时分配它们。
 * 
 * 直接字节缓冲区还可以通过mapping将文件区域直接映射到内存中来创建。J
 */

public class BufferDemo {

    public static void main(String[] args) {
        testByteBuffer();
    }

    static void testByteBuffer() {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile("D:/logs/call_log.sql", "r");
                FileChannel fileChannel = randomAccessFile.getChannel();) {
            // create buffer with capacity of 48 bytes
            ByteBuffer byteBuffer = ByteBuffer.allocate(48);
            int bytesRead = fileChannel.read(byteBuffer); // read into buffer.
            while (bytesRead != -1) {
                byteBuffer.flip(); // make buffer ready for read
                while (byteBuffer.hasRemaining()) {
                    System.out.print((char) byteBuffer.get()); // read 1 byte at a time
                }
                byteBuffer.clear(); // make buffer ready for writing
                bytesRead = fileChannel.read(byteBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 比较移动一个文件的耗时
     */
    static void compareIOandNIO() {
        long t2 = System.currentTimeMillis();
        copyFile_NIO("test.mp4", "D:\\", "G:\\logs/tmp2");
        long t3 = System.currentTimeMillis();
        System.out.println("copyFile_NIO takes " + (t3 - t2));
        /////////////////////
        long t0 = System.currentTimeMillis();
        copyFile_IO("test.mp4", "D:\\", "G:\\logs/tmp1");
        long t1 = System.currentTimeMillis();
        System.out.println("copyFile_IO takes " + (t1 - t0));
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
        File tmp = new File(destpath);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
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
        File tmp = new File(destpath);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
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
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8));
        return byteBuffer;
    }

    public static String getChars(ByteBuffer byteBuffer) {// 将字节转为字符(解码)
        byteBuffer.flip();
        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
        return charBuffer.toString();
    }
}
