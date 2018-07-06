package demo.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.junit.Test;

public class ResourceBundleDemo {

    public static void main(String[] args) {

    }

    /**
     * 通过 java.util.ResourceBundle 类来读取，这种方式比使用 Properties 要方便一些
     * 
     * @throws IOException
     */
    @Test
    public void demo(InputStream inStream) throws IOException {
        // config为属性文件名，放在包com.test.config下，如果是放在src下，直接用config即可
        ResourceBundle resource = ResourceBundle.getBundle("com/test/config/config");
        String key = resource.getString("keyWord");
        // 从 InputStream 中读取，获取 InputStream 的方法和上面一样，不再赘述
        ResourceBundle resource2 = new PropertyResourceBundle(inStream);
    }
}
