package day09;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static enum NodeType {GROUP, GARBAGE};
	
	static class Node {
		
		NodeType type;
		String contents;
		
		Node parent;
		List<Node> children = new ArrayList<>();
		
		public Node(NodeType type) {
			this.type = type;
		}
		
		public void addNode(Node node) {
			children.add(node);
			node.parent = this;
		}
		
		public int getDepth() {
			if (parent == null)
				return 1;
			else
				return parent.getDepth() + 1;
		}
		
		public int getTotalGroupCount() {
			if (type == NodeType.GARBAGE)
				return 0;
			
			int count = 1;
			for (Node child : children)
				count += child.getTotalGroupCount();
			return count;
		}
		
		public int getTotalScore() {
			if (type == NodeType.GARBAGE)
				return 0;
			
			int score = getDepth();
			for (Node child : children)
				score += child.getTotalScore();
			return score;
		}
		
		public int getGarbageSize() {
			char [] chars = contents.toCharArray();
			int count = 0;
			int nextIndex = 0;
			char ch;
			while(nextIndex < chars.length) {
				ch = chars[nextIndex];
				nextIndex++;
				if (ch == '!')
					nextIndex++;
				else
					count++;
			}
			return count;
		}
		
		public int getTotalGarbageSize() {
			if (type == NodeType.GARBAGE)
				return getGarbageSize();
			
			int total = 0;
			for (Node child : children) 
				total += child.getTotalGarbageSize();
			return total;
		}
		
	}
	
	static class Parser {
		
		char [] chars;
		int nextIndex = 0;
		
		public Parser(char [] chars) {
			this.chars = chars;
		}
		
		public Node readNode() {
			char ch = chars[nextIndex];
			
			if (ch == '{') {
				return readGroup();
			} else if (ch == '<') {
				return readGarbage();
			} else {
				throw new IllegalStateException("Node must start with '{' or '<'");
			}
		}
		
		public Node readGarbage() {
			if (chars[nextIndex] != '<')
				throw new IllegalStateException("Garbage must start with '<'");
			
			Node node = new Node(NodeType.GARBAGE);
			nextIndex++;
			
			StringBuilder content = new StringBuilder();
			char ch;
			while (true) {
				ch = chars[nextIndex];
				nextIndex++;
				
				if (ch == '>') {
					break;
				}
				else if (ch == '!') {
					nextIndex++;
				} else {
					content.append(ch);
				}
			}
			
			node.contents = content.toString();
			
			return node;
		}
		
		public Node readGroup() {
			System.out.println("Reading group at index " + nextIndex);
			
			if (chars[nextIndex] != '{')
				throw new IllegalStateException("Group must start with '{'");
			
			Node group = new Node(NodeType.GROUP);
			nextIndex++;
			
			char ch;
			while(true) {
				ch = chars[nextIndex];
				if (ch == '}') {
					nextIndex++;
					break;
				}
				
				if (ch == ',') {
					if (group.children.isEmpty())
						throw new IllegalStateException("Comma is only allowed within a group after the first child node");
					
					nextIndex++;
					group.addNode(readNode());
				} else {
					group.addNode(readNode());
				}
				
			}
			
			return group;
		}
		
	}
	
	public static Node parseInput(File file) throws IOException {
		String line = FileUtil.readLineFromFile(file);
		Parser parser = new Parser(line.toCharArray());
		return parser.readGroup();
	}
	public static Node parseText(String text) throws IOException {
		Parser parser = new Parser(text.toCharArray());
		return parser.readGroup();
	}
	
	public static void test(String text) throws IOException {
		Node node = parseText(text);
		System.out.println("Groups = " + node.getTotalGroupCount() + ", Score = " + node.getTotalScore());
		System.out.println();
	}
	
	public static void test() throws Exception {
		test("{{{}}}");							//3,6
		test("{{},{}}");						//3,5
		test("{{{},{},{{}}}}");					//6,16
		test("{<a>,<a>,<a>,<a>}");				//1,1
		test("{{<ab>},{<ab>},{<ab>},{<ab>}}");	//5,9
		test("{{<!!>},{<!!>},{<!!>},{<!!>}}");	//5,9
		test("{{<a!>},{<a!>},{<a!>},{<ab>}}");	//2,3
	}
	
	public static void solvePart1() throws Exception {
		Node node = parseInput(new File("files/day09/input.txt"));
		System.out.println("Groups = " + node.getTotalGroupCount() + ", Score = " + node.getTotalScore());
		System.out.println();
	}
	
	public static void solvePart2() throws Exception {
		Node node = parseInput(new File("files/day09/input.txt"));
		System.out.println("Garbage Size = " + node.getTotalGarbageSize());
		System.out.println();
	}
	
	public static void main(String [] args) {
		try {
			//test();
			//solvePart1();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
