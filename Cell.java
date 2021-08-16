package hw20;

public class Cell {
	private boolean occupiedByAnt, occupiedByDoodlebug;
	
	//constructor
	Cell() {
		occupiedByAnt = false;
		occupiedByDoodlebug = false;
	}
	
	void setOccupiedByAnt(boolean flag) {
		occupiedByAnt = flag;
	}
	
	void setOccupiedByDoodlebug(boolean flag) {
		occupiedByDoodlebug = flag;
	}
	
	boolean getOccupiedByAnt() {
		return occupiedByAnt;
	}
	
	boolean getOccupiedByDoodlebug() {
		return occupiedByDoodlebug;
	}
}
