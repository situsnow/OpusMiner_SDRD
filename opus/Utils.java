package opus;


public class Utils {

	//function safe_alloc is never used in Opus Miner
	
	public static boolean subset(ItemsetRec s1, ItemsetRec s2){
		int it1 = 0;
		int it2 = 0;
		
		while (it1 != s1.size() - 1){
			if (it2 == s2.size() - 1)
				return false;
			if (s1.get(it1) < s2.get(it2))
				return false;
			if (s1.get(it1) == s2.get(it2))
				it1 ++;
			it2 ++;
		}
		
		return true;
	}
	
	public static void gettids(Itemset is, Tidset t){
		assert (is.size() > 0);
		
		int it = 0;
		
		if (is.size() == 1){
			t = Globals.tids.get(it);
		}else{
			
			final int item1 = is.get(it++);
			final int item2 = is.get(it++);
			Tidset.intersection(t, Globals.tids.get(item1), Globals.tids.get(item2));

			//joint consequent when it's supervised descriptive rule discovery
			if (Globals.sdrd == true){
				Tidset temp = t;
				Tidset.intersection(t, temp, Globals.consequentTids);
			}
			
			while (it != is.size() - 1){
				Tidset.dintersection(t, Globals.tids.get(is.get(it++)));
			}
			
		}
	}
	
	public static float countToSup(final int count){
		return (float)count/(float)Globals.noOfTransactions;
	}
	
	public static double itemSup(final int item){
		return countToSup(Globals.tids.get(item).size());
	}
	
	/**
	 * return the result of a Fisher exact test for an itemset i with support count count relative 
	 * to support counts count1 and count2 for two subsets s1 and s2 that form a partition of i
	 * In a contingency table [(a,b),(c,d)]
	 * count = d
	 * count1 = b + d
	 * count2 = c + d 
	 * @param count
	 * @param count1
	 * @param count2
	 * @return
	 */
	public static double fisher(final int count, final int count1, final int count2){
		return Fisher.fisherTest(Globals.noOfTransactions - count1 - count2 + count, count1 - count, count2 - count, count);
	}
	
	public static int getNum(String str){
		int result = 0;
		int i = 0;
		while (i < str.length()){
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9')
				result = result * 10 + str.charAt(i) - '0';
			i++;
		}
		return result;
	}
	
	
	
}
