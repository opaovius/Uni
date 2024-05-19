package seq;

public class Edit {
	
	
	
	public static void main(String[] args) {
		
		String S1 = "GAAGGG";
		String S2 = "AGATGGT";
		//String S1 = "AAA";
		//String S2 = "GGGGGGGG";
		int incost = 2;
		int delcost = 2;
		int miscost = 1;
		
		int[][] matrix = matrix(S1,S2,incost,delcost,miscost);
		Alignment(matrix,S1,S2);
		System.out.println(matrix[S2.length()-1][S1.length()-1]);
		
		print(matrix);
		
	}
	 
	/*
	public static void main(String[] args) {
		
		String S1 = args[0];
		String S2 = args[1];
		
		int incost = Integer.parseInt(args[2]);
		int delcost = Integer.parseInt(args[3]);
		int miscost = Integer.parseInt(args[4]);
		
		int[][] matrix = matrix(S1,S2,incost,delcost,miscost);
		
		//print(matrix);
		System.out.println("Edit-Distanz: " + matrix[S2.length()][S1.length()]);
	}
	*/
	public static int[][] matrix(String S1, String S2, int incost, int delcost, int miscost) {
		
		int[][] matrix = new int[S2.length()+1][S1.length()+1];
		
		//(0,0) ist immer 0
		matrix[0][0] = 0;
		
		//Spalte 0 auffuelen
		for(int x = 1; x < matrix.length; x++) {
			matrix[x][0] = matrix[x-1][0] + delcost;
		}
		
		//Zeile 0 auffuelen
		for(int x = 1; x < matrix[0].length; x++) {
			matrix[0][x] = matrix[0][x-1] + incost;
		}
		
		//Rest auffuellen
		for(int x = 1; x < matrix.length; x++) {
			for(int y = 1; y < matrix[0].length; y++) {
				
				int oben = matrix[x-1][y] + delcost;
				int seite = matrix[x][y-1] + incost;
				int dia = Integer.MAX_VALUE;
				
				if(S2.charAt(x-1) == S1.charAt(y-1)) {
					dia = matrix[x-1][y-1];
				}
				else {
					dia = matrix[x-1][y-1] + miscost;
				}
				
				//Minimum der drei Zahlen bestimmen
				matrix[x][y] = min(oben,seite,dia);
			}
		}
		
		
		return matrix;
	}
	
	public static void Alignment(int[][] solution, String string1, String string2) {
		
		String alignedString1 = "";
		String alignedString2 = "";
		
		int x = solution.length - 1;
		int z = solution[0].length - 1;
		int miscost = 1;
		int delcost = 2;
		int incost = 2;
		
		int matchValue;
		
		while(x != 0 && z != 0) {
			
			if(string1.charAt(x-1) == string2.charAt(z)) {
				matchValue = 0;
			}
			else {
				matchValue = miscost;
			}
			
			if(solution[x-1][z-1] == solution[x][z] - matchValue) {
				
				alignedString1 = string1.charAt(x-1) + alignedString1;
				alignedString2 = string2.charAt(z-1) + alignedString2;
				
				x = x - 1;
				z = z - 1;
			}
			else if(solution[x][z-1] == solution[x][z] - incost) {
				
				alignedString1 = "-" + alignedString1;
				alignedString1 = string2.charAt(z-1) + alignedString2;
				
				z = z - 1;
			}
			else {
				
				alignedString1 = string1.charAt(x-1) + alignedString1;
				alignedString2 = "-" + alignedString2;
				
				x = x - 1;
			}
		}
		
		if(x == 0) {
			
			for(int y = 0; y == z; y++) {
				alignedString1 = "-" + alignedString1;
				alignedString2 = string2.charAt(z-y) + alignedString2;
			}
		}
		else {
			
			for(int y = 0; y < x; y++) {
				alignedString1 = string1.charAt(x-y) + alignedString1;
				alignedString2 = "-" + alignedString2;
			}
		}
		
		System.out.println("alignedS1: " + alignedString1);
		System.out.println("alignedS2: " + alignedString2);
		
	}
	
	
	//Hilfsfunktion fuer Minimum
	public static int min(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}
	
	//Ausgabefunktion
	public static void print(int[][] matrix) {
		
		for(int x = 0; x < matrix.length; x++) {
			System.out.print("|");
			for(int y = 0; y < matrix[0].length; y++) {
				System.out.print(matrix[x][y] + "|");
			}
			System.out.println();
		}
		
	}
	
}
