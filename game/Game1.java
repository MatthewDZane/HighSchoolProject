package game;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import display.Displayer;
import helpers.ArrayIndexOccupiedException;
import helpers.AttackListener;
import helpers.Game1ActionListener;

public class Game1 {
	private int level;
	private Grid grid;

	private static boolean restarted = false;
	private static boolean isDone = false;
	private static boolean isPaused = false;

	private Timer levelUpTimer = new Timer(100, new LevelUpUnPauseListener());
	
	public int getLevel() { return level; }
	public Grid getGrid() { return grid; }

	public static boolean getRestarted() { return restarted; }
	public static void setRestarted(boolean restartedIn) { restarted = restartedIn; }
	public static boolean getIsDone() { return isDone; }
	public static void setIsDone(boolean isDoneIn) { isDone = isDoneIn; }
	public static boolean getIsPaused() { return isPaused; }
	public static void setIsPaused(boolean isPausedIn) { isPaused = isPausedIn; }

	public void setAttackListener(AttackListener attackListenerIn) { 
		grid.setAttackListener(attackListenerIn);
	}

	public class EnemyTimerListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			if (!Game1.getIsPaused()) {
				grid.moveEnemies();
				grid.haveEnemiesAttack();
			}
		}

	}

	public class ProjectileTimerListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			if (!Game1.getIsPaused()) {
				grid.movePlayerProjectiles();
				grid.moveEnemyProjectiles();
			}
		}
	}

	public class LevelCheckTimerListener {

		public void actionPerformed() {
			
			if (grid.getPlayer().isDead()) {
				setIsDone(true);
			}
			else if ( !getGrid().areReachableEnemies()) {
				isPaused = true;
				
				levelUp();
				grid = getGrid();

				levelUpTimer.start();
			}
		}
	}
	
	public class LevelUpUnPauseListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			levelUpTimer.stop();
			System.out.println("unpausing");
			isPaused = false;			
		}
		
	}
	
	public class GameStartTimerListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			Game1.setIsDone(false);
		}

	}
	
	public static Displayer run() {

		Displayer display = new Displayer(1000, 1000);
		Game1 game = display.getGame();
		Grid grid = game.getGrid();
		
		EnemyTimerListener enemyTimerListener =  game.new EnemyTimerListener();
		ProjectileTimerListener projectileTimerListener = game.new ProjectileTimerListener(); 
		LevelCheckTimerListener levelCheckTimerListener = game.new LevelCheckTimerListener();
		
		Timer enemyTimer = new Timer(500, enemyTimerListener);
		Timer projectileTimer = new Timer(200, projectileTimerListener);

		grid.setLevelCheckTimerListener(levelCheckTimerListener);
		
		enemyTimer.start();
		projectileTimer.start();
		
		Game1.setIsPaused(false);
		
		display.repaint();
		
		while (!Game1.getIsDone()) {
			display.repaint();
		}
		
		display.endGame();
		
		while (!restarted) {
			display.repaint();
		}
		
		restarted = false;
		isDone = false;

		return display;
	}

	public Game1(Game1ActionListener enemyHitHandler, Game1ActionListener enemyKilledtHandler,
			Game1ActionListener damageTakenHandler, Game1ActionListener healedHandler, 
			Game1ActionListener attackUpHandler, Game1ActionListener levelUpHandler) {
		grid = new Grid(enemyHitHandler, enemyKilledtHandler, damageTakenHandler, 
				healedHandler, attackUpHandler, levelUpHandler);
		level = 1;
		placeFirstLevel();
	}
	public void levelUp() {
		level++;
		int gridLength = level + 4;
		
		grid.getPlayerProjectiles().removeAll(grid.getPlayerProjectiles());
		grid.getEnemyProjectiles().removeAll(grid.getEnemyProjectiles());
		
		Grid newGrid = new Grid(gridLength, gridLength);
		
		newGrid.copyGrid(grid);
		grid = newGrid;
		
		placeEnemies();
		placeObstacles();
		placePickUp();
		getGrid().getPlayer().upgrade();
	}
	
	public void placeFirstLevel() {
		try {
			grid.placeObstacle(0, 0);
			grid.placeObstacle(4, 0);
			grid.placeObstacle(0, 4);
			grid.placeObstacle(4, 4);
			grid.placeObstacle(2, 1);
			grid.placeObstacle(2, 2);
			grid.placeObstacle(2, 3);
			
			grid.placeEnemy(3, 2, level / 5 + 1, level / 5 + 1);
		} catch (ArrayIndexOccupiedException e) {}
		
	}
	
	public void placeObstacles() {
		for (int i = 0; i < (level + 10) / 5 && !grid.isFull(); i++) {
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
				grid.placeEnemy(x, y, level / 5 + 1, level / 5 + 1);
			} catch (ArrayIndexOccupiedException e) {
				i--;
			}
		}
		for (int i = 0; i < level / 2  && !grid.isFull(); i++) {
			int y = (int) (Math.random() * grid.getObstacleGrid().length);
			int x = (int) (Math.random() * grid.getObstacleGrid()[0].length);
			try {
				grid.placeMovingEnemy(x, y, level / 5 + 1, level / 5 + 1);
			} catch (ArrayIndexOccupiedException e) {
				i--;
			}
		}
		for (int i = 0; i < (int) Math.pow((level)/ 3, 2)  && !grid.isFull(); i++) {
			int y = (int) (Math.random() * grid.getObstacleGrid().length);
			int x = (int) (Math.random() * grid.getObstacleGrid()[0].length);
			try {
				grid.placeShootingEnemy(x, y, level / 5 + 1, level / 8 + 1);
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
}
