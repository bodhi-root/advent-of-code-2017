package day13;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import common.FileUtil;

public class Main {
	
	static class Scanner {
		
		int depth;
		int range;
		
		int position = 0;
		int dir = 1;
		
		public Scanner(int depth, int range) {
			this.depth = depth;
			this.range = range;
		}
		
		public void reset() {
			position = 0;
			dir = 1;
		}
		
		public void step() {
			if (range <= 1)
				return;
			
			//see if we need to reverse direction:
			int nextPos = position + dir;
			if (nextPos < 0 || nextPos >= range)
				dir = dir * -1;
			
			this.position += dir;
		}
		
	}
	
	static class World {
		
		List<Scanner> scanners;
		int t = 0;
		
		Scanner [] scannerArray;
		
		public World(List<Scanner> scanners) {
			this.scanners = scanners;
			
			int maxDepth = getMaxScannerDepth();
			this.scannerArray = new Scanner[maxDepth+1];
			for (Scanner scanner : scanners)
				scannerArray[scanner.depth]= scanner; 
		}
		
		public int getMaxScannerDepth() {
			int max = Integer.MIN_VALUE;
			for (Scanner scanner : scanners)
				max = Math.max(max, scanner.depth);
			
			return max;
		}
		
		public Scanner getScannerAtDepth(int depth) {
			for (Scanner scanner : scanners) {
				if (scanner.depth == depth)
					return scanner;
			}
			return null;
		}
		
		public void reset() {
			t = 0;
			for (Scanner scanner : scanners)
				scanner.reset();
		}
		
		public void step() {
			for (Scanner scanner : scanners) 
				scanner.step();
			
			t++;
		}
		
		public void runPart1Sim() {
			int totalSeverity = 0;
			
			int maxDepth = getMaxScannerDepth();
			for (int depth=0; depth<=maxDepth; depth++) {
				
				//player advances (to depth 'depth')
				
				//check for hit:
				Scanner scanner = getScannerAtDepth(depth);
				if (scanner != null) {
					if (scanner.position == 0) {
						System.out.println("Hit at depth " + depth);
						int severity = scanner.depth * scanner.range;
						totalSeverity += severity;
					}
				}
				
				//advance scanners:
				step();
			}
			
			System.out.println("Total Severity = " + totalSeverity);
		}
		
		public boolean runPart2Sim(int delay) {
			
			//add delay:
			for (int i=0; i<delay; i++)
				step();
			
			int maxDepth = scannerArray.length-1;
			for (int depth=0; depth<=maxDepth; depth++) {
				
				//player advances (to depth 'depth')
				
				//check for hit:
				if (scannerArray[depth] != null) {
					if (scannerArray[depth].position == 0) {
						return false;
					}
				}
				
				//advance scanners:
				step();
			}
			
			return true;
		}
		
		public void solvePart2() {
			int delay = 0;
			while (!runPart2Sim(delay)) {
				delay++;
				reset();
				
				if (delay % 1000 == 0)
					System.out.println(delay);
			}
			
			System.out.println("Minimum delay to avoid being hit = " + delay);
		}
		
		static class SavedState {
			
			int [] positions;
			int [] dirs;
			
			public SavedState(int [] positions, int [] dirs) {
				this.positions = positions;
				this.dirs = dirs;
			}
			
		}
		
		SavedState lastSave = null;
		
		public void saveState() {
			int [] positions = new int[scanners.size()];
			int [] dirs = new int[scanners.size()];
			
			for (int i=0; i<scanners.size(); i++) {
				positions[i] = scanners.get(i).position;
				dirs[i] = scanners.get(i).dir;
			}
			
			this.lastSave = new SavedState(positions, dirs);
		}
		
		public void restoreSavedState() {
			for (int i=0; i<scanners.size(); i++) {
				scanners.get(i).position = lastSave.positions[i];
				scanners.get(i).dir = lastSave.dirs[i];
			}
		}
		
		/**
		 * Optimized way to solve part 2.  Instead of running the initial delay
		 * calculations over and over again, we just save the state before
		 * running our test in a way that allows us to easily restore it afterward.
		 * This improved performance immensely! 
		 */
		public void solvePart2Optimized() {
			int delay = 0;
			while(true) {
				
				saveState();
				
				if (runPart2Sim(0))
					break;
				
				restoreSavedState();
				step();
				
				delay++;
				
				if (delay % 1000 == 0)
					System.out.println(delay);
			}
			
			System.out.println("Minimum delay to avoid being hit = " + delay);
		}
		
		public static World loadFromFile(File file) throws IOException {
			List<Scanner> scanners = new ArrayList<>(100);
			
			List<String> lines = FileUtil.readLinesFromFile(file);
			for (String line : lines) {
				//sample line:
				//0: 3
				String [] parts = line.split(":");
				int depth = Integer.parseInt(parts[0].trim());
				int range = Integer.parseInt(parts[1].trim());
				
				scanners.add(new Scanner(depth, range));
			}
			
			return new World(scanners);
		}
		
	}
	
	public static void test() throws Exception {
		World world = World.loadFromFile(new File("files/day13/test.txt"));
		world.runPart1Sim();
		
		world.reset();
		world.solvePart2();
		
		world.reset();
		world.solvePart2Optimized();
	}
	
	public static void solvePart1() throws Exception {
		World world = World.loadFromFile(new File("files/day13/input.txt"));
		world.runPart1Sim();
	}
	
	public static void solvePart2() throws Exception {
		World world = World.loadFromFile(new File("files/day13/input.txt"));
		world.solvePart2Optimized();
	}
	
	public static void main(String [] args) {
		try {
			//test();
			//solvePart1();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
