package cn.distributedcache;



import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class DcWordcount {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.addCacheFile(new Path(args[2]).toUri());

		job.setJarByClass(DcWordcount.class);
		job.setJobName("wasky_distributedcache");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		job.setMapperClass(DcMapper.class);
		job.setReducerClass(DcReducer.class);
		

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		
		job.waitForCompletion(true);

	}


}
