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
		
		PA.createSequenceSequenceAlignment("GAAGGG", "AGATGGT");
		
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
	
	public Node[][] createSequenceSequenceMatrix(String S1, String S2) {
		
		Node[][] matrix = new Node[S2.length()+1][S1.length()+1];
		
		//(0,0) ist immer 0
		matrix[0][0] = new Node(0.0,"none");
		
		//Spalte 0 auffuelen
		for(int x = 1; x < matrix.length; x++) {
			matrix[x][0] = new Node(matrix[x-1][0].getCost() + incost, "top");
		}
		
		//Zeile 0 auffuelen
		for(int x = 1; x < matrix[0].length; x++) {
			matrix[0][x] = new Node(matrix[0][x-1].getCost() + delcost, "side");
		}
		
		//Rest auffuellen
		for(int x = 1; x < matrix.length; x++) {
			for(int y = 1; y < matrix[0].length; y++) {
				
				Node topNode = new Node(matrix[x-1][y].getCost() + incost, "top");
				Node sideNode = new Node(matrix[x][y-1].getCost() + delcost, "side");
				Node diaNode;
				
				if(S2.charAt(x-1) == S1.charAt(y-1)) {
					diaNode = new Node(matrix[x-1][y-1].getCost(),"dia");
				}
				else {
					diaNode = new Node(matrix[x-1][y-1].getCost() + miscost, "dia");
				}
				
				//Minimum der drei Zahlen bestimmen
				matrix[x][y] = min(topNode,sideNode,diaNode);
			}
		}
		return matrix;
	}
	
	public List<String> createSequenceSequenceAlignment(String string1, String string2) {
		
		List<String> alignments = new ArrayList<>();
		
		String alignedString1 = "";
		String alignedString2 = "";
		
		Node[][] matrix = createSequenceSequenceMatrix(string1,string2);
		
		printMatrix(matrix);
		
		int x = matrix.length;
		int y = matrix[0].length;
	
		string1 = "-" + string1;
		string2 = "-" + string2;
		
		while(x != 1 && y != 1) {
			
			Node currNode = matrix[x-1][y-1];
			
			if(currNode.getTag().equals("dia")) {
				alignedString1 = string1.charAt(y-1) + alignedString1;
				alignedString2 = string2.charAt(x-1) + alignedString2;
				x--;
				y--;
			}
			else if(currNode.getTag().equals("top")) {
				alignedString1 = string1.charAt(y-1) + alignedString1;
				alignedString2 = "-" + alignedString2;
				y--;
			}
			else {
				alignedString1 = "-" + alignedString1;
				alignedString2 = string2.charAt(x-1) + alignedString2;
				x--;
			}
			
			if(x == 1) {
				while(y != 0) {
					alignedString1 = string1.charAt(y-1) + alignedString1;
					alignedString2 = "-" + alignedString2;
					y--;
				}
			}
			
			if(y == 1) {
				while(x != 0) {
					alignedString1 = "-" + alignedString1;
					alignedString2 = string2.charAt(x-1) + alignedString2;
					x--;	
				}
			}
			
			//System.out.println(alignedString1);
			//System.out.println(alignedString2);
			
		}
		
		alignedString1 = alignedString1.substring(1);
		alignedString2 = alignedString2.substring(1);
		
		
		System.out.println("alignedS1: " + alignedString1);
		System.out.println("alignedS2: " + alignedString2);
		
		alignments.add(alignedString1);
		alignments.add(alignedString2);
		
		return alignments;
	}
	
	public Node min(Node a, Node b, Node c) {
		if (a.getCost() < b.getCost() && a.getCost() < c.getCost()) {
			return a;
		} else if (b.getCost() < a.getCost() && b.getCost() < c.getCost()) {
			return b;
		} else {
			return c;
		}
	}
	
	public void printMatrix(Node[][] matrix) {
		for (int x = 0; x < matrix.length; x++) {
			System.out.print("|");
			for (int y = 0; y < matrix[0].length; y++) {

				if (matrix[x][y] == null) {
					System.out.print("?|");
				} else {
					System.out.print(Math.round(matrix[x][y].getCost()*100)/100 + "|");
				}
			}
			System.out.println();
		}
		System.out.println();
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