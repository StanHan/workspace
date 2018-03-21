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
 * 单表关联
 *
 */
public class SingleTableJoin {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if(otherArgs.length != 2){
			System.err.println("Usage: hadoop jar StJoin input output");
			System.exit(2);
		}
		Job job = Job.getInstance(conf, "Single Table Join");
		job.setJarByClass(SingleTableJoin.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

	public static int time = 0 ;
	
	public static class Map extends Mapper<Object, Text, Text, Text>{
		@Override
		protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			if(!"child#parent".equals(line)){
				String[] values = line.split("#");
				String childName = values[0];
				String parentName = values[1];
				context.write(new Text(parentName), new Text("#1"+childName+","+parentName));
				context.write(new Text(childName), new Text("#2"+childName+","+parentName));
			}
		}
	}
	
	public static class Reduce extends Reducer<Text, Text, Text, Text>{
		@Override
		protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			if(time == 0 ){
				context.write(new Text("grandChild"), new Text("#grandParent"));
				time ++;
			}
			List<String> childList = new ArrayList<String>();
			List<String> parentList = new ArrayList<String>();
			for (Text text : values) {
				String record = text.toString();
				if(record.startsWith("#1")){
					String child_parent = record.substring(2);
					String[] tmp = child_parent.split(",");
					childList.add(tmp[0]);
				}else if(record.startsWith("#2")){
					String child_parent = record.substring(2);
					String[] tmp = child_parent.split(",");
					parentList.add(tmp[1]);
				}
			}
			for (String grandChild : childList) {
				for (String grandParent : parentList) {
					context.write(new Text(grandChild), new Text(grandParent));
				}
			}
		}
	}
}
