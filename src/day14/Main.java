package day14;

import day10.KnotHash;

public class Main {
		
	// ----------------------------------------------------------------- Part 1

	public static void printHashes(String input) {
		int totalBits = 0;
		
		for (int i=0; i<128; i++) {
			String hash = KnotHash.hash(input + "-" + Integer.valueOf(i));
			System.out.println(hash);
			
			for (int j=0; j<hash.length(); j++) {
				int value = Integer.parseInt(hash.substring(j,j+1), 16);
				totalBits += Integer.bitCount(value);
			}
		}
		
		System.out.println("Total Bits = " + totalBits);
	}
	
	public static void test() {
		printHashes("flqrgnkx");
	}
	
	public static void solvePart1() throws Exception {
		printHashes("ffayrhll");
	}
	
	// ----------------------------------------------------------------- Part 2
	
	static class Memory {
		
		int [][] memory = new int[128][128];
		int height = 128;
		int width = 128;
		
		public int getTotalBitCount() {
			int total = 0;
			
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					total += memory[i][j];
				}
			}
			
			return total;
		}
		
		public int getTotalRegionCount() {
			
			int regionCount = 0;
			
			int [][] regions = new int[128][128];
			for (int i=0; i<128; i++) {
				for (int j=0; j<128; j++) {
					
					if (memory[i][j] != 0 && regions[i][j] == 0) {
						regionCount++;
						labelRegion(regions, i, j, regionCount);
					}
					
				}
			}
			
			//for (int i=0; i<128; i++) {
			//	System.out.println(Arrays.toString(regions[i]));
			//}
			
			return regionCount;
		}
		
		protected void labelRegion(int [][] regions, int i, int j, int regionId) {
			//label this square:
			regions[i][j] = regionId;
			
			//label adjacent squares (if bit present and not labeled already):
			if (i-1 >= 0 && memory[i-1][j] != 0 && regions[i-1][j] == 0)
				labelRegion(regions, i-1, j, regionId);
			if (i+1 < 128 && memory[i+1][j] != 0 && regions[i+1][j] == 0)
				labelRegion(regions, i+1, j, regionId);
			if (j-1 >= 0 && memory[i][j-1] != 0 && regions[i][j-1] == 0)
				labelRegion(regions, i, j-1, regionId);
			if (j+1 < 128 && memory[i][j+1] != 0 && regions[i][j+1] == 0)
				labelRegion(regions, i, j+1, regionId);
		}
		
		public void loadFromInput(String input) {
			for (int i=0; i<height; i++) {
				String hash = KnotHash.hash(input + "-" + Integer.valueOf(i));
				//System.out.println(hash);
				
				for (int j=0; j<hash.length(); j++) {
					int value = Integer.parseInt(hash.substring(j,j+1), 16);
					
					for (int bit=0; bit<4; bit++) {
						this.memory[i][j*4+bit] = (value >> (3-bit)) & 0x1;
					}
				}
			}
		}
		
	}
	
	public static void testPart2() throws Exception {
		Memory memory = new Memory();
		memory.loadFromInput("flqrgnkx");
		System.out.println("Total Bits = " + memory.getTotalBitCount());
		System.out.println("Total Regions = " + memory.getTotalRegionCount());
	}
	
	public static void solvePart2() throws Exception {
		Memory memory = new Memory();
		memory.loadFromInput("ffayrhll");
		System.out.println("Total Bits = " + memory.getTotalBitCount());
		System.out.println("Total Regions = " + memory.getTotalRegionCount());
	}
	
	public static void main(String [] args) {
		try {
			//test();
			//solvePart1();
			
			//testPart2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
