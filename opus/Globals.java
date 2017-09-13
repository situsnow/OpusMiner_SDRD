package opus;

import java.util.ArrayList;
import java.util.List;

public class Globals {
	
	public static int noOfTransactions = 0;
	//Total number of unique items in DB
	public static int noOfItems = 0;
	//Store the transaction list according to item id
	public static List<Tidset> tids = new ArrayList<Tidset>();
	
	//public static PriorityQueue<ItemsetRec> itemsets = new PriorityQueue<ItemsetRec>(ItemsetRec.ItemsetRecComparator);
	
	// the number of associations to return
	public static int k = 100;
	// if true perform a filter for self-sufficiency
	public static boolean filter = true;
	// if true we should correct alpha for the size of the search space
	public static boolean correctionForMultCompare = true;

	public static List<Double> alpha = new ArrayList<Double>();
	//Save all unique item names
	public static List<String> itemNames = new ArrayList<String>();
	
	public static boolean searchByLift = false;
	public static boolean redundancyTests = true;
	public static boolean printClosures = false;
	public static boolean marketBasketData = false;
	
	//supervised descriptive rule discovery - contrast-sets mining
	//public static boolean sdrd = false;
	
	//For contrast-set mining - name
	public static String consequentName = "";
	//For contrast-set mining - id
	public final static int consequentID = -1;
	//Save the transaction id for current consequent
	public static Tidset consequentTids = new Tidset();
	//Upper bound value for consequent only
	public static float conUbVal;
	
	public static void expandAlpha(final long depth){
		if (alpha.isEmpty()){
			alpha.add(1.0);
			alpha.add(1.0);
			if (depth <= 1) return;
		}
		
		if (depth > noOfItems){
			alpha.add(0.0);
		}else if (depth == noOfItems){
			double tmp = alpha.get(alpha.size() - 1);
			alpha.add(tmp);
		}else{
			long i;
			for (i = alpha.size(); i <= depth; i++){
				alpha.add(Math.min((Math.pow(0.5, depth - 1) / 
						Math.exp(Fisher.log_combin(noOfItems, (int)(depth-1)))) * 0.05, 
						alpha.get((int) (depth - 1))));
			}
			
		}
	}
	
	public static double getAlpha(final long depth){
		if (correctionForMultCompare)
			return 0.05;
		
		if (depth >= alpha.size())
			expandAlpha(depth);
		
		return alpha.get(Integer.parseUnsignedInt(String.valueOf(depth)));
	}
	
	//Convert data to 0~65535 (0xFFFF means WORD)
	public int getUnsignedByte(short data){
		return data&0x0FFFF;
	}
	
	//Convert data to 0~4294967295 (0xFFFFFFFF means DWORD)
	public long getUnsignedInt(int data){
		return data&0x0FFFFFFFF;
	}
}
