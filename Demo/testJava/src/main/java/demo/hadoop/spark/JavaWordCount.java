package demo.hadoop.spark;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class JavaWordCount {

	private static final Pattern SPACE = Pattern.compile(" ");
	
	public static void main(String[] args) {

		if(args == null || args.length <1){
			System.err.println("Usage: JavaWordCount <file>");
			System.exit(-1);
		}
		String fileName = args[0];
		
		System.out.println(fileName);
		
		SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount");
		
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		JavaRDD<String> lines = javaSparkContext.textFile(fileName).cache();
//		JavaRDD<String> lines = javaSparkContext.textFile(fileName, 1);
		
		JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

			@Override
			public Iterable<String> call(String t) throws Exception {
				String[] array = SPACE.split(t);
				// TODO Auto-generated method stub
				return Arrays.asList(array);
			}
			
		});
		
		JavaPairRDD<String,Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {

			@Override
			public Tuple2<String, Integer> call(String t) throws Exception {
				// TODO Auto-generated method stub
				return new Tuple2<String, Integer>(t, 1) ;
			}
			
		});
		
		JavaPairRDD<String,Integer> counts = ones.reduceByKey(new Function2<Integer,Integer,Integer>(){

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				// TODO Auto-generated method stub
				return v1 +v2;
			}
			
		});
		
		List<Tuple2<String, Integer>> output = counts.collect();
		for(Tuple2<String, Integer> tuple2:output){
			System.out.println(tuple2._1()+": "+tuple2._2());
		}
		
		javaSparkContext.stop();
		javaSparkContext.close();
	}

}
