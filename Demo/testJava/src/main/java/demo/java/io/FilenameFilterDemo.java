package demo.java.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 文件过滤器
 * 
 * @author Stan
 *
 */
public class FilenameFilterDemo implements FilenameFilter {
    private String regex;

    public FilenameFilterDemo(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.matches(regex);
    }
}
