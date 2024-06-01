package seq;

public class GlobalAlignmentAffine {
	
	String S1;
	String S2;
	int matchcost;
	int miscost;
	int gapopen;
	int gapextend;
	int[][] E;
	int[][] F;
	int[][] G;
	int[][] D;
	
	public static void main(String args[]) {
		
		String S1 = "ACAAGCTC";
		String S2 = "AATC";
		int miscost = 2;
		int gapopen = 3;
		int gapextend = 1;
		
		GlobalAlignmentAffine GAA = new GlobalAlignmentAffine(S1,S2,miscost,gapopen,gapextend);
		GAA.findMatrix();
		String[] aligned = GAA.recursiveFindPath(S1.length(), S2.length());
		
		for(String s : aligned) {
			System.out.println(s);
		}
		
	}
	
	public GlobalAlignmentAffine(String S1, String S2, int miscost, int gapopen, int gapextend) {
		this.S1 = S1;
		this.S2 = S2;
		this.matchcost = 0;
		this.miscost = miscost;
		this.gapopen = gapopen;
		this.gapextend = gapextend;
	}
	
	public void findMatrix() {
		
		S1 = "-" + S1;
		S2 = "-" + S2;
		
		E = new int[S1.length()][S2.length()];
		F = new int[S1.length()][S2.length()];
		G = new int[S1.length()][S2.length()];
		D = new int[S1.length()][S2.length()];
		
		G[0][0] = 0;
		
		//E[x][0] = undef
		for(int x = 0; x < E.length; x++) {
			E[x][0] = Integer.MAX_VALUE/2;
		}
		
		//E[0][x] = gapkosten
		for(int x = 1; x < E[0].length; x++) {
			if(x == 1) {
				E[0][x] = gapopen + gapextend;
			}
			else {
				E[0][x] = E[0][x-1] + gapextend;
			}
		}
		
		//F[0][x] = undef
		for(int x = 0; x < F[0].length; x++) {
			F[0][x] = Integer.MAX_VALUE/2;
		}
		
		//F[x][0] = gapkosten
		for(int x = 1; x < F.length; x++) {
			if(x == 1) {
				F[x][0] = gapopen + gapextend;
			}
			else{
				F[x][0] = F[x-1][0] + gapextend;
			}
		}
		
		//G[x][0] = undef
		for(int x = 1; x < G.length; x++) {
			G[x][0] = Integer.MAX_VALUE/2;
		}
		
		//G[0][x] = undef
		for(int x = 1; x < G[0].length; x++) {
			G[0][x] = Integer.MAX_VALUE/2;
		}
		
		D[0][0] = G[0][0];
		
		//D[0][x] = E[x][0]
		for(int x = 1; x < D[0].length; x++) {
			D[0][x] = E[0][x];
		}
		
		//D[x][0] = F[x][0]
		for(int x = 1; x < D.length; x++) {
			D[x][0] = F[x][0];
		}
		
		//Rest
		for(int y = 1; y < G[0].length; y++) {
			for(int x = 1; x < G.length; x++) {
				
				int dia = 0;
				
				if(S1.charAt(x) == S2.charAt(y)) {
					dia = matchcost;
				}
				else {
					dia = miscost;
				}
				
				E[x][y] = Math.min(E[x][y-1] + gapextend, Math.min(F[x][y-1], G[x][y-1]) + gapopen + gapextend);
				F[x][y] = Math.min(F[x-1][y] + gapextend, Math.min(E[x-1][y], G[x-1][y]) + gapopen + gapextend);
				G[x][y] = D[x-1][y-1] + dia;
				D[x][y] = min(E[x][y],F[x][y],G[x][y]);
			}
		}
		
		System.out.println("E");
		print(E);
		System.out.println("F");
		print(F);
		System.out.println("G");
		print(G);
		System.out.println("D");
		print(D);
		
		S1 = S1.substring(1);
		S2 = S2.substring(1);
	}
	
	public String[] recursiveFindPath(int i, int j) {
		String alignedString1 = "";
		String alignedString2 = "";
		if (i == 0) {
			for (int k = 1; k <= j; k++) {
				alignedString1 = "-" + alignedString1;
				alignedString2 = S2.charAt(j - k) + alignedString2;
				System.out.println(S2.charAt(j - k));
			}

			return new String[] { alignedString1, alignedString2 };

		} else if (j == 0) {

			for (int k = 1; k <= i; k++) {
				
				alignedString1 = S1.charAt(i - k) + alignedString1;
				alignedString2 = "-" + alignedString2;
				System.out.println(i-k);
				System.out.println(S1.charAt(i - k));
			}

			return new String[] { alignedString1, alignedString2 };
		}
		
		//suchen nach Minimum in D zum traceback
		int sprung = min(D[i-1][j-1],D[i-1][j],D[i][j-1]);
		
		
		//Ruecksprung nach Oben-Links
		if (D[i - 1][j - 1] == sprung) {

			alignedString1 = "" + S1.charAt(i - 1);
			alignedString2 = "" + S2.charAt(j - 1);
			//System.out.println(alignedString1);
			i -= 1;
			j -= 1;

		//Ruecksprung nach Links
		} else if (D[i][j - 1] == sprung) {

			alignedString1 = "-";
			alignedString2 = "" + S2.charAt(j - 1);

			j -= 1;

		//Ruecksprung nach Oben
		} else {

			alignedString1 = "" + S1.charAt(i - 1);
			alignedString2 = "-";

			i -= 1;
		}

		//Rekursiver Aufruf
		String[] alignedStrings = recursiveFindPath(i, j);

		alignedString1 = alignedStrings[0] + alignedString1;
		alignedString2 = alignedStrings[1] + alignedString2;
		
		return new String[] { alignedString1, alignedString2 };
	}

	
	public int min(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	// Ausgabefunktion
	public void print(int[][] matrix) {

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
