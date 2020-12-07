package day22;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.FileUtil;

public class World {

	Map<Location, GridPoint> points = new HashMap<>(10000);
	
	public GridPoint getPoint(int row, int col) {
		Location loc = new Location(row, col);
		GridPoint point = points.get(loc);
		if (point == null) {
			point = new GridPoint(loc);
			points.put(loc, point);
		}
		return point;
	}
	
	public static World loadFromFile(File file) throws IOException {
		World world = new World();
		
		List<String> lines = FileUtil.readLinesFromFile(file);
		for (int i=0; i<lines.size(); i++) {
			char [] chars = lines.get(i).toCharArray();
			for (int j=0; j<chars.length; j++) {
				GridPoint point = world.getPoint(i, j);
				point.infected = (chars[j] == '#');
			}
		}
		
		return world;
	}
	
	public Virus createVirus(int row, int col, Direction dir) {
		return new Virus(this, row, col, dir);
	}
	
	// --- Inner Classes ------------------------------------------------------
	
	public static class GridPoint {
		
		public Location location;
		public boolean infected;
		
		public GridPoint(Location location) {
			this.location = location;
			this.infected = false;
		}
		
	}
	
	public static class Virus {
		
		World world;
		
		int row;
		int col;
		Direction dir;
		
		int infectionCount = 0;
		
		public Virus(World world, int row, int col, Direction dir) {
			this.world = world;
			this.row = row;
			this.col = col;
			this.dir = dir;
		}
		
		public void step() {
			GridPoint point = world.getPoint(row, col);
			if (point.infected)
				turnRight();
			else
				turnLeft();
				
			point.infected = !point.infected;
			if (point.infected)
				infectionCount++;
			
			moveForward();
		}
		
		public void turnRight() {
			Direction nextDir;
			switch(dir) {
			case UP: nextDir = Direction.RIGHT; break;
			case RIGHT: nextDir = Direction.DOWN; break;
			case DOWN: nextDir = Direction.LEFT; break;
			case LEFT: nextDir = Direction.UP; break;
			default: throw new IllegalStateException();
			}
			this.dir = nextDir;
		}
		public void turnLeft() {
			Direction nextDir;
			switch(dir) {
			case UP: nextDir = Direction.LEFT; break;
			case LEFT: nextDir = Direction.DOWN; break;
			case DOWN: nextDir = Direction.RIGHT; break;
			case RIGHT: nextDir = Direction.UP; break;
			default: throw new IllegalStateException();
			}
			this.dir = nextDir;
		}
		public void moveForward() {
			switch(dir) {
			case UP: row--; break;
			case DOWN: row++; break;
			case LEFT: col--; break;
			case RIGHT: col++; break;
			}
		}
		
	}
	
	
}
