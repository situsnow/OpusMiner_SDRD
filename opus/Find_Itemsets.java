package opus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Find_Itemsets {

	public static float minValue = Float.MIN_VALUE;
	
	//Only save those itemsets that pass the bound value and productive
	public static Map<Itemset, Integer> TIDCount = new HashMap<Itemset, Integer>();
	
	//Supervised Descriptive Rule Discovery - Contrast-set mining, for {productive itemsets, Consequent} : cover
	public static Map<Itemset, Integer> TIDConCount = new HashMap<Itemset, Integer>();
	
	//for static function getTIDCount
	private static int count;
	//for static function checkImmediateSubsets
	private static boolean redundant;
	//check whether current itemset has any subset that not in memory (already pruned), if apriori is true, means there's one subset not exists in interesting
	private static boolean apriori;
	//for static function checkSubsetsX
	private static float val;
	private static double p;
	
	//TODO 
	/**Attention here: delete the second variable as Java is pass by value of reference,
	 * as the count can not refreshed with new value outside the function
	 * @param is
	 * @return
	 */
	public static boolean getTIDCount(Itemset is){
		if (is.size() == 1){
			count = Globals.tids.get(is.get(0)).size();
			return true;
		}else{
			Integer it = Find_Itemsets.TIDCount.get(is);
			if (it == null){
				count = 0;
				return false;
			}else{
				count = it;
				return true;
			}
		}
	}
	
	public static boolean checkTIDCount(Itemset is){
		if (is.size() == 1){
			return true;
		}else{
			Integer it = TIDCount.get(is);
			if (it == null) return false;
			else return true;
		}
	}
	
	//TODO
	/**
	 * Skip below coding blocks as no reference in orginal program
	 * 1. public float sortval;
	 * 2. int itemgt(const void *i1, const void *i2)
	 * 3. int itemlt(const void *i1, const void *i2) 
	 */
	
	//TODO
	/**
	 * Skip two vairables here as Java is pass by value of reference.
	 * bool &redudant
	 * bool &apriori
	 * @param is
	 * @param isCnt
	 */
	public static void checkImmediateSubsets(Itemset is, final int isCnt){
		Itemset subset = new Itemset();
		subset.addAll(is);
		int it;
		
		redundant = false;
		apriori = false;
		
		for (it = 0; it < is.size(); it++){
			int subsetCnt;
			
			//Iteratively remove one of the items in is, and check if all of its immediate subset is in memory
			subset.remove(new Integer(is.get(it)));
			//TODO
			/**
			 * Skip second variable subsetCnt here
			 */
			if (!getTIDCount(subset)){
				redundant = true;
				apriori = true;
				return;
			}
			subsetCnt = count;
			
			//The redundant property of self-sufficient itemsets
			if (Globals.redundancyTests && subsetCnt == isCnt){
				redundant = true;
			}
			
			Collections.reverse(subset);
			subset.add(is.get(it));
			Collections.reverse(subset);
			
		}
		return;
	}
	
	//TODO
	/**
	 * calculates leverage, p, whether the itemset is is redundant and whether it is possible to determine that all supersets of is will be redundant
	 * return true iff is is not redundant, val > minValue and p <= alpha
	 * 
	 * Skip two variables for this function as Java is pass by value of reference
	 * 1. float &val
	 * 2. double &p
	 * @param sofar
	 * @param remaining
	 * @param limit
	 * @param cnt
	 * @param new_sup
	 * @param alpha
	 * @return
	 */
	public static boolean checkSubsetsX(Itemset sofar, Itemset remaining, int limit, int cnt, double new_sup, double alpha){
		int sofarCnt;
		int remainingCnt;
		
		boolean sofarFlag = getTIDCount(sofar);
		sofarCnt = count;
		boolean remainingFlag = getTIDCount(remaining);
		remainingCnt = count;
		if (!sofarFlag || !remainingFlag){
			//As the count value will never used here, do not need to pass to neither sofarCnt or remainingCnt
			return false;
		}
		
		// do test for sofar against remaining
		float this_val = (float) (Globals.searchByLift? new_sup / (Utils.countToSup(remainingCnt) * Utils.countToSup(sofarCnt))
				: new_sup - Utils.countToSup(remainingCnt) * Utils.countToSup(sofarCnt));
		
		if (this_val < val){
			val = this_val;
			if (this_val <= minValue) return false;
		}
		
		double this_p = Utils.fisher(cnt, sofarCnt, remainingCnt);
		
		if (this_p > p){
			p = this_p;
			if (p > alpha) return false;
		}
		
		//TODO Snow: joint the consequent to see if {k-itemset, consequent} can pass the Fisher Exact Test
		
		if (remaining.size() > 1){
			Itemset new_remaining = new Itemset(remaining);
			
			int it;
			for (it = 0; it != remaining.size() - 1 && remaining.get(it) < limit; it++){
				Collections.reverse(sofar);
				sofar.add(remaining.get(it));
				Collections.reverse(sofar);
				
				new_remaining.remove(new Integer(remaining.get(it)));
				
				if (!checkSubsetsX(sofar, new_remaining, remaining.get(it), cnt, new_sup, alpha)){
					return false;
				}
				
				sofar.remove(new Integer(remaining.get(it)));
				
				Collections.reverse(new_remaining);
				new_remaining.add(remaining.get(it));
				Collections.reverse(new_remaining);
			}
		}
		
		return p <= alpha && val > minValue;
	}
	
	//TODO
	/**
	 * calculates leverage, p, whether is is redundant and whether it is possible to determine that all supersets of is will be redundant
	 * return true iff is is not redundant, val > minValue and p <= alpha
	 * 
	 * Skip two variables here as Java is passed by value of reference
	 * 1. float &val
	 * 2. double &p
	 * @param item
	 * @param is
	 * @param cnt
	 * @param new_sup
	 * @param parentCnt
	 * @param parentSup
	 * @param alpha
	 * @return
	 */
	public static boolean checkSubsets(int item, Itemset is, int cnt, double new_sup, int parentCnt, double parentSup, double alpha){
		assert (is.size() > 1);
		
		int itemCnt = Globals.tids.get(item).size();
		
		val = (float) (Globals.searchByLift? new_sup / (parentSup * Utils.itemSup(item))
				: new_sup - parentSup * Utils.itemSup(item));
		
		
		if (val <= minValue) return false;
		
		p = Utils.fisher(cnt, itemCnt, parentCnt);
		
		if (p > alpha) return false;
		
		if (is.size() > 2){
			Itemset sofar = new Itemset();
			Itemset remaining = new Itemset(is);
			
			//TODO originate is insert
			sofar.add(item);
			remaining.remove(new Integer(item));
			
			int it;
			for (it = 0; it != is.size() - 1; it++){
				if (is.get(it) != item){
					Collections.reverse(sofar);
					sofar.add(is.get(it));
					Collections.reverse(sofar);
					
					remaining.remove(new Integer(is.get(it)));
					
					if (!checkSubsetsX(sofar, remaining, is.get(it), cnt, new_sup, alpha)){
						return false;
					}
					
					sofar.remove(new Integer(is.get(it)));
					Collections.reverse(remaining);
					remaining.add(is.get(it));
					Collections.reverse(remaining);
					
				}
			}
		}
		
		return p <= alpha && val > minValue;
	}
	
	public static void insert_itemset(ItemsetRec is){
		if (OpusMiner.itemsets.size() >= Globals.k){
			OpusMiner.itemsets.poll();
		}
		
		OpusMiner.itemsets.offer(is);
		if (OpusMiner.itemsets.size() == Globals.k){
			float newMin = OpusMiner.itemsets.peek().value;
			if (newMin > minValue){
				minValue = newMin;
			}
		}
	}
	
	// perform OPUS search for specializations of is (which covers cover) using the candidates in queue q
	// maxItemSup is the maximum of the supports of all individual items in is
	public static void opus(ItemsetRec is, Tidset cover, ItemQClass q, int maxItemCount){
		//TODO pay attention here, originate unsigned int i
		int i;
		float parentSup = Utils.countToSup(cover.size());
		
		int depth = is.size() + 1;
		
		Tidset newCover = new Tidset();
		ItemQClass newQ = new ItemQClass();
		
		for (i = 0; i < q.size(); i++){
			int item = q.get(i).item;
			int count;
			
			Tidset currItemset = new Tidset();
			
			
			if (Globals.sdrd == true){
				Tidset.intersection(currItemset, Globals.consequentTids, Globals.tids.get(item));
			}else{
				currItemset = Globals.tids.get(item);
			}
			// determine the number of TIDs that the new itemset covers
			Tidset.intersection(newCover, cover, currItemset);
			
			count = newCover.size();
			
			int newMaxItemCount = Math.max(maxItemCount, currItemset.size());
			float new_sup = Utils.countToSup(count);
			
			// this is a lower bound on the p value that may be obtained for this itemset or any superset
			double lb_p = Utils.fisher(count, newMaxItemCount, count);
			
			// calculate an upper bound on the value that can be obtained by this itemset or any superset
			float ubval = (float)(Globals.searchByLift? ((count ==0) ? 0.0 : (1.0 / Utils.countToSup(maxItemCount)))
					: new_sup - new_sup * Utils.countToSup(maxItemCount));
			
			// performing OPUS pruning - if this test fails, the item will not be included in any superset of is
			if (lb_p <= Globals.getAlpha(depth) && ubval > minValue){
				
				// only continue if there is any possibility of this itemset or its supersets entering the list of best itemsets
				//TODO
				/**
				 * Skip four variables here
				 * 1. float val;
				 * 2. double p;
				 * 3. bool redundant
				 * 4. bool apriori
				 */
				
				//TODO: Duplicate itemset added here, check why
				//Might consider to use the List API which allows no duplicates
				Collections.reverse(is);
				is.add(item);
				Collections.reverse(is);
				
				checkImmediateSubsets(is, count);
				
				if (!apriori){
					if (checkSubsets(item, is, count, new_sup, cover.size(), parentSup, Globals.getAlpha(depth))){
						is.count = count;
						is.value = val;
						is.p = p;
						//TODO only need to save those with oriented consequent itemsets.
						insert_itemset(is);
						
					}
					
					// performing OPUS pruning - if this test fails, the item will not be included in any superset of is
					if (!redundant){
						//TODO Snow, saved all subsets here? Say, When checking {ABY}, AB is also need to saved.
						TIDCount.put(is, count);
						
						if (!newQ.isEmpty()){
							// there are only more nodes to expand if there is a queue of items to consider expanding it with
							opus(is, newCover, newQ, newMaxItemCount);
						}
						
						Collections.reverse(newQ);
						newQ.add(ubval, item);
						Collections.reverse(newQ);
					}
				}
				
				is.remove(new Integer(item));
			}
		}
	}
	
	public static void find_itemsets(){
		//A queue of items, to be sorted on an upper bound on value
		ItemQClass q = new ItemQClass();
		int i;
		//Check if consequent can pass the Fisher Exact Test, kind of exception handling
		if (Globals.sdrd == true){
			int consequentCover = Globals.consequentTids.size();
			float conSup = Utils.countToSup(consequentCover);
			float conUbVal = (float)(Globals.searchByLift? 1.0/conSup
					: conSup - conSup * conSup);
			if (Utils.fisher(consequentCover, consequentCover, consequentCover) > Globals.getAlpha(2)){
				System.err.print(String.format("Consequent '%s' is not productive.", Globals.consequentName));
				System.exit(1);
			}else{
				Globals.conUbVal = conUbVal;
			}
		}
		
		// initialize q - the queue of items ordered on an upper bound on value
		for (i = 0; i < Globals.noOfItems; i++){
			
			//In first level of lattice, only need to check if single item can pass the Fisher Exact Test
			Tidset newCover = new Tidset();
			
			//c : How many transactions that current item or +consequent occurs
			if (Globals.sdrd == true){
				Tidset.intersection(newCover, Globals.consequentTids, Globals.tids.get(i));
			}else{
				newCover = Globals.tids.get(i);
			}
			int c = newCover.size();
			
			float sup = Utils.countToSup(c);
			float ubVal = (float)(Globals.searchByLift ? 1.0 / sup
					: sup - sup * sup); //later one is leverage value
			
			// make sure that the support is high enough for it to be possible to create a significant itemset
			if (Utils.fisher(c, c, c) <= Globals.getAlpha(2)){
				//For Supervised Descriptive Rule Discovery, though it saved as current 1-itemset, but the upper bound value is 1-itemset + consequent
				q.append(ubVal, i);
			}
		}
		
		// this is the queue of items that will be available for the item currently being explored
		ItemQClass newq = new ItemQClass();
		
		if (q.size() > 0){
			q.sort();
			// the first item will have no previous items with which to be paired so is simply added to the queue of availabile items
			newq.add(q.get(0).ubVal, q.get(0).item);
		}
		
		//TODO remove after testing
		PrintStream queuef = null;
		try {
			queuef = new PrintStream(new File("queue.csv"));
			StringBuffer sb = new StringBuffer();
			sb.append("Index, ");
			sb.append("Item Name\n");
			
			for (int j = 0; j < q.size(); j++){
				ItemQElem elem = q.get(j);
				
				sb.append(elem.item);
				sb.append(", ");
				sb.append(Globals.itemNames.get(elem.item));
				sb.append(", ");
				sb.append(elem.ubVal);
				sb.append("\n");
			}
			
			queuef.print(sb.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (queuef != null){
				queuef.close();
			}
		}

		//TODO
		
		// remember the current minValue, and output an update if it improves in this iteration of the loop
		float prevMinVal = minValue;
		
		ItemsetRec is;
		
		// we are stepping through all associations of i with j<i, so the first value of i that will have effect is 1
		for (i = 1; i < q.size() && q.get(i).ubVal > minValue; i++){
			int item = q.get(i).item;
			
			is = new ItemsetRec();
			//TODO
			//Originate is insert()
			is.add(item);
			
			Tidset newCover = new Tidset();
			if (Globals.sdrd == true){
				Tidset.intersection(newCover, Globals.consequentTids, Globals.tids.get(item));
			}else{
				newCover = Globals.tids.get(item);
			}
			
			//TODO pay attention here if need to use the intersection of item+consequent for the last param: size
			//Make sure when checking itemset {A, B, Y}, all its subsets {A, B, AB, AY, BY} should be stored.
			opus(is, newCover, newq, newCover.size());
			
			newq.append(q.get(i).ubVal, item);
			
			if (prevMinVal < minValue){
				System.out.print(String.format("<%f>", minValue));
				prevMinVal = minValue;
			}else System.out.print(String.valueOf('.'));
			
			System.out.flush();
		}
		
		System.out.print(String.valueOf('\n'));
	}
	
}
