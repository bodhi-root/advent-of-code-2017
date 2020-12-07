package day03;

import java.util.HashMap;
import java.util.Map;

public class Main {

	static class XY {
		
		int x;
		int y;
		
		public XY(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean equals(Object o) {
			XY that = (XY)o;
			return (this.x == that.x && this.y == that.y);
		}
		public int hashCode() {
			return x ^ 7 + y;
		}
		
		public int getManhattanDistanceToOrigin() {
			return Math.abs(x) + Math.abs(y);
		}
		
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
		
	}
	
	static enum Direction {RIGHT, UP, LEFT, DOWN};
	
	public static XY getCoordinatesFor(int value) {
		if (value <= 0)
			throw new IllegalArgumentException("Value must be positive");
		
		int x = 0;
		int y = 0;
		
		//we can lay out the spiral by moving:
		// RIGHT 1, UP 1, LEFT 2, DOWN 2,
		// RIGHT 3, UP 3, LEFT 4, DOWN 4, ...
		Direction dir = Direction.RIGHT;
		int targetDistance = 1;
		int actualDistance = 0;
		
		for (int i=1; i<value; i++) {
			switch(dir) {
			case RIGHT: x++; break;
			case UP:    y++; break;
			case LEFT:  x--; break;
			case DOWN:  y--; break;
			}
			
			actualDistance++;
			
			//turn:
			if (targetDistance == actualDistance) {
				actualDistance = 0;
				switch(dir) {
				case RIGHT: dir = Direction.UP; break;
				case UP:    dir = Direction.LEFT; targetDistance++; break;
				case LEFT:  dir = Direction.DOWN; break;
				case DOWN:  dir = Direction.RIGHT; targetDistance++; break;
				}
			}
		}
		
		return new XY(x, y);
	}
	
	public static int findFirstSumGreaterThan(int targetSum) {
		if (targetSum <= 0)
			throw new IllegalArgumentException("Value must be positive");
		
		Map<XY, Integer> map = new HashMap<>(1000);
		
		int x = 0;
		int y = 0;
		
		map.put(new XY(x,y), 1);
		
		//we can lay out the spiral by moving:
		// RIGHT 1, UP 1, LEFT 2, DOWN 2,
		// RIGHT 3, UP 3, LEFT 4, DOWN 4, ...
		Direction dir = Direction.RIGHT;
		int targetDistance = 1;
		int actualDistance = 0;
		
		while(true) {
			switch(dir) {
			case RIGHT: x++; break;
			case UP:    y++; break;
			case LEFT:  x--; break;
			case DOWN:  y--; break;
			}
			
			XY xy = new XY(x, y);
			int sum = map.getOrDefault(new XY(x+1,y), 0) +
					  map.getOrDefault(new XY(x+1,y+1), 0) +
					  map.getOrDefault(new XY(x,  y+1), 0) +
					  map.getOrDefault(new XY(x-1,y+1), 0) +
					  map.getOrDefault(new XY(x-1,y), 0) +
					  map.getOrDefault(new XY(x-1,y-1), 0) +
					  map.getOrDefault(new XY(x,  y-1), 0) +
					  map.getOrDefault(new XY(x+1,y-1), 0);
			
			map.put(xy, sum);
			if (sum >= targetSum) {
				return sum;
			}
			
			actualDistance++;
			
			//turn:
			if (targetDistance == actualDistance) {
				actualDistance = 0;
				switch(dir) {
				case RIGHT: dir = Direction.UP; break;
				case UP:    dir = Direction.LEFT; targetDistance++; break;
				case LEFT:  dir = Direction.DOWN; break;
				case DOWN:  dir = Direction.RIGHT; targetDistance++; break;
				}
			}
		}
		
	}
	
	public static void main(String [] args) {
		//doPart1();
		doPart2();
	}
	
	public static void doPart1() {
		for (int i=0; i<13; i++) {
			System.out.println((i+1) + ": " + getCoordinatesFor(i+1));
		}
		
		System.out.println(getCoordinatesFor(12).getManhattanDistanceToOrigin());	//3
		System.out.println(getCoordinatesFor(1024).getManhattanDistanceToOrigin());	//31
		
		System.out.println(getCoordinatesFor(325489).getManhattanDistanceToOrigin());
	}
	
	public static void doPart2() {
		System.out.println(findFirstSumGreaterThan(100));
		System.out.println(findFirstSumGreaterThan(325489));
	}
	
}
