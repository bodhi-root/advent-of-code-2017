package day22;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.FileUtil;

public class World2 {

	Map<Location, GridPoint> points = new HashMap<>(10000);
	
	public GridPoint getPoint(int row, int col) {
		Location loc = new Location(row, col);
		GridPoint point = points.get(loc);
		if (point == null) {
			point = new GridPoint(loc, State.CLEAN);
			points.put(loc, point);
		}
		return point;
	}
	
	public static World2 loadFromFile(File file) throws IOException {
		World2 world = new World2();
		
		List<String> lines = FileUtil.readLinesFromFile(file);
		for (int i=0; i<lines.size(); i++) {
			char [] chars = lines.get(i).toCharArray();
			for (int j=0; j<chars.length; j++) {
				GridPoint point = world.getPoint(i, j);
				point.state = (chars[j] == '#') ? State.INFECTED : State.CLEAN;
			}
		}
		
		return world;
	}
	
	public Virus createVirus(int row, int col, Direction dir) {
		return new Virus(this, row, col, dir);
	}
	
	// --- Inner Classes ------------------------------------------------------
	
	public static enum State {CLEAN, WEAKENED, INFECTED, FLAGGED};
	
	public static class GridPoint {
		
		public Location location;
		public State state;
		
		public GridPoint(Location location, State state) {
			this.location = location;
			this.state = state;
		}
		
		public void advanceState() {
			State nextState;
			switch(state) {
			case CLEAN:    nextState = State.WEAKENED; break;
			case WEAKENED: nextState = State.INFECTED; break;
			case INFECTED: nextState = State.FLAGGED;  break;
			case FLAGGED:  nextState = State.CLEAN;    break;
			default: throw new IllegalStateException();
			}
			this.state = nextState;
		}
		
		public boolean isInfected() {
			return (state == State.INFECTED);
		}
		
	}
	
	public static class Virus {
		
		World2 world;
		
		int row;
		int col;
		Direction dir;
		
		int infectionCount = 0;
		
		public Virus(World2 world, int row, int col, Direction dir) {
			this.world = world;
			this.row = row;
			this.col = col;
			this.dir = dir;
		}
		
		public void step() {
			GridPoint point = world.getPoint(row, col);
			switch(point.state) {
			case CLEAN: turnLeft(); break;
			case WEAKENED: break;
			case INFECTED: turnRight(); break;
			case FLAGGED: reverse(); break;
			}
			
			point.advanceState();
			if (point.isInfected())
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
		
		public void reverse() {
			Direction nextDir;
			switch(dir) {
			case UP: nextDir = Direction.DOWN; break;
			case LEFT: nextDir = Direction.RIGHT; break;
			case DOWN: nextDir = Direction.UP; break;
			case RIGHT: nextDir = Direction.LEFT; break;
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
