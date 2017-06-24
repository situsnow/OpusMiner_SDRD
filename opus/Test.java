package opus;

import java.util.PriorityQueue;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PriorityQueue<ItemsetRec> test = new PriorityQueue<ItemsetRec>(10, ItemsetRec.ItemsetRecComparator);
		
		for(int i = 0; i< 8; i++){
			ItemsetRec isr = new ItemsetRec();
			isr.value = i;
			
			test.add(isr);
		}
		
		while(test.size() > 0){
			System.out.println(test.poll().value);
		}
		
	}

}
