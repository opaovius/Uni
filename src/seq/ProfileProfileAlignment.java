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
	List<Character> alphabet;
	
	
	public static void main(String[] args) {
		
		double incost = Double.parseDouble(args[0]);
		double delcost = Double.parseDouble(args[1]);
		double miscost = Double.parseDouble(args[2]);
		List<String> SP1 = new ArrayList<>();
		List<String> SP2 = new ArrayList<>();
		
		
		int x = 3;
		String curr = args[x];
		
		boolean split = false;
		
		while(x < args.length) {
			if(!split) {
				SP1.add(curr);
				x++;
				curr = args[x];
			}
			else {
				SP2.add(curr);
				x++;
				if(x < args.length) {
					curr = args[x];
				}
			}
			
			if(curr.equals("$")) {
				split = true;
				x++;
				curr = args[x];
			}
		}
		
		ProfileProfileAlignment PPA = new ProfileProfileAlignment(SP1,SP2,incost,delcost,miscost);
		System.out.println("Alignment:");
		PPA.createAlignment();
		
	}
	
	
	/*
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
		System.out.println("Alignment:");
		PPA.createAlignment();
		
	}
	*/
	public ProfileProfileAlignment(List<String> SP1, List<String> SP2, double incost, double delcost, double miscost) {
		this.incost = incost;
		this.delcost = delcost;
		this.miscost = miscost;
		this.seqCountP1 = SP1.size();
		this.seqCountP2 = SP2.size();
		this.alphabetP1 = createAlphabet(SP1);
		this.alphabetP2 = createAlphabet(SP2);
		this.alphabetP1 = checkGap(alphabetP1);
		this.alphabetP2 = checkGap(alphabetP2);
		this.alphabet = combineAlphabets(alphabetP1,alphabetP2);
		System.out.println("Profil 1:");
		this.P1 = createProfile(SP1,seqCountP1);
		System.out.println();
		System.out.println("Profil 2:");
		this.P2 = createProfile(SP2,seqCountP2);
		System.out.println();
		this.matrix = createMatrix(P1,P2);
	}
	
	public Node[][] createMatrix(double[][] P1, double[][] P2){
		
		Node[][] matrix = new Node[P1[0].length+1][P2[0].length+1];
		
		
		matrix[0][0] = new Node(0.0,"start");
		
		// D(j,0) = D(j-1,0) + kosten
		for (int x = 1; x < matrix.length; x++) {

			double cost = 0.0;

			for (int a = 0; a < alphabet.size(); a++) {
				if ('-' == alphabet.get(a)) {
					cost = cost + 0.0;
				} else {
					cost = cost + delcost * P1[a][x - 1];
				}
			}
			matrix[x][0] = new Node(matrix[x - 1][0].getCost() + cost, "top");
		}

		// D(0,i) = D(0,i.1) + kosten
		for (int y = 1; y < matrix[0].length; y++) {

			double cost = 0.0;

			for (int a = 0; a < alphabet.size(); a++) {
				if ('-' == alphabet.get(a)) {
					cost = cost + 0.0;
				} else {
					cost = cost + delcost * P2[a][y - 1];
				}
			}
			matrix[0][y] = new Node(matrix[0][y-1].getCost() + cost, "side");
		}
		
		// Rest
		for (int x = 1; x < matrix.length; x++) {
			for (int y = 1; y < matrix[0].length; y++) {
				
				double cost = 0.0;

				for (int a = 0; a < alphabet.size(); a++) {
					if ('-' == alphabet.get(a)) {
						cost = cost + 0.0;
					} else {
						cost = cost + delcost * P2[a][y - 1];
					}
				}

				Node sideCost = new Node(matrix[x][y-1].getCost() + cost, "side");

				cost = 0.0;
				
				for(int a = 0; a < alphabet.size(); a++) {
					if('-' == alphabet.get(a)) {
						cost = cost + 0.0;
					}
					else {
						cost = cost + incost*P1[a][x-1];
					}
				}
				
				Node topCost = new Node(matrix[x-1][y].getCost() + cost,"top");
				
				cost = 0.0;
				
				for(int a = 0; a < alphabet.size(); a++) {
					for(int b = 0; b < alphabet.size(); b++) {
						if(alphabet.get(a) == alphabet.get(b)) {
							cost = cost + 0;
						}
						else {
							cost = cost + miscost*P1[a][x-1]*P2[b][y-1];
						}
					}
				}
				
				Node diaCost = new Node(matrix[x-1][y-1].getCost() + cost,"dia");
				
				cost = 0.0;
				
				matrix[x][y] = min(diaCost,topCost,sideCost);

			}
		}

		System.out.println("Kostenmatrix:");
		printMatrix(matrix);
		
		System.out.println("Kosten: " + matrix[matrix.length-1][matrix[0].length-1].getCost());
		System.out.println();
		return matrix;
	}
	
	public void createAlignment() {
		
		double[][] alignP1 = new double[P1.length][P1[0].length];
		double[][] alignP2 = new double[P2.length][P2[0].length];
		
		int x = matrix.length-1;
		int y = matrix[0].length-1;
		
		while(x > 0 && y > 0) {
			
			if(matrix[x][y].getTag().equals("dia")) {
				
				for(int line = 0; line < P1.length; line++) {
					alignP1[line][x-1] = P1[line][x-1];
				}
				
				for(int line = 0; line < P2.length; line++) {
					alignP2[line][y-1] = P2[line][y-1];
				}
				
				x--;
				y--;
			}
			else if(matrix[x][y].getTag().equals("top")) {
				
				for(int line = 0; line < P1.length; line++) {
					alignP1[line][x-1] = P1[line][x-1];
				}
				
				alignP2 = gapProfile(x-1,alignP2,alphabet);
				x--;
			}
			else {
				alignP1 = gapProfile(y-1,alignP1,alphabet);
				
				for(int line = 0; line < P2.length; line++) {
					alignP2[line][y] = P2[line][y];
				}
				
				y--;
			}
		}
		
		if (x > 0) {
			while (x > 0) {

				for (int line = 0; line < P1.length; line++) {
					alignP1[line][x-1] = P1[line][x-1];
				}

				alignP2 = gapProfile(x-1, alignP2, alphabet);
				x--;
			}
		}
		
		if (y > 0) {
			while (y > 0) {

				alignP1 = gapProfile(y-1, alignP1, alphabet);

				for (int line = 0; line < P2.length; line++) {
					alignP2[line][y-1] = P2[line][y-1];
				}

				y--;
			}
		}

		printProfile(alignP1);
		System.out.println();
		printProfile(alignP2);		
		
		combineProfiles(alignP1,alignP2);
	}

	public List<Character> createAlphabet(List<String> SAll) {

		List<Character> alphabet = new ArrayList<>();

		for (int x = 0; x < SAll.size(); x++) {

			String curr = SAll.get(x);

			for (int y = 0; y < curr.length(); y++) {

				if (alphabet.size() == 0) {
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
	
	public List<Character> checkGap(List<Character> alphabet){
		
		boolean inside = false;
		
		for(int x = 0; x < alphabet.size(); x++) {
			if(alphabet.get(x) == '-') {
				inside = true;
				break;
			}
		}
		
		if(!inside) {
			alphabet.add('-');
		}
		
		return alphabet;
	}
	
	public List<Character> combineAlphabets(List<Character> alphabetP1, List<Character> alphabetP2){
		
		for(int x = 0; x < alphabetP1.size(); x++) {
			
			boolean inside = false;
			
			for(int y = 0; y < alphabetP2.size(); y++) {
				if(alphabetP1.get(x) == alphabetP2.get(y)) {
					inside = true;
					break;
				}
			}
			
			if(!inside) {
				alphabetP2.add(alphabetP1.get(x));
			}
		}
		return alphabetP2;
	}
	
	public double[][] combineProfiles(double[][] P1, double[][] P2){
		
		double[][] combinedP = new double[P1.length][P1[0].length];
		
		for(int x = 0; x < P1.length; x++) {
			for(int y = 0; y < P1[0].length; y++) {
				P1[x][y] = P1[x][y]*seqCountP1;
				P2[x][y] = P2[x][y]*seqCountP2;
				combinedP[x][y] = P1[x][y] + P2[x][y];
			}
		}
		
		double seqCountCombined = seqCountP1 + seqCountP2;
		
		for(int x = 0; x < combinedP.length; x++) {
			for(int y = 0; y < combinedP[0].length; y++) {
				combinedP[x][y] = combinedP[x][y]/seqCountCombined;
			}
		}
		System.out.println();
		System.out.println("Gemeinsames Profil:");
		printProfile(combinedP);
		return combinedP;
	}
	
	public double[][] createProfile(List<String> SAll,double seqCount) {
		
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
		
		printProfile(profile);
		return profile;
	}

	public double[][] gapProfile(int place, double[][] profile, List<Character> alphabet) {

		double[][] tempProfile = new double[profile.length][profile[0].length + 1];

		for (int x = 0; x < place; x++) {
			for (int y = 0; y < profile.length; y++) {
				tempProfile[y][x] = profile[y][x];
			}
		}

		for (int y = 0; y < profile.length; y++) {
			if (alphabet.get(y) == '-') {
				tempProfile[y][place] = 1.0;
			} else {
				tempProfile[y][place] = 0.0;
			}
		}

		for (int x = place + 1; x < tempProfile[0].length; x++) {
			for (int y = 0; y < profile.length; y++) {
				tempProfile[y][x] = profile[y][x - 1];
			}
		}

		return tempProfile;
	}

	// Ausgabefunktion
	public void printProfile(double[][] matrix) {

		for (int x = 0; x < matrix.length; x++) {
			System.out.print(alphabet.get(x) + "|");
			for (int y = 0; y < matrix[0].length; y++) {
				System.out.print(Math.round(matrix[x][y] * 100.0) / 100.0 + "|");
			}
			System.out.println();
		}
	}

	public void printMatrix(Node[][] matrix) {
		for (int x = 0; x < matrix.length; x++) {
			System.out.print("|");
			for (int y = 0; y < matrix[0].length; y++) {

				if (matrix[x][y] == null) {
					System.out.print("?|");
				} else {
					System.out.print(Math.round(matrix[x][y].getCost() * 100.0) / 100.0 + "|");
				}
			}
			System.out.println();
		}
		System.out.println();
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
