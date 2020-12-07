package day06;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.FileUtil;

public class Main {
	
	static class Memory {
		
		int [] banks;
		
		public Memory(int [] banks) {
			this.banks = banks;
		}
		
		public static Memory loadFromFile(File file) throws IOException {
			String line = FileUtil.readLineFromFile(file);
			String [] parts = line.split("\\s+");
			int [] banks = new int[parts.length];
			for (int i=0; i<banks.length; i++)
				banks[i] = Integer.parseInt(parts[i]);
			
			return new Memory(banks);
		}
		
		public void reallocate() {
			
			//find maximum bank:
			int maxIndex = 0;
			for (int i=1; i<banks.length; i++) {
				if (banks[i] > banks[maxIndex])
					maxIndex = i;
			}
			
			//reallocate
			int toDistribute = banks[maxIndex];
			banks[maxIndex] = 0;
			//System.out.println("  " + this.toString());
			
			int nextIndex = maxIndex + 1;
			while(toDistribute > 0) {
				banks[nextIndex%banks.length]++;
				toDistribute--;
				//System.out.println("  " + this.toString());
				nextIndex++;
			}
		}
		
		public int findFirstRepeat() {
			Set<String> sets = new HashSet<>(1000);
			sets.add(this.toString());
			int steps = 0;
			
			while(true) {
				reallocate();
				steps++;
				
				if (!sets.add(this.toString()))
					return steps;
			}
		}
		
		public int findRepeatCycleLength() {
			List<String> sets = new ArrayList<>(10000);
			sets.add(this.toString());
			
			while(true) {
				reallocate();
				
				String state = this.toString();
				int index = sets.indexOf(state);
				
				if (index < 0)
					sets.add(state);
				else {
					System.out.println("State " + index + " appeared again at index " + sets.size());
					return (sets.size() - index);
				}
			}
		}
		
		public String toString() {
			return Arrays.toString(banks);
		}
		
	}
	
	public static void test() {
		Memory memory = new Memory(new int [] {0,2,7,0});
		System.out.println(memory.toString());
		for (int i=0; i<5; i++) {
			memory.reallocate();
			System.out.println(memory.toString());
		}
		
		memory = new Memory(new int [] {0,2,7,0});
		System.out.println(memory.findFirstRepeat());
		
		memory = new Memory(new int [] {0,2,7,0});
		System.out.println(memory.findRepeatCycleLength());
	}
	
	public static void solvePart1() throws Exception {
		Memory memory = Memory.loadFromFile(new File("files/day06/input.txt"));
		System.out.println(memory.toString());
		System.out.println(memory.findFirstRepeat());
	}
	
	public static void solvePart2() throws Exception {
		Memory memory = Memory.loadFromFile(new File("files/day06/input.txt"));
		System.out.println(memory.findRepeatCycleLength());
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
