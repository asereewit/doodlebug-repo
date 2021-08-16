package ppsimulation;

public class Ant extends Critter {

	// default constructor 
	Ant() {
		super();
	}
	
	// constructor with coordinates given
	Ant(int x, int y) {
		super(x, y);
	}
	
	Ant breed(int direction) {
		Ant baby = new Ant(this.getXcoor(), this.getYcoor()); // first born with parent's coordinates
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
}
