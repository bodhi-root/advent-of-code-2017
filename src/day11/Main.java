package day11;

import java.io.File;

import common.FileUtil;

public class Main {
	
	static enum Direction {N, NE, SE, S, SW, NW};
	
	/**
	 * We keep track of our location using typical (x,y) coordinates.  We imagine
	 * our hexagons are laid out so that moving north or south will move us 2
	 * units while moving diagonally will only move us up/down 1 and over 1. This
	 * arrangement is geometrically feasible if we squish the hexagons to the
	 * desired dimensions.
	 * 
	 * We now are able to keep track of how many diagonal moves we have made
	 * via the x coordinate.  We can also calculate how many up or down 
	 * movements we would need to make after making diagonal moves.  This is
	 * all that is needed to calculate the distance of any point to the center.
	 */
	static class Location {
		
		int x = 0;
		int y = 0;
		
		public void move(Direction dir) {
			switch(dir) {
			case N:  y += 2; break;
			case NE: y += 1; x += 1; break;
			case SE: y -= 1; x += 1; break;
			case S:  y -= 2; break;
			case SW: y -= 1; x -= 1; break;
			case NW: y += 1; x -= 1; break;
			default: throw new IllegalStateException("Invalid direction: " + dir);
			}
		}
		
		public String toString() {
			return "(" + x + "," + y + ")";
		}
		
		public int getStepsToLocation() {
			int x = Math.abs(this.x);
			int y = Math.abs(this.y);
			
			//how many diagonal moves do we have to make?
			//NOTE: if diagMoves > y we can zig-zag our way back to the origin
			int diagMoves = x;
			if (diagMoves > y)
				return diagMoves;
			
			int vertMoves = (y - diagMoves) / 2;
			return diagMoves + vertMoves;
		}
		
	}
	
	public static Direction parseDirection(String text) {
		if (text.equals("n"))       return Direction.N;
		else if (text.equals("ne")) return Direction.NE;
		else if (text.equals("se")) return Direction.SE;
		else if (text.equals("s"))  return Direction.S;
		else if (text.equals("sw")) return Direction.SW;
		else if (text.equals("nw")) return Direction.NW;
		else
			throw new IllegalArgumentException("Invalid diretion: " + text);
	}
	
	public static int calculateDistanceOfPath(String input) {
		String [] parts = input.split(",");
		Location location = new Location();
		
		for (int i=0; i<parts.length; i++) {
			Direction dir = parseDirection(parts[i]);
			location.move(dir);
			System.out.println(location);
		}
		
		int steps = location.getStepsToLocation();
		System.out.println("Steps = " + steps);
		return steps;
	}
	
	public static int calculateMaxDistanceOfPath(String input) {
		String [] parts = input.split(",");
		Location location = new Location();
		
		int maxDistance = 0;
		
		for (int i=0; i<parts.length; i++) {
			Direction dir = parseDirection(parts[i]);
			location.move(dir);
			int distance = location.getStepsToLocation();
			maxDistance = Math.max(maxDistance, distance);
		}
		
		int steps = location.getStepsToLocation();
		System.out.println("Steps = " + steps);
		System.out.println("Max Steps = " + maxDistance);
		return maxDistance;
	}
	
	public static void test() {
		calculateDistanceOfPath("ne,ne,ne");		// is 3 steps away.
		calculateDistanceOfPath("ne,ne,sw,sw");		// is 0 steps away (back where you started).
		calculateDistanceOfPath("ne,ne,s,s");		// is 2 steps away (se,se).
		calculateDistanceOfPath("se,sw,se,sw,sw");	// is 3 steps away (s,s,sw).
	}
	
	public static void solvePart1() throws Exception {
		String line = FileUtil.readLineFromFile(new File("files/day11/input.txt"));
		calculateDistanceOfPath(line);
	}
	
	public static void solvePart2() throws Exception {
		String line = FileUtil.readLineFromFile(new File("files/day11/input.txt"));
		calculateMaxDistanceOfPath(line);
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
