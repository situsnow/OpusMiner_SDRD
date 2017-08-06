package opus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Load_Data {
	
	private static String SEPARATOR = ",";

	public static void load_data(final String filename){
		String c;
		String s;
		//for non-market-basket-data applications
		int fCounter = 1;
		//Save item name <> unique item id
		Map<String, Integer> itemstrs = new HashMap<String, Integer>();
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(filename));
			//File cannot open exception handle in catch section.
			while ((c = fileReader.readLine()) != null){
				
				if (!"".equals(c)){
					String[] items = c.split(SEPARATOR);
					
					if (items.length > 0){
						for (int i = 0; i < items.length; i++){
							s = items[i].trim();
							if (!s.isEmpty()){
								//Save the consequent separately when in Supervised Descriptive Rule Discovery
								if (Globals.consequentName.equals(s)){
									Globals.consequentTids.add((long)Globals.noOfTransactions);
									fCounter++;
								}else{
									
									//The item id for each unique item in DB
									if (!Globals.marketBasketData){
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
						}
						//next transaction
						Globals.noOfTransactions++;
						if (!Globals.marketBasketData)
							fCounter = 1;
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
