package day21;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static class Image {
		
		int [][] pixels;
		
		public Image(int [][] pixels) {
			this.pixels = pixels;
		}
		
		public int getTotalPixelOnCount() {
			int count = 0;
			for (int i=0; i<pixels.length; i++) {
				for (int j=0; j<pixels[i].length; j++) {
					count += pixels[i][j];
				}
			}
			return count;
		}
		
		public void apply(List<Rule> rules) {
			int inputSize = pixels.length;
			
			int inputPatternSize = (inputSize % 2 == 0) ? 2 : 3;
			int outputPatternSize = inputPatternSize + 1;
			
			int outputSize = (inputSize / inputPatternSize) * outputPatternSize;
			int [][] output = new int[outputSize][outputSize];
			
			int inputStartRow = 0;
			int inputStartCol = 0;
			int outputStartRow = 0;
			int outputStartCol = 0;
			
			while (inputStartRow < inputSize) {
				
				//find matching rule to produce output
				boolean matchFound = false;
				for (Rule rule : rules) {
					if (rule.getPatternSize() == inputPatternSize &&
						isMatch(rule, inputStartRow, inputStartCol)) {
						
						for (int i=0; i<outputPatternSize; i++) {
							for (int j=0; j<outputPatternSize; j++) {
								output[outputStartRow + i][outputStartCol + j] = rule.output[i][j];
							}
						}
						
						matchFound = true;
						break;
					}
				}
				if (!matchFound)
					throw new IllegalStateException("No pattern found to match at (" + inputStartRow + "," + inputStartCol + ")");
				
				//move to next section (typewriter style):
				inputStartCol += inputPatternSize;
				outputStartCol += outputPatternSize;
				if (inputStartCol >= inputSize) {
					inputStartCol = 0;
					inputStartRow += inputPatternSize;
					outputStartCol = 0;
					outputStartRow += outputPatternSize;
				}
				
			}
			
			this.pixels = output;
		}
		
		protected boolean isMatch(Rule rule, int startRow, int startCol) {
			int patternSize = rule.getPatternSize();
			
			//start = (top,left). move = left to right, top to bottom
			boolean match = true;
			for (int i=0; i<patternSize; i++) {
				for (int j=0; j<patternSize; j++) {
					if (pixels[startRow+i][startCol+j] != rule.pattern[i][j]) {
						match = false;
						break;
					}
				}
			}
			if (match)
				return true;
			
			//start = (bottom, left). move = left to right, bottom to top
			match = true;
			for (int i=0; i<patternSize; i++) {
				for (int j=0; j<patternSize; j++) {
					if (pixels[startRow+i][startCol+j] != rule.pattern[patternSize-1-i][j]) {
						match = false;
						break;
					}
				}
			}
			if (match)
				return true;
			
			//start = (top, left). move = top to bottom, left to right
			match = true;
			for (int i=0; i<patternSize; i++) {
				for (int j=0; j<patternSize; j++) {
					if (pixels[startRow+i][startCol+j] != rule.pattern[j][i]) {
						match = false;
						break;
					}
				}
			}
			if (match)
				return true;
			
			//start = (bottom, left). move = bottom to top, left to right
			match = true;
			for (int i=0; i<patternSize; i++) {
				for (int j=0; j<patternSize; j++) {
					if (pixels[startRow+i][startCol+j] != rule.pattern[patternSize-1-j][i]) {
						match = false;
						break;
					}
				}
			}
			if (match)
				return true;
			
			//start = (top,right). move = right to left, top to bottom
			match = true;
			for (int i=0; i<patternSize; i++) {
				for (int j=0; j<patternSize; j++) {
					if (pixels[startRow+i][startCol+j] != rule.pattern[i][patternSize-1-j]) {
						match = false;
						break;
					}
				}
			}
			if (match)
				return true;
			
			//start = (bottom,right). move = right to left, bottom to top
			match = true;
			for (int i=0; i<patternSize; i++) {
				for (int j=0; j<patternSize; j++) {
					if (pixels[startRow+i][startCol+j] != rule.pattern[patternSize-1-i][patternSize-1-j]) {
						match = false;
						break;
					}
				}
			}
			if (match)
				return true;
			
			//start = (top,right). move = top to bottom, right to left
			match = true;
			for (int i=0; i<patternSize; i++) {
				for (int j=0; j<patternSize; j++) {
					if (pixels[startRow+i][startCol+j] != rule.pattern[j][patternSize-1-i]) {
						match = false;
						break;
					}
				}
			}
			if (match)
				return true;
			
			//start = (bottom,right). move = bottom to top, right to left
			match = true;
			for (int i=0; i<patternSize; i++) {
				for (int j=0; j<patternSize; j++) {
					if (pixels[startRow+i][startCol+j] != rule.pattern[patternSize-1-j][patternSize-1-i]) {
						match = false;
						break;
					}
				}
			}
			if (match)
				return true;
			
			return false;
		}
		
		public void print(PrintStream out) {
			StringBuilder s = new StringBuilder();
			for (int i=0; i<pixels.length; i++) {
				s.setLength(0);
				for (int j=0; j<pixels[i].length; j++) {
					if (pixels[i][j] == 0)
						s.append('.');
					else 
						s.append('#');
				}
				System.out.println(s.toString());
				
			}
		}
		
	}
	
	static class Rule {
		
		int [][] pattern;
		int [][] output;
		
		public Rule(int [][] pattern, int [][] output) {
			this.pattern = pattern;
			this.output = output;
		}
		
		public int getPatternSize() {
			return pattern.length;
		}
		
		/**
		 * example:
		 * .#./..#/### => #..#/..../..../#..#
		 */
		public static Rule parseFrom(String text) {
			String [] parts = text.split("\\s+");
			int [][] pattern = parseCompactPattern(parts[0]);
			int [][] output = parseCompactPattern(parts[2]);
			return new Rule(pattern, output);
		}
		
		protected static int [][] parseCompactPattern(String text) {
			String [] parts = text.split("\\/");
			int [][] pattern = new int[parts.length][parts.length];
			for (int i=0; i<parts.length; i++) {
				for (int j=0; j<parts[i].length(); j++) {
					char ch = parts[i].charAt(j);
					if (ch == '.')
						pattern[i][j] = 0;
					else if (ch == '#')
						pattern[i][j] = 1;
					else
						throw new IllegalArgumentException("Invalid character: '" + ch + "'");
				}
			}
			return pattern;
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			
			for (int i=0; i<pattern.length; i++) {
				if (i > 0)
					s.append('/');
				for (int j=0; j<pattern[i].length; j++) 
					s.append(pattern[i][j] == 1 ? '#' : '.');
			}
			s.append(" => ");
			for (int i=0; i<output.length; i++) {
				if (i > 0)
					s.append('/');
				for (int j=0; j<output[i].length; j++) 
					s.append(output[i][j] == 1 ? '#' : '.');
			}
			
			return s.toString();
		}
		
	}
	
	public static Image getStartingImage() {
		/*
		.#.
		..#
		###
		*/
		return new Image(new int [][] {
			{0,1,0},
			{0,0,1},
			{1,1,1}
		});
	}
	
	public static void run(List<Rule> rules, int iterations) {
		Image image = getStartingImage();
		image.print(System.out);
		System.out.println();
		
		for (int i=0; i<iterations; i++) {
			image.apply(rules);
			image.print(System.out);
			System.out.println();
		}
		
		System.out.println();
		System.out.println("On count = " + image.getTotalPixelOnCount());
	}
	
	public static void testPart1() throws Exception {
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.parseFrom("../.# => ##./#../..."));
		rules.add(Rule.parseFrom(".#./..#/### => #..#/..../..../#..#"));
		
		for (Rule rule : rules) 
			System.out.println(rule.toString());
		System.out.println();
		
		run(rules, 2);
	}
	
	public static void solvePart1() throws Exception {
		//parse rules:
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day21/input.txt"));
		List<Rule> rules = new ArrayList<>(lines.size());
		for (String line : lines)
			rules.add(Rule.parseFrom(line));
		
		run(rules, 5);
	}
	
	public static void solvePart2() throws Exception {
		//parse rules:
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day21/input.txt"));
		List<Rule> rules = new ArrayList<>(lines.size());
		for (String line : lines)
			rules.add(Rule.parseFrom(line));

		run(rules, 18);
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
