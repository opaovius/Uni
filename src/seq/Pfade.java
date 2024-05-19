package seq;

public class Pfade {
	
	public static void main(String[] args) {
	
		String S1 = "";
		String S2 = "";
		
		int size = 4;
		int size2 = 4;
		
		for(int x = 0; x < size; x++) {
			S1 = S1 + "a";
		}
		
		for(int x = 0; x < size2; x++) {
			S2 = S2 + "a";
		}
		
		int[][] matrix = new int[S2.length()+1][S1.length()+1];
		
		for(int x = 0; x < matrix[0].length; x++) {
			matrix[0][x] = 1;
		}
		
		for(int x = 1; x < matrix.length; x++) {
			matrix[x][0] = 1;
		}
		
		for(int x = 1; x < matrix[0].length; x++) {
			for(int y = 1; y < matrix.length; y++) {
				matrix[y][x] = matrix[y-1][x-1] + matrix[y-1][x] + matrix[y][x-1];
			}
		}
		
		print(matrix);
		System.out.println(matrix[S2.length()][S1.length()]);

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
