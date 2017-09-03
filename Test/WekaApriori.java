package Test;

import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaApriori {
	
	public static void main(String[] args){
		//load dataset
		String dataset = "/Users/situsnow/git/OpusMiner_SDRD/File/input/Adult_Header.arff";
		DataSource source;
		try {
			source = new DataSource(dataset);
			//get instances object 
			Instances data = source.getDataSet();
			//the Apriori algorithm
			Apriori model = new Apriori();
			
			//set options
			String[] options = new String[3];
			//number of rules
			options[0] = "-N 100";
			//The metric type by which to rank rules: <0=confidence | 1=lift | 2=leverage | 3=Conviction>
			options[1] = "-T 1";
			//options[1] = "-T 2";
			//options[2] = "-S 0.05";
			options[2] = "-A";
			//-c <the class index> The class index. (default = last)
			//options[4] = "-c ";
			
			model.setOptions(options);
			//build model
			model.buildAssociations(data);
			//print out the extracted rules
			System.out.println(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
