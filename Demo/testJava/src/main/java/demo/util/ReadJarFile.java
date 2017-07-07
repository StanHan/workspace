package demo.util;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadJarFile {

	List<String> jarList = new ArrayList<String>();
	List<String> filesURL = new ArrayList<String>();

	/**
	 * 读取指定文件夹的文件
	 * 
	 * @param jarFileName
	 *            路径
	 * @param strings
	 *            后缀
	 */
	public ReadJarFile(String jarFileName, String[] strings) {
		int index = jarFileName.lastIndexOf('/');
		if (index == jarFileName.length()-1)
			jarFileName = jarFileName.substring(0, index);
		
		File f = new File(jarFileName);
		File[] fl = f.listFiles();
		for (File file : fl) {
			for (String str : strings) {
				if (file.getName().endsWith(str)) {
					jarList.add(file.getPath());
					filesURL.add(file.toURI().toString());
				}
			}
		}
	}

	/**
	 * 获取文件名
	 * 
	 * @return
	 */
	public List<String> getFiles() {
		return jarList;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public List<String> getFilesURL() {
		return filesURL;
	}

}
