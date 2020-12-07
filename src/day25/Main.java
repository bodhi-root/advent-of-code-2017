package day25;

import java.util.HashMap;
import java.util.Map;

public class Main {
	
	static class TapeCell {
		
		int index;
		long value;
		
		public TapeCell(int index, long value) {
			this.value = value;
		}
		
	}
	
	static class TuringMachine {
		
		Map<Integer, TapeCell> cells = new HashMap<>(10000);
		int index = 0;
		String state;
		
		public TuringMachine(String state) {
			this.state = state;
		}
		
		public void setState(String state) {
			this.state = state;
		}
		public String getState() {
			return state;
		}
		
		public TapeCell getCell(int index) {
			TapeCell cell = cells.get(index);
			if (cell == null) {
				cell = new TapeCell(index, 0);
				cells.put(index, cell);
			}
			return cell;
		}
		public TapeCell getCurrentCell() {
			return getCell(index);
		}
		
		public void moveTape(int diff) {
			this.index += diff;
		}
		
		public int checkSum() {
			int count = 0;
			for (TapeCell cell : cells.values()) {
				if (cell.value == 1)
					count++;
			}
			return count;
		}
		
	}
	
	static class TestProgram {
		
		TuringMachine machine;
		
		public TestProgram(TuringMachine machine) {
			this.machine = machine;
			machine.setState("A");
		}
		
		public void step() {
			String state = machine.getState();
			TapeCell cell = machine.getCurrentCell();
			
			if (state.equals("A")) {
				if (cell.value == 0) {
					cell.value = 1;
					machine.moveTape(1);
					machine.setState("B");
				} else if (cell.value == 1) {
					cell.value = 0;
					machine.moveTape(-1);
					machine.setState("B");
				} else {
					System.err.println("Value is neither 0 nor 1");
				}
			} else if (state.equals("B")) {
				if (cell.value == 0) {
					cell.value = 1;
					machine.moveTape(-1);
					machine.setState("A");
				} else if (cell.value == 1) {
					cell.value = 1;
					machine.moveTape(1);
					machine.setState("A");
				} else {
					System.err.println("Value is neither 0 nor 1");
				}
			} else {
				System.err.println("Invalid state");
			}

		}
		
	}
	
	static class InputProgram {
		
		TuringMachine machine;
		
		public InputProgram(TuringMachine machine) {
			this.machine = machine;
			machine.setState("A");
		}
		
		public void step() {
			String state = machine.getState();
			TapeCell cell = machine.getCurrentCell();
			
			if (state.equals("A")) {
				if (cell.value == 0) {
					cell.value = 1;
					machine.moveTape(1);
					machine.setState("B");
				} else if (cell.value == 1) {
					cell.value = 0;
					machine.moveTape(-1);
					machine.setState("D");
				} else {
					System.err.println("Value is neither 0 nor 1");
				}
			}
			else if (state.equals("B")) {
				if (cell.value == 0) {
					cell.value = 1;
					machine.moveTape(1);
					machine.setState("C");
				} else if (cell.value == 1) {
					cell.value = 0;
					machine.moveTape(1);
					machine.setState("F");
				} else {
					System.err.println("Value is neither 0 nor 1");
				}
			}
			else if (state.equals("C")) {
				if (cell.value == 0) {
					cell.value = 1;
					machine.moveTape(-1);
					machine.setState("C");
				} else if (cell.value == 1) {
					cell.value = 1;
					machine.moveTape(-1);
					machine.setState("A");
				} else {
					System.err.println("Value is neither 0 nor 1");
				}
			}
			else if (state.equals("D")) {
				if (cell.value == 0) {
					cell.value = 0;
					machine.moveTape(-1);
					machine.setState("E");
				} else if (cell.value == 1) {
					cell.value = 1;
					machine.moveTape(1);
					machine.setState("A");
				} else {
					System.err.println("Value is neither 0 nor 1");
				}
			}
			else if (state.equals("E")) {
				if (cell.value == 0) {
					cell.value = 1;
					machine.moveTape(-1);
					machine.setState("A");
				} else if (cell.value == 1) {
					cell.value = 0;
					machine.moveTape(1);
					machine.setState("B");
				} else {
					System.err.println("Value is neither 0 nor 1");
				}
			}
			else if (state.equals("F")) {
				if (cell.value == 0) {
					cell.value = 0;
					machine.moveTape(1);
					machine.setState("C");
				} else if (cell.value == 1) {
					cell.value = 0;
					machine.moveTape(1);
					machine.setState("E");
				} else {
					System.err.println("Value is neither 0 nor 1");
				}
			}
			else {
				System.err.println("Invalid state");
			}

		}
		
	}
	
	public static void testPart1() throws Exception {
		TuringMachine machine = new TuringMachine("A");
		TestProgram program = new TestProgram(machine);
		for (int i=0; i<6; i++)
			program.step();
		System.out.println("Checksum: " + machine.checkSum());
	}
	
	public static void solvePart1() throws Exception {
		TuringMachine machine = new TuringMachine("A");
		InputProgram program = new InputProgram(machine);
		for (int i=0; i<12317297; i++)
			program.step();
		System.out.println("Checksum: " + machine.checkSum());
	}
	
	public static void solvePart2() throws Exception {
		
	}
	
	public static void main(String [] args) {
		try {
			testPart1();
			solvePart1();
			//solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
