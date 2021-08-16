package hw20;

public class Doodlebug extends Critter{
	private int stepsWithoutAnts;
	
	// default constructor
	Doodlebug() {
		super();
		stepsWithoutAnts = 0;
	}
	
	// constructor with coordinates given
	Doodlebug(int x, int y){
		super(x,y);
		stepsWithoutAnts = 0;
	}
	
	Doodlebug breed(int direction) {
		Doodlebug baby = new Doodlebug(this.getXcoor(), this.getYcoor()); // first born with parent's coordinates
		switch(direction) { //adjust new critter's coordinate based on breed direction
		case 1:
			baby.setYcoor(baby.getYcoor()-1);
			break;
		case 2: 
			baby.setXcoor(baby.getXcoor()+1);
			break;
		case 3:
			baby.setYcoor(baby.getYcoor()+1);
			break;
		default: // case 4
			baby.setXcoor(baby.getXcoor()-1);
			break;
		// parameter wouldn't be zero using outside of class control
		}
		
		return baby;
	}
	
	// accessor function
	int getStepsWithoutAnts() {
		return stepsWithoutAnts;
	}
	
	// mutator function
	void setStepsWithoutAnts(int x) {
		stepsWithoutAnts = x;
	}
	
}
