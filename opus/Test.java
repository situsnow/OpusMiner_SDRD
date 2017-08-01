package opus;

import java.util.Collections;

public class Test {

	public static void main(String[] args) {

		Itemset test = new Itemset();
		
		test.add(-1);
		test.add(70);
		test.add(11);
		test.add(69);
		
		Itemset test2 = new Itemset();
		
		test2.add(-1);
		test2.add(70);
		test2.add(11);
		test2.add(69);
		
		Collections.sort(test);
		
		
		for(int i: test){
			System.out.println(i);
		}
	}

}
