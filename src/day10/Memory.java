package day10;

import java.util.Arrays;

public class Memory {

	int [] state;
	int position = 0;
	int skipLength = 0;
	
	public Memory(int size) {
		this.state = new int[size];
		for (int i=0; i<size; i++)
			this.state[i] = i;
	}
	
	public void twist(int twistLength) {
		
		if (twistLength > state.length)
			throw new IllegalArgumentException("Twist lengths longer than state length are not allowed");
		
		int halfTwistLength = twistLength / 2;
		int x1, x2, tmp;
		for (int i=0; i<halfTwistLength; i++) {
			x1 = (position + i) % state.length;
			x2 = (position + twistLength - i - 1) % state.length;
			
			//swap values:
			tmp = state[x1];
			state[x1] = state[x2];
			state[x2] = tmp;
		}
		
		this.position = (this.position + twistLength + this.skipLength) % this.state.length;
		this.skipLength++;
	}
	
	public String toString() {
		return Arrays.toString(state);
	}
	
	public int [] toDenseHashBytes() {
		int denseLength = this.state.length / 16;
		if (this.state.length % 16 != 0)
			throw new IllegalStateException("State length must be a multiple of 16");
		
		int [] denseHash = new int[denseLength];
		for (int i=0; i<denseLength; i++) {
			int value = state[i*16];
			for (int j=1; j<16; j++)
				value = value ^ state[i*16+j];
			denseHash[i] = value;
		}
		return denseHash;
	}
	
	public String toDenseHashText() {
		StringBuilder s = new StringBuilder();
		
		int [] bytes = toDenseHashBytes();
		//System.out.println(Arrays.toString(bytes));
		for (int i=0; i<bytes.length; i++) {
			int upper = (bytes[i] & 0xf0) >> 4;
			s.append(Character.forDigit(upper, 16));
			int lower = (bytes[i] & 0x0f);
			s.append(Character.forDigit(lower, 16));
		}
		return s.toString();
	}
	
}
