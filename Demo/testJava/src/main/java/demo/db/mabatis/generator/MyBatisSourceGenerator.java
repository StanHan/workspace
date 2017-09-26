package demo.db.mabatis.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MyBatisSourceGenerator {

    public static void main(String[] args) {
        List<String> warnings = new ArrayList<String>();
        String configPath = MyBatisSourceGenerator.class.getResource("generatorConfig.xml").getPath();
        // 导入配置表mybatis-generator.xml
        File configFile = new File(configPath);
        try {
            // 解析
            ConfigurationParser configurationParse = new ConfigurationParser(warnings);
            Configuration configuration = configurationParse.parseConfiguration(configFile);
            // 是否覆盖
            DefaultShellCallback dsc = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, dsc, warnings);
            myBatisGenerator.generate(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String string : warnings) {
            System.out.println(string);
        }
    }

}
