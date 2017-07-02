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

	public static void load_data(final String filename){
		int c;
		String s;
		//for non-market-basket-data applications
		int fCounter = 1;
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
					if (Globals.marketBasketData == false)
						fCounter = 1;
				}else{
					
					s = "";
					StringBuffer sb = new StringBuffer();
					while (c > ' ' && c != ',' && c != EOF){
						sb.append(String.valueOf((char)c));
						c = fileReader.read();
					}
					
					s = sb.toString();
					
					if (!s.isEmpty()){
						//Save the consequent separately when in Supervised Descriptive Rule Discovery
						if (Globals.sdrd == true && Globals.consequentName.equals(s)){
							Globals.consequentTids.add((long)Globals.noOfTransactions);
							fCounter++;
						}else{
							
							//The item id for each unique item in DB
							if (Globals.marketBasketData == false){
								s = "field" + fCounter + " = " + s;
								fCounter++;
							}
								
							int thisid;
							
							Integer it = itemstrs.get(s);
							
							if (it == null){
								// if it doesn't have an id, assign one
								thisid = Globals.itemNames.size();
								itemstrs.put(s, thisid);
								Globals.itemNames.add(s);
								Globals.noOfItems = Globals.itemNames.size();
								
								//tids.resize(noOfItems);
								//There's no need to resize a List in Java
								
							}else{
								thisid = itemstrs.get(s);
							}
							
							// insert the current TID into the tids for thisval, unless it has already been inserted and latest update
							if (Globals.tids.size() == thisid){
								//If the item appear in dataset the first time
								Tidset tmp = new Tidset();
								tmp.add(Globals.noOfTransactions);
								Globals.tids.add(tmp);
							}else if(Globals.tids.get(thisid).get(Globals.tids.get(thisid).size() - 1) != Globals.noOfTransactions){
								Globals.tids.get(thisid).add(Globals.noOfTransactions);
							}
						}
					}
					while (c != EOF && c != '\n' && (c <= ' ' || c == '\t' || c == ',')){
						c = fileReader.read();
					}
				}
			}
		} catch (FileNotFoundException e) {

			if (fileReader == null){
				System.out.print(String.format("Cannot open input file '%s'\n", filename));
				System.exit(1);;
			}
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} 
	}
	
}
