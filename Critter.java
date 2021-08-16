package ppsimulation;

public class Critter {
	private boolean alive;
	private int xcoor, ycoor;
	private int lifespan;
	
	// constructor with randomized coordinates
	Critter(){
		xcoor = World.randInt(0, World.cellLength-1); //randomly generate a coordinate inside the world's dimension
		ycoor = World.randInt(0,World.cellWidth-1); 
		lifespan = 0;
		alive = true;
	}
	
	// constructor with set coordinates
	Critter(int x, int y){
		xcoor = x;
		ycoor = y;
		lifespan = 0;
		alive = true;
	}
	
	// move 
	void move(int direction) { // update the critter's coordinates 
		switch(direction) {
		case 1: // move up
			xcoor += 0;
			ycoor += -1;
			break;
		case 2: // move right
			xcoor += 1;
			ycoor += 0;
			break;
		case 3: // move down
			xcoor += 0;
			ycoor += 1;
			break;
		case 4: // move left
			xcoor += -1;
			ycoor += 0;
			break;
		default: // case 0
			xcoor += 0;
			ycoor += 0;
			break;
		}
		
		lifespan += 1;
	}
	
	// breed
	Critter breed(int direction) {
		Critter baby = new Critter(xcoor, ycoor); // first born with parent's coordinates
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

	// accessor functions
	int getXcoor() {
		return xcoor;
	}
	
	int getYcoor() {
		return ycoor;
	}
	
	int getLifespan() {
		return lifespan;
	}
	
	boolean isAlive() {
		return alive;
	}
	
	// mutator functions
	void setXcoor(int x)	{
		xcoor = x;
	}
	
	void setYcoor(int y) {
		ycoor = y;
	}
	
	void setLifespan(int k) {
		lifespan = k;
	}
	
	void setAlive(boolean flag) {
		alive = flag;
	}
}
