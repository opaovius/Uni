package seq;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProgressiveAlignment {
	
	Map<String,double[][]> allProfiles;
	double incost;
	double delcost;
	double miscost;

	public static void main(String[] args) {

		List<String> commands = new ArrayList<>();
		Map<String,String> sequenzen = new HashMap<>();

		try {
			File reihe = new File("src/seq/reihenfolge.txt");
			Scanner reihenfolgeScanner = new Scanner(reihe);

			while (reihenfolgeScanner.hasNext()) {
				String line = reihenfolgeScanner.next();
				commands.add(line);
			}

			reihenfolgeScanner.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Reihenfolgedatei nicht gefunden!");
		}
		
		try {
			File seqs = new File("src/seq/sequenzen.fa");
			Scanner sequenzenScanner = new Scanner(seqs);
			
			while(sequenzenScanner.hasNext()) {
				String key = sequenzenScanner.next();
				String value = sequenzenScanner.next();
				key = key.substring(1);
				sequenzen.put(key, value);
			}
			
			sequenzenScanner.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("Sequenzdatei nicht gefunden!");
		}
		
		ProgressiveAlignment PA = new ProgressiveAlignment(1.0,1.0,1.0);
		PA.runCommands(commands, sequenzen);
		
	}
	
	public ProgressiveAlignment(double incost, double delcost, double miscost) {
		this.incost = incost;
		this.delcost = delcost;
		this.miscost = miscost;
	}
	
	public void runCommands(List<String> commands, Map<String,String> sequens) {
		
		for(String com : commands) {
			
			String operantOne = "";
			String operantTwo = "";
			String result = "";
			boolean first = true;
			boolean second = true;
			
			
			for(int x = 0; x < com.length(); x++) {
				Character curr = com.charAt(x);
				
				if(first) {
					if(curr == '+') {
						first = false;
					}
					else {
						operantOne = operantOne + curr;
					}
				}
				else if(second) {
					if(curr == '=') {
						second = false;
					}
					else {
						operantTwo = operantTwo + curr;
					}
				}
				else {
					result = result + curr;
				}	
			}
			
			if(operantOne.charAt(0) == 'S' && operantTwo.charAt(0) == 'S') {
				//align(S,S,result);
			}
			else if(operantOne.charAt(0) == 'S') {
				//align(S,P,result);
			}
			else {
				//align(P,P,result);
			}
		}
		
	}

}

class Node {
	
	double cost;
	String tag;
	
	public Node(double cost, String tag) {
		this.cost = cost;
		this.tag = tag;
	}
	
	public void setCost(int cost){
		this.cost = cost;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public double getCost() {
		return cost;
	}
	
	public String getTag() {
		return tag;
	}
	
}