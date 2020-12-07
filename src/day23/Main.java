package day23;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import common.FileUtil;

public class Main {
	
	public static void solvePart1() throws Exception {
		List<String> program = FileUtil.readLinesFromFile(new File("files/day23/input.txt"));
		Computer computer = new Computer();
		computer.setProgram(program);
		computer.runAll();
		System.out.println("'mul' called " + computer.multiplyCount + " times");
	}
	
	/**
	 * Helper function to step through the program, printing out each command
	 * and the register state at each point.  This was helpful in making
	 * sure I was stepping through it correctly.  Since the program runs
	 * forever this function does not return.
	 */
	public static void testPart2() throws Exception {
		List<String> program = FileUtil.readLinesFromFile(new File("files/day23/input.txt"));
		Computer computer = new Computer();
		computer.setProgram(program);
		computer.getRegister("a").value = 1;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int commandIndex = 1;
		
		int skip = 1691189;
		for (int i=0; i<skip; i++) {
			computer.step();
			commandIndex++;
		}
		
		while(true) {
			
			computer.printRegisterState();
			String cmd = computer.getNextCommand();
			System.out.println(commandIndex + ": " + cmd);
			
			System.out.print("[Enter to continue]: ");
			in.readLine();
			
			computer.step();
			commandIndex++;
		}
		//computer.runAll();
		
		//System.out.println("Register(h) = " + computer.getRegister("h").value);
	}
	
	/**
	 * If you step through the program and its key parts, I think it
	 * is trying to implement the algorithm below... it gave the
	 * right answer, anyway.
	 */
	public static void solvePart2() {
		int h = 0;
		for (int b=105700; b<=122700; b+=17) {
			
			boolean divisible = false;
			for (int d=2; d<105700; d++) {
						
				int e = 2;
				int g = d * e - b;

				if (g % d == 0)
					divisible = true;

			}
			
			if (divisible)
				h++;
		}
		System.out.println("h = " + h);
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
