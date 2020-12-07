package day22;

import java.io.File;

public class Main {
	
	public static void testPart1() throws Exception {
		World world = World.loadFromFile(new File("files/day22/test.txt"));
		World.Virus virus = world.createVirus(1, 1, Direction.UP);
		for (int i=0; i<10000; i++) {
			virus.step();
			
			if (i == 6) {
				System.out.println("After 7 steps: " + virus.infectionCount);
			} else if (i == 69) {
				System.out.println("After 70 steps: " + virus.infectionCount);
			}
		}
		
		System.out.println("After 10000 steps: " + virus.infectionCount);
	}
	
	public static void solvePart1() throws Exception {
		World world = World.loadFromFile(new File("files/day22/input.txt"));
		World.Virus virus = world.createVirus(12, 12, Direction.UP);
		for (int i=0; i<10000; i++)
			virus.step();
		
		System.out.println("After 10000 steps: " + virus.infectionCount);
	}
	
	public static void testPart2() throws Exception {
		World2 world = World2.loadFromFile(new File("files/day22/test.txt"));
		World2.Virus virus = world.createVirus(1, 1, Direction.UP);
		for (int i=0; i<10000000; i++) {
			virus.step();
			
			if (i == 99) {
				System.out.println("After 100 steps: " + virus.infectionCount);
			}
		}
		
		System.out.println("After 10,000,000 steps: " + virus.infectionCount);
	}
	
	public static void solvePart2() throws Exception {
		World2 world = World2.loadFromFile(new File("files/day22/input.txt"));
		World2.Virus virus = world.createVirus(12, 12, Direction.UP);
		for (int i=0; i<10000000; i++)
			virus.step();
			
		System.out.println("After 10,000,000 steps: " + virus.infectionCount);
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			//testPart2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
