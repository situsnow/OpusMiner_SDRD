package opus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Test {

	public static void main(String[] args) {
		String c;
		
		int total = 0;
		
		String filename = "File/Input/Adult.csv";
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(filename));
			
			//File cannot open exception handle in catch section.
			while((c = fileReader.readLine()) != null){
				
				if (!"".equals(c)){
					//String[] items = c.split(",");
					
//					for (int i = 0; i < items.length; i++){
//						System.out.print(items[i] + ", ");
//					}
					total++;
				}
			}
			
			
			System.out.println(total);
		} catch (FileNotFoundException e) {

			if (fileReader == null){
				System.out.print(String.format("Cannot open input file '%s'\n", filename));
				System.exit(1);;
			}
			e.printStackTrace();
		}  catch (IOException e) {

			e.printStackTrace();
		}
	}

}
