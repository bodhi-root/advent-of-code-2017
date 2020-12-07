package day12;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.FileUtil;

public class Main {
	
	static class Node {
		
		String id;
		Set<Node> connections = new HashSet<>();
		
		public Node(String id) {
			this.id = id;
		}
		
		public boolean connectTo(Node node) {
			return connections.add(node);
		}
		
		public Set<Node> getConnectedNodes() {
			Set<Node> set = new HashSet<>();
			addToConnectedNodeSet(this, set);
			return set;
		}
		
		/**
		 * Adds the given node to the set and if the node has not been 
		 * visited yet recusively calls this for all connected nodes.
		 */
		protected void addToConnectedNodeSet(Node node, Set<Node> set) {
			if (set.add(node)) {
				for (Node neighbor : node.connections) {
					addToConnectedNodeSet(neighbor, set);
				}
			}
		}
		
	}
	
	public static Map<String, Node> readInput() throws IOException {
		Map<String, Node> nodes = new HashMap<>(1000);
		
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day12/input.txt"));
		
		//first pass: create nodes:
		for (String line : lines) {
			//sample:
			//4 <-> 2, 3, 6
			
			String [] parts = line.split("\\s+");
			String id = parts[0];
			
			Node node = new Node(id);
			nodes.put(id, node);
		}
		
		//second pass: link nodes
		for (String line : lines) {
			String [] parts = line.split("\\s+");
			String id = parts[0];
			
			Node node = nodes.get(id);
			for (int i=2; i<parts.length; i++) {
				String connectId = parts[i];
				connectId = connectId.replaceAll(",","");
				
				Node connectNode = nodes.get(connectId);
				node.connectTo(connectNode);
				connectNode.connectTo(node);
			}
		}
		
		return nodes;
	}
	
	public static void solvePart1() throws Exception {
		Map<String, Node> nodes = readInput();
		Node node = nodes.get("0");
		System.out.println(node.getConnectedNodes().size());
	}
	
	/**
	 * Returns a string that can uniquely identify the given group.  This is 
	 * simply a comma-separated list of node IDs with the node IDs sorted
	 * alphabetically.
	 */
	public static String getCanonicalGroupKey(Set<Node> nodes) {
		List<String> nodeIds = new ArrayList<>(nodes.size());
		for (Node node : nodes)
			nodeIds.add(node.id);
		
		Collections.sort(nodeIds);
		
		StringBuilder s = new StringBuilder();
		for (String id : nodeIds) {
			if (s.length() > 0)
				s.append(",");
			s.append(id);
		}
		return s.toString();
	}
	
	public static void solvePart2() throws Exception {
		Set<String> groups = new HashSet<>();
		
		Map<String, Node> nodes = readInput();
		for (Node node : nodes.values()) {
			Set<Node> group = node.getConnectedNodes();
			String key = getCanonicalGroupKey(group);
			groups.add(key);
		}
		
		System.out.println(groups.size());
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
