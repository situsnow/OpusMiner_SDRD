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
	
	//Supervised Descriptive Rule Discovery - Contrast-set mining, for {1-itemsets, Consequent} : upper bound value
	public static Map<Itemset, Float> SDRDTIDCount = new HashMap<Itemset, Float>();
	
	//for static function getTIDCount
	private static int count;
	//for static function checkImmediateSubsets
	private static boolean redundant;
	//check whether current itemset has any subset that not in memory (already pruned), if apriori is true, means there's one subset not exists in interesting
	private static boolean apriori;
	//for static function checkSubsetsX
	private static float val;
	private static double p;
	
	/**Attention here: delete the second variable as Java is pass by value of reference,
	 * as the count can not refreshed with new value outside the function
	 * @param is
	 * @return
	 */
	public static boolean getTIDCount(Itemset is){
		if (is.size() == 1){
			//if (Globals.sdrd == true && is.get(0) == Globals.consequentID)
			if (is.get(0) == Globals.consequentID)
			{
				count = Globals.consequentTids.size();
			}else{
				count = Globals.tids.get(is.get(0)).size();
			}
			return true;
		}else{
			Itemset tmp = new Itemset();
			tmp.addAll(is);
			//Add sorting here in case there's matching subsets but in different order
			Collections.sort(tmp);
			Integer it = Find_Itemsets.TIDCount.get(tmp);
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
	
	/**
	 * Skip below coding blocks as no reference in orginal program
	 * 1. public float sortval;
	 * 2. int itemgt(const void *i1, const void *i2)
	 * 3. int itemlt(const void *i1, const void *i2) 
	 */
	
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
//		float this_val = (float) (Globals.searchByLift? new_sup / (Utils.countToSup(remainingCnt) * Utils.countToSup(sofarCnt))
//				: new_sup - Utils.countToSup(remainingCnt) * Utils.countToSup(sofarCnt));
//		
//		if (this_val < val){
//			//TODO
//			//val = this_val;
//			if (this_val <= minValue) return false;
//		}
		
		double this_p = Utils.fisher(cnt, sofarCnt, remainingCnt);
		
		if (this_p > p){
			//TODO
			//p = this_p;
			if (p > alpha) return false;
		}
		
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
		
		return p <= alpha;
		//return p <= alpha && val > minValue;
	}
	
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
		
		int itemCnt;
		if (Globals.consequentID == item){
			itemCnt = Globals.consequentTids.size();
		}else{
			itemCnt = Globals.tids.get(item).size();
		}
		
		//In Supervised Descriptive Rule Discovery, the val calculation only need to use one combination 
				//sup (antecedent + consequent) - sup (antecedent) * sup (consequent)
		val = (float) (Globals.searchByLift? new_sup / (parentSup * Utils.itemSup(item))
				: new_sup - parentSup * Utils.itemSup(item));
		
		if (val <= minValue) return false;
		
		p = Utils.fisher(cnt, itemCnt, parentCnt);
		
		if (p > alpha) return false;
		
		if (is.size() > 2){
			Itemset sofar = new Itemset();
			Itemset remaining = new Itemset(is);
			
			sofar.add(item);
			remaining.remove(new Integer(item));
			
			int it;
			for (it = 0; it < is.size(); it++){
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

		ItemsetRec tmp = new ItemsetRec(is.count, is.value, is.p, is.selfSufficient);
		tmp.addAll(is);
		//Add sorting here in case there's same itemset but in different orders
		//Collections.sort(tmp);
		OpusMiner.itemsets.add(tmp);
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
		
		
		ItemQClass newQ = new ItemQClass();
		
		for (i = 0; i < q.size(); i++){
			
			Tidset newCover = new Tidset();
			int item = q.get(i).item;
			int count;
			
			Tidset currItemset = new Tidset();
			
			
			//if (Globals.sdrd == true && Globals.consequentID == item){
			if (Globals.consequentID == item){
				currItemset = Globals.consequentTids;
			}else{
				currItemset = Globals.tids.get(item);
			}
			
			int newMaxItemCount = Math.max(maxItemCount, currItemset.size());
			
			//Check if itemset: {1-itemset, consequent} has been checked before, if true, skip calculation
			if (is.size() == 1 && (Globals.consequentID == item || is.contains(Globals.consequentID))){
				is.add(item);
				if (!newQ.isEmpty()){
					opus(is, newCover, newQ, newMaxItemCount);
				}
				
				Itemset tmp = new Itemset(is);
				Collections.sort(tmp);
				newQ.add(SDRDTIDCount.get(tmp), item);
				
				is.remove(new Integer(item));
				continue;
			}
			// determine the number of TIDs that the new itemset covers
			Tidset.intersection(newCover, cover, currItemset);
			count = newCover.size();
			
			float new_sup = Utils.countToSup(count);
			
			// this is a lower bound on the p value that may be obtained for this itemset or any superset
			double lb_p = Utils.fisher(count, newMaxItemCount, count);
			
			// calculate an upper bound on the value that can be obtained by this itemset or any superset
			
			//TODO maybe the new calculation for upper bound should also apply to situations where consequent does not exist?
			float ubVal = 0;
			if (is.contains(Globals.consequentID) || Globals.consequentID == item){
				//actually the consequent id will always in item as the itemset is sorted by id (consequent is with -1, will already on top) 
				//Hence, the support of is is the support of antecedent
				float conSup = Utils.countToSup(Globals.consequentTids.size());
				if (Globals.searchByLift){
					ubVal = (float) (1.0 / conSup);
				}else if (parentSup <= 0.5){
					ubVal = Math.min(conSup, parentSup) - conSup * parentSup;
				}else{
					ubVal = (float) (Math.min(conSup, 0.5) - conSup * 0.5);
				}
				
				//Original
//				ubVal = (float)(Globals.searchByLift? ((count == 0)? 0.0 : (1.0 / Utils.countToSup(maxItemCount)))
//						: new_sup - new_sup * Utils.countToSup(maxItemCount));
				
				//Actual lift/leverage
//				ubVal = (float)(Globals.searchByLift? new_sup/(Utils.countToSup(currItemset.size()) * parentSup)
//						:new_sup - Utils.countToSup(currItemset.size()) * parentSup);
				
			}else{
				
				float conSup = Utils.countToSup(Globals.consequentTids.size());
				if (Globals.searchByLift){
					ubVal = (float) (1.0 / conSup);
				}else if (new_sup <= 0.5){
					ubVal = Math.min(conSup, new_sup) - conSup * new_sup;
				}else{
					ubVal = (float) (Math.min(conSup, 0.5) - conSup * 0.5);
				}
				
			}
				
			// performing OPUS pruning - if this test fails, the item will not be included in any superset of is
			if (lb_p <= Globals.getAlpha(depth) && ubVal > minValue){
				// only continue if there is any possibility of this itemset or its supersets entering the list of best itemsets
				
				is.add(item);
				
				checkImmediateSubsets(is, count);
				
				if (!apriori){
					//only save those with oriented consequent itemsets.
					if (is.contains(Globals.consequentID) && 
							checkSubsets(item, is, count, new_sup, cover.size(), parentSup, Globals.getAlpha(depth))){
							is.count = count;
							is.value = val;
							is.p = p;
							
							insert_itemset(is);
					}
					
					
					// performing OPUS pruning - if this test fails, the item will not be included in any superset of is
					if (!redundant){
						ItemsetRec tmp_rec = new ItemsetRec(is.count, is.value, is.p, is.selfSufficient);
						tmp_rec.addAll(is);
						Collections.sort(tmp_rec);
						TIDCount.put(tmp_rec, count);
						
						if (!newQ.isEmpty()){
							// there are only more nodes to expand if there is a queue of items to consider expanding it with
							opus(is, newCover, newQ, newMaxItemCount);
						}
						
						newQ.add(ubVal, item);
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
		int consequentCover = Globals.consequentTids.size();
		float conSup = Utils.countToSup(consequentCover);
		float conUbVal = (float)(Globals.searchByLift? 1.0/conSup
				: conSup - conSup * conSup);
		if (Utils.fisher(consequentCover, consequentCover, consequentCover) > Globals.getAlpha(2)){
			System.err.print(String.format("Consequent '%s' is not productive.", Globals.consequentName));
			System.exit(1);
		}
		
		Globals.conUbVal = conUbVal;
		// initialize q - the queue of items ordered on an upper bound on value
		for (i = 0; i < Globals.noOfItems; i++){
			
			//In first level of lattice, only need to check if single item can pass the Fisher Exact Test
			float antSup = Utils.countToSup(Globals.tids.get(i).size());
			int maxCount = Math.max(Globals.consequentTids.size(), Globals.tids.get(i).size());
			
//			float ubVal = 0;
//			if (Globals.searchByLift){
//				ubVal = (float) (1.0 / conSup);
//			}else if (antSup <= 0.5){
//				ubVal = Math.min(conSup, antSup) - conSup * antSup;
//			}else{
//				ubVal = (float) (Math.min(conSup, 0.5) - conSup * 0.5);
//			}
			
			// make sure that the support is high enough for it to be possible to create a significant itemset
			//c : How many transactions that current item or +consequent occurs
			Tidset newCover = new Tidset();
			Tidset.intersection(newCover, Globals.consequentTids, Globals.tids.get(i));
			int c = newCover.size();
			double p = Utils.fisher(c, maxCount, c);
			//TODO: In original OPUS_MINER, the only pruning criteria is the FISHER EXACT TEST here, so it's more loose.
			//if (p <= Globals.getAlpha(2) && ubVal >minValue){
			if (p <= Globals.getAlpha(2)){	
				//For Supervised Descriptive Rule Discovery, though it saved as current 1-itemset, but the upper bound value is 1-itemset + consequent
				float lVal = (float)(Globals.searchByLift? Utils.countToSup(c)/(conSup * antSup)
						:Utils.countToSup(c) - conSup * antSup); 
				q.append(lVal, i);
				
				//Save the 2-itemset {1-itemset, consequent} to memory
				ItemsetRec is = new ItemsetRec();
				is.add(Globals.consequentID);
				is.add(i);
				is.count = c;
				is.value = lVal;
				is.p = p;
				//Sort is in case no match in get
				Collections.sort(is);
				
				insert_itemset(is);
				TIDCount.put(is, c);
				
				SDRDTIDCount.put(is, lVal);
			}
		}
		
		// this is the queue of items that will be available for the item currently being explored
		ItemQClass newq = new ItemQClass();
		
		if (q.size() > 0){
			//Sort the Item queue with upper bound values
			q.sort();
			// the first item will have no previous items with which to be paired so is simply added to the queue of availabile items
			newq.add(q.get(0).ubVal, q.get(0).item);

			//Ensure the consequent will be on top of the queue
			ItemQElem tmp = new ItemQElem(Globals.conUbVal, Globals.consequentID);
			Collections.reverse(newq);
			newq.add(tmp);
			Collections.reverse(newq);
				
		}
		
		//TODO remove after testing
		PrintStream queuef = null;
		try {
			queuef = new PrintStream(new File("File/queue.csv"));
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
			e.printStackTrace();
		} finally {
			if (queuef != null){
				queuef.close();
			}
		}
		//TODO remove after testing
		
		// remember the current minValue, and output an update if it improves in this iteration of the loop
		float prevMinVal = minValue;
		
		ItemsetRec is;
		
		// we are stepping through all associations of i with j<i, so the first value of i that will have effect is 1
		for (i = 1; i < q.size(); i++){
		//for (i = 1; i < q.size() && q.get(i).ubVal > minValue; i++){	
			//System.out.println("q.get(i).ubVal: "+ q.get(i).ubVal + ", minValue: " +  minValue);
			int item = q.get(i).item;
			
			is = new ItemsetRec();
			
			is.add(item);
			
			Tidset newCover = new Tidset();
			newCover = Globals.tids.get(item);
			
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
