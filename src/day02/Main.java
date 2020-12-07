package day02;

import java.io.File;
import java.util.List;

import common.FileUtil;

public class Main {

	public static int checkSum(List<String> lines) {
		int sum = 0;
		
		for (String line : lines) {
			System.out.println(line);
			
			String [] parts = line.split("\\s+");
		    int [] values = new int[parts.length];
		    for (int i=0; i<parts.length; i++)
		    	values[i] = Integer.valueOf(parts[i]);
		    
		    int max = values[0];
		    int min = values[0];
		    
		    for (int i=1; i<values.length; i++) {
		    	max = Math.max(max, values[i]);
		    	min = Math.min(min, values[i]);
		    }
		    
		    sum += (max - min);
		}
		
		return sum;
	}
	
	public static int checkSum2(List<String> lines) {
		int sum = 0;
		
		for (String line : lines) {
			System.out.println(line);
			
			String [] parts = line.split("\\s+");
		    int [] values = new int[parts.length];
		    for (int i=0; i<parts.length; i++)
		    	values[i] = Integer.valueOf(parts[i]);
		    
		    for (int i=0; i<values.length; i++) {
		    	for (int j=0; j<values.length; j++) {
		    		
		    		if (i == j)
		    			continue;
		    		
		    		if ((values[i] % values[j]) == 0) {
		    			sum += values[i] / values[j];
		    			
		    			System.out.println(values[i] + " / " + values[j] + " = " + (values[i] / values[j]));
		    			
		    			//break loop:
		    			i = values.length;
		    			j = values.length;
		    		}
		    	}
		    }
		    
		}
		
		return sum;
	}
	
	public static void main(String [] args) {
		try {
			List<String> lines = FileUtil.readLinesFromFile(new File("files/day02/input.txt"));
			int value = checkSum2(lines);
			System.out.println(value);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
