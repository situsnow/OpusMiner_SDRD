package opus;

import java.util.ArrayList;
import java.util.Collections;

public class Itemset extends ArrayList<Integer> {

	private static final long serialVersionUID = 1L;
	
	//Copy another array list
	public Itemset(Itemset itemset){
		Collections.copy(this, itemset);
	}
	
	//Default constructor
	public Itemset(){
		
	}

}
