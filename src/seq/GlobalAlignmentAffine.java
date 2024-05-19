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
		//System.out.println("Edit-Distanz: " + matrix[matrix.length-1][matrix[0].length-1]);

	}
}
