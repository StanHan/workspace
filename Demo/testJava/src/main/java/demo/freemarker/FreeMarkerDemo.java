package demo.freemarker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * <h5>Freemarker的介绍</h5>
 * <p>
 * Freemarker 是一款模板引擎，是一种基于模版生成静态文件的通用 工具，它是为java程序员提供的一个开发包，或者说是一个类库，它不是面向最终用户的，而是为程序员提供了一款可以嵌入他们开发产品的应用程序。 Freemarker
 * 是使用纯java编写的，为了提高页面的访问速度，需要把页面静态化， 那么Freemarker就是被用来生成html页面。
 * <h5>Freemarker生成静态页面的原理</h5>
 * <p>
 * Freemarker 生成静态页面，首先需要使用自己定义的模板页面，这个模板页面可以是最最普通的html，也可以是嵌套freemarker中的 取值表达式，
 * 标签或者自定义标签等等，然后后台读取这个模板页面，解析其中的标签完成相对应的操作， 然后采用键值对的方式传递参数替换模板中的的取值表达式， 做完之后 根据配置的路径生成一个新的html页面， 以达到静态化访问的目的。
 * <h5>Freemarker提供的标签</h5>
 * <p>
 * Freemarker标签都是<#标签名称>这样子命名的，${value} 表示输出变量名的内容 。
 * <li>list：该标签主要是进行迭代服务器端传递过来的List集合，如 ， <#list nameList as names>${names}</#list>
 * <li>if： 该标签主要是做if判断用的，比如：<#if (names=="陈靖仇")> 他的武器是: 十五~~ </#if>
 * <li>include：该标签用于导入文件用的。<#include "include.html"/>
 */
public class FreeMarkerDemo {

    public static void main(String[] args) {
        try {
            // 创建一个合适的Configration对象
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File("D:\\logs\\tmp"));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8"); // 这个一定要设置，不然在生成的页面中 会乱码
            // 获取或创建一个模版。
            Template template = configuration.getTemplate("static.html");
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("description", "我正在学习使用Freemarker生成静态文件！");

            List<String> nameList = new ArrayList<String>();
            nameList.add("陈靖仇");
            nameList.add("玉儿");
            nameList.add("宇文拓");
            paramMap.put("nameList", nameList);

            Map<String, Object> weaponMap = new HashMap<String, Object>();
            weaponMap.put("first", "轩辕剑");
            weaponMap.put("second", "崆峒印");
            weaponMap.put("third", "女娲石");
            weaponMap.put("fourth", "神农鼎");
            weaponMap.put("fifth", "伏羲琴");
            weaponMap.put("sixth", "昆仑镜");
            weaponMap.put("seventh", null);
            paramMap.put("weaponMap", weaponMap);

            Writer writer = new OutputStreamWriter(new FileOutputStream("success.html"), "UTF-8");
            template.process(paramMap, writer);

            System.out.println("恭喜，生成成功~~");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}
