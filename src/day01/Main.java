package day01;

import java.io.File;

import common.FileUtil;

public class Main {

	public static int sumText(String text) {
		int sum = 0;
		
		char [] chars = text.toCharArray();
		for (int i=0; i<chars.length; i++) {
			int iNext = (i + 1) % chars.length;
			if (chars[i] == chars[iNext]) {
				sum += Character.getNumericValue(chars[i]);
			}
		}
		
		return sum;
	}
	
	public static int sumText(String text, int lookAhead) {
		int sum = 0;
		
		char [] chars = text.toCharArray();
		for (int i=0; i<chars.length; i++) {
			int iNext = (i + lookAhead) % chars.length;
			if (chars[i] == chars[iNext]) {
				sum += Character.getNumericValue(chars[i]);
			}
		}
		
		return sum;
	}
	
	public static void main(String [] args) {
		//doPart1();
		doPart2();
	}
	
	public static void doPart1() {
		System.out.println(sumText("1122")); // 3
		System.out.println(sumText("1111")); // 4
		System.out.println(sumText("1234")); // 0
		System.out.println(sumText("91212129")); // 9
		
		try {
			String text = FileUtil.readLineFromFile(new File("files/day01/input.txt"));
			System.out.println(sumText(text));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void doPart2() {
		System.out.println(sumText("1212", 2)); // 6
		System.out.println(sumText("1221", 2)); // 0
		System.out.println(sumText("123425", 3)); // 4
		System.out.println(sumText("123123", 3)); // 12
		System.out.println(sumText("12131415", 4)); // 4
		
		try {
			String text = FileUtil.readLineFromFile(new File("files/day01/input.txt"));
			System.out.println(sumText(text, text.length() / 2));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
