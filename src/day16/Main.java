package day16;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import common.FileUtil;

public class Main {
	
	// ----------------------------------------------------------------- Part 1
	// Simple implementation: keep characters in an array and exchange/swap
	// them as indicated. We use a back-buffer to speed up the spin() function,
	// allowing us to arraycopy into the back-buffer and then swap it with the
	// main buffer.  There are several inefficiencies here that could be a
	// a problem in part 2:
	//
	//  * spin() - still has to do an arraycopy of the entire array
	//  * swap() - has to lookup program positions by their character ID
	//  * run()  - parses the commands each time we run them
	
	static class Dance {
		
		char [] programs;
		char [] buffer;
		
		public Dance(int len) {
			programs = new char[len];
			for (int i=0; i<len; i++)
				programs[i] = (char)('a' + i);
			
			buffer = new char[len];
		}
		
		public void spin(int n) {
			//copy new sequence into buffer:
			System.arraycopy(programs, 0, buffer, n, programs.length-n);
			System.arraycopy(programs, programs.length-n, buffer, 0, n);
			
			//swap buffers:
			char [] tmp = programs;
			programs = buffer;
			buffer = tmp;
		}
		
		public void exchange(int a, int b) {
			char tmp = programs[a];
			programs[a] = programs[b];
			programs[b] = tmp;
		}
		
		protected int indexOf(char ch) {
			for (int i=0; i<programs.length; i++) {
				if (programs[i] == ch)
					return i;
			}
			return -1;
		}
		
		public void swap(char aChar, char bChar) {
			exchange(indexOf(aChar), indexOf(bChar));
		}
		
		public void run(String cmd) {
			char ch = cmd.charAt(0);
			String [] parts;
			switch(ch) {
			case 's':
				int n = Integer.parseInt(cmd.substring(1));
				spin(n);
				break;
				
			case 'x':
				parts = cmd.substring(1).split("/");
				int a = Integer.parseInt(parts[0]);
				int b = Integer.parseInt(parts[1]);
				exchange(a, b);
				break;
				
			case 'p':
				parts = cmd.substring(1).split("/");
				char aChar = parts[0].charAt(0);
				char bChar = parts[1].charAt(0);
				swap(aChar, bChar);
				break;
				
			default: throw new IllegalArgumentException("Invalid command: " + cmd);
			}
		}
		
		public String toString() {
			return new String(programs);
		}
		
	}
	
	public static void testPart1() {
		Dance2 dance = new Dance2(5);
		System.out.println(dance.toString());
		
		String [] program = new String [] {
			"s1",	// a spin of size 1: eabcd.
			"x3/4", //swapping the last two programs: eabdc.
			"pe/b"  //swapping programs e and b: baedc.
		};
		for (int i=0; i<program.length; i++) {
			dance.run(program[i]);
			System.out.println(dance.toString());
		}
		
	}
	
	public static void solvePart1() throws Exception {
		Dance2 dance = new Dance2(16);
		
		String line = FileUtil.readLineFromFile(new File("files/day16/input.txt"));
		String [] parts = line.split(",");
		for (int i=0; i<parts.length; i++)
			dance.run(parts[i]);
		
		System.out.println(dance.toString());
	}
	
	// ----------------------------------------------------------------- Part 2
	// In Part 2 I tried to speed up the program (and did so, even though
	// it ultimately didn't matter).  Dance2 runs much faster by:
	//
	//   * Pre-compiling the commands into Command objects that we can run
	//     without having to parse the command again.
	//   * Manages two arrays: one with programs stored by index and one 
	//     with them stored sequentially by ID.  This eliminates the search
	//     for programs by ID since we can use the latter array to jump
	//     right to them. (Of course, there is the added overhead of having
	//     to manage both arrays, but that should be minimal.)
	//   * Avoided array copies by instead keeping a "startIndex" that kept
	//     track of where the array actually begins. spin(n) now becomes a
	//     simple matter of moving the startIndex forward by (len - n).
	//
	// In the end, even all of these improvements were not enough to make
	// it run fast enough to work. The solution was found instead by looking
	// for repeating cycles where we end up at a state we have previously
	// visited. We found a cycle with a relatively short cycle length (30).
	// This allowed us to jump instantly from t = 30 to t = 999999990 and
	// solve the remainder very quickly.
	
	static class Program {
		
		char id;
		int index;
		
		public Program(char id, int index) {
			this.id = id;
			this.index = index;
		}
		
	}
	
	static class Dance2 {
		
		Program [] programsById;
		Program [] programsByIndex;
		int startIndex = 0;
		int len;
		
		public Dance2(int len) {
			this.len = len;
			
			programsById = new Program[len];
			programsByIndex = new Program[len];
			
			for (int i=0; i<len; i++) {
				programsByIndex[i] = new Program((char)('a' + i), i);
				programsById[i] = programsByIndex[i];
			}
		}
		
		public void spin(int n) {
			startIndex = (startIndex + (len - n)) % len;
		}
		
		int aIdx, bIdx;
		int aIdIdx, bIdIdx;
		int tmpIdx;
		
