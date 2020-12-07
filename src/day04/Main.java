package day04;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.FileUtil;

public class Main {
	
	static interface Rule {
		boolean isValid(String text);
	}
	
	static class Rule1 implements Rule {
		
		public boolean isValid(String text) {
			String [] parts = text.split("\\s+");
			Set<String> set = new HashSet<>(parts.length);
			for (String part : parts) {
				if (!set.add(part))
					return false;
			}
			
			return true;
		}
		
	}
	
	static class Rule2 implements Rule {
		
		public boolean isValid(String text) {
			String [] parts = text.split("\\s+");
			Set<String> set = new HashSet<>(parts.length);
			for (String part : parts) {
				part = sortChars(part);
				if (!set.add(part))
					return false;
			}
			
			return true;
		}
		
		protected String sortChars(String text) {
			char [] chars = text.toCharArray();
			Arrays.sort(chars);
			return new String(chars);
		}
		
	}
	
	public static void countValidPasswords(Rule rule) throws Exception {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day04/input.txt"));
		
		int validCount = 0;
		for (String line : lines) {
			if (rule.isValid(line))
				validCount++;
		}
		
		System.out.println(validCount + " (out of " + lines.size() + ") passwords are valid");
	}
	
	public static void solvePart1() throws Exception {
		countValidPasswords(new Rule1());
	}
	
	public static void solvePart2() throws Exception {
		countValidPasswords(new Rule2());
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
