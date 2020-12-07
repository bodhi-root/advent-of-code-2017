package day10;

/**
 * This knot hash was needed for day 14, so I separated it into its
 * own class.
 */
public class KnotHash {

	public static String hash(String text) {
		Memory memory = new Memory(256);
		
		char [] chars = text.toCharArray();
		
		//convert to integers and append suffix:
		int [] twistLengths = new int[chars.length + 5];
		for (int i=0; i<chars.length; i++)
			twistLengths[i] = (int)chars[i];

		int [] suffix = new int [] {17, 31, 73, 47, 23};
		for (int i=0; i<5; i++)
			twistLengths[chars.length + i] = suffix[i];

		//System.out.println(Arrays.toString(twistLengths));

		for (int rep=0; rep<64; rep++) {
			for (int i=0; i<twistLengths.length; i++)
				memory.twist(twistLengths[i]);
		}
		
		return memory.toDenseHashText();
	}
	
}
