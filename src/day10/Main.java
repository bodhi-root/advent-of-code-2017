package day10;

import java.io.File;

import common.FileUtil;

public class Main {
	
	public static void test() {
		Memory memory = new Memory(5);
		System.out.println(memory.toString());
		
		int [] sizes = new int [] {3,4,1,5};
		for (int i=0; i<sizes.length; i++) {
			memory.twist(sizes[i]);
			System.out.println(memory.toString());
		}
	}
	
	public static void solvePart1() throws Exception {
		Memory memory = new Memory(256);
		
		String line = FileUtil.readLineFromFile(new File("files/day10/input.txt"));
		String [] parts = line.split(",");
		
		for (int i=0; i<parts.length; i++) {
			int twistLength = Integer.parseInt(parts[i]);
			memory.twist(twistLength);
		}
		
		int product = memory.state[0] * memory.state[1];
		System.out.println("Product = " + product);
	}
	
	
	
	public static void runAsciiProgram(String text) {
		System.out.println(KnotHash.hash(text));
	}
	
	public static void test2() {
		runAsciiProgram("");			// a2582a3a0e66e6e86e3812dcb672a272
		runAsciiProgram("AoC 2017");	// 33efeb34ea91902bb2f59c9920caa6cd.
		runAsciiProgram("1,2,3");		// 3efbe78a8d82f29979031a4aa0b16a9d.
		runAsciiProgram("1,2,4");		// 63960835bcdc130f0b66d7ff4f6a5a8e
	}
	
	public static void solvePart2() throws Exception {
		String line = FileUtil.readLineFromFile(new File("files/day10/input.txt"));
		runAsciiProgram(line);			// e0387e2ad112b7c2ef344e44885fe4d8
	}
	
	public static void main(String [] args) {
		try {
			//test();
			//solvePart1();
			
			//test2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
