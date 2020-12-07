package day15;

public class Main {
	
	static class Generator {
		
		long factor;
		long value;
		
		public Generator(long factor, long value) {
			this.factor = factor;
			this.value = value;
		}
		
		public long next() {
			value = (value * factor) % 2147483647;
			return value;
		}
		
	}
	
	public static void doPart1(int seedA, int seedB) {
		Generator A = new Generator(16807, seedA);
		Generator B = new Generator(48271, seedB);
		
		int matches = 0;
		
		for (int i=0; i<40000000; i++) {
			long a = A.next();
			long b = B.next();
			
			//System.out.println(a);
			//System.out.println(b);
			//System.out.println();
			
			if ((a & 0xFFFF) == (b & 0xFFFF))
				matches++;
		}
		
		System.out.println("Matches = " + matches);
	}
	
	public static void testPart1() throws Exception {
		doPart1(65, 8921);
	}
	
	public static void solvePart1() throws Exception {
		doPart1(277, 349);
	}
	
	// ----------------------------------------------------------------- Part 2
	
	static class Generator2 {
		
		long factor;
		long value;
		long multiple;
		
		public Generator2(long factor, long value, long multiple) {
			this.factor = factor;
			this.value = value;
			this.multiple = multiple;
		}
		
		public long next() {
			do {
				value = (value * factor) % 2147483647;
			} while (value % multiple > 0);
			
			return value;
		}
		
	}
	
	public static void doPart2(int seedA, int seedB) {
		Generator2 A = new Generator2(16807, seedA, 4);
		Generator2 B = new Generator2(48271, seedB, 8);
		
		int matches = 0;
		
		for (int i=0; i<5000000; i++) {
			long a = A.next();
			long b = B.next();
			
			//System.out.println(a);
			//System.out.println(b);
			//System.out.println();
			
			if ((a & 0xFFFF) == (b & 0xFFFF))
				matches++;
		}
		
		System.out.println("Matches = " + matches);
	}
	
	public static void testPart2() throws Exception {
		doPart2(65, 8921);
	}
	
	public static void solvePart2() throws Exception {
		doPart2(277, 349);
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			
			testPart2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
