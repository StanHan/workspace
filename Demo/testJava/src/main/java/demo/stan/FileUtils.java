package demo.stan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileUtils {

	public static void main(String[] args) throws IOException {
		String path = "D:/Pictures/2017/";
		// test(path);
		long count = Files.list(Paths.get(path)).peek(e -> {
			File file = e.getFileName().toFile();
			System.out.println(file.getName());
		}).count();
		System.out.println(count);
	}

	static final String[] patterns = { "wx_camera_*", "mmexport" };

	static void test(String path) {
		File file = new File(path);
		if (file.isDirectory()) {// 文件
			String[] files = file.list();
			if (files == null || files.length == 0) {
				System.out.println("空文件夹：" + path);
			}
			for (String string : files) {
				System.out.println(string);
			}
		}
	}

	static void getDateFromFileName(String fileName) {

	}

	static boolean matchPatterns(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return false;
		}
		for (String string : patterns) {
			boolean result = fileName.matches(string);
			if (result) {
				return true;
			}
		}
		return false;
	}

}
