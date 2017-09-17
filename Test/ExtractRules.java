package Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ExtractRules {
	
	public static int size;
	
	public static void main(String[] args){
		/**
		 * OpusMinder
		 */
		//OpusMiner_Leverage.csv
		String opusOutput = "File/Adult/Exp-LE50K/Output_Lift500.csv";
		//String opusSorted = "File/Adult/Exp-LE50K/Output_Lift_Layered-Sorted.csv";
		
		Map<Integer, ArrayList<OpusRules>> opusRuleMap = ExtractRules.readOpusResult(opusOutput);
		
		//ExtractRules.writeOpusResult(opusSorted, ruleMap);
		
		ArrayList<OpusRules> opusRule1 = opusRuleMap.get(1);
		ArrayList<OpusRules> opusRule2 = opusRuleMap.get(2);
				
		/**
		 * BigML
		 */
		String bigMLOutput = "File/Adult/Exp-LE50K/BigML-Lift.csv";
		//String bigMLSorted = "File/Adult/Exp-LE50K/BigML-Association-SearchByLift-Sorted.csv";
		
		ArrayList<BigMLRules> bigMLRuleList = ExtractRules.readBigMLResult(bigMLOutput);
		
		//ExtractRules.writeBigMLResult(bigMLSorted, bigMLRuleList);
		
		
		
		System.out.print("Self-sufficiency rule in BIGML:\n");
		for (OpusRules opusR : opusRule1){
			for (int i = 0; i < bigMLRuleList.size(); i++){
				BigMLRules bigMLR = bigMLRuleList.get(i);
				
				String antR = opusR.getRule().substring(8);
				
				if (bigMLR.getAnt().equals(antR)){
					System.out.println("Index: " + i + ", " + bigMLR.getAnt());
				}
			}
		}
		
		System.out.print("Non-Self-sufficiency rule in BIGML:\n");
		for (OpusRules opusR : opusRule2){
			for (int i = 0; i < bigMLRuleList.size(); i++){
				BigMLRules bigMLR = bigMLRuleList.get(i);
				String antR = opusR.getRule().substring(8);
				if (bigMLR.getAnt().equals(antR)){
					System.out.println("Index: " + i + ", " + bigMLR.getAnt());
				}
			}
		}
		
		/**
		 * Apriori
		 */
//		String aprioriOutput = "File/Adult/Exp-LE50K/WEKA-PredictiveApriori.csv";
//		ArrayList<AprioriRules> aprioriRuleList = ExtractRules.readAprioriResult(aprioriOutput);
//		System.out.print("Self-sufficiency rule in Apriori:\n");
//		for (OpusRules opusR : opusRule1){
//			for (int i = 0; i < aprioriRuleList.size(); i++){
//				AprioriRules aprioriR = aprioriRuleList.get(i);
//				
//				String antR = opusR.getRule().substring(8);
//				
//				if (aprioriR.getAntecedent().equals(antR)){
//					System.out.println("Index: " + i + ", " + aprioriR.getAntecedent());
//				}
//			}
//		}
//		
//		System.out.print("Non-Self-sufficiency rule in Apriori:\n");
//		for (OpusRules opusR : opusRule2){
//			for (int i = 0; i < aprioriRuleList.size(); i++){
//				AprioriRules aprioriR = aprioriRuleList.get(i);
//				String antR = opusR.getRule().substring(7);
//				if (aprioriR.getAntecedent().equals(antR)){
//					System.out.println("Index: " + i + ", " + aprioriR.getAntecedent());
//				}
//			}
//		}
		
		
		
	}
	
	public static Map<Integer, ArrayList<OpusRules>> readOpusResult(String opusOutput){
		
		//read files
		BufferedReader fileReader = null;
		String c;
		try {
			boolean flag = false;
			fileReader = new BufferedReader(new FileReader(opusOutput));
			
			ArrayList<OpusRules> ruleList = new ArrayList<OpusRules>();
			ArrayList<OpusRules> filteredRuleList = new ArrayList<OpusRules>();
			
			//skip first line
			c = fileReader.readLine();
			while ((c = fileReader.readLine()) != null){
				String[] cells = c.split(",");
				
				if (cells != null){
					OpusRules rule = new OpusRules();
					if (cells.length == 7){
						rule.setRule(handleOpusRuleStr(cells[0]));
						rule.setRuleSize(ExtractRules.size);
						rule.setNumOfTran(Integer.parseInt(cells[1]));
						rule.setLeverage(Float.parseFloat(cells[2]));
						rule.setLift(Float.parseFloat(cells[3]));
						rule.setpVal(Double.parseDouble(cells[4]));
						rule.setAntSup(Float.parseFloat(cells[5]));
						rule.setStrength(Float.parseFloat(cells[6]));
						
					}else{
						flag = true;
						continue;
					}
					
					if (flag){
						filteredRuleList.add(rule);
					}else{
						ruleList.add(rule);
					}
				}
				
				
			}
			
			Map<Integer, ArrayList<OpusRules>> result = new HashMap<Integer, ArrayList<OpusRules>>();
			result.put(1, ruleList);
			result.put(2, filteredRuleList);
			return result;
		} catch (FileNotFoundException e) {

			if (fileReader == null){
				System.out.print(String.format("Cannot open input file '%s'\n", opusOutput));
				System.exit(1);;
			}
			e.printStackTrace();
			return null;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		} finally{
			if (fileReader != null){
				try {
					fileReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeOpusResult(String opusSorted, Map<Integer, ArrayList<OpusRules>> ruleMap){
		
		BufferedWriter bw = null;
		
		try{
			File file = new File(opusSorted);
			if (!file.exists()){
				file.createNewFile();
			}
			
			
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			
			StringBuffer sb = new StringBuffer();
			sb.append("Rule Size,").append("Rule,").append("No. of transactions,").append("Leverage,").
			append("Lift,").append("P-value,").append("Antecedent Support,").append("Strength\n");
			
			bw.write(sb.toString());
			
			ArrayList<OpusRules> rule = null;
			
			rule = ruleMap.get(1);
			
			bw.write(generateOpusContent(rule, false));
			
			rule = ruleMap.get(2);
			sb = new StringBuffer();
			sb.append(rule.size()).append(" timesets failed test for self sufficiency\n");
			bw.write(sb.toString());
			
			bw.write(generateOpusContent(rule, true));
			
		} catch (IOException ex){
			ex.printStackTrace();
		} finally{
			if (bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static String generateOpusContent(ArrayList<OpusRules> rule, boolean flag){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rule.size(); i ++){
			
			sb.append(rule.get(i).getRuleSize());
			sb.append(",");
			sb.append(rule.get(i).getRule());
			sb.append(",");
			sb.append(rule.get(i).getNumOfTran());
			sb.append(",");
			sb.append(rule.get(i).getLeverage());
			sb.append(",");
			sb.append(rule.get(i).getLift());
			sb.append(",");
			sb.append(rule.get(i).getpVal());
			sb.append(",");
			sb.append(rule.get(i).getAntSup());
			sb.append(",");
			sb.append(rule.get(i).getStrength());
			if (flag){
				sb.append(",Not-Self-Sufficient");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static String handleOpusRuleStr(String rule){
		
		String[] items = rule.split("&");
		
		ExtractRules.size = items.length;
		
		StringBuffer sb = new StringBuffer();
		sb.append(items[0].trim());
		
		Map<Integer, String> fieldMap = new HashMap<Integer, String>();
		for (int i = 1; i < items.length; i++){
			String item = items[i].trim();
			int index = Integer.parseInt(item.substring(5, item.indexOf(" = ")));
			
			fieldMap.put(index, item);
		}
		
		SortedSet<Integer> keys = new TreeSet<Integer>(fieldMap.keySet());
		for (int key : keys){
			sb.append(" & ").append(fieldMap.get(key));
		}
		
		return sb.toString();
	}
	
	public static String handleBigMLRuleStr(String rule){
		
		String[] items = rule.split("&");
		
		ExtractRules.size = items.length;
		
		StringBuffer sb = new StringBuffer();
		
		Map<Integer, String> fieldMap = new HashMap<Integer, String>();
		for (int i = 0; i < items.length; i++){
			String item = items[i].trim();
			item = item.replace("\"", "");
			int index = Integer.parseInt(item.substring(5, item.indexOf(" = ")));
			
			fieldMap.put(index, item);
		}
		
		SortedSet<Integer> keys = new TreeSet<Integer>(fieldMap.keySet());
		int i = 0;
		for (int key : keys){
			sb.append(fieldMap.get(key));
			i ++;
			if (i < keys.size()){
				sb.append(" & ");
			}
		}
		
		return sb.toString();
	}

	
	public static ArrayList<BigMLRules> readBigMLResult(String bigMLOutput){
		
		//read files
		BufferedReader fileReader = null;
		String c;
		try {
			fileReader = new BufferedReader(new FileReader(bigMLOutput));
			
			ArrayList<BigMLRules> ruleList = new ArrayList<BigMLRules>();
			//Filter header
			c = fileReader.readLine();
			while ((c = fileReader.readLine()) != null){
				String[] cells = c.split(",");
				
				if (cells != null){
					BigMLRules rule = new BigMLRules();
					rule.setRuleID(cells[0]);
					rule.setAnt(handleBigMLRuleStr(cells[1]));
					rule.setRuleSize(ExtractRules.size + 1);
					rule.setCon(cells[2]);
					rule.setAntCovP(Float.parseFloat(cells[3]));
					rule.setAntCov(Integer.parseInt(cells[4]));
					rule.setSupP(Float.parseFloat(cells[5]));
					
					rule.setSup(Integer.parseInt(cells[6]));
					rule.setConfidence(Float.parseFloat(cells[7]));
					rule.setLeverage(Float.parseFloat(cells[8]));
					rule.setLift(Float.parseFloat(cells[9]));
					rule.setpVal(Double.parseDouble(cells[10]));
					rule.setConCovP(Float.parseFloat(cells[11]));
					rule.setConCov(Integer.parseInt(cells[12]));
					
					ruleList.add(rule);
				}
				
				
			}
			
			return ruleList;
		} catch (FileNotFoundException e) {

			if (fileReader == null){
				System.out.print(String.format("Cannot open input file '%s'\n", bigMLOutput));
				System.exit(1);;
			}
			e.printStackTrace();
			return null;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		} finally{
			if (fileReader != null){
				try {
					fileReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeBigMLResult(String bigMLSorted, ArrayList<BigMLRules> ruleList){
		
		BufferedWriter bw = null;
		
		try{
			File file = new File(bigMLSorted);
			if (!file.exists()){
				file.createNewFile();
			}
			
			
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			
			StringBuffer sb = new StringBuffer();
			sb.append("Rule Size,").append("Rule ID,").append("Antecedent,").append("Consequent,").append("Antecedent Coverage %,").
			append("Antecedent Coverage,").append("Support %,").append("Support,").append("Confidence,")
			.append("Leverage,").append("Lift,").append("p-value,").append("Consequent Coverage %,").append("Consequent Coverage\n");
			
			bw.write(sb.toString());
			
			bw.write(generateBigMLContent(ruleList));
			
			
		} catch (IOException ex){
			ex.printStackTrace();
		} finally{
			if (bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static String generateBigMLContent(ArrayList<BigMLRules> rule){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rule.size(); i ++){
			
			sb.append(rule.get(i).getRuleSize());
			sb.append(",");
			sb.append(rule.get(i).getRuleID());
			sb.append(",");
			sb.append(rule.get(i).getAnt());
			sb.append(",");
			sb.append(rule.get(i).getCon());
			sb.append(",");
			sb.append(rule.get(i).getAntCovP());
			sb.append(",");
			sb.append(rule.get(i).getAntCov());
			sb.append(",");
			sb.append(rule.get(i).getSupP());
			sb.append(",");
			sb.append(rule.get(i).getSup());
			sb.append(",");
			sb.append(rule.get(i).getCon());
			sb.append(",");
			sb.append(rule.get(i).getLeverage());
			sb.append(",");
			sb.append(rule.get(i).getLift());
			sb.append(",");
			sb.append(rule.get(i).getpVal());
			sb.append(",");
			sb.append(rule.get(i).getConCovP());
			sb.append(",");
			sb.append(rule.get(i).getConCov());
			sb.append("\n");
		}
		return sb.toString();
	}


	public static ArrayList<AprioriRules> readAprioriResult(String aprioriOutput){

		//read files
		BufferedReader fileReader = null;
		String c;
		try {
			fileReader = new BufferedReader(new FileReader(aprioriOutput));
			
			ArrayList<AprioriRules> ruleList = new ArrayList<AprioriRules>();
			//Filter header
			c = fileReader.readLine();
			while ((c = fileReader.readLine()) != null){
				String[] cells = c.split(",");
				
				if (cells != null){
					AprioriRules rule = new AprioriRules();
					rule.setIndex(Integer.parseInt(cells[0]));
					rule.setAntecedent(cells[1]);
					rule.setAnt_cov(Integer.parseInt(cells[2]));
					rule.setConsequent(cells[3]);
					rule.setCon_cov(Integer.parseInt(cells[4]));
					rule.setConfidence(Float.parseFloat(cells[5]));
					
					ruleList.add(rule);
				}
				
				
			}
			
			return ruleList;
		} catch (FileNotFoundException e) {

			if (fileReader == null){
				System.out.print(String.format("Cannot open input file '%s'\n", aprioriOutput));
				System.exit(1);;
			}
			e.printStackTrace();
			return null;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		} finally{
			if (fileReader != null){
				try {
					fileReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}



















