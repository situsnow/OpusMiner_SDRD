package opus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.PriorityQueue;

public class OpusMiner {

	public static PriorityQueue<ItemsetRec> itemsets = new PriorityQueue<ItemsetRec>(10, ItemsetRec.ItemsetRecComparator);
	
	//TODO: Attention. Change the type of argv to be fit for Java mechanism
	public static void print_header(PrintStream f, int argc, String[] argv){
		
		StringBuffer sb = new StringBuffer();
		sb.append("OPUS Miner: Filtered Top-k Association Discovery of Self-Sufficient Itemsets\n");
		sb.append("Version 1.2\n");
		sb.append("Copyright (C) 2012-2016 Geoffrey I Webb\n");
		sb.append("This program comes with ABSOLUTELY NO WARRANTY. This is free software, \n");
		sb.append("and you are welcome to redistribute it under certain conditions.\n");
		sb.append("See the GNU General Public Licence <http://www.gnu.org/licenses/> for details.\n");
		sb.append("\n");
		sb.append("If you publish results obtained by using this software please cite\n");
		sb.append("  Webb, G.I. & Vreeken, J. (2014) Efficient Discovery of the Most Interesting Associations.\n");
		sb.append("  ACM Transactions on Knowledge Discovery from Data. 8(3), Art. no. 15.\n");
		sb.append("\n");
		f.print(sb.toString());
		
		for (int i = 1; i < argc; i++){
			if (argv[i].charAt(0) == '-' && (argv[i].charAt(1) == 'k' || argv[i].charAt(1) == 's') && i < argc - 1){
				f.print(String.format("  %s %s\n", argv[i], argv[i+1]));
				++i;
			}else{
				f.print(String.format("  %s\n", argv[i]));
			}
		}
		
		//TODO discard f.close() here as for
		//System.out, there is no need to close it
		//For file PrintStream, it will close when it's not needed.
		//f.close();
	}
	
	public static void main(String []argv){
		
		String inputFileName = "";
		String outputFileName = "";
		String usageStr = "Usage: %s [-c] [-f] [-k <k>] [-l] [-p] [-r] [-s <consequent>] <input file> <output file>\n";
		ArrayList<ItemsetRec> is = new ArrayList<ItemsetRec>();
		
		PrintStream outf = null;
		int argc = argv.length;
		if (argc < 3){
			System.err.print(String.format(usageStr, argv[0]));
			System.exit(1);
		}
		
		print_header(System.out, argc, argv);
		
		int i;
		
		for (i = 1; i < argc; i++){
			if (argv[i].charAt(0) == '-'){
				switch (argv[i].charAt(1)){
				case 'c':
					Globals.printClosures = true;
					break;
				case 'f':
					Globals.filter = false;
					break;
				case 'k':
					//In case no space between command and defined 'k'
					if (argv[i].length() == 2){
						Globals.k = Utils.getNum(argv[++i]);
					}else{
						Globals.k = Utils.getNum(argv[i].substring(2, argv[i].length()));
					}
					break;
				case 'l':
					Globals.searchByLift = true;
					break;
				case 'p':
					// do not correct alpha for the size of the search space
					//Layered significance value if it's true
					Globals.correctionForMultCompare = false;
					break;
				case 'r':
					Globals.redundancyTests = false;
					break;
				case 's':
					//If it's for supervised descriptive rule discovery - contrast-sets mining
					//Attention here, it only accepts one specific consequent in single test and no space in consequent label
					Globals.sdrd = true;
					Globals.consequentName = argv[++i];
					break;
				default:
					System.err.println(String.format(usageStr, argv[0]));
					System.exit(1);
				}
			}else if ("".equals(inputFileName)){
				inputFileName = argv[i];
			}else if ("".equals(outputFileName)){
				outputFileName = argv[i];
			}else{
				System.err.println(String.format(usageStr, argv[0]));
				System.exit(1);
			}
		}
		
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date start_t = new Date();
		
		try{
			System.out.print(String.format("\nLoading data from %s\n", inputFileName));
			Load_Data.load_data(inputFileName);
			
			System.out.print(String.format("%d transactions, %d items\n", 
					Globals.noOfTransactions, Globals.noOfItems));
			
			outf = new PrintStream(new File(outputFileName));
			
			print_header(outf, argc, argv);
			
			outf.print(String.format("\n%s: %d items, %d transactions\n", inputFileName, 
					(long)Globals.noOfItems, (long)Globals.noOfTransactions));
			
			System.out.print("Finding itemsets\n");
			
			Date find_start_t = new Date();
			
			Find_Itemsets.find_itemsets();
			
			Date find_end_t = new Date();
			
			while (!OpusMiner.itemsets.isEmpty()){
				is.add(OpusMiner.itemsets.poll());
			}
			
			if (Globals.filter){
				System.out.print("Filtering itemsets\n");
				Filter_Itemsets.filter_itemsets(is);
			}
			
			Date print_start_t = new Date();
			
			long tm = print_start_t.getTime()-find_start_t.getTime();
			
			outf.print(String.format("Found %d non-redundant productive itemsets in %d seconds\n", (long)is.size(), tm));
			
			System.out.print("Printing itemsets\n");
			Print_Itemsets.print_itemsets(outf, is);
			
			Date end_t = new Date();
			
			long t = end_t.getTime() - start_t.getTime();
			
			System.out.print(String.format("%d seconds (%d input, %d search, %d filter, %d output)", 
					t, find_start_t.getTime() - start_t.getTime(), find_end_t.getTime() - find_start_t.getTime(),
					print_start_t.getTime() - find_end_t.getTime(), end_t.getTime() - print_start_t.getTime()));
			System.out.print(String.format(" for %d itemsets\n", (long)is.size()));
			
		} catch (FileNotFoundException ex){
			if (outf == null){
				System.err.print(String.format("Cannot open output file '%s'\n", outputFileName));
			}
		} catch (OutOfMemoryError ex){
			System.err.print("Out of memory\n");
		} catch (Exception ex){
			System.err.print("Unhandled exception\n");
			ex.printStackTrace(System.err);
		} finally{
			if (outf != null)
				outf.close();
		}
	}
}
