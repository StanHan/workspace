package demo.java.classLoader;

import java.net.URL;

public class ClassLoaderTest {

	public static void main(String[] args) {
		URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
		
		
		for(URL url:urls){
			System.out.println(url);
		}
		
		System.out.println("JRE的扩展目录:" +System.getProperty("java.ext.dirs"));
		ClassLoader extensionClassloader=ClassLoader.getSystemClassLoader().getParent();
		System.out.println("the parent of extension classloader : "+extensionClassloader.getParent());
		System.out.println("java.class.path = "+System.getProperty("java.class.path"));
		
		System.out.println("System.class.getClassLoader() = "+System.class.getClassLoader());
		
		System.out.println("the Launcher's classloader is "+sun.misc.Launcher.getLauncher().getClass().getClassLoader());
		
		
	}

}
