package day07;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.FileUtil;

public class Main {
	
	static class Node {
		
		String name;
		int weight;
		Node parent = null;
		List<Node> children = new ArrayList<>();
		
		public Node(String name, int weight) {
			this.name = name;
			this.weight = weight;
		}
		
		public void addChild(Node node) {
			children.add(node);
			node.parent = this;
		}
		
		public int getTotalWeight() {
			if (children.isEmpty())
				return weight;
			
			int total = weight;
			for (Node child : children)
				total += child.getTotalWeight();
			
			return total;
		}
		
	}
	
	public static Map<String, Node> loadNodesFromFile(File file) throws IOException {
		Map<String, Node> nodes = new HashMap<>(1000);
		
		//iterate through once to create all nodes:
		List<String> lines = FileUtil.readLinesFromFile(file);
		for (String line: lines) {
			
			//samples:
			//gbyvdfh (155) -> xqmnq, iyoqt, dimle
			//oweiea (97)
			
			String [] parts = line.split("\\s+");
			String name = parts[0];
			int weight = Integer.parseInt(parts[1].substring(1,parts[1].length()-1));
			
			Node node = new Node(name, weight);
			nodes.put(node.name, node);
			
			System.out.println(node.name + " : " + node.weight);
		}
		
		//iterate through again to setup relationships
		for (String line : lines) {
			
			String [] parts = line.split("\\s+");
			
			if (parts.length > 3) {
				String name = parts[0];
				Node node = nodes.get(name);
				if (node == null) 
					throw new IllegalStateException("Node not found: '" + name + "'");
				
				for (int i=3; i<parts.length; i++) {
					String childName = parts[i];
					if (childName.endsWith(","))
						childName = childName.substring(0, childName.length() - 1);
					
					Node child = nodes.get(childName);
					if (child == null) 
						throw new IllegalStateException("Node not found: '" + childName + "'");
					
					node.addChild(child);
				}
			}
			
		}
		
		return nodes;
	}
	
	public static void solvePart1() throws Exception {
		
		Map<String, Node> nodes = loadNodesFromFile(new File("files/day07/input.txt"));
		for (Node node : nodes.values()) {
			if (node.parent == null) {
				System.out.println("Root Node: " + node.name);
			}
		}
		
	}
	
	public static void solvePart2() throws Exception {
		
		Map<String, Node> nodes = loadNodesFromFile(new File("files/day07/input.txt"));
		Node root = nodes.get("qibuqqg");
		
		System.out.println("=== Original ===");
		visit(root);
		
		System.out.println("=== Balanced ===");
		Node node = nodes.get("egbzge");
		node.weight = node.weight - 7;
		System.out.println("New Weight: " + node.weight);
		visit(root);
		
	}
	
	public static void visit(Node node) {
		if (node.children.size() > 0) {
			int [] weights = new int[node.children.size()];
			for (int i=0; i<weights.length; i++)
				weights[i] = node.children.get(i).getTotalWeight();
			
			boolean balanced = true;
			for (int i=1; i<weights.length; i++) {
				if (weights[i] != weights[0]) {
					balanced = false;
					break;
				}
			}
			
			if (!balanced) {
				System.out.println("Unbalanced node: " + node.name);
				for (int i=0; i<weights.length; i++) {
					System.out.println("  " + node.children.get(i).name + " : " + weights[i]);
				}
			}
			
			for (Node child : node.children) {
				visit(child);
			}
		}
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
