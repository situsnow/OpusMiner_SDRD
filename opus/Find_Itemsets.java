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
	private static float levVal;
	private static float lifVal;
	
	/**Attention here: delete the second variable as Java is pass by value of reference,
	 * as the count can not refreshed with new value outside the function
	 * @param is
	 * @return
	 */
	public static boolean getTIDCount(Itemset is){
		if (is.size() == 1){
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
		
		boolean sofarFlag = getTIDCount(sofar);
		int sofarCnt = count;
		boolean remainingFlag = getTIDCount(remaining);
		int remainingCnt = count;
		if (!sofarFlag || !remainingFlag){
			//As the count value will never used here, do not need to pass to neither sofarCnt or remainingCnt
			return false;
		}
		
		// do test for sofar against remaining
//		float this_val = (float) (Globals.searchByLift? new_sup / (Utils.countToSup(remainingCnt) * Utils.countToSup(sofarCnt))
//				: new_sup - Utils.countToSup(remainingCnt) * Utils.countToSup(sofarCnt));
//		
//		if (this_val < val){
//			//val = this_val;
//			if (this_val <= minValue) return false;
//		}
		
		double this_p = Utils.fisher(cnt, sofarCnt, remainingCnt);
		
		if (this_p > p && p > alpha){
			return false;
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
		
		return true;
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
		// the item here will always equal to the consequent
		itemCnt = Globals.consequentTids.size();
		
		//In Supervised Descriptive Rule Discovery, the val calculation only need to use one combination 
				//sup (antecedent + consequent) - sup (antecedent) * sup (consequent)
		levVal = (float) (new_sup - parentSup * Utils.itemSup(item));
		lifVal = (float) (new_sup / (parentSup * Utils.itemSup(item)));
		val = Globals.searchByLift? lifVal : levVal;
		
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
		
		// As when search by lift, current itemset's value will not checked against the minValue, need to double check here
		if (OpusMiner.itemsets.size() >= Globals.k && OpusMiner.itemsets.peek().value > is.value)
			return;
		
		if (OpusMiner.itemsets.size() >= Globals.k){
			OpusMiner.itemsets.poll();
		}

		ItemsetRec tmp = new ItemsetRec(is.count, is.value, is.p, is.selfSufficient, 
				is.leverage, is.lift, is.antSup, is.strength);
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
	public static void opus(ItemsetRec is, Tidset cover, ItemQClass q, int maxItemCount, float preUbVal){
		boolean proceedFlag = true;
		int i;
		float parentSup = Utils.countToSup(cover.size());
		
		//As now the program traversing the lattice, the consequent shouldn't included in the depth.
		int depth = is.size() + 1;
		
		ItemQClass newQ = new ItemQClass();
		
		//boolean filteredCon = false;
		for (i = 0; i < q.size(); i++){
			
			Tidset newCover = new Tidset();
			int item = q.get(i).item;
			int count;

			Tidset currItemset = new Tidset();
			
			if (Globals.consequentID == item){
				currItemset = Globals.consequentTids;
			}else{
				currItemset = Globals.tids.get(item);
			}
			
			//Check if itemset: {1-itemset, consequent} has been checked before, if true, skip calculation
			if (is.size() == 1 && (Globals.consequentID == item || is.contains(Globals.consequentID))){
				is.add(item);
				
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
			float ubVal = 0;
			if (item != Globals.consequentID){
				//At this moment, the current itemset is excluded the consequent. Otherwise, this overall itemset had been checked in immediate previous step.
				Tidset ruleCover = new Tidset();
				Tidset.intersection(ruleCover, newCover, Globals.consequentTids);
				
				// this is the actual p value for current k-itemset and consequent
				// Originally in OpusMiner, this is a lower bound on the p value that may be obtained for this itemset or any superset
				double lb_p = Utils.fisher(ruleCover.size(), Globals.consequentTids.size(), count);
				
				float conSup = Utils.countToSup(Globals.consequentTids.size());
				//actually the consequent id will always in item as the itemset is sorted by id (consequent is with -1, will already on top) 
				//And when the skipFlag is false, means currently is checking itemset without consequent, so the new_sup is actually the
				//support of antecedent in final rule
				
				// calculate an upper bound on the value that can be obtained by this itemset or any superset
				if (new_sup <= 0.5){
					ubVal = Math.min(conSup, new_sup) - conSup * new_sup;
				}else{
					ubVal = (float) (Math.min(conSup, 0.5) - conSup * 0.5);
				}
				//If it cannot pass, skip the superset checking once and for all (current itemset + consequent).
				//Current itemset only includes the antecedent, then depth should be the actual antecedent size.
				//The upperbound value here is calculated with the support of consequent.
				proceedFlag = lb_p <= Globals.getAlpha(depth) && (Globals.searchByLift || ubVal > minValue); 
				
				if (proceedFlag == false)
					continue;

			}
			
			is.add(item);
			
			checkImmediateSubsets(is, count);
			
			if (!apriori){
				//only save those with oriented consequent itemsets.
				//If current itemset includes consequent, the size of the antecedent should deduct 1.
				if (is.contains(Globals.consequentID) && 
						checkSubsets(item, is, count, new_sup, cover.size(), parentSup, Globals.getAlpha(depth - 1))){
					
						is.count = count;
						is.value = val;
						is.p = p;
						is.leverage = levVal;
						is.lift = lifVal;
						is.antSup = parentSup;
						is.strength = new_sup / parentSup;
						insert_itemset(is);
				}
				
				// performing OPUS pruning - if this test fails, the item will not be included in any superset of is
				if (!redundant){
					ItemsetRec tmp_rec = new ItemsetRec(is.count, is.value, is.p, is.selfSufficient, 
							is.leverage, is.lift, is.antSup, is.strength);
					tmp_rec.addAll(is);
					Collections.sort(tmp_rec);
					TIDCount.put(tmp_rec, count);
					
					if (!newQ.isEmpty()){
						// there are only more nodes to expand if there is a queue of items to consider expanding it with
						opus(is, newCover, newQ, count, ubVal);
					}
					
					//if item is consequent, the ubVal will be 0, but it does not matter as consequent will always order on top of queue
					newQ.add(ubVal > 0?ubVal:preUbVal, item);
				}
			}
			
			is.remove(new Integer(item));
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
		if (Utils.fisher(consequentCover, consequentCover, consequentCover) > Globals.getAlpha(1)){
			System.err.print(String.format("Consequent '%s' is not productive.", Globals.consequentName));
			System.exit(1);
		}
		
		Globals.conUbVal = conUbVal;
		// initialize q - the queue of items ordered on an upper bound on value
		
		for (i = 0; i < Globals.noOfItems; i++){
			//first check if each item can pass the significant test alone
			int itemCover = Globals.tids.get(i).size();
			
			if (Utils.fisher(itemCover, itemCover, itemCover) > Globals.getAlpha(1)){
				continue;
			}
			
			//In first level of lattice, only need to check if single item can pass the Fisher Exact Test
			float antSup = Utils.countToSup(itemCover);
			
			float ubVal = 0;

			//for calculation of search by leverage only
			ubVal = Math.min(conSup, antSup) - conSup * Math.min(conSup, antSup);
			
			if(!Globals.searchByLift && ubVal < minValue){
				//if current itemset's upper bound value is less than the minimum k-th itemsets in memory, ignore
				continue;
			}
			// make sure that the support is high enough for it to be possible to create a significant itemset
			//c : How many transactions that current item or +consequent occurs
			Tidset newCover = new Tidset();
			Tidset.intersection(newCover, Globals.consequentTids, Globals.tids.get(i));
			int c = newCover.size();
			float ruleSup = Utils.countToSup(c);
			ubVal = ruleSup - conSup * Math.min(conSup, antSup);
			
			
			double p = Utils.fisher(c, Globals.consequentTids.size(), Globals.tids.get(i).size());
			
			//In original OPUS_MINER, the only pruning criteria is the FISHER EXACT TEST here, so it's more loose.
			
			//only skip the checking of ubVal when search by leverage
			
			//Since the upper bound value was already checked in previous section, can skip here
			//if (p <= Globals.getAlpha(2) && (Globals.searchByLift || ubVal >minValue)){
			if (p <= Globals.getAlpha(1)){	
				//For Supervised Descriptive Rule Discovery, though it saved as current 1-itemset, but the upper bound value is 1-itemset + consequent
				float leverage = ruleSup - conSup * antSup;
				float lift = ruleSup / (conSup * antSup);
				float lVal = (float)(Globals.searchByLift? lift : leverage); 
				q.append(ubVal, i);
				
				//Save the 2-itemset {1-itemset, consequent} to memory
				ItemsetRec is = new ItemsetRec();
				is.add(Globals.consequentID);
				is.add(i);
				is.count = c;
				is.value = lVal;
				is.lift = lift;
				is.leverage = leverage;
				is.p = p;
				is.antSup = antSup;
				is.strength = ruleSup / antSup;
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
			newq.add(tmp);
			Collections.reverse(newq);
				
		}
		
//		//TODO remove after testing
		PrintStream queuef = null;
		try {
			queuef = new PrintStream(new File("File/queue.csv"));
			StringBuffer sb = new StringBuffer();
			sb.append("Index, ");
			sb.append("Item Name, ");
			sb.append("Upper bound value\n");
			
			
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
//		//TODO remove after testing
		
		// remember the current minValue, and output an update if it improves in this iteration of the loop
		float prevMinVal = minValue;
		
		ItemsetRec is;
		
		// we are stepping through all associations of i with j<i, so the first value of i that will have effect is 1
		//for (i = 1; i < q.size(); i++){
		for (i = 1; i < q.size() && (q.get(i).ubVal > minValue || Globals.searchByLift); i++){	
			
			//System.out.println("q.get(i).ubVal: "+ q.get(i).ubVal + ", minValue: " +  minValue);
			
			int item = q.get(i).item;
			
			is = new ItemsetRec();
			
			is.add(item);
			
			Tidset newCover = new Tidset();
			newCover = Globals.tids.get(item);
			
			opus(is, newCover, newq, newCover.size(), 0);
			
			//DEBUG TODO
			if (i == 50){
				System.out.println("Stops here..");
			}
			System.out.println("Current item is: " + item);
			
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
