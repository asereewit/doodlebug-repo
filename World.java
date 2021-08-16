package ppsimulation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class World {
	public static int cellLength = 20;
	public static int cellWidth = 20;
	Cell world[][] = new Cell[cellWidth][cellLength];
	ArrayList<Doodlebug> doodlebug; //= new ArrayList<Doodlebug>();
	ArrayList<Ant> ant; //= new ArrayList<Ant>();
	
	World(int antNum, int doodlebugNum) {
		doodlebug = new ArrayList<Doodlebug>(doodlebugNum); // create an array list of doodlebugs according to the given parameter
		ant = new ArrayList<Ant>(antNum);	// create an array of ants according to the given parameter
		
		for(int i=0; i<cellWidth; i++) {	// initialize cell objects in world
				for(int j=0; j<cellLength; j++) {
					world[i][j] = new Cell();
				}
			}
		
		for(int i=0; i<doodlebugNum; i++) { // set the cell corresponding to the doodlebugs' coordinates occupied.
				
				Doodlebug babyDoodle;
				
				do {
					babyDoodle = new Doodlebug();
				} while(world[babyDoodle.getYcoor()][babyDoodle.getXcoor()].getOccupiedByDoodlebug() |
						world[babyDoodle.getYcoor()][babyDoodle.getXcoor()].getOccupiedByAnt()); //check newly created doodlebugs have unique coordinates
				
				doodlebug.add(babyDoodle);
				world[doodlebug.get(i).getYcoor()][doodlebug.get(i).getXcoor()].setOccupiedByDoodlebug(true);
			}
		
		for(int i=0; i<antNum; i++) {	// set the cell corresponding to the ants' coordinates occupied.
				
				Ant babyAnt;
				
				do {
					babyAnt = new Ant();
				} while(world[babyAnt.getYcoor()][babyAnt.getXcoor()].getOccupiedByDoodlebug() |
						world[babyAnt.getYcoor()][babyAnt.getXcoor()].getOccupiedByAnt()); //check newly created ants have unique coordinates
				
				ant.add(babyAnt);
				world[ant.get(i).getYcoor()][ant.get(i).getXcoor()].setOccupiedByAnt(true);
			}
		
		showState(); //show the state of the world once it is initialized.
	}
	
	void elapse() {
		
		ArrayList<Integer> deadCritterIndex = new ArrayList<Integer>(); // arraylist for critters to be killed.
		ArrayList<Integer> pregnantCritterIndex = new ArrayList<Integer>(); // arraylist for storing the index of ready-to-breed critters.
		ArrayList<Integer> pregnantCritterCorrespondingBreedDirection = new ArrayList<Integer>(); // arraylist for storing the breed direction of the critters.
		
		try {
			
			for(int i=0; i<doodlebug.size(); i++) {

			if(doodlebugEatDirection(i) > 0) { //if there are ants around the doodlebug, determine doodlebug's move direction and update cell's status.
				world[doodlebug.get(i).getYcoor()][doodlebug.get(i).getXcoor()].setOccupiedByDoodlebug(false);
				doodlebug.get(i).move(doodlebugEatDirection(i));
				world[doodlebug.get(i).getYcoor()][doodlebug.get(i).getXcoor()].setOccupiedByAnt(false); 
				world[doodlebug.get(i).getYcoor()][doodlebug.get(i).getXcoor()].setOccupiedByDoodlebug(true);
				doodlebug.get(i).setStepsWithoutAnts(0); //reset the doodlebug's stepsWithoutAnts variable to zero once it has eaten. 
				
				for(int j=0; j<ant.size(); j++) { 
					if(doodlebug.get(i).getXcoor() == ant.get(j).getXcoor() && doodlebug.get(i).getYcoor() == ant.get(j).getYcoor()) { // find the adjacent ant
						ant.remove(j);
					}
				}
				
			} else { // when there is no ant adjacent to the doodlebug, its adajcent cells are either 1) empty or 2) occupied by other doodlebugs.
				world[doodlebug.get(i).getYcoor()][doodlebug.get(i).getXcoor()].setOccupiedByDoodlebug(false);
				doodlebug.get(i).move(doodlebugMoveDirectionWtAnt(i));
				doodlebug.get(i).setStepsWithoutAnts(doodlebug.get(i).getStepsWithoutAnts()+1);
				world[doodlebug.get(i).getYcoor()][doodlebug.get(i).getXcoor()].setOccupiedByDoodlebug(true);	
			}
			
			// check for starving doodlebug and set them dead
			if(doodlebug.get(i).isAlive() & doodlebug.get(i).getStepsWithoutAnts() >= 3) {
				world[doodlebug.get(i).getYcoor()][doodlebug.get(i).getXcoor()].setOccupiedByDoodlebug(false);
				doodlebug.get(i).setAlive(false);
				deadCritterIndex.add(i); // add index of dead doodlebugs to array list to be killed later.
			}
			
			//check ready-to-breed doodlebug and breed direction and set cell with new doodlebug occupied by doodlebug.
			if(doodlebug.get(i).isAlive() & doodlebug.get(i).getLifespan() >= 8) {
				doodlebug.get(i).setLifespan(0); // reset doodlebug's lifespan to zero.
				int temp = doodlebugBreedDirection(i);
				if(temp != 0) {
					pregnantCritterIndex.add(i); // add ready-to-breed doodlebug's index to an array list
					pregnantCritterCorrespondingBreedDirection.add(temp); // add corresponding breed direction to an array list
					switch(temp) {
					case 1: // set cell above occupied by doodlebug
						world[doodlebug.get(i).getYcoor()-1][doodlebug.get(i).getXcoor()].setOccupiedByDoodlebug(true);
						break;
					case 2: // set cell on the right occupied by doodlebug
						world[doodlebug.get(i).getYcoor()][doodlebug.get(i).getXcoor()+1].setOccupiedByDoodlebug(true);
						break;
					case 3: // set doodlebug below occupied by doodlebug
						world[doodlebug.get(i).getYcoor()+1][doodlebug.get(i).getXcoor()].setOccupiedByDoodlebug(true);
						break;
					case 4: // set doodlebug on the left occupied by doodelbug
						world[doodlebug.get(i).getYcoor()][doodlebug.get(i).getXcoor()-1].setOccupiedByDoodlebug(true);
						break;
					default:
						break;
					}
				}
			} 
		}
		} catch(Throwable exc) {
			System.out.println("Error 4: " + exc);
		}
		
		// breed doodlebug and add new doodlebug to the arraylist
		for(int i=0; i<pregnantCritterIndex.size(); i++) {
			doodlebug.add(doodlebug.get(pregnantCritterIndex.get(i)).breed(pregnantCritterCorrespondingBreedDirection.get(i)));
		}
		
		// clear all elements in arrays
		pregnantCritterIndex.clear();
		pregnantCritterCorrespondingBreedDirection.clear();
		
		// killed critters in critter index array list
		Collections.sort(deadCritterIndex); //sort and reverse the arraylist so that removing an object starts from the end of the arraylist and won't generate index out of bound exception
		Collections.reverse(deadCritterIndex);
		for(int i=0; i<deadCritterIndex.size(); i++) {
				doodlebug.remove((int)deadCritterIndex.get(i));
		}
		// clear all elements of the array
		deadCritterIndex.clear();
		
		try {
			//Ant move
			for(int i=0; i<ant.size(); i++) { // ants move to where there is no ant and no doodlebug
			
			world[ant.get(i).getYcoor()][ant.get(i).getXcoor()].setOccupiedByAnt(false);
			ant.get(i).move(antMoveDirection(i));
			world[ant.get(i).getYcoor()][ant.get(i).getXcoor()].setOccupiedByAnt(true);
			
			//check ready-to-breed ants index and direction
			if(ant.get(i).getLifespan() >= 3) {
				ant.get(i).setLifespan(0); // reset ant's lifespan
				int temp = antMoveDirection(i);
				if(temp != 0) {
					pregnantCritterIndex.add(i); // add ready-to-breed doodlebug's index to an array list
					pregnantCritterCorrespondingBreedDirection.add(temp); // add corresponding breed direction to an array list
					switch(temp) {
					case 1: // set cell above occupied by ant
						world[ant.get(i).getYcoor()-1][ant.get(i).getXcoor()].setOccupiedByAnt(true);
						break;
					case 2: // set cell on the right occupied by ant
						world[ant.get(i).getYcoor()][ant.get(i).getXcoor()+1].setOccupiedByAnt(true);
						break;
					case 3: // set cell below occupied by ant
						world[ant.get(i).getYcoor()+1][ant.get(i).getXcoor()].setOccupiedByAnt(true);
						break;
					case 4: // set cell on the left occupied by ant
						world[ant.get(i).getYcoor()][ant.get(i).getXcoor()-1].setOccupiedByAnt(true);
						break;
					default:
						break;
					}
				}
			}
		}
		} catch(Throwable exc) {
			System.out.println("Error 7: " + exc);
		}
		
		//breed ants and add them to arraylist
		for(int i=0; i<pregnantCritterIndex.size(); i++) {
			ant.add(ant.get(pregnantCritterIndex.get(i)).breed(pregnantCritterCorrespondingBreedDirection.get(i)));
		}
		
		//clear all elements in arrays
		pregnantCritterIndex.clear();
		pregnantCritterCorrespondingBreedDirection.clear();
		showState();
	}

	void showState() {
		System.out.println("A: " + ant.size());
		System.out.println("D: " + doodlebug.size());
		
		//show the state of the world in string. 
		for(int i=0; i<cellWidth; i++) {
			for(int j=0; j<cellLength; j++) {
				if(world[i][j].getOccupiedByAnt() & !world[i][j].getOccupiedByDoodlebug()) {
					System.out.print("[A]");
				} else if(world[i][j].getOccupiedByDoodlebug() & !world[i][j].getOccupiedByAnt()) {
					System.out.print("[D]");
				} else if(world[i][j].getOccupiedByAnt() & world[i][j].getOccupiedByDoodlebug()){
					System.out.print("[X]");
				} else { 
					System.out.print("[ ]");
				}
			}
			System.out.println();
		}
	}
	
	int doodlebugEatDirection(int doodlebugIndex) {
		int eatDirection = 0; 
		int canEatUp = 0;
		int canEatDown = 0;
		int canEatLeft = 0;
		int canEatRight = 0;
		
		try {
			if(world[doodlebug.get(doodlebugIndex).getYcoor()-1][doodlebug.get(doodlebugIndex).getXcoor()].getOccupiedByAnt()) // check if there is an ant on the left
				canEatUp = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canEatUp = 0;
		}
		
		try {
			if(world[doodlebug.get(doodlebugIndex).getYcoor()][doodlebug.get(doodlebugIndex).getXcoor()+1].getOccupiedByAnt()) // check if there is an ant on the right
				canEatRight = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canEatRight = 0;
		}
		
		try {
			if(world[doodlebug.get(doodlebugIndex).getYcoor()+1][doodlebug.get(doodlebugIndex).getXcoor()].getOccupiedByAnt()) // check if there is an ant on the right
				canEatDown = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canEatDown = 0;
		}
		
		try {
			if(world[doodlebug.get(doodlebugIndex).getYcoor()][doodlebug.get(doodlebugIndex).getXcoor()-1].getOccupiedByAnt()) // check if there is an ant on the left
				canEatLeft = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canEatLeft = 0;
		}
		
		eatDirection = canEatUp + canEatRight + canEatDown + canEatLeft; 
		
		if(eatDirection > 0) { // if eatDirection is greater than zero, call direction() to randomize eatDirection; otherwise return 0.
			eatDirection = direction(canEatUp, canEatRight, canEatDown, canEatLeft);
		}
		
		return eatDirection;
	}
	
	int doodlebugMoveDirectionWtAnt(int doodlebugIndex) {
		int moveDirection = 0; 
		int canMoveUp = 0;
		int canMoveDown = 0;
		int canMoveLeft = 0;
		int canMoveRight = 0;
		
		try {
			if(!world[doodlebug.get(doodlebugIndex).getYcoor()-1][doodlebug.get(doodlebugIndex).getXcoor()].getOccupiedByDoodlebug()) // check if there is a doodlebug on the left
				canMoveUp = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canMoveUp = 0;
		}
		
		try {
			if(!world[doodlebug.get(doodlebugIndex).getYcoor()][doodlebug.get(doodlebugIndex).getXcoor()+1].getOccupiedByDoodlebug()) // check if there is a doodlebug on the right
				canMoveRight = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canMoveRight = 0;
		}
		
		try {
			if(!world[doodlebug.get(doodlebugIndex).getYcoor()+1][doodlebug.get(doodlebugIndex).getXcoor()].getOccupiedByDoodlebug()) // check if there is a doodlebug on the right
				canMoveDown = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canMoveDown = 0;
		}
		
		try {
			if(!world[doodlebug.get(doodlebugIndex).getYcoor()][doodlebug.get(doodlebugIndex).getXcoor()-1].getOccupiedByDoodlebug()) // check if there is a doodlebug on the left
				canMoveLeft = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canMoveLeft = 0;
		}
		
		moveDirection = direction(canMoveUp, canMoveRight, canMoveDown, canMoveLeft);
		
		return moveDirection;
	}

	int doodlebugBreedDirection(int doodlebugIndex) { 
		int breedDirection = 0; 
		int canBreedUp = 0;
		int canBreedDown = 0;
		int canBreedLeft = 0;
		int canBreedRight = 0;
		
		try {
			if(!world[doodlebug.get(doodlebugIndex).getYcoor()-1][doodlebug.get(doodlebugIndex).getXcoor()].getOccupiedByAnt() &
					!world[doodlebug.get(doodlebugIndex).getYcoor()-1][doodlebug.get(doodlebugIndex).getXcoor()].getOccupiedByDoodlebug()) // check if there is an ant or a doodlebug on the left
				canBreedUp = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canBreedUp = 0;
		}
		
		try {
			if(!world[doodlebug.get(doodlebugIndex).getYcoor()][doodlebug.get(doodlebugIndex).getXcoor()+1].getOccupiedByAnt() &
					!world[doodlebug.get(doodlebugIndex).getYcoor()][doodlebug.get(doodlebugIndex).getXcoor()+1].getOccupiedByDoodlebug()) // check if there is an ant or a doodlebug on the right
				canBreedRight = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canBreedRight = 0;
		}
		
		try {
			if(!world[doodlebug.get(doodlebugIndex).getYcoor()+1][doodlebug.get(doodlebugIndex).getXcoor()].getOccupiedByAnt() &
					!world[doodlebug.get(doodlebugIndex).getYcoor()+1][doodlebug.get(doodlebugIndex).getXcoor()].getOccupiedByDoodlebug()) // check if there is an ant or a doodelbug on the right
				canBreedDown = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canBreedDown = 0;
		}
		
		try {
			if(!world[doodlebug.get(doodlebugIndex).getYcoor()][doodlebug.get(doodlebugIndex).getXcoor()-1].getOccupiedByAnt() &
					!world[doodlebug.get(doodlebugIndex).getYcoor()][doodlebug.get(doodlebugIndex).getXcoor()-1].getOccupiedByDoodlebug()) // check if there is an ant or a doodlebug on the left
				canBreedLeft = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canBreedLeft = 0;
		}
		
		breedDirection = direction(canBreedUp, canBreedRight, canBreedDown, canBreedLeft);
		
		return breedDirection;
	}
	
	int antMoveDirection(int antIndex) { // is also used to find direction to breed ant.
		int moveDirection = 0; 
		int canMoveUp = 0;
		int canMoveDown = 0;
		int canMoveLeft = 0;
		int canMoveRight = 0;
		
		try {
			if(!world[ant.get(antIndex).getYcoor()-1][ant.get(antIndex).getXcoor()].getOccupiedByAnt() &
					!world[ant.get(antIndex).getYcoor()-1][ant.get(antIndex).getXcoor()].getOccupiedByDoodlebug()) // check if there is an ant or a doodlebug on the left
				canMoveUp = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canMoveUp = 0;
		}
		
		try {
			if(!world[ant.get(antIndex).getYcoor()][ant.get(antIndex).getXcoor()+1].getOccupiedByAnt() &
					!world[ant.get(antIndex).getYcoor()][ant.get(antIndex).getXcoor()+1].getOccupiedByDoodlebug()) // check if there is an ant or a doodlebug on the right
				canMoveRight = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canMoveRight = 0;
		}
		
		try {
			if(!world[ant.get(antIndex).getYcoor()+1][ant.get(antIndex).getXcoor()].getOccupiedByAnt() &
					!world[ant.get(antIndex).getYcoor()+1][ant.get(antIndex).getXcoor()].getOccupiedByDoodlebug()) // check if there is an ant or a doodelbug on the right
				canMoveDown = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canMoveDown = 0;
		}
		
		try {
			if(!world[ant.get(antIndex).getYcoor()][ant.get(antIndex).getXcoor()-1].getOccupiedByAnt() &
					!world[ant.get(antIndex).getYcoor()][ant.get(antIndex).getXcoor()-1].getOccupiedByDoodlebug()) // check if there is an ant or a doodlebug on the left
				canMoveLeft = 1;
		} catch (ArrayIndexOutOfBoundsException exc) {
			canMoveLeft = 0;
		}
		
		moveDirection = direction(canMoveUp, canMoveRight, canMoveDown, canMoveLeft);
		
		return moveDirection;
	}
	
	int direction(int canUp, int canRight, int canDown, int canLeft) {
		int direction = 0;
		int possibleDirection = 0;
		int rand;
		int randCount = 0;
		int arr[] = {canUp, canRight, canDown, canLeft};
		
		for(int i=0; i<arr.length; i++) {
			if(arr[i]==1) {
				possibleDirection += 1;
			}
		}
		
		if(possibleDirection == 0) { // if the total number of possible direction is zero, return zero
			direction = 0;
			return direction;
		}
		
		rand = randInt(1,possibleDirection); //randomly generates a number between 1 and the total number of possible direction.
		
		for(int i=0; i<arr.length; i++) { 
			if(arr[i]==1) {
				randCount += 1;
					if(randCount == rand) {
						direction = i+1; // randomly selecting the final direction using given directions allowed.
						break;
					}
			}
		}
		return direction;
	}

	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

}
