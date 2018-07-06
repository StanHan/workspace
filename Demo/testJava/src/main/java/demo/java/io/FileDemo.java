package demo.java.io;

import java.io.File;

import demo.java.lang.ClassDemo;

/**
 * 
 * <h2>getPath()、getAbsolutePath()、getCanonicalPath()的区别</h2>
 * <li>getPath()获取的是新建文件时的路径
 * <li>getAbsolutePath()获取的是文件的绝对路径，返回当前目录的路径+构造file时候的路径,
 * <li>getCanonicalPath()获取的也是文件的绝对路径，而且把..或者.这样的符号解析出来，
 *
 */
public class FileDemo {

    public static void main(String[] args) {
        demoPath();
    }

    /**
     * <h2>获取资源的路径</h2>
     * 
     * <li>1:输入path 以'/' 或者 ’\\‘ 开头的 ，是以项目所在的硬盘位基础路径
     * <li>2:输入path 以 字母开头 的,是以项目的路径为基础路径 即： System.getProperty("user.dir")
     * <li>3.输入绝对路径的，就是以该绝对路径做为路径咯
     * <li>获取上级目录:getParent()或者getParentFile();
     */
    static void demoPath() {
        File file = new File("/");
        System.out.println("根目录：" + file.getAbsolutePath());

        file = new File("");
        System.out.println("获取当前类的所在工程路径：" + file.getAbsolutePath());

//        ClassDemo.demoGetResource();

        System.out.println("获取当前工程路径 :" + System.getProperty("user.dir"));

        System.out.println(System.getProperty("java.class.path"));
    }

}
