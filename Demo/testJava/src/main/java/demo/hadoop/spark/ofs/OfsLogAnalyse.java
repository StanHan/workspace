package demo.hadoop.spark.ofs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharacterCodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class OfsLogAnalyse {
	
	private static Log log = LogFactory.getLog(OfsLogAnalyse.class);

	public static void main(String[] args) {
		
		Configuration configuration = new Configuration();
		
		InputStream inputStream = OfsLogAnalyse.class.getClassLoader().getResourceAsStream("test/hadoop/ofs_cfg.xml");
		
		configuration.addResource(inputStream);
		
		String inputPath = configuration.get("input.path");
		
		log.info(inputPath);
		
		SparkConf sparkConf = new SparkConf().setAppName("OfsLogAnalyse");		
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);		
		
		String outputPath = configuration.get("output.path");
		
		System.out.println("------------------------output-------------------------");
		System.out.println(outputPath);
		
		FileSystem fileSystem = null;
		try {
			fileSystem = FileSystem.get(configuration);
		} catch (IOException e) {
			log.error(e);
		}
		
		JavaPairRDD<String, Text> javaPairRdd_file = 
				javaSparkContext.newAPIHadoopFile(inputPath, OfsLogTextInputFormat.class, String.class, Text.class, configuration);
		
		try {
			deletePath(fileSystem, outputPath);
		} catch (IOException e) {
			log.error(e);
		}
		javaPairRdd_file.saveAsTextFile(outputPath);
	}

	private static void deletePath(FileSystem fileSystem, String output) throws IOException {
		Path path = new Path(output);
		if (fileSystem.exists(path)) {
			fileSystem.delete(path, true);
		}
	}
	
}
