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
		
		PSA.addSequence("-SL");
		
	}
	
	/*
	public static void main(String[] args) {
		double incost = Double.parseDouble(args[0]);
		double delcost = Double.parseDouble(args[1]);
		double miscost = Double.parseDouble(args[2]);
		String toAlign = args[3];
		String S1 = args[4];
		String S2 = args[5];
		String S3 = args[6];
		
		List<String> SAll = new ArrayList<String>();
		
		SAll.add(S1);
		SAll.add(S2);
		SAll.add(S3);
		
		ProfileSequenceAlignment PSA = new ProfileSequenceAlignment(SAll.size(),SAll,incost,delcost,miscost,toAlign);
		
		PSA.addSequence("-SL");
	}
	*/
	
	
	
	
	public ProfileSequenceAlignment(int seqCount, List<String> SAll, double miscost, double incost, double delcost, String toAlign) {
		this.seqCount = seqCount;
		this.SAll = SAll;
		this.miscost = miscost;
		this.incost = incost;
		this.delcost = delcost;
		this.toAlign = toAlign;
		this.alphabet = createAlphabet();
		System.out.println("Profile:");
		this.profile = createProfile();
		System.out.println();
		System.out.println("Cost-Matrix");
		this.matrix = createMatrix();
		System.out.println("Alignment");
		createAlignment();
	}
	
	public double[][] createProfile() {
	
		double[][] profile = new double[alphabet.size()][SAll.get(0).length()];
		
		// Durchgehen aller Strings
		for(int x = 0; x < SAll.size(); x++) {
			// Durchgehen aller Buchstaben des Stringes
			for(int y = 0; y < SAll.get(x).length(); y++) {
				
				char curr = SAll.get(x).charAt(y);
				
				// Hinzufuegen, falls Buchstabe des Stringes an Stelle y == Stelle z des Profiles
				for(int z = 0; z < alphabet.size(); z++) {
					if(curr == alphabet.get(z)) {
						profile[z][y]++;
					}
				}
			}
		}
		
		// Teilen durch die Summer aller Sequenzen
		for(int x = 0; x < profile.length; x++) {
			for(int y = 0; y < profile[0].length; y++) {
				profile[x][y] = profile[x][y]/seqCount;
			}
		}
		
		printProfile(profile);
		return profile;
	}
	
	public Node[][] createMatrix() {
		
		Node[][] matrix = new Node[profile[0].length+1][toAlign.length()+1];
		
		matrix[0][0] = new Node(0,"end");
		
		
		//D(0,i) = D(0,i-1) + kosten 
		for(int x = 1; x < matrix[0].length; x++) {
			matrix[0][x] = new Node(matrix[0][x-1].getCost() + incost, "side");
		}
		
		//D(j,0) = D(j-1,0) + kosten
		for(int x = 1; x < matrix.length; x++) {
			
			double cost = 0.0;
			
			for(int a = 0; a < alphabet.size(); a++) {
				if('-' == alphabet.get(a)) {
					cost = cost + 0.0;
				}
				else {
					cost = cost + delcost*profile[a][x-1];
				}
			}
			matrix[x][0] = new Node(matrix[x-1][0].getCost() + cost,"top");
		}
		
		//rest D(j,i)
		
		for(int x = 1; x < matrix[0].length; x++) {
			for(int y = 1; y < matrix.length; y++) {
				
				Node sideCost = new Node(matrix[y][x-1].getCost() + incost, "side");
				
				double cost = 0.0;
				
				for(int a = 0; a < alphabet.size(); a++) {
					if('-' == alphabet.get(a)) {
						cost = cost + 0.0;
					}
					else {
						cost = cost + delcost*profile[a][y-1];
					}
				}
				
				Node topCost = new Node(matrix[y-1][x].getCost() + cost,"top");
				
				cost = 0.0;
				
				for(int a = 0; a < alphabet.size(); a++) {
					if(toAlign.charAt(x-1) == alphabet.get(a)) {
						cost = cost + 0;
					}
					else {
						cost = cost + miscost*profile[a][y-1];;
					}
				}
				
				Node diaCost = new Node(matrix[y-1][x-1].getCost() + cost,"dia");
				
				matrix[y][x] = min(sideCost,topCost,diaCost);
				
			}
		}
		
		printMatrix(matrix);
		return matrix;
	}
	
	public void createAlignment() {
		
		String align1 = "";
		double[][] align2 = new double[profile.length][profile[0].length];
		
		int y = matrix.length-1;
		int x = matrix[0].length-1;
		
		while(x > 0 && y > 0) {
			if(matrix[y][x].getTag().equals("side")) {
				align1 = toAlign.charAt(x-1) + align1;
				align2 = gapProfile(x,align2);
				x--;
			}
			else if(matrix[y][x].getTag().equals("top")) {
				align1 = "-" + align1;
				
				for(int line = 0; line < profile.length; line++) {
					align2[line][x] = profile[line][x];
				}
				
				y--;
			}
			else {
				align1 = toAlign.charAt(x-1) + align1;
				
				for(int line = 0; line < profile.length; line++) {
					align2[line][x] = profile[line][x];
				}
				
				x--;
				y--;
			}
		}
		
		if(x > 0) {
			while(x > 0) {
				align1 = toAlign.charAt(x-1) + align1;
				align2 = gapProfile(x,align2);
				x--;
			}
		}
		
		if(y > 0) {
			while(y > 0) {
				align1 = "-" + align1;
				
				for(int line = 0; line < profile.length; line++) {
					align2[line][x] = profile[line][x];
				}
				
				y--;
			}
		}
		
		
		System.out.println(align1);
		printProfile(align2);
		
	}
	
	public List<Character> createAlphabet() {
		
		List<Character> alphabet = new ArrayList<>();
		
		// Alle Sequenzen durchgehen
		for(int x = 0; x < SAll.size(); x++) {
			
			String curr = SAll.get(x);
			
			// Durchgehen der Buchstaben einer Sequenz
			for(int y = 0; y < curr.length(); y++) {

				// Wenn Alphabet leer
				if(alphabet.size() == 0) {
					alphabet.add(curr.charAt(y));
				}
				else {
					
					boolean inside = false;
					
					// Nachschauen, ob der derzeitige Buchstabe im Alphabet bereits vorhanden ist 
					for(int z = 0; z < alphabet.size(); z++) {
						
						char currChar = alphabet.get(z);
						
						if(currChar == curr.charAt(y)) {
							inside = true;
							break;
						}
					}
					// Wenn nicht hinzufuegen
					if(!inside) {
						alphabet.add(curr.charAt(y));
					}
				}
			}
		}
		return alphabet;
	}
	
	public double[][] gapProfile(int place, double[][] profile) {
		
		double[][] tempProfile = new double[profile.length][profile[0].length+1];
		
		for(int x = 0; x < place; x++) {
			for(int y = 0; y < profile.length; y++) {
				tempProfile[y][x] = profile[y][x];
			}
		}
		
		for(int y = 0; y < profile.length; y++) {
			if(alphabet.get(y) == '-') {
				tempProfile[y][place] = 1.0;
			}
			else {
				tempProfile[y][place] = 0.0;
			}
		}
		
		for(int x = place + 1; x < tempProfile[0].length; x++) {
			for(int y = 0; y < profile.length; y++) {
				tempProfile[y][x] = profile[y][x-1];
			}
		}
		
		return tempProfile;
	}
	
	public void addSequence(String toAdd) {
		
		for(int x = 0; x < toAdd.length(); x++) {
			
			Character curr = toAdd.charAt(x);
			boolean inIt = false;
			
			for(int y = 0; y < alphabet.size(); y++) {
				if(curr == alphabet.get(y)) {
					inIt = true;
					break;
				}
			}
			
			if(!inIt) {
				alphabet.add(curr);
			}
		}
		
		double[][] tempProfile = new double[alphabet.size()][profile[0].length];
		
		for(int x = 0; x < profile[0].length; x++) {
			for(int y = 0; y < profile.length; y++) {
				tempProfile[y][x] = profile[y][x]*seqCount;
			}
		}
		
		seqCount++;
		
		for(int x = 0; x < toAdd.length(); x++) {
			
			Character curr = toAdd.charAt(x);
			
			for(int y = 0; y < alphabet.size(); y++) {
				if(curr == alphabet.get(y)) {
					tempProfile[y][x]++;
				}
			}
		}
		
		for(int x = 0; x < tempProfile[0].length; x++) {
			for(int y = 0; y < tempProfile.length; y++) {
				tempProfile[y][x] = tempProfile[y][x]/seqCount;
			}
		}
		
		
		System.out.println();
		System.out.println("Shared Profile:");
		printProfile(tempProfile);
	}
	
	public Node min(Node a, Node b, Node c) {
		if(a.getCost() < b.getCost() && a.getCost() < c.getCost()) {
			return a;
		}
		else if(b.getCost() < a.getCost() && b.getCost() < c.getCost()) {
			return b;
		}
		else {
			return c;
		}
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
					System.out.print(Math.round(matrix[x][y].getCost()*100.0)/100.0+ "|");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

}