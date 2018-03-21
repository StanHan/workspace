package demo.hadoop.mapReduce.wordCount;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Hadoop 1.0版 旧
 * <p>
 * API(org.apache.hadoop.mapred)
 * </p>
 */
public class WordCount1 {

	public static void main(String[] args) throws IOException {
		System.out.println("Usage: WordCount <inputPath> <outputPath>");

		JobConf jobConf = new JobConf(WordCount1.class);
		jobConf.setJobName("WordCount");

		jobConf.setOutputKeyClass(Text.class);
		jobConf.setOutputValueClass(IntWritable.class);

		jobConf.setMapperClass(WordCountMapper.class);
		jobConf.setReducerClass(WordCountReduce.class);

		jobConf.setInputFormat(TextInputFormat.class);
		jobConf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(jobConf, new Path(args[0]));
		FileOutputFormat.setOutputPath(jobConf, new Path(args[1]));

		JobClient.runJob(jobConf);
	}

}

class WordCountMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
	private Text word = new Text();
	private static final IntWritable one = new IntWritable(1);

	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {

		String line = value.toString();
		/*
		 * StringTokenizer 是出于兼容性的原因而被保留的遗留类（虽然在新代码中并不鼓励使用它） 。建议所有寻求此功能的人使用
		 * String 的 split 方法或 java.util.regex 包。 StringTokenizer stringTokenizer
		 * = new StringTokenizer(line); while (stringTokenizer.hasMoreTokens())
		 * { word.set(stringTokenizer.nextToken()); output.collect(word, one); }
		 */

		/* \s,匹配任何空白字符，包括空格、制表符、换页符等等。等价于[\f\n\r\t\v]。 */
		String[] words = line.split("\\s");
		for (String tmpString : words) {
			String[] tmpArray = tmpString.split("\t");
			for (String aa : tmpArray) {
				word.set(aa);
				output.collect(word, one);
			}
		}
	}

}

class WordCountReduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

	@Override
	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output,
			Reporter reporter) throws IOException {
		int sum = 0;
		while (values.hasNext()) {
			sum += values.next().get();
		}
		output.collect(key, new IntWritable(sum));
	}

}

class MyPartitioner extends Partitioner<Text, IntWritable> {

	@Override
	public int getPartition(Text key, IntWritable value, int numPartitions) {
		return (key.hashCode() & Integer.MAX_VALUE) % numPartitions;
	}

}