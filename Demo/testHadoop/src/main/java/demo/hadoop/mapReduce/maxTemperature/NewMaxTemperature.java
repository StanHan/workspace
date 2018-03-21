package demo.hadoop.mapReduce.maxTemperature;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class NewMaxTemperature {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		if(args.length !=2){
			System.err.println("Usage: NewMaxTemperature <input path> <output path>");
			System.exit(-1);
		}
		
		Job job = Job.getInstance();
		job.setJarByClass(NewMaxTemperature.class);
		
		job.setMapperClass(NewMaxTemperatureMapper.class);
		job.setReducerClass(NewMaxTemperatureReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		System.exit(job.waitForCompletion(true)?1:1);
	}

	static class NewMaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
		
		private static final int MISSING = 9999;
		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			String year = line.substring(15, 19);
			int airTemperature;
			if (line.charAt(87) == '+') {
				airTemperature = Integer.parseInt(line.substring(88, 92));
			} else {
				airTemperature = Integer.parseInt(line.substring(87, 92));
			}
			String quality = line.substring(92, 93);
			if(airTemperature != MISSING && quality.matches("[01459]")){
				context.write(new Text(year), new IntWritable(airTemperature));
			}
		}
	}
	
	static class NewMaxTemperatureReducer extends Reducer<Text, IntWritable, Text, IntWritable>{

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
				Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
			int maxValue = Integer.MIN_VALUE;
			for(IntWritable value:values){
				maxValue = Math.max(maxValue, value.get());
			}
			context.write(key,new IntWritable(maxValue));
		}
		
	}
}
