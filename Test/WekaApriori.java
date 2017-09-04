package Test;

import java.io.PrintWriter;

import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaApriori {
	
	public static void main(String[] args){
		//load dataset
		String dataset = "/Users/situsnow/git/OpusMiner_SDRD/File/input/Adult_Header.arff";
		DataSource source;
		//PrintWriter writer = null;
		try {
			source = new DataSource(dataset);
			//get instances object 
			Instances data = source.getDataSet();
			//the Apriori algorithm
			Apriori model = new Apriori();
			
			//Set Class Association Rule as true
			model.setCar(true);
			//Set number of rules extracted
			model.setNumRules(2000);
			//Set minimum confidence (in CAR, only confidence can be used)
			model.setMinMetric(0.001);
			//model.setSignificanceLevel(0.05);
			//SelectedTag d = new SelectedTag(1, Apriori.TAGS_SELECTION);
			//model.setMetricType(d);
			
			model.setLowerBoundMinSupport(0.001);
			
			
			//build model
			model.buildAssociations(data);
			
//			writer = new PrintWriter("Apriori-Output.txt", "UTF-8");
//			for (String i : model.getOptions()){
//				writer.print(i);
//			}
			
			//writer.println("---------------------------------------------");
			//print out the extracted rules
			System.out.println(model);
			
			
			
			//System.out.println("Done.");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
//			if(writer != null){
//				writer.close();
//			}
		}
		
	}
	
}
