package opus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Itemset extends ArrayList<Integer> {

	private static final long serialVersionUID = 1L;
	
	//Copy another array list
	public Itemset(Itemset itemset){
		this.addAll(itemset);
	}
	
	//Default constructor
	public Itemset(){
		
	}
	
	@Override
	public boolean add(Integer i){
		if (this.contains(i)){
			return false;
		}else{
			return super.add(i);
		}
	}
	
	@Override
	public void add(int index, Integer i){
		if (this.contains(i)){
			return;
		}else{
			super.add(index, i);
		}
	}
	
	@Override
	public boolean addAll(Collection<? extends Integer> is){
		Itemset copy = (Itemset) is;
		copy.removeAll(this);
		return super.addAll(copy);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends Integer> is){
		Itemset copy = (Itemset) is;
		copy.removeAll(this);
		return super.addAll(index, copy);
	}
	
}
