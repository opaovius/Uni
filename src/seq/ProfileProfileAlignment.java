package seq;

import java.util.ArrayList;
import java.util.List;

public class ProfileProfileAlignment {
	
	double incost;
	double delcost;
	double miscost;
	double[][] P1;
	double[][] P2;
	Node[][] matrix;
	double seqCountP1;
	double seqCountP2;
	List<Character> alphabetP1;
	List<Character> alphabetP2;
	
	
	public static void main(String args[]) {
		String P1S1 = "B-A";
		String P1S2 = "A-A";
		String P1S3 = "BAA";
		String P1S4 = "BAA";
		
		String P2S1 = "BA";
		String P2S2 = "BB";
		
		List<String> SP1 = new ArrayList<String>();
		List<String> SP2 = new ArrayList<String>();
		
		SP1.add(P1S1);
		SP1.add(P1S2);
		SP1.add(P1S3);
		SP1.add(P1S4);
		
		SP2.add(P2S1);
		SP2.add(P2S2);
		
		ProfileProfileAlignment PPA = new ProfileProfileAlignment(SP1,SP2,1,1,1);
		
	}
	
	public ProfileProfileAlignment(List<String> SP1, List<String> SP2, double incost, double delcost, double miscost) {
		this.incost = incost;
		this.delcost = delcost;
		this.miscost = miscost;
		this.seqCountP1 = SP1.size();
		this.seqCountP2 = SP2.size();
		this.alphabetP1 = createAlphabet(SP1);
		this.alphabetP2 = createAlphabet(SP2);
		System.out.println(alphabetP1);
		System.out.println("Profil 1:");
		this.P1 = createProfile(SP1,alphabetP1,seqCountP1);
		System.out.println();
		System.out.println(alphabetP2);
		System.out.println("Profil 2:");
		this.P2 = createProfile(SP2,alphabetP2,seqCountP2);
		System.out.println();
		System.out.println("Kostenmatrix:");
		this.matrix = createMatrix(P1,P2);
	}
	
	public Node[][] createMatrix(double[][] P1, double[][] P2){
		Node[][] matrix = new Node[P1[0].length+1][P2[0].length+1];
		
		
		printMatrix(matrix);
		return matrix;
	}

	public List<Character> createAlphabet(List<String> SAll) {
		
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
	
	public double[][] createProfile(List<String> SAll, List<Character> alphabet,double seqCount) {
		
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
				profile[x][y] = profile[x][y]/seqCount;
			}
		}
		
		printProfile(profile,alphabet);
		return profile;
	}
	
		// Ausgabefunktion
		public void printProfile(double[][] matrix,List<Character> alphabet) {

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
						System.out.print(Math.round(matrix[x][y].getCost()*100.0)/100.0+ "|");
					}
				}
				System.out.println();
			}
			System.out.println();
		}

	
}
