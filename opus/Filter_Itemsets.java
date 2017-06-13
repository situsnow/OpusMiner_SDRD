package opus;

import java.util.ArrayList;
import java.util.Collections;

public class Filter_Itemsets {

//	public static boolean sizegt(Itemset i1, Itemset i2){
//		return i1.size() > i2.size();
//	}
	
	// check all combinations of intersections of partitions of tidsavail moved to either tidsleft or tidsright
	public static boolean checkSS2(ArrayList<Tidset> uniqueTids, final int no, Tidset tidsleft, Tidset tidsright, 
			final int availabletids, final int count, double alpha){
		if (no == 0){
			if (Fisher.fisherTest(availabletids - tidsleft.size() - tidsright.size() + count, tidsleft.size() - count, 
					tidsright.size() - count, count) > alpha){
				return false;
			}else{
				return true;
			}
		}
		
		// first try with the tidset committed to the left then try with it committed to the right
		Tidset newtids = new Tidset();
		Tidset.intersection(newtids, uniqueTids.get(no - 1), tidsleft);
		
		if (!checkSS2(uniqueTids, no - 1, newtids, tidsright, availabletids, count, alpha)){
			return false;
		}
		
		return true;
	}
	
	// check whether itemset is is self sufficient given that it has supersets that cover the TIDs in supsettids
	public static boolean checkSS(Itemset is, Tidset supsettids){
		boolean result = true;
		
		// find for each item in is the TIDs that it covers that are not in supsettids
		ArrayList<Tidset> uniqueTids = new ArrayList<Tidset>();
		uniqueTids.ensureCapacity(is.size());
		
		int i;
		for (i = 0; i != is.size() - 1; i++){
			uniqueTids.get(i).ensureCapacity(Globals.tids.get(i).size());
			
			//TODO double check logics
			Tidset temp = Globals.tids.get(i);
			temp.removeAll(supsettids);
			uniqueTids.set(i, temp);
			
			// there cannot be a significant association from adding this tidset
			if (uniqueTids.size() == 0){
				result = false;
				break;
			}
		}
		
		if (result){
			// set up a process that will check whether uniqueCov.size() is significantly greater than can be predicted by assuming independence between any partition of is
			Tidset uniqueCov = new Tidset();// this is the TIDs covered by is that are not in supsettids
			if (Globals.sdrd == true){
				Tidset.intersection(uniqueCov, uniqueTids.get(0), Globals.consequentTids);
			}else{
				uniqueCov = uniqueTids.get(0);
			}
			
			// calculate uniqueCov
			for (i = 0; i < is.size(); i++){
				Tidset.dintersection(uniqueCov, uniqueTids.get(i));
			}
			
			// this is the cover of the items committed to the right - initialise it to the last unique TID
			Tidset tidsright = uniqueTids.get(uniqueTids.size() - 1);
			
			// start with the last item committed to the right, then successively commit eeach item first to the left then to the right
			for (i = uniqueTids.size() - 2; i >= 0; i--){
				result = checkSS2(uniqueTids, i, uniqueTids.get(i), tidsright, 
						Globals.noOfTransactions - supsettids.size(), uniqueCov.size(), Globals.getAlpha(is.size()));
				
				if (result == false)
					return false;
				
				if (i > 0){
					Tidset.dintersection(tidsright, uniqueTids.get(i));
				}
			}
		}
		
		return result;
	}
	
	// check whether itemsets can be explained by their supersets
	public static void filter_itemsets(ArrayList<ItemsetRec> is){
		if (!is.isEmpty()){
			// Sort the itemsets so that the largest are first.
		    // This way we only need to look at the itemsets before one that we are processing to find superset.
		    // Also, we will determine whether a superset is self sufficient before trying to determine whether its subsets are
			
			Collections.sort(is, ItemsetRec.ItemsetRecSizeComparator);
			
			int subset_it;
			
			Itemset supitems; // the additional items n the supersets of the current itemset
			Tidset supsettids; // the tids covered by the supitems
			Tidset thissupsettids = new Tidset(); // the tids covered by the supitems
			
			for (subset_it = 1; subset_it != is.size() - 1; subset_it++){
				// get the TIDs that are covered by the current itemset's supersets
				supsettids = new Tidset();
				int supset_it;
				for (supset_it = 0; supset_it != subset_it; supset_it++){
					if(is.get(supset_it).selfSufficient){
						supitems = new Itemset();
						
						if (Utils.subset(is.get(subset_it), is.get(supset_it))){
							int it;
							
							for (it = 0; it != is.get(supset_it).size() - 1; it++){
								if (is.get(subset_it).indexOf(is.get(it)) == is.get(subset_it).size() - 1){
									Collections.reverse(supitems);
									supitems.addAll(is.get(it));
									Collections.reverse(supitems);
								}
							}
							
							if (!supitems.isEmpty()){
								Utils.gettids(supitems, thissupsettids);
								
								if (supsettids.isEmpty()){
									supsettids = thissupsettids;
								}else{
									Tidset.dunion(supsettids, thissupsettids);
								}
							}
						}
					}
				}
				
				if (!supsettids.isEmpty() && !checkSS(is.get(subset_it), supsettids)){
					// only call chechSS if one or more supersets were found (and hence TIDs retrieved
					is.get(subset_it).selfSufficient = false;
				}
			}
			
			
		}
	}
	
}
