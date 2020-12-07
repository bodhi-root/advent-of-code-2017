package day19;

import java.io.File;
import java.io.IOException;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static class World {
		
		char [][] map;
		int height;
		int width;
		
		public World(int height, int width) {
			this.map = new char[height][width];
			this.height = height;
			this.width = width;
		}
		
		public static World loadFromFile(File file) throws IOException {
			
			List<String> lines = FileUtil.readLinesFromFile(file);
			int height = lines.size();
			int width = lines.get(0).length();
			
			World world = new World(height, width);
			
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					world.map[i][j] = lines.get(i).charAt(j);
				}
			}
			
			return world;
		}
		
		public int getStartingColumn() {
			for (int j=0; j<width; j++) {
				if (map[0][j] == '|')
					return j;
			}
			return -1;
		}
		
		public Explorer explore() {
			
			int startingColumn = getStartingColumn();
			Explorer explorer = new Explorer(this, 0, startingColumn);
			explorer.explore();
			return explorer;
			
		}
		
	}
	
	static class Explorer {
		
		World world;
		int i;
		int j;
	
		int di = 1;
		int dj = 0;
		
		int steps = 0;
		StringBuilder buff = new StringBuilder();
		
		public Explorer(World world, int i, int j) {
			this.world = world;
			this.i = i;
			this.j = j;
		}
		
		public String getText() {
			return buff.toString();
		}
		public int getTotalSteps() {
			return steps;
		}
		
		/**
		 * Peek at the given location on the map. This will return -1 if the location
		 * is outside the boundaries of the map.
		 */
		protected int peek(int i, int j) {
			if (i < 0 || i >= world.height ||
				j < 0 || j >= world.width) {		
				return -1;
			}
			return world.map[i][j];
		}
		
		protected boolean isValidTrack(int i, int j) {
			int ch = peek(i, j);
			if (ch == -1 || ch == ' ')
				return false;
			
			return true;
		}
		
		public int step() {
			//see if we need to turn:
			if (world.map[i][j] == '+') {
				
				//horizontal (look up and down):
				if (di == 0) {
					if (isValidTrack(i-1,j)) {
						di = -1;
						dj = 0;
					} else if (isValidTrack(i+1, j)) {
						di = +1;
						dj = 0;
					} else {
						System.out.println("On '+' with nowhere to turn. Path stopped.");
						return -1;
					}
				} 
				//vertical (look left and right):
				else {
					if (isValidTrack(i, j-1)) {
						di = 0;
						dj = -1;
					} else if (isValidTrack(i, j+1)) {
						di = 0;
						dj = +1;
					} else {
						System.out.println("On '+' with nowhere to turn. Path stopped.");
						return -1;
					}
				}
				
			}
			
			//advance (inspecting next position first to make sure it is OK):
			int iNext = i + di;
			int jNext = j + dj;
			
			if (iNext < 0 || iNext >= world.height ||
				jNext < 0 || jNext >= world.width) {
				System.out.println("Next position is outside of world boundaries");
				return -1;
			}
			
			if (world.map[iNext][jNext] == ' ') {
				System.out.println("Next position is empty. Path stopped.");
				return -1;
			}
			
			this.i = iNext;
			this.j = jNext;
			this.steps++;
			//System.out.println(steps + " (" + i + "," + j + ") = '" + world.map[i][j] + "'");
			
			//collect letter:
			if ((world.map[i][j] >= 'A' && world.map[i][j] <= 'Z') ||
				(world.map[i][j] >= 'a' && world.map[i][j] <= 'z')) {
				buff.append(world.map[i][j]);
			}
			
			return world.map[i][j];
		}
		
		public void explore() {
			while(step() != -1) {
				//do nothing
			}
		}
		
	}
	
	public static void test() throws Exception {
		World world = World.loadFromFile(new File("files/day19/test.txt"));
		Explorer explorer = world.explore();
		System.out.println("Text = " + explorer.getText());
		System.out.println("Distance = " + (explorer.getTotalSteps() + 1));
	}
	
	public static void solvePart1() throws Exception {
		World world = World.loadFromFile(new File("files/day19/input.txt"));
		System.out.println(world.explore().getText());
	}
	
	public static void solvePart2() throws Exception {
		World world = World.loadFromFile(new File("files/day19/input.txt"));
		Explorer explorer = world.explore();
		System.out.println("Text = " + explorer.getText());
		System.out.println("Distance = " + (explorer.getTotalSteps() + 1));
	}
	
	public static void main(String [] args) {
		try {
			test();
			//solvePart1();
			//solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
