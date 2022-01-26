import java.awt.Point;
import java.util.ArrayList;
public class Simulation1Grid{

	private int width;
	private int height;

	private boolean roundWorldOn = false;
	
	private ArrayList<Organism> organisms = new ArrayList<Organism>();

	private int[][] grid;
	public static final int EMPTY_CELL = 0;	

	public int getWidth() { return width; }
	public void setWidth(int widthIn) { width = widthIn; }
	public int getHeight() { return height; }
	public void setHeight(int heightIn) { height = heightIn; }

	public void setRoundWorldOn(boolean roundWorldOnIn) { roundWorldOn = roundWorldOnIn; }
	public boolean getRoundWorldOn() { return roundWorldOn; }
	
	public ArrayList<Organism> getOrganisms() { return organisms; }

	public int[][] getGrid() { return grid; }
	public void setGrid(int[][] nextGrid) { grid = nextGrid; } 

	public Simulation1Grid(int sideLength) {
		//instantiate variables
		width = sideLength;
		height = sideLength;

		//instantiate empty grid with correct size
		grid = new int [sideLength][sideLength];
	}

	//WILL HAVE FUTURE USE
	public void setSides(int sideLength) {
		setWidth(sideLength);
		setHeight(sideLength);
	}

	public int getNumOrganisms() { return organisms.size(); }

