package seq;

import java.util.ArrayList;
import java.util.List;

public class LocalAlignment {
	
	String S1;
	String S2;
	int[][] matrix;
	int miscost;
	int incost;
	int delcost;
	int matchcost;

	/*
	public static void main(String args[]) {
		LocalAlignment LC = new LocalAlignment("AGATGGT"
				, "GAAGGG"
				,1 , 0, -1, -1);
		LC.print();
		System.out.println();
		
		List<Integer> places = LC.findMax();
		
		for(int x = 0; x < places.size(); x++) {
			int xStart = places.get(x);
			x++;
			int yStart = places.get(x);
			LC.findPath(xStart, yStart);
		}

	}
	*/
	
	public static void main(String args[]) {
		
		String S1 = args[1];
		String S2 = args[0];
		int matchScore = Integer.parseInt(args[2]);
		int misScore = Integer.parseInt(args[3]);
		int inScore = Integer.parseInt(args[4]);
		int delScore = Integer.parseInt(args[5]);
		
		LocalAlignment LC = new LocalAlignment(S1, S2, matchScore , misScore, inScore, delScore);
		
		LC.print();
		
		List<Integer> places = LC.findMax();
		
		for(int x = 0; x < places.size(); x++) {
			int xStart = places.get(x);
			x++;
			int yStart = places.get(x);
			LC.findPath(xStart, yStart);
		}
	}
	
	
	
	public LocalAlignment(String S1, String S2,int matchcost, int miscost, int incost, int delcost) {
		this.S1 = S1;
		this.S2 = S2;
		this.miscost = miscost;
		this.incost = incost;
		this.delcost = delcost;
		this.matchcost = matchcost;
		this.matrix = matrix();
	}
	
	public int[][] matrix() {

		int[][] matrix = new int[S2.length() + 1][S1.length() + 1];

		// (0,0) ist immer 0
		matrix[0][0] = 0;

		// Spalte 0 auffuelen
		for (int x = 1; x < matrix.length; x++) {
			matrix[x][0] = 0;
		}

		// Zeile 0 auffuelen
		for (int x = 1; x < matrix[0].length; x++) {
			matrix[0][x] = 0;
		}

		// Rest auffuellen
		for (int x = 1; x < matrix.length; x++) {
			for (int y = 1; y < matrix[0].length; y++) {

				
				int	oben = matrix[x - 1][y] + delcost;
				int seite = matrix[x][y - 1] + incost;
				int dia = Integer.MIN_VALUE;
				
				if (S2.charAt(x - 1) == S1.charAt(y - 1)) {
					dia = matrix[x - 1][y - 1] + matchcost;
				} else {
					dia = matrix[x - 1][y - 1] + miscost;
				}

				// Minimum der drei Zahlen bestimmen
				matrix[x][y] = max(oben, seite, dia);
			}
		}
		return matrix;
		
	}
	
	public List<Integer> findMax() {
		List<Integer> places = new ArrayList<>();
		
		int max = 0;
		
		for(int x = 1; x < matrix.length; x++) {
			for(int y = 1; y < matrix[0].length; y++) {
				if(matrix[x][y] > max) {
					max = matrix[x][y];
					places.clear();
					places.add(x);
					places.add(y);
				}
				else if(matrix[x][y] == max) {
					places.add(x);
					places.add(y);
				}
			}
		}
		return places;
	}
	
	public void findPath(int xStart, int yStart) {

		S1 = "-" + S1;
		S2 = "-" + S2;

		int x = yStart + 1;
		int y = xStart + 1;
		String alignedS1 = "";
		String alignedS2 = "";

		while (x > 0 || y > 0) {

			if (y == 1) {
				/*
				for(int i = x; i > 0; i--) {
					alignedS1 = S1.charAt(i) + alignedS1;
					alignedS2 = "-" + alignedS2;
				}
				*/
				y = 0;
				x = 0;
			} else if (x == 1) {
				/*
				for(int i = y; i > 0; i--) {
					alignedS2 = S2.charAt(i) + alignedS2;
					alignedS1 = "-" + alignedS1;
				}
				*/
				y = 0;
				x = 0;
			} else {

				int dia = Integer.MIN_VALUE;

				if (S1.charAt(x - 1) == S2.charAt(y - 1)) {
					dia = matchcost;
				} else {
					dia = miscost;
				}
				
				if (x - 2 >= 0 && y - 2 >= 0) {
					if (matrix[y - 2][x - 2] == matrix[y - 1][x - 1] - dia) {
						alignedS1 = S1.charAt(x - 1) + alignedS1;
						alignedS2 = S2.charAt(y - 1) + alignedS2;
						x--;
						y--;
					} else if (matrix[y - 2][x - 1] == matrix[y - 1][x - 1] - incost) {
						alignedS1 = "-" + alignedS1;
						alignedS2 = S2.charAt(y - 1) + alignedS2;
						y--;
					} else {
						alignedS1 = S1.charAt(x - 1) + alignedS1;
						alignedS2 = "-" + alignedS2;
						x--;
					}
				}

			}
		}
		
		String temp = "";
		
		for(int z = 1; z < S1.length(); z++) {
			temp = temp + S1.charAt(z);
		}
		S1 = temp;
		temp = "";
		for(int z = 1; z < S2.length(); z++) {
			temp = temp + S2.charAt(z);
		}
		S2 = temp;

		System.out.println(alignedS1);
		System.out.println(alignedS2);
		System.out.println();
	}
	
	public int max(int a, int b, int c) {
		return Math.max(Math.max(a, b), c);
	}

	// Ausgabefunktion
	public void print() {

		for (int x = 0; x < matrix.length; x++) {
			System.out.print("|");
			for (int y = 0; y < matrix[0].length; y++) {
				System.out.print(matrix[x][y] + "|");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Edit-Distanz: " + matrix[matrix.length-1][matrix[0].length-1]);

	}

}
