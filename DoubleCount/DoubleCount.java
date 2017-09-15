package cn.hadoop.dc;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DoubleCount {
public static class doubMapper extends   Mapper<LongWritable,Text,Text,LongWritable>
{
    
    public void map(LongWritable key,Text values,Context context) throws IOException, InterruptedException
    {	
        String []words=values.toString().trim().split(" ");
        for(int i=0;i<(words.length) - 1 ;i++)
         {	if(words.length==1){
        	continue;
        }
        	
            context.write(new Text(words[i]+" "+words[i+1]), new LongWritable(1));
        }
    }
}
public static class douReducer extends Reducer<Text,LongWritable,Text,LongWritable>
{
    
    public void reduce(Text key,Iterable<LongWritable> values,Context context) throws IOException, InterruptedException
    {
        long counter = 0;
        for(LongWritable l: values)
        {
        	counter += l.get();
        }
        
        context.write(key, new LongWritable(counter));
    }
}
public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
    
    Configuration conf = new Configuration();
	Job job = Job.getInstance(conf);

    job.setJarByClass(DoubleCount.class);
    
    job.setMapperClass(doubMapper.class);
    job.setReducerClass(douReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(LongWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.waitForCompletion(true);

}

}