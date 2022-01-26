import java.awt.Point;
import java.util.ArrayList;


public class GridRunner {
	private Simulation1Grid grid;
	private boolean isDone = false;
	private boolean isPaused = true;
	private boolean evolutionOn = false;
	

	private int gridTime = 0;
	private int numDead = 0;

	public Simulation1Grid getGrid() { return grid; }
	public boolean getIsDone() { return isDone;	}
	public void setIsDone(boolean isDoneIn) { isDone = isDoneIn; }
	public void setIsPaused(boolean isPausedIn) { isPaused = isPausedIn; }
	public boolean getIsPaused() { return isPaused; }
	public void setEvolutionOn(boolean isOnIn) { evolutionOn = isOnIn; }
	public boolean getEvolutionOn() { return evolutionOn; }
	

	public int getNumDead() { return numDead; }

	public int getGridTime() { return gridTime; }
	public void setGridTime(int gridTimeIn) { gridTime = gridTimeIn; }

	public GridRunner(Simulation1Grid gridIn) {
		grid = gridIn;
	}
	public void restartGrid() {
		gridTime = 0;
		numDead = 0;
		grid.resetGrid();
	}
	public void moveOrganism(Direction directionIn, int ID, int x, int y) {
		int[][] tempGrid = grid.getGrid();
		if (directionIn == Direction.UP) {
			if (tempGrid[y - 1][x] != 0) {
				throw new ArrayIndexOutOfBoundsException();
			}
			tempGrid[y][x] = 0;
			tempGrid[y - 1][x] = ID;
		}
		else if (directionIn == Direction.DOWN) {

		}
		else if (directionIn == Direction.RIGHT) {

		}
		else if (directionIn == Direction.LEFT) {

		}
	}
	/**
	 * 
	 * @return ArrayList of new organisms
	 */
	@SuppressWarnings("unused")
	public ArrayList<Organism> doOrganismsActions() {
		ArrayList<Organism> newOrganisms = new ArrayList<Organism>();
		ArrayList<Organism> organisms = (ArrayList<Organism>) grid.getOrganisms().clone();
		int[][] tempGrid = grid.getGrid();
		for (Organism temp : organisms) {
			Object action = temp.action();
			if (action instanceof MovingOrganism) {
				int y, x;
				//move temp vector
				MovementVector temp1 = (MovementVector) action;
				Point p = grid.getCoordinates(temp.getID());
				y = (int) p.getY();
				x = (int) p.getX();
				int distance = temp1.getDistance();
				Direction direction = temp1.getDirection();
				for (int i = 0; i < distance; i++) {
					try {
						moveOrganism(direction, temp.getID(), x, y);
					} catch (ArrayIndexOutOfBoundsException e) {
						break;
					}
				}
			}
			else if (action instanceof Direction) {
				//organismsToDo.add(temp);
				int y = 0, x = 0;
				Point p = grid.getCoordinates(temp.getID());
				y = (int) p.getY();
				x = (int) p.getX();
				if (action == Direction.UP) {
					try {
						if (tempGrid[y - 1][x] == 0) {
							if (temp instanceof MovingOrganism) {
								MovingOrganism temp1 = (MovingOrganism) temp;
								if (!evolutionOn) {
									newOrganisms.add(grid.createMovingOrganism(temp1.getLifeSpan(), temp1.getMaxSpeed(),  x, y - 1));
								}
								else {
									int newLifeSpan = temp1.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
									if (newLifeSpan < 1) {
										newLifeSpan = 1;
									}
									newOrganisms.add(grid.createMovingOrganism(newLifeSpan, temp1.getMaxSpeed(),  x, y - 1));
								}
							}
							else {
								if (!evolutionOn) {
									newOrganisms.add(grid.createOrganism(temp.getLifeSpan(), x, y - 1));
								}
								else {
									int newLifeSpan = temp.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
									if (newLifeSpan < 1) {
										newLifeSpan = 1;
									}
									newOrganisms.add(grid.createOrganism(newLifeSpan , x, y - 1));
								}
							}
						}
					}catch (ArrayIndexOutOfBoundsException e) {
						if (grid.getRoundWorldOn()) {
							if (tempGrid[tempGrid.length - 1][x] == 0) {
								if (temp instanceof MovingOrganism) {
									MovingOrganism temp1 = (MovingOrganism) temp;
									if (!evolutionOn) {
										newOrganisms.add(grid.createMovingOrganism(temp1.getLifeSpan(), temp1.getMaxSpeed(),  x, tempGrid.length - 1));
									}
									else {
										int newLifeSpan = temp1.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
										if (newLifeSpan < 1) {
											newLifeSpan = 1;
										}
										newOrganisms.add(grid.createMovingOrganism(newLifeSpan, temp1.getMaxSpeed(),  x, tempGrid.length - 1));
									}
								}
								else {
									if (!evolutionOn) {
										newOrganisms.add(grid.createOrganism(temp.getLifeSpan(), x, tempGrid.length - 1));
									}
									else {
										int newLifeSpan = temp.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
										if (newLifeSpan < 1) {
											newLifeSpan = 1;
										}
										newOrganisms.add(grid.createOrganism(newLifeSpan , x, tempGrid.length - 1));
									}
								}
							} 
						}
					}
				}
				else if (action == Direction.DOWN) {
					try {
						if (tempGrid[y + 1][x] == 0) {
							if (temp instanceof MovingOrganism) {
								MovingOrganism temp1 = (MovingOrganism) temp;
								if (!evolutionOn) {
									newOrganisms.add(grid.createMovingOrganism(temp1.getLifeSpan(), temp1.getMaxSpeed(),  x, y + 1));
								}
								else {
									int newLifeSpan = temp1.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
									if (newLifeSpan < 1) {
										newLifeSpan = 1;
									}
									newOrganisms.add(grid.createMovingOrganism(newLifeSpan, temp1.getMaxSpeed(),  x, y + 1));
								}
							}
							else {
								if (!evolutionOn) {
									newOrganisms.add(grid.createOrganism(temp.getLifeSpan(), x, y + 1));
								}
								else {
									int newLifeSpan = temp.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
									if (newLifeSpan < 1) {
										newLifeSpan = 1;
									}
									newOrganisms.add(grid.createOrganism(newLifeSpan, x, y + 1));
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						if (grid.getRoundWorldOn()) {
							if (tempGrid[0][x] == 0) {
								if (temp instanceof MovingOrganism) {
									MovingOrganism temp1 = (MovingOrganism) temp;
									if (!evolutionOn) {
										newOrganisms.add(grid.createMovingOrganism(temp1.getLifeSpan(), temp1.getMaxSpeed(),  x, 0));
									}
									else {
										int newLifeSpan = temp1.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
										if (newLifeSpan < 1) {
											newLifeSpan = 1;
										}
										newOrganisms.add(grid.createMovingOrganism(newLifeSpan, temp1.getMaxSpeed(),  x, 0));
									}
								}
								else {
									if (!evolutionOn) {
										newOrganisms.add(grid.createOrganism(temp.getLifeSpan(), x, 0));
									}
									else {
										int newLifeSpan = temp.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
										if (newLifeSpan < 1) {
											newLifeSpan = 1;
										}
										newOrganisms.add(grid.createOrganism(newLifeSpan, x, 0));
									}
								}
							}
						}
					}
				}
				else if (action == Direction.RIGHT) {
					try {
						if (tempGrid[y][x + 1] == 0) {
							if (temp instanceof MovingOrganism) {
								MovingOrganism temp1 = (MovingOrganism) temp;
								if (!evolutionOn) {
									newOrganisms.add(grid.createMovingOrganism(temp1.getLifeSpan(), temp1.getMaxSpeed(),  x + 1, y));
								}
								else {
									int newLifeSpan = temp1.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
									if (newLifeSpan < 1) {
										newLifeSpan = 1;
									}
									newOrganisms.add(grid.createMovingOrganism(newLifeSpan, temp1.getMaxSpeed(),  x + 1, y));
								}
							}
							else {
								if (!evolutionOn) {
									newOrganisms.add(grid.createOrganism(temp.getLifeSpan(), x + 1, y));
								}
								else {
									int newLifeSpan = temp.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
									if (newLifeSpan < 1) {
										newLifeSpan = 1;
									}
									newOrganisms.add(grid.createOrganism(newLifeSpan, x + 1, y));
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						if (grid.getRoundWorldOn()) {
							if (tempGrid[y][0] == 0) {
								if (temp instanceof MovingOrganism) {
									MovingOrganism temp1 = (MovingOrganism) temp;
									if (!evolutionOn) {
										newOrganisms.add(grid.createMovingOrganism(temp1.getLifeSpan(), temp1.getMaxSpeed(), 0, y));
									}
									else {
										int newLifeSpan = temp1.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
										if (newLifeSpan < 1) {
											newLifeSpan = 1;
										}
										newOrganisms.add(grid.createMovingOrganism(newLifeSpan, temp1.getMaxSpeed(), 0, y));
									}
								}
								else {
									if (!evolutionOn) {
										newOrganisms.add(grid.createOrganism(temp.getLifeSpan(), 0, y));
									}
									else {
										int newLifeSpan = temp.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
										if (newLifeSpan < 1) {
											newLifeSpan = 1;
										}
										newOrganisms.add(grid.createOrganism(newLifeSpan, 0, y));
									}
								}
							}
						}
					}
				}
				else if (action == Direction.LEFT) {
					try {
						if (tempGrid[y][x - 1] == 0) {
							if (temp instanceof MovingOrganism) {
								MovingOrganism temp1 = (MovingOrganism) temp;
								if (!evolutionOn) {
									newOrganisms.add(grid.createMovingOrganism(temp1.getLifeSpan(), temp1.getMaxSpeed(),  x - 1, y));
								}
								else {
									int newLifeSpan = temp1.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
									if (newLifeSpan < 1) {
										newLifeSpan = 1;
									}
									newOrganisms.add(grid.createMovingOrganism(newLifeSpan, temp1.getMaxSpeed(),  x - 1, y));
								}
							}
							else {
								if (!evolutionOn) {
									newOrganisms.add(grid.createOrganism(temp.getLifeSpan(), x - 1, y));
								}
								else {
									int newLifeSpan = temp.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
									if (newLifeSpan < 1) {
										newLifeSpan = 1;
									}
									newOrganisms.add(grid.createOrganism(newLifeSpan, x - 1, y));
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						if (grid.getRoundWorldOn()) {
							if (tempGrid[y][tempGrid[0].length - 1] == 0) {
								if (temp instanceof MovingOrganism) {
									MovingOrganism temp1 = (MovingOrganism) temp;
									if (!evolutionOn) {
										newOrganisms.add(grid.createMovingOrganism(temp1.getLifeSpan(), temp1.getMaxSpeed(),  tempGrid[0].length - 1, y));
									}
									else {
										int newLifeSpan = temp1.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
										if (newLifeSpan < 1) {
											newLifeSpan = 1;
										}
										newOrganisms.add(grid.createMovingOrganism(newLifeSpan, temp1.getMaxSpeed(),  tempGrid[0].length - 1, y));
									}
								}
								else {
									if (!evolutionOn) {
										newOrganisms.add(grid.createOrganism(temp.getLifeSpan(), tempGrid[0].length - 1, y));
									}
									else {
										int newLifeSpan = temp.getLifeSpan() + Modifier.MAX_SPEED.getRandomMod();
										if (newLifeSpan < 1) {
											newLifeSpan = 1;
										}
										newOrganisms.add(grid.createOrganism(newLifeSpan, tempGrid[0].length - 1, y));
									}
								}
							}
						}
					}
				}
				else if (action == null) {}
			}
		}
		//grid.displayGrid();
		return newOrganisms;
	}
	/*public ArrayList<Organism> moveOrganisms() {
		//[ID][Spaces to move]
		ArrayList<int[]>organismsToMove = new ArrayList<int[]>();
		ArrayList<Integer>organismsOutOfMoves = new ArrayList<Integer>();
		ArrayList<Organism>newOrganisms = new ArrayList<Organism>();
		for(Organism temp : grid.getOrganisms()) {
			int[] tempArray = new int[2];    
			tempArray[0] = temp.getID();
			tempArray[1] = temp.moveSpaces();
			if (tempArray[1] != 0 ) {
				organismsToMove.add(tempArray);
			}
		}
		if(organismsToMove.size() == 0){
			grid.displayGrid();
			repaint();
			try{
				Thread.sleep(simSpeed);
			}catch(Exception e){
				System.out.println(e);
			}
		}

		while(organismsToMove.size() != 0){
			for(int[] tempArray : organismsToMove) {
				for(Organism temp : grid.getOrganisms()){
					if(temp.getID() == tempArray[0]) {
						int[][] nextGrid = grid.cloneGrid();

						Organism.Direction direction = temp.chooseDirection();
						for (int y = 0; y < grid.getHeight(); y++) {
							for (int x = 0; x < grid.getWidth(); x++) {
								if (grid.getGrid()[y][x] == temp.getID()) {
									try {
										switch (direction) {
										case UP:
											if(grid.getGrid()[y - 1][x]==0) {
												nextGrid[y - 1][x] = temp.getID();
												nextGrid[y][x] = 0;
											}
											else {
												nextGrid[y][x] = temp.getID();
											}
											break;
										case DOWN:
											if (grid.getGrid()[y + 1][x] == 0) {
												nextGrid[y + 1][x] = temp.getID();
												nextGrid[y][x] = 0;
											}
											else {
												nextGrid[y][x] = temp.getID();
											}
											break;
										case LEFT:
											if (grid.getGrid()[y][x - 1] == 0) {
												nextGrid[y][x - 1] = temp.getID();
												nextGrid[y][x] = 0;
											}
											else {
												nextGrid[y][x] = temp.getID();
											}
											break;
										case RIGHT:
											if (grid.getGrid()[y][x + 1] == 0) {
												nextGrid[y][x + 1] = temp.getID();
												nextGrid[y][x] = 0;
											}
											else {
												nextGrid[y][x] = temp.getID();
											}
											break;
										default:
											System.out.println("Invalid direction.");
										}
									} catch (IndexOutOfBoundsException e) {
										nextGrid[y][x] = temp.getID();
									}
								}
							}
						}
						grid.setGrid(nextGrid);
					}
				}
			}
			repaint();
			grid.displayGrid();
			try {
				Thread.sleep(simSpeed);
			} catch (Exception e){
				System.out.println(e);
			}
			System.out.println("Organisms reproducing");
			newOrganisms = grid.sexuallyReproduce();
			repaint();
			grid.displayGrid();
			for (int i = 0; i < organismsToMove.size(); i++ ) {
				organismsToMove.get(i)[1] -= 1;
			}
			//checks the organism who have 0 moves left and adds them to a list via their ID number
			for (int[] temp : organismsToMove) {
				if (temp[1] == 0 ) {
					organismsOutOfMoves.add(temp[0]);
				}
			}
			//deletes from organismToMove if its ID is on the organismsOutOfMoves list
			for (int temp : organismsOutOfMoves) {
				for (int i = 0; i < organismsToMove.size(); i++) {
					if (temp == organismsToMove.get(i)[0])
						organismsToMove.remove(i);
				}
			}
			grid.displayGrid();
		}
		return newOrganisms;
	}*/

	/**
	 * executes one turn of the simulation from the GridRunner class
	 */
	public void takeTurn() {
		gridTime++;
		ArrayList<Organism> newOrganisms = doOrganismsActions();

		ArrayList<Organism> organisms = grid.getOrganisms();
		for (Organism temp : organisms) {
			temp.age();
		}
		int prevNum = organisms.size();
		grid.killOrganisms();
		int postNum = organisms.size();
		numDead += prevNum - postNum;
	}


}
