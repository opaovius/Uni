package seq;

public class SemiGlobalAlignment {

	String S1;
	String S2;
	int[][] matrix;
	int miscost;
	int incost;
	int delcost;

	public static void main(String args[]) {
		SemiGlobalAlignment SGA = new SemiGlobalAlignment("GTG", "AGATGGT", 1, 2, 2);
		SGA.print();
		SGA.findPath();

	}

	/*
	 * public static void main(String args[]) { int incost =
	 * Integer.parseInt(args[2]); int delcost = Integer.parseInt(args[3]); int
	 * miscost = Integer.parseInt(args[4]);
	 * 
	 * SemiGlobalAlignment SGA = new
	 * SemiGlobalAlignment(args[1],args[0],incost,delcost,miscost);
	 * 
	 * SGA.print(); SGA.findPath();
	 * 
	 * }
	 */
	public SemiGlobalAlignment(String S1, String S2, int miscost, int incost, int delcost) {
		this.S1 = S1;
		this.S2 = S2;
		this.miscost = miscost;
		this.incost = incost;
		this.delcost = delcost;
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
			matrix[0][x] = matrix[0][x - 1] + incost;
		}

		// Rest auffuellen
		for (int x = 1; x < matrix.length; x++) {
			for (int y = 1; y < matrix[0].length; y++) {

				int oben = Integer.MAX_VALUE;

				if (y == matrix[0].length - 1) {
					oben = matrix[x - 1][y];
				} else {
					oben = matrix[x - 1][y] + delcost;
				}

				int seite = matrix[x][y - 1] + incost;
				int dia = Integer.MAX_VALUE;

				if (S2.charAt(x - 1) == S1.charAt(y - 1)) {
					dia = matrix[x - 1][y - 1];
				} else {
					dia = matrix[x - 1][y - 1] + miscost;
				}

				// Minimum der drei Zahlen bestimmen
				matrix[x][y] = min(oben, seite, dia);
			}
		}

		return matrix;
	}

	public void findPath() {

		S1 = "-" + S1;
		S2 = "-" + S2;

		int x = S1.length();
		int y = S2.length();
		String alignedS1 = "";
		String alignedS2 = "";
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		System.out.println();
		

		while (x > 0 || y > 0) {

			if (y == 1) {
				for(int i = x; i > 0; i--) {
					alignedS1 = S1.charAt(i) + alignedS1;
					alignedS2 = "-" + alignedS2;
				}
				y = 0;
				x = 0;
			} else if (x == 1) {
				for(int i = y; i > 0; i--) {
					alignedS2 = S2.charAt(i) + alignedS2;
					alignedS1 = "-" + alignedS1;
				}
				y = 0;
				x = 0;
			} else {

				int dia = Integer.MAX_VALUE;

				if (S1.charAt(x - 1) == S2.charAt(y - 1)) {
					dia = 0;
				} else {
					dia = miscost;
				}

				int oben = Integer.MAX_VALUE;

				if (x == S1.length()) {
					oben = 0;
				} else {
					oben = incost;
				}
				if (x - 2 >= 0 && y - 2 >= 0) {
					if (matrix[y - 2][x - 2] == matrix[y - 1][x - 1] - dia) {
						alignedS1 = S1.charAt(x - 1) + alignedS1;
						alignedS2 = S2.charAt(y - 1) + alignedS2;
						x--;
						y--;
					} else if (matrix[y - 2][x - 1] == matrix[y - 1][x - 1] - oben) {
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
			System.out.println("x: " + x + "|y:" + y);
			System.out.println(alignedS1);
			System.out.println(alignedS2);
			System.out.println("--------");
		}

		System.out.println(alignedS1);
		System.out.println(alignedS2);

	}

	// Hilfsfunktion fuer Minimum
	public int min(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
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

	}

}
