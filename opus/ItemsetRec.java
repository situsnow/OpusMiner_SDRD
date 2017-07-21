package opus;

import java.util.Comparator;

public class ItemsetRec extends Itemset{

	public static final long serialVersionUID = 1L;
	public int count;
	public float value;
	public double p;
	public boolean selfSufficient;
	public float leverage;
	public float lift;
	public float antSup;
	//strength = sup(rule)/sup(ant), which is equal to confidence
	public float strength;
	
	public ItemsetRec(int count, float value, double p, boolean selfSufficient, 
			float leverage, float lift, float antSup, float strength){
		this.count = count;
		this.value = value;
		this.p = p;
		this.selfSufficient = selfSufficient;
		this.leverage = leverage;
		this.lift = lift;
		this.antSup = antSup;
		this.strength = strength;
	}
	
	public ItemsetRec(){
		//By default
		this.count = 0;
		this.value = (float)0.0;
		this.p = 1.0;
		this.selfSufficient = true;
	}

	public boolean lessThan(ItemsetRec pI){
		return this.value > pI.value;
	}
	
	//Sort the ItemsetRec with ascending order, when there's bigger values, will remove the smallest one in peek
	public static Comparator<ItemsetRec> ItemsetRecComparatorA = new Comparator<ItemsetRec>(){

		@Override
		public int compare(ItemsetRec o1, ItemsetRec o2) {

			if (o1.value < o2.value)
				return -1;
			if (o1.value > o2.value)
				return 1;
			return 0;
		}
	};
	//Sort the ItemsetRec with descending order when printing
	public static Comparator<ItemsetRec> ItemsetRecComparatorD = new Comparator<ItemsetRec>(){

		@Override
		public int compare(ItemsetRec o1, ItemsetRec o2) {
			if (o1.value > o2.value)
				return -1;
			if (o1.value < o2.value)
				return 1;
			return 0;
		}
	};
	
	public static Comparator<ItemsetRec> ItemsetRecSizeComparator = new Comparator<ItemsetRec>(){
		@Override
		public int compare(ItemsetRec o1, ItemsetRec o2) {
			if (o1.size() > o2.size())
				return -1;
			if (o1.size() < o2.size())
				return 1;
			return 0;
		}
	};
	
}
