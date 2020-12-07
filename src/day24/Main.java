package day24;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static class Piece {
		
		int id;
		int port1;
		int port2;
		
		public Piece(int id, int port1, int port2) {
			this.id = id;
			this.port1 = port1;
			this.port2 = port2;
		}
		
		public boolean hasPort(int port) {
			return (port1 == port || port2 == port);
		}
		
		public void reverse() {
			int tmp = port1;
			port1 = port2;
			port2 = tmp;
		}
		
		public int getStrength() {
			return port1 + port2;
		}
		
		public String toString() {
			return port1 + "/" + port2;
		}
		
		public static Piece fromString(int id, String text) {
			String [] parts = text.split("\\/");
			int port1 = Integer.parseInt(parts[0]);
			int port2 = Integer.parseInt(parts[1]);
			return new Piece(id, port1, port2);
		}
		
		public Piece copy() {
			return new Piece(id, port1, port2);
		}
		
	}
	
	static class Bridge {
		
		List<Piece> pieces = new ArrayList<>();
		
		public int size() {
			return pieces.size();
		}
		
		public void add(Piece piece) {
			pieces.add(piece);
		}
		
		public Piece getLastPiece() {
			if (pieces.isEmpty())
				return null;
			return pieces.get(pieces.size()-1);
		}
		
		public Piece removeLastPiece() {
			return pieces.remove(pieces.size()-1);
		}
		
		public int getStrength() {
			int total = 0;
			for (Piece piece : pieces)
				total += piece.getStrength();
			return total;
		}
		
		public Bridge copy() {
			Bridge b2 = new Bridge();
			for (Piece piece : pieces)
				b2.add(piece.copy());
			return b2;
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			for (int i=0; i<pieces.size(); i++) {
				if (i > 0)
					s.append("--");
				s.append(pieces.get(i).toString());
			}
			return s.toString();
		}
		
	}
	
	public static List<Piece> readInput() throws IOException {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day24/input.txt"));
		List<Piece> pieces = new ArrayList<>(lines.size());
		for (int i=0; i<lines.size(); i++)
			pieces.add(Piece.fromString(i, lines.get(i)));
		return pieces;
	}
	
	static Comparator<Bridge> COMPARE_STRENGTH = new Comparator<Bridge>() {

		@Override
		public int compare(Bridge o1, Bridge o2) {
			return o1.getStrength() - o2.getStrength();
		}
		
	};
	
	static Comparator<Bridge> COMPARE_SIZE_THEN_STRENGTH = new Comparator<Bridge>() {

		@Override
		public int compare(Bridge o1, Bridge o2) {
			int diff = o1.size() - o2.size();
			if (diff != 0)
				return diff;
			
			return o1.getStrength() - o2.getStrength();
		}
		
	};
	
	public static Bridge buildBestBridge(Bridge bridge, List<Piece> piecesRemaining, Comparator<Bridge> comparator) {
		int lastPort = bridge.getLastPiece().port2;
		Bridge bestExtension = null;
		
		//build list of next possible pieces (avoid concurrent modification of piecesRemaining list)
		List<Piece> nextPieces = new ArrayList<>();
		for (Piece piece : piecesRemaining) {	
			if (piece.hasPort(lastPort)) 
				nextPieces.add(piece);
		}
		
		for (Piece piece : nextPieces) {
		
			if (piece.port2 == lastPort)
				piece.reverse();

			bridge.add(piece);
			piecesRemaining.remove(piece);

			Bridge finalBridge = buildBestBridge(bridge, piecesRemaining, comparator);
			if (bestExtension == null || comparator.compare(finalBridge, bestExtension) > 0)
				bestExtension = finalBridge.copy();

			bridge.removeLastPiece();
			piecesRemaining.add(piece);
		}
		
		return (bestExtension == null) ? bridge : bestExtension;
	}
	
	public static void run(Comparator<Bridge> comparator) throws Exception {
		List<Piece> pieces = readInput();
		
		Bridge bestBridge = null;
		
		for (Piece piece : pieces) {
			
			//find starting pieces:
			if (piece.hasPort(0)) {
				
				//make sure port1 = 0
				if (piece.port2 == 0)
					piece.reverse();
				
				Bridge bridge = new Bridge();
				bridge.add(piece);
				
				List<Piece> piecesRemaining = new ArrayList<>(pieces);
				piecesRemaining.remove(piece);
				
				Bridge finalBridge = buildBestBridge(bridge, piecesRemaining, comparator);
				if (bestBridge == null || comparator.compare(finalBridge, bestBridge) > 0)
					bestBridge = finalBridge.copy();
			}
			
		}
		
		System.out.println("Best Bridge:");
		System.out.println(bestBridge.toString());
		System.out.println("Pieces = " + bestBridge.pieces.size());
		System.out.println("Strength = " + bestBridge.getStrength());
	}
	
	public static void solvePart1() throws Exception {
		run(COMPARE_STRENGTH);
	}
	
	public static void solvePart2() throws Exception {
		run(COMPARE_SIZE_THEN_STRENGTH);
	}
	
	public static void main(String [] args) {
		try {
			solvePart1();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
