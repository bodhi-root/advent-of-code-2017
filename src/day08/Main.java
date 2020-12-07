package day08;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.FileUtil;

public class Main {
	
	static class Register {
		
		String name;
		int value;
		
		public Register(String name, int value) {
			this.name = name;
			this.value = value;
		}
		public Register(String name) {
			this(name, 0);
		}
		
		public Register copy() {
			return new Register(name, value);
		}
		
	}
	
	static class Computer {
		
		Map<String, Register> registers = new HashMap<>(1000);
		
		public Register getRegister(String name) {
			Register register = registers.get(name);
			if (register == null) {
				register = new Register(name);
				registers.put(name, register);
			}
			
			return register;
		}
		
		public void run(String text) {
			/** sample:
			b inc 5 if a > 1
			a inc 1 if b < 5
			c dec -10 if a >= 1
			c inc -20 if c == 10
			*/
			String [] parts = text.split("\\s+");
			
			String reg = parts[0];
			String op = parts[1];
			int opValue = Integer.parseInt(parts[2]);
			
			String testReg = parts[4];
			String testComp = parts[5];
			int testValue = Integer.parseInt(parts[6]);
			
			int testRegValue = getRegister(testReg).value;
			if (test(testRegValue, testComp, testValue)) {
				
				Register register = getRegister(reg);
				if (op.equals("inc")) 
					register.value += opValue;
				else if (op.equals("dec"))
					register.value -= opValue;
				else
					throw new IllegalStateException("Unknown operation: '" + op + "'");
				
			}
		}
		
		public boolean test(int x1, String comp, int x2) {
			if (comp.equals("=="))
				return x1 == x2;
			else if (comp.equals("!="))
				return x1 != x2;
			else if (comp.equals(">="))
				return x1 >= x2;
			else if (comp.equals(">"))
				return x1 > x2;
			else if (comp.equals("<="))
				return x1 <= x2;
			else if (comp.equals("<"))
				return x1 < x2;
			else
				throw new IllegalStateException("Unknown comparison: '" + comp + "'");
		}
		
		public Register getMaxRegister() {
			Register max = null;
			for (Register test : registers.values()) {
				if (max == null || test.value > max.value)
					max = test;
			}
			
			return max;
		}
		
	}
	
	public static void solvePart1() throws Exception {
		Computer computer = new Computer();
		
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day08/input.txt"));
		for (String line : lines)
			computer.run(line);
			
		Register max = computer.getMaxRegister();
		System.out.println(max.name + " = " + max.value);
	}
	
	public static void solvePart2() throws Exception {
		Computer computer = new Computer();
		
		Register max = null;
		
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day08/input.txt"));
		for (String line : lines) {
			computer.run(line);
			
			Register test = computer.getMaxRegister();
			if (max == null || test.value > max.value)
				max = test.copy();
		}
			
		System.out.println(max.name + " = " + max.value);
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
