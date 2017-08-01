package opus;

import java.util.Collections;

public class Utils {

	//function safe_alloc is never used in Opus Miner
	
	public static boolean subset(ItemsetRec s1, ItemsetRec s2){
		int it1 = 0;
		int it2 = 0;
		
		Collections.sort(s1);
		Collections.sort(s2);
		
		while (it1 != s1.size()){
			if (it2 == s2.size())
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
			if (is.get(0) == Globals.consequentID){
				t.addAll(Globals.consequentTids);
			}else{
				t.addAll(Globals.tids.get(is.get(0)));
			}
		}else{
			final int item1 = is.get(it++);
			final int item2 = is.get(it++);
			Tidset item1Tid = new Tidset();
			Tidset item2Tid = new Tidset();
			if (item1 == Globals.consequentID){
				item1Tid.addAll(Globals.consequentTids);
				item2Tid.addAll(Globals.tids.get(item2));
			}else if(item2 == Globals.consequentID){
				item1Tid.addAll(Globals.tids.get(item1));
				item2Tid.addAll(Globals.consequentTids);
			}else{
				item1Tid.addAll(Globals.tids.get(item1));
				item2Tid.addAll(Globals.tids.get(item2));
			}
			Tidset.intersection(t, item1Tid, item2Tid);

			while (it < is.size()){
				Tidset itemTid = new Tidset();
				if (is.get(it) == Globals.consequentID){
					itemTid.addAll(Globals.consequentTids);
				}else{
					itemTid.addAll(Globals.tids.get(is.get(it)));
				}
				Tidset.dintersection(t, itemTid);
				it++;
			}
			
		}
	}
	
	public static float countToSup(final int count){
		return (float)count/(float)Globals.noOfTransactions;
	}
	
	public static double itemSup(final int item){
		if (Globals.consequentID == item){
			return countToSup(Globals.consequentTids.size());
		}else{
			return countToSup(Globals.tids.get(item).size());
		}
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