	public int[][] cloneGrid(){
		int[][] nextGrid = new int[width][height];
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				nextGrid[j][i] = grid[j][i];
			}
		}
		return nextGrid;
	}

	/**
	 * Prints grid in form of ints to console
	 */
	public void displayGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * converts int version of grid to String
	 * @return String version of grid
	 */
	public String toStringGrid() {
		String strng = "\n";
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				strng = strng + grid[i][j] + " ";
			}
			strng = strng +  "\n";
		}
		return strng;
	}

	/**
	 * overridden toString method
	 */
	public String toString() {
		return "Class: SquareGrid\nWidth: " + width + "\nHeight: " + height + "\nNumber of " + "organisms: " + organisms.size() +"\nGrid:\n" + toStringGrid();
	}

	/**
	 * Precondition: tempGrid contains ID.
	 * @param ID
	 * @return
	 */
	public Point getCoordinates(int ID) {
		int[][] tempGrid = grid;
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[0].length; x++) {
				if (tempGrid[y][x] == ID) {
					return new Point(x, y);
				}
			}
		}
		return null;
	}

	/**
	 * Kills an organism that has the designated ID by changing the its ID number on the grid
	 * to -1 and deleting the object from the ArrayList.
	 */
	public void killOrganism(int ID) {
		//deletes organism from list
		for (int i = 0; i < organisms.size(); i++) {
			if (ID == organisms.get(i).getID()) {
				organisms.remove(i);
			}
		}
		//deletes organism from grid
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if ( grid[i][j] == ID ) {
					grid[i][j] = EMPTY_CELL;
					return;
				}
			}
		}
	}
	/**
	 * Creates a default organism and adds it to the specified coordinates
	 * @param x 
	 * @param y
	 * @return organism that was created
	 */
	public Organism createOrganism(int x ,int y) {
		if (grid[y][x] == 0) {
			Organism o = new Organism();
			organisms.add(o);
			grid[y][x] = o.getID();
			return o;
		}
		else {
			System.out.println("Organism failed to be created. Cell already occupied");
			return null;
		}
	}
	public boolean isSurrounded(int ID) {
		int numOrgNeighbors = 0;
		Point p = getCoordinates(ID);
		int x = (int) p.getX();
		int y = (int) p.getY();
		try {
			if (grid[y - 1][x] != 0) {
				numOrgNeighbors++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			if (roundWorldOn && grid[grid.length - 1][x] != 0) {
				numOrgNeighbors++;
			}
		}
		try {
			if (grid[y + 1][x] != 0) {
				numOrgNeighbors++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			if (roundWorldOn && grid[0][x] != 0) {
				numOrgNeighbors++;
			}
		}
		try {
			if (grid[y][x - 1] != 0) {
				numOrgNeighbors++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			if (roundWorldOn && grid[y][grid[0].length - 1] != 0) {
				numOrgNeighbors++;
			}
		}
		try {
			if (grid[y][x + 1] != 0) {
				numOrgNeighbors++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			if (roundWorldOn && grid[y][0] != 0) {
				numOrgNeighbors++;
			}
		}
		return numOrgNeighbors >= 3;
	}
	/**
	 * Creates an overloaded organism and adds it to the specified coordinates
	 * @param lifeSpanIn
	 * @param x
	 * @param y
	 * @return organism that was created
	 */
	public Organism createOrganism(int lifeSpanIn, int x, int y) {
		if (grid[y][x] == 0) {
			Organism o = new Organism(lifeSpanIn);
			organisms.add(o);
			grid[y][x] = o.getID();
			return o;
		}
		else {
			System.out.println("Organism failed to be created. Cell already occupied");
			return null;
		}
	}
	/**
	 * Creates a new organism at specified coordinates with specified life span and max speed
	 * @param lifeSpan - lifeSpan statistic for organism
	 * @param maxSpeedIn - maxSpeed statistic for organism
	 * @param xCord - x-Coordinate for placement of the organism
	 * @param yCord - y-Coordinate for placement of the organism
	 */
	public MovingOrganism createMovingOrganism(int lifeSpanIn, int maxSpeedIn, int x, int y){
		if (grid[y][x] == 0) {
			MovingOrganism o = new MovingOrganism(lifeSpanIn, maxSpeedIn);
			organisms.add(o);
			grid[y][x] = o.getID();
			return o;
		}
		else {
			System.out.println("Organism failed to be created. Cell already occupied");
			return null;
		}
	}

	//TO DO METHOD
	public void growOrganism(int ID, int xCord, int yCord) {
		if (grid[yCord][xCord] == 0) {
			grid[yCord][xCord] = ID;
		}
	}

	//TO DO METHOD
	public void shrinkOrganism(int ID, int xCord, int yCord) {
		if (grid[yCord][xCord] == ID) {
			grid[yCord][xCord] = 0;
		}
	}

	public void resetGrid () {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = 0;
			}
		}
		while (organisms.size() > 0 ) {
			organisms.remove(0);
		}
		IDCounter.resetCount();
	}
	/**
	 * Identifies organism object by given ID and returns based on isNewBorn
	 * @param IDIn - ID of chosen organism
	 * @return whether organism is a new born
	 */
	/*public boolean checkIsNewBorn(int IDIn) {
		for (Organism temp : organisms) {
			if (temp.getID() == IDIn) {
				if (temp.getIsNewBorn()) {
					return true;
				}
			}
		}
		return false;
	}*/

	/*private int getNewOrgAttribute(Organism o1, Organism o2, Modifier m) {
		int value1;
		int value2;

		switch (m) {
		case MAX_SPEED:
			value1 = o1.getMaxSpeed();
			value2 = o2.getMaxSpeed();
			break;
		case LIFE_SPAN:
			value1 = o1.getLifeSpan();
			value2 = o2.getLifeSpan();
			break;
		default:
			return -1;
		}


		int newValue;
		//calculate modifier for Max speed
		do{
			int newMod = (int)(Math.random() * m.getScale() + m.getOffset());
			newValue = (int)((value1 + value2) / 2) + newMod;
		}while(newValue < 0);
		return newValue;
	} */

	/*public ArrayList<Organism> sexuallyReproduce() {
		ArrayList<Organism>newOrganisms = new ArrayList<Organism>();
		int x;
		int y;
		for(y = 0; y < height; y++){
			for(x = 0; x < width; x++){
				//grid to hold changes, initialized to copy current grid

				if(grid[y][x] == 0) {
					//check above and below for two organism to reproduce
					try {
						Organism[] neighbors = getVertNeighbors(x, y);
						if (neighbors == null) {
							neighbors = getHorizNeighbors(x, y);
						}
						if (neighbors != null) {
							int newLifeSpan = getNewOrgAttribute(neighbors[0], neighbors[1], Modifier.LIFE_SPAN);
							int newMaxSpeed = getNewOrgAttribute(neighbors[0], neighbors[1], Modifier.MAX_SPEED);
							Organism o = createOrganism(newLifeSpan, newMaxSpeed, x, y);
							newOrganisms.add(o);
						}
					} catch (IndexOutOfBoundsException e) {}	
				}	
			}
		}
		return newOrganisms;
	} */

	/**
	 * Return the pair of neighbors that live above and below the specified cell
	 * @param x
	 * @param y
	 * @return the pair of neighbors that live above and below the specified cell, but return null if
	 * there are fewer than two vertical neighbors
	 */
	/*private Organism[] getVertNeighbors(int x, int y) {
		try {
			Organism upper = null;
			Organism lower = null;
			for(Organism o : organisms){
				if (!o.getIsNewBorn()){
					if (grid[y - 1][x] == o.getID()) {
						upper = o;
					}
					if (grid[y + 1][x] == o.getID()) {
						lower = o;
					}
				}
			}
			if (upper == null || lower == null){
				return null;
			}
			Organism[] pair = new Organism[2];
			pair[0] = upper;
			pair[1] = lower;
			return pair;
		} catch (Exception e){
			return null;
		}
	}

	/**
	 * Return the pair of neighbors that live above and below the specified cell
	 * @param x
	 * @param y
	 * @return the pair of neighbors that live above and below the specified cell, but return null if
	 * there are fewer than two vertical neighbors
	 */
	/*private Organism[] getHorizNeighbors (int x, int y) {
		try {
			Organism left = null;
			Organism right = null;
			for(Organism o : organisms){
				if (!o.getIsNewBorn()) {
					if(grid[y][x - 1] == o.getID()){
						left = o;
					}
					if(grid[y][x + 1] == o.getID()){
						right = o;
					}
				}
			}
			if (left == null || right == null){
				return null;
			}
			Organism[] pair = new Organism[2];
			pair[0] = left;
			pair[1] = right;
			return pair;
		} catch (Exception e){
			return null;
		}
	}*/


	/**
	 * deletes organism if turnsLeft = 0
	 */
	public void killOrganisms(){
		for (int i = 0; i < organisms.size(); i++) {
			if (isSurrounded(organisms.get(i).getID())) {
				organisms.get(i).setTurnsLeft(0);
			}
		}
		for (int i = 0; i < organisms.size(); i++) {
			if (organisms.get(i).canDie()) {
				outerloop:
					for(int y = 0; y < height; y++){
						for(int x = 0; x < width; x++){
							if( grid[y][x] == organisms.get(i).getID()){
								grid[y][x] = 0;
								break outerloop;
							}
						}
					}
			organisms.remove(i);
			i--;
			}
		}
	}
}
