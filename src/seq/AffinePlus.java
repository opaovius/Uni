package seq;

public class AffinePlus {
	
	String S1;
	String S2;
	int matchcost;
	int miscost;
	int gapopen;
	int gapextend;
	Node[][] E;
	Node[][] F;
	Node[][] G;
	Node[][] D;
	
	public static void main(String args[]) {
		String S1 = "ACAAGCTC";
		String S2 = "AATC";
		int miscost = 2;
		int gapopen = 3;
		int gapextend = 1;
		
		AffinePlus AP = new AffinePlus(S1,S2,miscost,gapopen,gapextend);
		AP.findMatrix();
		AP.findAlignment();
	}
	
	public AffinePlus(String S1, String S2, int miscost, int gapopen, int gapextend) {
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
		
		E = new Node[S1.length()][S2.length()];
		F = new Node[S1.length()][S2.length()];
		G = new Node[S1.length()][S2.length()];
		D = new Node[S1.length()][S2.length()];
		
		G[0][0] = new Node(0,"none");
		
		//E[x][0] = undef
		for(int x = 0; x < E.length; x++) {
			E[x][0] = new Node(Integer.MAX_VALUE/2,"none");
		}
		
		//E[0][x] = gapkosten
		for(int x = 1; x < E[0].length; x++) {
			if(x == 1) {
				E[0][x] = new Node(gapopen + gapextend,"none");
			}
			else {
				E[0][x] = new Node(E[0][x-1].getCost() + gapextend,"none");
			}
		}
		
		// F[0][x] = undef
		for (int x = 0; x < F[0].length; x++) {
			F[0][x] = new Node(Integer.MAX_VALUE / 2, "none");
		}

		// F[x][0] = gapkosten
		for (int x = 1; x < F.length; x++) {
			if (x == 1) {
				F[x][0] = new Node(gapopen + gapextend, "none");
			} else {
				F[x][0] = new Node(F[x - 1][0].getCost() + gapextend, "none");
			}
		}
		
		// G[x][0] = undef
		for (int x = 1; x < G.length; x++) {
			G[x][0] = new Node(Integer.MAX_VALUE / 2,"none");
		}

		// G[0][x] = undef
		for (int x = 1; x < G[0].length; x++) {
			G[0][x] = new Node(Integer.MAX_VALUE / 2,"none");
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

		// Rest
		for (int y = 1; y < G[0].length; y++) {
			for (int x = 1; x < G.length; x++) {

				int dia = 0;

				if (S1.charAt(x) == S2.charAt(y)) {
					dia = matchcost;
				} else {
					dia = miscost;
				}

				E[x][y] = new Node(Math.min(E[x][y - 1].getCost() + gapextend
						, Math.min(F[x][y - 1].getCost(), G[x][y - 1].getCost()) + gapopen + gapextend)
						,"seite");
				F[x][y] = new Node(Math.min(F[x - 1][y].getCost() + gapextend
						, Math.min(E[x - 1][y].getCost(), G[x - 1][y].getCost()) + gapopen + gapextend)
						,"oben");
				G[x][y] = new Node(D[x - 1][y - 1].getCost() + dia,"dia");
				D[x][y] = min(E[x][y], F[x][y], G[x][y]);
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
	
	public void findAlignment() {
		
		int x = S1.length();
		int y = S2.length();
		String alignedS1 = "";
		String alignedS2 = "";
		
		
		while(x != 0 && y != 0) {
			Node curr = D[x][y];
			
			if(curr.getTag().equals("dia")) {
				alignedS1 = S1.charAt(x-1) + alignedS1;
				alignedS2 = S2.charAt(y-1) + alignedS2;
				x--;
				y--;
			}
			else if(curr.getTag().equals("seite")) {
				alignedS1 = "-" + alignedS1;
				alignedS2 = S2.charAt(y-1) + alignedS2;
				y--;
			}
			else {
				alignedS1 = S1.charAt(x-1) + alignedS1;
				alignedS2 = "-" + alignedS2;
				x--;
			}
			System.out.println("cost: " + curr.getCost() + "|tag: " + curr.getTag());
			System.out.println("x: " + x + "|y: " + y);
		}
		
		while(x != 0) {
			alignedS1 = S1.charAt(x-1) + alignedS1;
			alignedS2 = "-" + alignedS2;
			x--;
		}
		
		while(y != 0) {
			alignedS1 = "-" + alignedS1;
			alignedS2 = S2.charAt(y-1) + alignedS2;
			y--;
		}
		
		System.out.println(alignedS1);
		System.out.println(alignedS2);
	}
	
	public void print(Node[][] matrix) {
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

}
