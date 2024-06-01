package seq;

import java.util.ArrayList;
import java.util.List;

public class ProfileSequenceAlignment {
	
	List<Character> alphabet;
	int seqCount;
	List<String> SAll;
	double[][] profile;
	Node[][] matrix;
	double miscost;
	double delcost;
	double incost;
	String toAlign;
	
	public static void main(String args[]) {
		String S1 = "HAL";
		String S2 = "SAA";
		String S3 = "-AL";
		
		List<String> SAll = new ArrayList<String>();
		
		SAll.add(S1);
		SAll.add(S2);
		SAll.add(S3);
		
		ProfileSequenceAlignment PSA = new ProfileSequenceAlignment(SAll.size(),SAll,1,1,1,"SL");
		
	}
	
	public ProfileSequenceAlignment(int seqCount, List<String> SAll, double miscost, double incost, double delcost, String toAlign) {
		this.seqCount = seqCount;
		this.SAll = SAll;
		this.miscost = miscost;
		this.incost = incost;
		this.delcost = delcost;
		this.toAlign = toAlign;
		this.alphabet = createAlphabet();
		this.profile = createProfile();
		System.out.println();
		this.matrix = createMatrix();
	}
	
	public double[][] createProfile() {
	
		double[][] profile = new double[alphabet.size()][SAll.get(0).length()];
		
		for(int x = 0; x < SAll.size(); x++) {
			for(int y = 0; y < SAll.get(x).length(); y++) {
				
				char curr = SAll.get(x).charAt(y);
				
				for(int z = 0; z < alphabet.size(); z++) {
					if(curr == alphabet.get(z)) {
						profile[z][y]++;
					}
				}
			}
		}
		
		for(int x = 0; x < profile.length; x++) {
			for(int y = 0; y < profile[0].length; y++) {
				profile[x][y] = profile[x][y]/3;
			}
		}
		
		printProfile(profile);
		return profile;
	}
	
	public Node[][] createMatrix() {
		
		Node[][] matrix = new Node[profile[0].length+1][toAlign.length()+1];
		
		matrix[0][0] = new Node(0,"end");
		
		for(int x = 1; x < matrix[0].length; x++) {
			matrix[0][x] = new Node(matrix[0][x-1].getCost() + incost, "side");
		}
		
		printMatrix(matrix);
		return matrix;
	}
	
	public List<Character> createAlphabet() {
		
		List<Character> alphabet = new ArrayList<>();
		
		for(int x = 0; x < SAll.size(); x++) {
			
			String curr = SAll.get(x);
			
			for(int y = 0; y < curr.length(); y++) {

				if(alphabet.size() == 0) {
					alphabet.add(curr.charAt(y));
				}
				else {
					
					boolean inside = false;
					
					for(int z = 0; z < alphabet.size(); z++) {
						
						char currChar = alphabet.get(z);
						
						if(currChar == curr.charAt(y)) {
							inside = true;
							break;
						}
					}
					if(!inside) {
						alphabet.add(curr.charAt(y));
					}
				}
			}
		}
		return alphabet;
	}

	// Ausgabefunktion
	public void printProfile(double[][] matrix) {

		for (int x = 0; x < matrix.length; x++) {
			System.out.print(alphabet.get(x) + "|");
			for (int y = 0; y < matrix[0].length; y++) {
				System.out.print( Math.round(matrix[x][y]*100.0)/100.0 + "|");
			}
			System.out.println();
		}
	}
	
	public void printMatrix(Node[][] matrix) {
		for (int x = 0; x < matrix.length; x++) {
			System.out.print("|");
			for (int y = 0; y < matrix[0].length; y++) {
				
				if(matrix[x][y] == null) {
					System.out.print("?|");
				}
				else {
					System.out.print(matrix[x][y].getCost() + "|");
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
