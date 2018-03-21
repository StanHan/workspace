package demo.hadoop.mapReduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 多表连接
 *
 */
public class MutipleTalbesJoin {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if(otherArgs.length != 2){
			System.err.println("Usage: hadoop jar MutipleTalbesJoin input output");
			System.exit(2);
		}
		Job job = Job.getInstance(conf, "Single Table Join");
		job.setJarByClass(MutipleTalbesJoin.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	public static int time = 0;
	
	public static class Map extends Mapper<Object, Text, Text, Text>{
		@Override
		protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			if(line.contains("factoryName")||line.contains("addressId")){
				return;
			}
			String[] values = line.split("#");
			if(values[0].length() < 3){
				String addressId = values[0];
				String addressName = values[1];
				context.write(new Text(addressId), new Text("#1"+addressId+","+addressName));
			}else{
				String factoryName = values[0];
				String addressId = values[1];
				context.write(new Text(addressId), new Text("#2"+addressId+","+factoryName));
			}
		}
	}
	
	public static class Reduce extends Reducer<Text, Text, Text, Text>{
		@Override
		protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			if(time == 0 ){
				context.write(new Text("factoryName"), new Text("#addressName"));
				time ++;
			}
			List<String> factoryName = new ArrayList<String>();
			List<String> addressName = new ArrayList<String>();
			for (Text text : values) {
				String record = text.toString();
				if(record.startsWith("#1")){
					String addressId_addressName = record.substring(2);
					String[] tmp = addressId_addressName.split(",");
					addressName.add(tmp[1]);
				}else if(record.startsWith("#2")){
					String addressId_factoryName = record.substring(2);
					String[] tmp = addressId_factoryName.split(",");
					factoryName.add(tmp[1]);
				}
			}
			for (String factory : factoryName) {
				for (String address : addressName) {
					context.write(new Text(factory), new Text("#"+address));
				}
			}
		}
	}
}
