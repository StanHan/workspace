package demo.util;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLClassLoader;



public class ClassLoad {
	//static URLClassLoader loader = null;
	static URLClassLoaderUtil urlClass = null;
	/**
	 * 在默认的目录加载jar
	 * 
	 * @return
	 * @throws MalformedURLException 
	 * @throws TaskException 
	 */
	public static URLClassLoader getClassLoad(String jarFilePath) throws MalformedURLException{
		if (urlClass == null) {
			urlClass = new URLClassLoaderUtil(jarFilePath, false);
		}
		return urlClass.getClassLoader();
	}

	/**
	 * 在给定的路径加载jar文件
	 * 
	 * @param url
	 *            指定路径
	 * @param isFile
	 *            true 文件 false 目录
	 * @return
	 * @throws MalformedURLException 
	 * @throws TaskException 
	 */
	public static URLClassLoader getClassLoad(String url, boolean isFile) throws MalformedURLException {
		urlClass = new URLClassLoaderUtil(url, isFile);
		URLClassLoader loader = urlClass.getClassLoader();
		return loader;
	}

	public static URLClassLoader getInstance() throws Exception  {
		if (urlClass != null) {
			return urlClass.getClassLoader();
		} else
			throw new Exception("JAR ClassLoder is not being initialized.");
	}
	
	public static void loadAllClass() throws ClassNotFoundException, IOException {
		urlClass.callBack();
	}
}
