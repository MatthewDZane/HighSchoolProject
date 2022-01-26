package game;

import display.Displayer;
import helpers.ArrayIndexOccupiedException;
import helpers.AttackListener;
import helpers.Game1ActionListener;

public class Game1 {
	private int level;
	private int turn = 1;
	private Grid grid;

	private boolean isDone = false;
	private boolean hasMoved = false;

	private AttackListener attackListener;

	public int getLevel() { return level; }
	public int getTurn() { return turn; }
	public Grid getGrid() { return grid; }

	public boolean getIsDone() { return isDone; }
	public void setIsDone(boolean isDoneIn) { isDone = isDoneIn; }
	public boolean getHasMoved() { return hasMoved; }
	public void setHasMoved(boolean hasMovedIn) { hasMoved = hasMovedIn; }


	public void setAttackListener(AttackListener attackListenerIn) { 
		attackListener = attackListenerIn; 
		grid.setAttackListener(attackListenerIn);
	}


	public static void run() {
		Displayer display = new Displayer(1000, 1000);
		Game1 game = display.getGame();
		Grid grid = game.getGrid();

		display.repaint();
		while (!game.getIsDone()) {
			display.repaint();

			game.setHasMoved(false);
			display.enableKeys();
			while (!game.getHasMoved()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
				//display.update();
			}
			grid.moveProjectiles();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {};
			game.incrementTurn();


			grid.moveEnemies();
			grid.haveEnemiesAttack();
			display.repaint();

			if (grid.getPlayer().isDead()) {
				game.setIsDone(true);
			}
			else if ( !game.getGrid().areReachableEnemies()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {}
				game.levelUp();
				grid = game.getGrid();
				
			}

		}
		display.endGame();

	}

	public Game1(Game1ActionListener enemyHitHandler, Game1ActionListener enemyKilledtHandler,
			Game1ActionListener damageTakenHandler, Game1ActionListener healedHandler, 
			Game1ActionListener attackUpHandler, Game1ActionListener levelUpHandler) {
		grid = new Grid(enemyHitHandler, enemyKilledtHandler, damageTakenHandler, 
				healedHandler, attackUpHandler, levelUpHandler);
		level = 1;
		placeEnemies();
		placeObstacles();

	}
	public void levelUp() {
		level++;
		int gridLength = level + 2;
		Grid newGrid = new Grid(gridLength, gridLength);
		newGrid.copyGrid(grid);
		grid = newGrid;
		placeEnemies();
		placeObstacles();
		placePickUp();
		getGrid().getPlayer().upgrade();
	}
	public void placeObstacles() {
		for (int i = 0; i < (level + 8) / 4 && !grid.isFull(); i++) {
			if ((int) (Math.random() * 2) == 0) {
				int y = (int) (Math.random() * grid.getObstacleGrid().length);
				try {
					grid.placeObstacle(grid.getObstacleGrid()[0].length - 1, y);
				} catch (ArrayIndexOccupiedException e) {
					i--;
				}
			}
			else {
				int x = (int) (Math.random() * grid.getObstacleGrid()[0].length);
				try {
					grid.placeObstacle(x, grid.getObstacleGrid().length - 1);
				} catch (ArrayIndexOccupiedException e) {
					i--;
				}
			}
		}
	}
	public void placeEnemies() {
		for (int i = 0; i < (level + 3)/ 3 && !grid.isFull(); i++) {
			int y = (int) (Math.random() * grid.getObstacleGrid().length);
			int x = (int) (Math.random() * grid.getObstacleGrid()[0].length);
			try {
				grid.placeEnemy(x, y, level / 5 + 1, level / 8 + 1);
			} catch (ArrayIndexOccupiedException e) {
				i--;
			}
		}
		for (int i = 0; i < level / 2  && !grid.isFull(); i++) {
			int y = (int) (Math.random() * grid.getObstacleGrid().length);
			int x = (int) (Math.random() * grid.getObstacleGrid()[0].length);
			try {
				grid.placeMovingEnemy(x, y, level / 5 + 1, level / 8 + 1);
			} catch (ArrayIndexOccupiedException e) {
				i--;
			}
		}
	}
	public void placePickUp() {
		for (int i = 0; i < (level + 6) / 7 && !grid.isFull(); i++) {
			int y = (int) (Math.random() * grid.getObstacleGrid().length);
			int x = (int) (Math.random() * grid.getObstacleGrid()[0].length);
			try {
				grid.placeHealthPack(x, y, (level - 1) / 10 + 1);
			} catch (ArrayIndexOccupiedException e) {
				i--;
			}
		}
	}

	public void incrementTurn() { turn++; }

}
