package opus;

import java.util.ArrayList;
import java.util.Collections;

public class Filter_Itemsets {


	//check one combination of current itemsets: {antecedents} + {consequent}
	public static boolean checkSS2(Tidset tidsleft, Tidset tidsright, 
			final int availabletids, final int count, double alpha){

		double p = Fisher.fisherTest(availabletids - tidsleft.size() - tidsright.size() + count, tidsleft.size() - count, 
				tidsright.size() - count, count);
		if (p > alpha){
			return false;
		}else{
			return true;
		}
	}
	
	// check whether itemset is is self sufficient given that it has supersets that cover the TIDs in supsettids
	public static boolean checkSS(Itemset is, Tidset supsettids){
		boolean result = true;
		
		// find for each item in is the TIDs that it covers that are not in supsettids
		ArrayList<Tidset> uniqueTids = new ArrayList<Tidset>();
		//uniqueTids.ensureCapacity(is.size());
		
		int i;
		for (i = 0; i < is.size(); i++){
			Tidset temp = new Tidset();
			if (Globals.consequentID == is.get(i)){
				temp.addAll(Globals.consequentTids);
			}else{
				temp.addAll(Globals.tids.get(is.get(i)));
			}
			
			Collections.sort(supsettids);
			temp.removeAll(supsettids);
			if (temp.size() == 0){
				// there cannot be a significant association from adding this tidset
				result = false;
				break;
			}
			uniqueTids.add(temp);
			
			
		}
		
		if (result){
			// set up a process that will check whether uniqueCov.size() is significantly greater than can be predicted by 
			//assuming independence between any partition of is
			Tidset uniqueCov = new Tidset();// this is the TIDs covered by is that are not in supsettids
			
			
			Tidset antCov = new Tidset();
			antCov = uniqueTids.get(1); //there will be at least one item in antecedent
			
			// calculate antecedent cover
			for (i = 2; i < is.size(); i++){
				Tidset.dintersection(antCov, uniqueTids.get(i));
			}
			// calculate uniqueCov
			Tidset.intersection(uniqueCov, antCov, uniqueTids.get(0));
			
			// this is the cover of the items committed to the right - initialise it to the last unique TID
			//In the context of SDRD, the first element will always be the consequent
			Tidset tidsright = uniqueTids.get(0);
			
			//The depth of alpha should be the actual size of the antecedent, so deduct 1 (consequent)
			result = checkSS2(antCov, tidsright, 
						Globals.noOfTransactions - supsettids.size(), uniqueCov.size(), Globals.getAlpha(is.size()-1));
				
				
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
			Tidset thissupsettids; // the tids covered by the supitems
			
			for (subset_it = 1; subset_it < is.size(); subset_it++){
				// get the TIDs that are covered by the current itemset's supersets
				supsettids = new Tidset();
				
				int supset_it;
				
				for (supset_it = 0; supset_it != subset_it; supset_it++){
					
					if(is.get(supset_it).selfSufficient && Utils.subset(is.get(subset_it), is.get(supset_it))){
						
						supitems = new Itemset();
						thissupsettids = new Tidset();
						int it;
						Itemset subset = is.get(subset_it);
						Itemset supset = is.get(supset_it);
						Collections.sort(subset);
						Collections.sort(supset);
						
						for (it = 0; it < supset.size(); it++){
							//The original logic in C++ is : if (subset_it->find(*it) == subset_it->end()) {
							if (!subset.contains(supset.get(it))){
								Collections.reverse(supitems);
								supitems.add(supset.get(it));
								Collections.reverse(supitems);
							}
						}
						
						if (!supitems.isEmpty()){
							Utils.gettids(supitems, thissupsettids);
							
							if (supsettids.isEmpty()){
								supsettids.addAll(thissupsettids);
							}else{
								Tidset.dunion(supsettids, thissupsettids);
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
