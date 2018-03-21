package demo.hadoop.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class SimpleApp {

	public static void main(String[] args) {
		String logFile = "";
		SparkConf sparkConf = new SparkConf().setAppName("Simple Application");
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		JavaRDD<String> javaRdd = javaSparkContext.textFile(logFile).cache();
		
		long numA = javaRdd.filter(new Function<String,Boolean>(){
			public Boolean call(String s){
				return s.contains("a");
			}
		}).count();
		
		long numB = javaRdd.filter(new Function<String,Boolean>(){
			public Boolean call(String s){
				return s.contains("b");
			}
		}).count();
		
		System.out.println("lines with a:"+numA +",lines with b:"+numB);
		javaSparkContext.close();
	}

}
