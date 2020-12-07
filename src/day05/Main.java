package day05;

import java.io.File;
import java.io.IOException;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static enum Part {A, B};
	
	static class Computer {
		
		int [] program;
		int position;
		int t=0;
		Part part;
		
		public Computer(int [] program, Part part) {
			this.program = program;
			this.position = 0;
		}
		
		public boolean step() {
			int jump = program[position];
			
			if (part == Part.A) { 
				program[position]++;
			} else {
				if (program[position] >= 3)
					program[position]--;
				else
					program[position]++;
			}
			
			position += jump;
			t++;
			
			return (position >= 0 && position < program.length);
		}
		
		public int runAll() {
			while (step()) {};
			return t;
		}
		
		public static Computer createFromFile(File file, Part part) throws IOException {
			List<String> lines = FileUtil.readLinesFromFile(file);
			int [] program = new int[lines.size()];
			for (int i=0; i<program.length; i++)
				program[i] = Integer.parseInt(lines.get(i));
			
			return new Computer(program, part);
		}
		
	}
	
	public static void solvePart1() throws Exception {
		//Computer computer = new Computer(new int [] {0,3,0,1,-3}, Part.A);
		Computer computer = Computer.createFromFile(new File("files/day05/input.txt"), Part.A);
		int steps = computer.runAll();
		System.out.println(steps);
	}
	
	public static void solvePart2() throws Exception {
		//Computer computer = new Computer(new int [] {0,3,0,1,-3}, Part.B);
		Computer computer = Computer.createFromFile(new File("files/day05/input.txt"), Part.B);
		int steps = computer.runAll();
		System.out.println(steps);
	}
	
	public static void main(String [] args) {
		try {
			//solvePart1();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
