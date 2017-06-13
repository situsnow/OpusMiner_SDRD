package opus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Load_Data {
	
	private static int EOF = -1;

	//TODO Attention. Change char to String of filename
	public static void load_data(final String filename){
		int c;
		String s;
		//Save item name <> unique item id
		Map<String, Integer> itemstrs = new HashMap<String, Integer>();
		Reader fileReader = null;
		try {
			fileReader = new InputStreamReader(new FileInputStream(filename));
			//File cannot open exception handle in catch section.
			c = fileReader.read();
			while (c != EOF){
				if (c == '\n'){
					//next transaction
					Globals.noOfTransactions++;
					c = fileReader.read();
				}else{
					//TODO
//					if (Globals.noOfTransactions == 0){
//						Globals.noOfTransactions++;
//					}
					
					s = "";
					StringBuffer sb = new StringBuffer();
					while (c > ' ' && c != ',' && c != EOF){
						sb.append(String.valueOf((char)c));
						c = fileReader.read();
					}
					
					s = sb.toString();
					
					if (!s.isEmpty()){
						//Save the consequent separately when in Supervised Descriptive Rule Discovery
						if (Globals.sdrd == true || Globals.consequentName.equals(s)){
							Globals.consequentTids.add((long)Globals.noOfTransactions);
						}else{
							//The item id for each unique item in DB
							int thisid;
							
							Integer it = itemstrs.get(s);
							
							if (it == null){
								thisid = Globals.itemNames.size();
								itemstrs.put(s, thisid);
								Globals.itemNames.add(s);
								Globals.noOfItems = Globals.itemNames.size();
								
								//TODO
								//tids.resize(noOfItems);
								//There's no need to resize a List in Java
								
							}else{
								thisid = itemstrs.get(s);
							}
							
							// insert the current TID into the tids for thisval, unless it has already been inserted and latest update
							if (Globals.tids.size() > thisid && Globals.tids.get(thisid) != null){
								//TODO Need to double check the logic here
								int lastIndex = Globals.tids.get(thisid).size() - 1;
								long lastList = Globals.tids.get(thisid).get(lastIndex);
								
								if (lastList != Globals.noOfTransactions){
									Globals.tids.get(thisid).add(Globals.noOfTransactions);
								}
							}else{
								Tidset tmp = new Tidset();
								tmp.add(Globals.noOfTransactions);
								Globals.tids.add(tmp);
							}
						}
					}
					while (c != EOF && c != '\n' && (c <= ' ' || c == '\t' || c == ',')){
						c = fileReader.read();
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			if (fileReader == null){
				System.out.print(String.format("Cannot open input file '%s'\n", filename));
				System.exit(1);;
			}
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
