package seq;

public class Main {
	
	public static void main(String args[]) {
		SemiGlobalAlignment SGA = new SemiGlobalAlignment("GTG","AGATGGT",1,2,2);
		SGA.print();
		SGA.findPath();
		
	}

}
