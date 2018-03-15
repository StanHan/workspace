package demo.java.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class IoDemo {

    public static void main(String[] args) throws IOException {
        String xml = "d:\\cache\\ReportMessage.xml";
        String content = readFile(xml);
        // System.out.println(content);
        // content = readFile(xml, "GBK");
        // System.out.println(content);
        // String content = readFile2(xml, "GBK");
        System.out.println(content);
    }

    public static void testBufferedInputStream3() throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("d:\\电影\\sn.ts"));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("d:\\电影sn1.ts"));
        int i;
        do {
            i = bis.read();
            if (i != -1) {
                bos.write(i);
            }
        } while (i != -1);
        bis.close();
        bos.close();
    }

    public static void testBufferedInputStream2() throws IOException {
        File file = new File("c:\\mm.txt");
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        while (bis.available() > 0) {
            System.out.print((char) bis.read());
        }
        bis.close();
    }

    public static void testBufferedInputStream1() throws IOException {
        File file = new File("c:\\mm.txt");
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        byte[] contents = new byte[1024];
        int byteRead = 0;
        String strFileContents;

        try {
            while ((byteRead = bis.read(contents)) != -1) {
                strFileContents = new String(contents, 0, byteRead);
                System.out.println(strFileContents);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bis.close();
    }

    public static void testFile() {
        String path = "‪D:/cache/PCCCUSTS.150101";
        File file = new File(path);
        System.out.println(file.getName());
        System.out.println(file.getParentFile());
        System.out.println(file.getPath());
        String fileName = file.getName();
        String[] array = fileName.split("\\.");
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
        String suffix = fileName.substring(fileName.indexOf("."));
        System.out.println(suffix);
    }

    /**
     * 读取文件第一行。使用try-with-resources, 可以自动关闭实现了AutoCloseable或者Closeable接口的资源。比如下面的函数，在try语句结束后，
     * 不论其包括的代码是正常执行完毕还是发生异常，都会自动调用BufferdReader的Close方法。
     * 
     * @param path
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    static String readFirstLineFromFile(String path) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }
    }

    /**
     * 在出现try-with-resources之前可以使用finally子句来确保资源被关闭, 比如下面的方法。
     * 但是两者有一个不同在于，readFirstLineFromFileWithFinallyBlock方法中，如果finally子句中抛出异常， 将会抑制try代码块中抛出的异常。
     * 相反，readFirstLineFromFile方法中，如果try-with- resources语句中打开资源的Close方法和try代码块中都抛出了异常，Close方法抛出的异常被抑制，try代码块中的异常会被抛出。
     * 注意：前面提到，如果try-with-resources语句中打开资源的Close方法和try代码块中都抛出了异常，Close 方法抛出的异常被抑制，try代码块中的异常会被抛出。
     * Java7之后，可以使用Throwable.getSuppressed方法获得被抑制的异常。
     * 
     * @param path
     * @return
     * @throws IOException
     */
    static String readFirstLineFromFileWithFinallyBlock(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            if (br != null)
                br.close();
        }
    }

    /**
     * 可以在一个try-with-resources语句中声明多个资源，这些资源将会以声明的顺序相反之顺序关闭,
     * 注意：一个try-with-resources语句也能够有catch和finally子句。catch和finally子句将会在try-with- resources子句中打开的资源被关闭之后得到调用。
     * 
     * @param zipFileName
     * @param outputFileName
     * @throws java.io.IOException
     */
    public static void writeToFileZipFileContents(String zipFileName, String outputFileName)
            throws java.io.IOException {

        Charset charset = Charset.forName("US-ASCII");
        Path outputFilePath = Paths.get(outputFileName);

        // Open zip file and create output file with try-with-resources
        // statement
        try (ZipFile zipFile = new ZipFile(zipFileName);
                BufferedWriter writer = Files.newBufferedWriter(outputFilePath, charset)) {
            // Enumerate each entry
            for (Enumeration entries = zipFile.entries(); entries.hasMoreElements();) {
                // Get the entry name and write it to the output file
                String newLine = System.getProperty("line.separator");
                String zipEntryName = ((ZipEntry) entries.nextElement()).getName() + newLine;
                writer.write(zipEntryName, 0, zipEntryName.length());
            }
        }
    }

    /**
     * 遍历文件列表
     * 
     * @throws IOException
     */
    public static void testScan() throws IOException {
        File file = new File("C:\\Users\\Stan\\Desktop\\aa");
        scanFileList(file, true, "CC.*");
        System.out.println("next");
        scanFileList(file, true, "^CC\\..{6}");
        System.out.println("next");
        scanFileList(file, true, "^CC\\.......");
    }

    /**
     * 写文件
     * 
     * @param content
     * @param filePath
     * @throws IOException
     */
    public static void writeFile(String content, String filePath) throws IOException {
        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
    }

    /**
     * 读取文本文件(带换行符)
     * 
     * @param filePath
     * @param charset
     * @return
     */
    public static String readFile(String filePath, String charset) {
        String content = null;
        try {
            content = FileUtils.readFileToString(new File(filePath), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 读取文本文件（不带换行符）
     * 
     * @param filePath
     * @param charset
     * @return
     */
    public static String readFile2(String filePath, String charset) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 读取文本文件（不带换行符）
     * 
     * @param filePath
     * @param charset
     * @return
     */
    public static String readFile3(String filePath, String charset) {
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            inputStreamReader = new InputStreamReader(fileInputStream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            if (bufferedReader != null) {
                System.out.println("bufferedReader.close();");
                bufferedReader.close();
            }
            if (inputStreamReader != null) {
                System.out.println("inputStreamReader.close();");
                inputStreamReader.close();
            }
            if (fileInputStream != null) {
                System.out.println("fileInputStream.close();");
                fileInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    System.out.println("bufferedReader.close();");
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    System.out.println("inputStreamReader.close();");
                    inputStreamReader.close();
                }
                if (fileInputStream != null) {
                    System.out.println("fileInputStream.close();");
                    fileInputStream.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
    
    /**
     * 读取JSON
     *
     * @param file
     * @return
     * @throws IOException 
     */
    public static String readStringFromFile(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.lines().map(e -> new StringBuilder(e)).reduce(new StringBuilder(), StringBuilder::append)
                    .toString();
        }
    }

    /**
     * 读取文本文件,使用系统默认字符集（不带换行符）
     * 
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 写文件
     * 
     * @param fileName
     * @param content
     * @param charsetName
     */
    public static void writeFile(String fileName, String content, String charsetName) {
        File file = new File(fileName);
        Charset charset = Charset.forName(charsetName);

        try (FileOutputStream fos = new FileOutputStream(file);) {
            fos.write(content.getBytes(charset));
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试文件过滤器
     * 
     */
    public static void testFileFilter() {
        FilenameFilter filenameFilter2 = new FilenameFilterDemo("^PCCCUSTS\\..{6}");
        File filePath2 = new File("D:\\SVN\\hadoop\\0100 项目明细\\010003文件存储\\020001需求分析\\信用卡对账单补打");

        /*
         * String[] fileNames = filePath.list(filenameFilter); for(String fileName:fileNames){
         * System.out.println(fileName); }
         */

        String[] fileNames2 = filePath2.list(filenameFilter2);

        for (String fileName : fileNames2) {
            System.out.println(fileName);
        }

        File[] files = filePath2.listFiles(filenameFilter2);
        for (File file : files) {
            System.out.println(file.getName());
            System.out.println(file.getAbsolutePath());
        }
    }

    /**
     * 保存对象为文件
     * 
     * @param object
     * @param fileName
     * @throws IOException
     */
    public static void saveObjectToFile(Object object, String fileName) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));) {
            objectOutputStream.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 读取对象序列化文件
     * 
     * @param filePath
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Object readObjectFromFile(String filePath) throws ClassNotFoundException, IOException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));) {
            Object object = in.readObject();
            return object;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 扫描文件列表
     * 
     * @param file
     * @param isRecursive
     * @param regex
     * @throws IOException
     */
    private static void scanFileList(File file, Boolean isRecursive, String regex) throws IOException {
        if (file.isDirectory()) {
            if (isRecursive.booleanValue()) {
                File afile[];
                int j = (afile = file.listFiles()).length;
                for (int i = 0; i < j; i++) {
                    File f = afile[i];
                    scanFileList(f, isRecursive, regex);
                }
            }
        } else if (StringUtils.isBlank(regex) || file.getName().matches(regex)) {
            System.out.println(file.getAbsolutePath());
        }
    }

}
