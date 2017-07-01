package opus;

import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) {

		ItemsetRec is1 = new ItemsetRec();
		is1.add(1);
		is1.add(2);
		is1.count = 10;
		is1.value = 10;
		
		
		ItemsetRec is2 = new ItemsetRec();
		is2.add(1);
		is2.add(2);
		
		Map<Itemset, Integer> TIDCount = new HashMap<Itemset, Integer>();
		
		TIDCount.put(is1, 1);
		
		System.out.println(TIDCount.containsKey(is1));
		System.out.println(TIDCount.containsKey(is2));
	}

}
