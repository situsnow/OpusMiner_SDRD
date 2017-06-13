package opus;

import java.util.Comparator;

public class ItemQElem {

	public float ubVal;
	public int item;
	
	public ItemQElem(float ubVal, int item){
		this.ubVal = ubVal;
		this.item = item;
	}
	
	public static Comparator<ItemQElem> ItemsQElemComparator = new Comparator<ItemQElem>(){

		@Override
		public int compare(ItemQElem o1, ItemQElem o2) {
			// TODO Auto-generated method stub
			if (o1.ubVal < o2.ubVal)
				return -1;
			if (o1.ubVal > o2.ubVal)
				return 1;
			return 0;
		}
	};
}
