package day17;

import java.util.ArrayList;
import java.util.List;

public class Main {
	
	static class SpinLock {
		
		List<Integer> values = new ArrayList<>(2018);
		
		int t = 0;
		int position = 0;
		
		public SpinLock() {
			values.add(0);
		}
		
		public void step(int stepSize) {
			t++;
			
			this.position = (this.position + stepSize) % values.size();
			values.add(position + 1, t);
			
			this.position++;
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			for (int i=0; i<values.size(); i++) {
				if (i > 0)
					s.append(",");
				
				if (i == position)
					s.append("(").append(values.get(i)).append(")");
				else
					s.append(values.get(i));
			}
			return s.toString();
		}
	}
	
	public static void testPart1() throws Exception {
		SpinLock lock = new SpinLock();
		System.out.println(lock.toString());
		for (int i=0; i<10; i++) {
			lock.step(3);
			System.out.println(lock.toString());
		}
		
		lock = new SpinLock();
		for (int i=0; i<2017; i++)
			lock.step(3);
		int index = lock.values.indexOf(2017);
		System.out.println(lock.values.get(index+1));
	}
	
	public static void solvePart1() throws Exception {
		SpinLock lock = new SpinLock();
		for (int i=0; i<2017; i++)
			lock.step(359);
		
		//System.out.println(lock.values.get(0));
		//System.out.println(lock.values.get(1));
		
		int index = lock.values.indexOf(2017);
		System.out.println(lock.values.get(index+1));
	}
	
	/**
	 * For part 2 we only care about values that are written to index 1.
	 */
	static class SpinLock2 {
		
		int len = 1;
		int position = 0;
		
		int lastValue = 0;
		
		public void step(int stepSize) {
			
			this.position = (this.position + stepSize) % len;
			if (this.position == 0)
				lastValue = len;
			
			this.len++;
			this.position++;
		}
		
		public int getLastValue() {
			return lastValue;
		}
	}
	
	public static void solvePart2() throws Exception {
		SpinLock2 lock = new SpinLock2();
		for (int i=1; i<=50000000; i++) {
			if (i % 100000 == 0)
				System.out.println(i);
			lock.step(359);
		}
		
		System.out.println("Value[1] = " + lock.getLastValue());
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
