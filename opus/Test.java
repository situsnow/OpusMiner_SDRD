package opus;

import java.util.Collections;

public class Test {

	public static void main(String[] args) {

		ItemQClass iqc = new ItemQClass();
		
		//iqc.add(ubval, item);
		iqc.add(3, 3);
		iqc.add(1, 1);
		iqc.add(4, 4);
		iqc.add(2, 2);
		
		iqc.sort();
		Collections.reverse(iqc);
		iqc.add(1, -1);
		Collections.reverse(iqc);
		
		for(int i = 0; i < iqc.size(); i++){
			System.out.println("item: " + iqc.get(i).item + ", ubval: " + iqc.get(i).ubVal);
		}
	}

}
