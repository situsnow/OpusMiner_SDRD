package opus;

import java.util.ArrayList;
import java.util.Collections;

public class ItemQClass extends ArrayList<ItemQElem>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void append(float ubVal, int item){
		int initialSize = this.size();
		
		this.ensureCapacity(initialSize + 1);
		
		ItemQElem tmp = new ItemQElem(ubVal, item);
		this.add(initialSize, tmp);
	}
	
	public void sort(){
		
		if (this.size() > 0 && this.get(0).item == Globals.consequentID){
			//Ensure the consequent id will be in front
			ItemQElem conItem = this.get(0);
			this.remove(0);
			Collections.sort(this, ItemQElem.ItemsQElemComparator);
			Collections.reverse(this);
			this.add(conItem);
			Collections.reverse(this);
		}else{
			Collections.sort(this, ItemQElem.ItemsQElemComparator);
		}
	}
//	public void sortSDRD(){
//		Collections.sort(this, ItemQElem.ItemQElemSDRDComparator);
//	}
	
	
	public void add(float ubVal, int item){
//		int initialSize = this.size();
//		
//		this.ensureCapacity(initialSize + 1);
//		
//		ItemQElem itemQElem = new ItemQElem(ubVal, item);
//		
//		if (initialSize == 0){
//			this.add(itemQElem);
//		}else{
//			int first = 0;
//			int last = initialSize - 1;
//			
//			//Find the location for current itemQElem in order to make the final ItemQClass still in order
//			while (first < last){
//				int mid = first + (last - first) / 2;
//				if (ubVal <= this.get(mid).ubVal){
//					first = mid + 1;
//				}else{
//					last = mid;
//				}
//			}
//			
//			if (this.get(first).ubVal >= ubVal){
//				// this should only happen if all items in the queue have lower value than the new item
//				first ++;
//			}
//			
//			for (last = initialSize; last > first; last--){
//				this.set(last, this.get(last - 1));
//			}
//			
//			super.add(first, itemQElem);
//		}
		ItemQElem iqe = new ItemQElem(ubVal, item);
		this.add(iqe);
		this.sort();
	}
}
