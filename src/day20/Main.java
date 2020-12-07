package day20;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static class World {
		
		List<Particle> particles = new ArrayList<>(1000);
		boolean allowCollisions = false;
		
		public void step() {
			for (Particle particle : particles)
				particle.step();
			
			if (allowCollisions) {
				for (int i=0; i<particles.size(); i++) {
					int removed = 0;
					for (int j=particles.size()-1; j>i; j--) {
						
						if (Main.equals(particles.get(i).x, particles.get(j).x)) {
							particles.remove(j);
							removed++;
							//j--;
						}
					}
					
					if (removed > 0) {
						particles.remove(i);
						i--;
					}
				}
			}
		}
		
		public static World loadFromFile(File file) throws IOException {
			World world = new World();
			
			List<String> lines = FileUtil.readLinesFromFile(file);
			for (int i=0; i<lines.size(); i++) {
				Particle p = Particle.fromText(i, lines.get(i));
				world.particles.add(p);
			}
			
			return world;
		}
		
	}
	
	static class Particle {
		
		int id;
		long [] x;
		long [] v;
		long [] a;
		
		long distance = 0;
		
		public Particle(int id, long [] x, long [] v, long [] a) {
			this.id = id;
			this.x = x;
			this.v = v;
			this.a = a;
		}
		
		public void step() {
			this.distance = 0;
			
			for (int i=0; i<3; i++) {
				v[i] += a[i];
				x[i] += v[i];
				
				this.distance += Math.abs(x[i]);
			}
		}
		
		public long getManhattanDistanceFromOrigin() {
			return this.distance;
		}
		
		//p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>
		public static Particle fromText(int id, String text) {
			long [][] arrays = new long[3][];
			int idx = 0;
			
			for (int i=0; i<3; i++) {
				int idxLeft = text.indexOf('<', idx);
				int idxRight = text.indexOf('>', idxLeft+1);
				
				arrays[i] = parseVector(text.substring(idxLeft+1, idxRight));
				
				idx = idxRight + 1;
			}
			
			return new Particle(id, arrays[0], arrays[1], arrays[2]);
		}
		
		protected static long [] parseVector(String text) {
			String [] parts = text.split(",");
			long [] x = new long[parts.length];
			for (int i=0; i<parts.length; i++)
				x[i] = Long.parseLong(parts[i]);
			
			return x;
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append("id=").append(id);
			s.append(", x=<");
			append(s, x);
			s.append(">, v=<");
			append(s, v);
			s.append(">, a=<");
			append(s, a);
			s.append(">");
			return s.toString();
		}
		
		protected void append(StringBuilder buff, long [] x) {
			buff.append(x[0]);
			for (int i=1; i<x.length; i++)
				buff.append(",").append(x[i]);
		}
		
	}
	
	public static boolean equals(long [] x1, long [] x2) {
		for (int i=0; i<3; i++)
			if (x1[i] != x2[i])
				return false;
		return true;
	}
	
	static Comparator<Particle> SORT_BY_DISTANCE_TO_ORIGIN = new Comparator<Particle>() {

		@Override
		public int compare(Particle o1, Particle o2) {
			return Long.compare(o1.distance,
				                o2.distance);
		}
		
	};
	
	public static void test() {
		Particle p = Particle.fromText(0, "p=<-317,1413,1507>, v=<19,-102,-108>, a=<1,-3,-3>");
		System.out.println(p.toString());
	}
	
	/**
	 * Removes collisions from the list, taking advantage of an assumption that the list is
	 * already sorted by manhattan distance and you can't have collissions if your distance
	 * does not match.
	 */
	protected static void removeCollisionsQuick(List<Particle> particles) {
		
		Particle p1;
		for (int i=0; i<particles.size()-1; i++) {
			p1 = particles.get(i);
			
			int removed = 0;
			for (int j=i+1; j<particles.size(); j++) {
				if (p1.distance == particles.get(j).distance &&
					equals(p1.x, particles.get(j).x)) {
					particles.remove(j);
					j--;
					removed++;
				}
			}
			
			if (removed > 0) {
				particles.remove(i);
				i--;
			}
		}
		
	}
	
	/**
	 * Looks at a list of particles sorted by manhattan distance to origin to determine
	 * if the simulation is done
	 */
	protected static boolean isSimDone(List<Particle> particles) {
		
		//make sure signs of p, v, and a components align:
		double sign;
		for (int i=0; i<3; i++) {
			for (Particle particle : particles) {
				sign = Math.signum((double)particle.x[i]);
				if (Math.signum((double)particle.v[i]) != sign ||
					Math.signum((double)particle.a[i]) != sign)
					return false;
			}
		}
		
		//make sure position coordinates (x,y,z) are sorted by absolute magnitude
		for (int i=0; i<3; i++) {
			long lastX = Math.abs(particles.get(0).x[i]);
			long lastV = Math.abs(particles.get(0).v[i]);
			long lastA = Math.abs(particles.get(0).a[i]);
			
			long tmp;
			for (int j=1; j<particles.size(); j++) {
				tmp = Math.abs(particles.get(j).x[i]);
				if (tmp < lastX)
					return false;
				lastX = tmp;
				
				tmp = Math.abs(particles.get(j).v[i]);
				if (tmp < lastV)
					return false;
				lastV = tmp;
				
				tmp = Math.abs(particles.get(j).a[i]);
				if (tmp < lastA)
					return false;
				
				lastA = tmp;
			}
		}
		
		return true;
	}
	
	public static void runSim(boolean allowCollisions) throws Exception {
		
		World world = World.loadFromFile(new File("files/day20/input.txt"));
		world.allowCollisions = allowCollisions;
		
		//StringBuilder s = new StringBuilder();
		
		int steps = 0;
		
		int closestId = -1;
		int closestIdSteps = 0;
		
		while(true) {
			
			world.step();
			steps++;
			
			world.particles.sort(SORT_BY_DISTANCE_TO_ORIGIN);
			
			/*
			s.setLength(0);
			for (int i=0; i<3; i++) {
				Particle p = world.particles.get(i);
				if (i > 0)
					s.append(", ");
				s.append(p.id).append(" (").append(p.getManhattanDistanceFromOrigin()).append(")");
			}
			System.out.println(s.toString());
			*/
			
			int newClosestId = world.particles.get(0).id;
			if (newClosestId != closestId) {
				closestId = newClosestId;
				closestIdSteps = 0;
			} else {
				closestIdSteps++;
			}
			
			if (steps % 100000 == 0)
				System.out.println(steps + " (" + world.particles.size() + " particles remaining)");
			
			if (steps > 1000000 && closestIdSteps > 100000)
				break;
		}
	
		System.out.println("Simulation ended after " + steps + " steps");
		System.out.println("Particle " + closestId + " is the closest");
		System.out.println(world.particles.size() + " particles remain in world");
	}
	
	public static void solvePart1() throws Exception {
		runSim(false);
	}
	
	public static void runSim2(boolean allowCollisions) throws Exception {
		
		World world = World.loadFromFile(new File("files/day20/input.txt"));
		//world.allowCollisions = allowCollisions;
		
		int steps = 0;
		
		while(true) {
			
			world.step();
			steps++;
			
			if (steps % 100000 == 0)
				System.out.println(steps + " (" + world.particles.size() + " particles remaining)");
			
			world.particles.sort(SORT_BY_DISTANCE_TO_ORIGIN);
			if (allowCollisions)
				removeCollisionsQuick(world.particles);
			
			if (isSimDone(world.particles))
				break;
		}
	
		System.out.println("Simulation ended after " + steps + " steps");
		System.out.println(world.particles.size() + " particles remain in world");
	}
	
	public static void solvePart2() throws Exception {
		runSim(true);
		//doesn't finish, but shows 461 particles after 1 million steps
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
