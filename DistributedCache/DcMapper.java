package cn.distributedcache;


	
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


	public class DcMapper extends Mapper<Object, Text, Text, LongWritable> {
		
		ArrayList<String> foundpatterns = new ArrayList<String>();
		
		@Override
		protected void setup(Mapper<Object, Text, Text, LongWritable>.Context context)
				throws IOException, InterruptedException {
				
			URI[] files = context.getCacheFiles(); 
			for(URI file : files){
			     if(file.getPath().contains("wordpatterns")){
			       Path path = new Path(file);
			       BufferedReader reader = new BufferedReader(new FileReader(path.getName()));
				   String line = null;
					while ((line = reader.readLine()) != null) {
						StringTokenizer st = new StringTokenizer(line);					
						while (st.hasMoreTokens()) {
							foundpatterns.add(st.nextToken());
						}
					}
					reader.close();
			     }
			super.setup(context);}
			}
		


		@Override
		public void map(Object key, Text value, Context context) throws IOException,
				InterruptedException {
			// Break line into words for processing
			StringTokenizer words = new StringTokenizer(value.toString());
			
			while (words.hasMoreTokens()) {
				String presentTk = words.nextToken();
				if (foundpatterns.contains(presentTk)) {	
					context.write(new Text(presentTk), new LongWritable(1));
				}
			}
		}

		
		
	}
			


