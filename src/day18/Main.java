package day18;

import java.io.File;
import java.util.List;

import common.FileUtil;

public class Main {
	
	public static void testPart1() throws Exception {
		List<String> program = FileUtil.readLinesFromFile(new File("files/day18/test.txt"));
		Computer1 computer = new Computer1();
		computer.setProgram(program);
		
		computer.runToFirstRecover();
	}
	
	public static void solvePart1() throws Exception {
		List<String> program = FileUtil.readLinesFromFile(new File("files/day18/input.txt"));
		Computer1 computer = new Computer1();
		computer.setProgram(program);
		
		computer.runToFirstRecover();
	}
	
	public static void solvePart2() throws Exception {
		List<String> program = FileUtil.readLinesFromFile(new File("files/day18/input.txt"));
		
		Computer2 computer0 = new Computer2(0);
		Computer2 computer1 = new Computer2(1);
		
		computer0.setProgram(program);
		computer1.setProgram(program);
		
		Computer2.MessageQueue output0 = new Computer2.MessageQueue(0, 1);
		Computer2.MessageQueue output1 = new Computer2.MessageQueue(1, 0);
		computer0.output = output0;
		computer1.output = output1;
		computer0.input = output1;
		computer1.input = output0;
		
		while(true) {
			String cmd0 = computer0.step();
			String cmd1 = computer1.step();
			
			//see if both programs terminated:
			if (cmd0 == null && cmd1 == null)
				break;
			
			//check for deadlock:
			if (computer0.state == Computer2.State.WAITING_ON_INPUT &&
				computer1.state == Computer2.State.WAITING_ON_INPUT)
				break;
		}
		
		System.out.println("Total writes from program 1: " + output1.writeCount);
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