		public void exchange(int a, int b) {
			//since we're modifying startIndex we need to convert a and b
			//to the actual indices in our array
			aIdx = (startIndex + a) % len;
			bIdx = (startIndex + b) % len;
			
			aIdIdx = programsByIndex[aIdx].id - 'a';
			bIdIdx = programsByIndex[bIdx].id - 'a';
			
			programsById[aIdIdx].index = bIdx;
			programsById[bIdIdx].index = aIdx;
			
			programsByIndex[aIdx] = programsById[bIdIdx];
			programsByIndex[bIdx] = programsById[aIdIdx];
		}
		
		public void swap(char aChar, char bChar) {
			aIdIdx = aChar - 'a';
			bIdIdx = bChar - 'a';
			
			tmpIdx = programsById[aIdIdx].index;
			programsById[aIdIdx].index = programsById[bIdIdx].index;
			programsById[bIdIdx].index = tmpIdx;
			
			programsByIndex[programsById[aIdIdx].index] = programsById[aIdIdx];
			programsByIndex[programsById[bIdIdx].index] = programsById[bIdIdx];
		}
		
		public void run(String cmd) {
			char ch = cmd.charAt(0);
			String [] parts;
			switch(ch) {
			case 's':
				int n = Integer.parseInt(cmd.substring(1));
				spin(n);
				break;
				
			case 'x':
				parts = cmd.substring(1).split("/");
				int a = Integer.parseInt(parts[0]);
				int b = Integer.parseInt(parts[1]);
				exchange(a, b);
				break;
				
			case 'p':
				parts = cmd.substring(1).split("/");
				char aChar = parts[0].charAt(0);
				char bChar = parts[1].charAt(0);
				swap(aChar, bChar);
				break;
				
			default: throw new IllegalArgumentException("Invalid command: " + cmd);
			}
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			for (int i=0; i<programsByIndex.length; i++) {
				int realIdx = (startIndex + i) % len;
				s.append(programsByIndex[realIdx].id);
			}
			return s.toString();
		}
		
	}
	
	static interface Command {
		public void run(Dance2 dance);
	}
	
	static class Spin implements Command {
		
		int n;
		
		public Spin(int n) {
			this.n = n;
		}
		
		public void run(Dance2 dance) {
			dance.spin(n);
		}
		
	}
	
	static class Exchange implements Command {
		
		int a;
		int b;
		
		public Exchange(int a, int b) {
			this.a = a;
			this.b = b;
		}
		public void run(Dance2 dance) {
			dance.exchange(a,b);
		}
	}
	
	static class Swap implements Command {
		
		char a;
		char b;
		
		public Swap(char a, char b) {
			this.a = a;
			this.b = b;
		}
		public void run(Dance2 dance) {
			dance.swap(a,b);
		}
	}
	
	public static Command parseCommand(String cmd) {
		char ch = cmd.charAt(0);
		String [] parts;
		switch(ch) {
		case 's':
			int n = Integer.parseInt(cmd.substring(1));
			return new Spin(n);
			
		case 'x':
			parts = cmd.substring(1).split("/");
			int a = Integer.parseInt(parts[0]);
			int b = Integer.parseInt(parts[1]);
			return new Exchange(a, b);
			
		case 'p':
			parts = cmd.substring(1).split("/");
			char aChar = parts[0].charAt(0);
			char bChar = parts[1].charAt(0);
			return new Swap(aChar, bChar);
			
		default: throw new IllegalArgumentException("Invalid command: " + cmd);
		}
	}
	
	public static void solvePart2() throws Exception {
		Dance2 dance = new Dance2(16);
		
		String line = FileUtil.readLineFromFile(new File("files/day16/input.txt"));
		String [] parts = line.split(",");
		
		Command [] commands = new Command[parts.length];
		for (int i=0; i<parts.length; i++)
			commands[i] = parseCommand(parts[i]);
		
		for (int t=0; t<1000000000; t++) {
			if (t % 100000 == 0)
				System.out.println(t);
			
			for (int i=0; i<commands.length; i++)
				commands[i].run(dance);
		}
		
		System.out.println(dance.toString());
	}
	
	/**
	 * Take advantage of fact that we return to original state after 30 steps
	 */
	public static void solvePart2Fast() throws Exception {
		Dance2 dance = new Dance2(16);
		
		String line = FileUtil.readLineFromFile(new File("files/day16/input.txt"));
		String [] parts = line.split(",");
		
		List<String> states = new ArrayList<>(10000000);
		states.add(dance.toString());
		
		Command [] commands = new Command[parts.length];
		for (int i=0; i<parts.length; i++)
			commands[i] = parseCommand(parts[i]);
		
		for (int t=1; t<=1000000000; t++) {
			if (t % 100000 == 0)
				System.out.println(t);
			
			for (int i=0; i<commands.length; i++)
				commands[i].run(dance);
			
			//see if we have encountered this state before:
			String state = dance.toString();
			int index = states.indexOf(state);
			if (index >= 0) {
				System.out.println("Encountered previous state " + index + " at time t=" + t);
				
				int cycleLength = t - index;
				while (1000000000 - t > cycleLength)	//i'm too lazy to figure out the exact equation
					t += cycleLength;
				
				System.out.println("Skipping ahead to time t = " + t);
			}
		}
		
		System.out.println(dance.toString());
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			solvePart2Fast();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
