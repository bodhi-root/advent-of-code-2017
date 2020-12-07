package day22;

public class Location {

	public int row;
	public int col;
	
	public Location(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int hashCode() {
		return row ^ 7 + col;
	}
	public boolean equals(Object o) {
		if (o instanceof Location) {
			Location that = (Location)o;
			return this.row == that.row &&
				   this.col == that.col;
		}
		return false;
	}
	
}
