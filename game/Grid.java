package game;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import entities.Arrow;
import entities.Enemy;
import entities.HealthPack;
import entities.MovingEnemy;
import entities.Obstacle;
import entities.PickUp;
import entities.Player;
import entities.Projectile;
import helpers.AStar;
import helpers.ArrayIndexOccupiedException;
import helpers.AttackListener;
import helpers.Direction;
import helpers.Game1ActionListener;
import helpers.Maze;

public class Grid {
	private final int FIRST_LEVEL_GRID_SIZE = 3;

	private Obstacle[][] obstacleGrid;
	private Object [][] grid;

	private Player player;
	private static List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private static List<Enemy> enemies = new ArrayList<Enemy>();
	private static List<MovingEnemy> movingEnemies = new ArrayList<MovingEnemy>();
	private static List<PickUp> pickUps = new ArrayList<PickUp>();
	private static List<Projectile> projectiles = new ArrayList<Projectile>();

	private Game1ActionListener enemyHitHandler;
	//private Game1ActionListener gameOverHandler;
	private Game1ActionListener enemyKilledtHandler;
	private Game1ActionListener damageTakenHandler;
	private Game1ActionListener healedHandler;
	private Game1ActionListener attackUpHandler;
	private Game1ActionListener levelUpHandler;
	private AttackListener attackListener;

	public Game1ActionListener getEnemyHitHandler() { return enemyHitHandler; }
	//public Game1ActionListener getGameOverHandler() { return gameOverHandler; }
	public Game1ActionListener getEnemyKilledtHandler() { return enemyKilledtHandler; }
	public Game1ActionListener getDamageTakenHandler() { return damageTakenHandler; }
	public Game1ActionListener getHealedHandler() { return healedHandler; }
	public Game1ActionListener getAttackUpHandler() { return attackUpHandler; }
	public Game1ActionListener getLevelUpHandler() { return levelUpHandler; }

	public Obstacle[][] getObstacleGrid() { return obstacleGrid; }
	public Object[][] getGrid() { return grid;}

	public Player getPlayer() { return player; }
	public List<Obstacle> getObstacles() { return obstacles; }
	public List<Enemy> getEnemies() { return enemies; }
	public List<MovingEnemy> getMovingEnemies() { return movingEnemies; }
	public List<PickUp> getPickUps() { return pickUps; }
	public List<Projectile> getProjectiles() { return projectiles; }

	public AttackListener getAttackListener() { return attackListener; }
	public void setAttackListener(AttackListener attackUpListenerIn) { attackListener = attackUpListenerIn; }

	public Grid(Game1ActionListener enemyHitHandlerIn,
			Game1ActionListener enemyKilledtHandlerIn,Game1ActionListener damageTakenHandlerIn,
			Game1ActionListener healedHandlerIn, Game1ActionListener attackUpHandlerIn,
			Game1ActionListener levelUpHandlerIn) {

		enemyHitHandler = enemyHitHandlerIn;
		enemyKilledtHandler = enemyKilledtHandlerIn;
		damageTakenHandler = damageTakenHandlerIn;
		healedHandler = healedHandlerIn;
		attackUpHandler = attackUpHandlerIn;
		levelUpHandler = levelUpHandlerIn;

		obstacleGrid = new Obstacle[FIRST_LEVEL_GRID_SIZE][FIRST_LEVEL_GRID_SIZE];
		player = new Player(FIRST_LEVEL_GRID_SIZE / 2, FIRST_LEVEL_GRID_SIZE / 2);
		obstacleGrid[FIRST_LEVEL_GRID_SIZE / 2][FIRST_LEVEL_GRID_SIZE / 2] = player;

		grid = new Object[FIRST_LEVEL_GRID_SIZE][FIRST_LEVEL_GRID_SIZE];
	}

	public Grid(int width, int height) {
		obstacleGrid = new Obstacle[height][width];
		grid = new Object[height][width];
	}

	/**
	 * Copies the contents from gridIn to this grid starting from point (0, 0)
	 * to the end of gridIn
	 * @param gridIn - Pre-Condition: size must be less than or equal to this
	 */
	public void copyGrid(Grid gridIn) {
		Obstacle[][] smallerObstacleGrid = gridIn.getObstacleGrid();
		Object[][] smallerGrid = gridIn.getGrid();

		player = gridIn.getPlayer();

		for (int y = 0; y < smallerObstacleGrid.length; y++) {
			for (int x = 0; x < smallerObstacleGrid[0].length; x++) {
				if (smallerObstacleGrid[y][x] != null)
					obstacleGrid[y][x] = smallerObstacleGrid[y][x];
				if (smallerGrid[y][x] != null) {
					grid[y][x] = smallerGrid[y][x];
				}
			}
		}

		enemyHitHandler = gridIn.getEnemyHitHandler();
		enemyKilledtHandler =gridIn.getEnemyKilledtHandler();
		damageTakenHandler = gridIn.getDamageTakenHandler();
		healedHandler = gridIn.getHealedHandler();
		attackUpHandler = gridIn.getAttackUpHandler();
		levelUpHandler = gridIn.getLevelUpHandler();
		attackListener = gridIn.getAttackListener();
	}

	//Methods used to move the player
	public boolean movePlayer(Direction direction) {
		switch (direction) {
		case UP: return movePlayerUp();
		case DOWN: return movePlayerDown();
		case LEFT: return movePlayerLeft();
		case RIGHT: return movePlayerRight();
		default: return false;
		}
	}
	private boolean movePlayerUp() {
		boolean success = true;
		Point coordinates = player.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y - 1][x] instanceof Enemy) {
				success = attackEnemy(Direction.UP);
			}
			else if (obstacleGrid[y - 1][x] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y - 1][x] = player;
				success = true;
			} 
			else if (obstacleGrid[y - 1][x] instanceof Obstacle) {
				success = false;
			}
			if (grid[y - 1][x] instanceof PickUp && success) {
				PickUp pickUp = (PickUp) grid[y - 1][x];
				pickUp.usePower(player);
				removePickUp(pickUp);
				obstacleGrid[y][x] = null;
				obstacleGrid[y - 1][x] = player;
				grid[y - 1][x] = null;
				success = true;
				if (pickUp instanceof HealthPack) {
					healedHandler.actionPreformed();
				}
			}
			if (success) {
				coordinates.translate(0, -1);
			}
			else {
				System.out.println("A " + obstacleGrid[y - 1][x] + " is in the way.");
			}
			return success;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Out of Bounds");
			return false;
		}
	}
	private boolean movePlayerDown() {
		boolean success = true;
		Point coordinates = player.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y + 1][x] instanceof Enemy) {
				success = attackEnemy(Direction.DOWN);
			}
			else if (obstacleGrid[y + 1][x] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y + 1][x] = player;
				success = true;
			}
			else if (obstacleGrid[y + 1][x] instanceof Obstacle) {
				success = false;
			}
			if (grid[y + 1][x] instanceof PickUp && success) {
				PickUp pickUp = (PickUp) grid[y + 1][x];
				pickUp.usePower(player);
				removePickUp(pickUp);
				obstacleGrid[y][x] = null;
				obstacleGrid[y + 1][x] = player;
				grid[y + 1][x] = null;
				success = true;
				if (pickUp instanceof HealthPack) {
					healedHandler.actionPreformed();
				}
			}
			if (success) {
				coordinates.translate(0, 1);
			}
			else {
				System.out.println("A " + obstacleGrid[y + 1][x] + " is in the way.");
			}
			return success;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Out of Bounds");
			return false;
		}
	}
	private boolean movePlayerLeft() {
		boolean success = true;
		Point coordinates = player.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y][x - 1] instanceof Enemy) {
				success = attackEnemy(Direction.LEFT);
			}
			else if (obstacleGrid[y][x - 1] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y][x - 1] = player;
				success = true;
			}
			else if (obstacleGrid[y][x - 1] instanceof Obstacle) {
				success = false;
			}
			if (grid[y][x - 1] instanceof PickUp && success) {
				PickUp pickUp = (PickUp) grid[y][x - 1];
				pickUp.usePower(player);
				removePickUp(pickUp);
				obstacleGrid[y][x] = null;
				obstacleGrid[y][x - 1] = player;
				grid[y][x - 1] = null;
				if (pickUp instanceof HealthPack) {
					healedHandler.actionPreformed();
				}
				success = true;
			}
			if (success) {
				coordinates.translate(-1, 0);
			}
			else {
				System.out.println("A " + obstacleGrid[y][x - 1] + " is in the way.");
			}
			return success;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Out of Bounds");
			return false;
		}
	}
	private boolean movePlayerRight() {
		boolean success = true;
		Point coordinates = player.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y][x + 1] instanceof Enemy) {
				success = attackEnemy(Direction.RIGHT);
			}
			else if (obstacleGrid[y][x + 1] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y][x + 1] = player;
				success = true;
			}
			else if (obstacleGrid[y][x + 1] instanceof Obstacle) {
				success = false;
			}
			if (grid[y][x + 1] instanceof PickUp && success) {
				PickUp pickUp = (PickUp) grid[y][x + 1];
				pickUp.usePower(player);
				removePickUp(pickUp);
				obstacleGrid[y][x] = null;
				obstacleGrid[y][x + 1] = player;
				grid[y][x + 1] = null;
				success = true;
				if (pickUp instanceof HealthPack) {
					healedHandler.actionPreformed();
				}
			}
			if (success) {
				coordinates.translate(1, 0);
			}
			else {
				System.out.println("A " + obstacleGrid[y][x + 1] + " is in the way.");
			}
			return success;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Out of Bounds");
			return false;
		}
	}

	//methods used to attack enemies
	private boolean attackEnemy(Direction direction) {
		switch (direction) {
		case UP: return attackEnemyUp();
		case DOWN: return attackEnemyDown();
		case LEFT: return attackEnemyLeft();
		case RIGHT: return attackEnemyRight();
		default: return false;
		}
	}
	private boolean attackEnemyUp() {
		
		Point coordinates = player.getCoordinates();
		attackListener.actionPreformed(Direction.UP, new Point(coordinates.x, coordinates.y - 1));
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		Enemy enemy = (Enemy) obstacleGrid[y - 1][x];
		enemy.takeDamage(player.getDamage());
		if (enemy.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y - 1][x] = player;
			return killEnemy(enemy);
		}
		else {
			enemyHitHandler.actionPreformed();
			try {
				for (int i = 0; i < player.getKnockback() && obstacleGrid[y - 2 - i][x] == null; i++) {
					obstacleGrid[y - 2 - i][x] = enemy;
					obstacleGrid[y - 1 - i][x] = null;
					Point enemyCoordinates = enemy.getCoordinates();
					enemyCoordinates.translate(0, -1);
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			return true;
		}
	}
	private boolean attackEnemyDown() {
		Point coordinates = player.getCoordinates();
		attackListener.actionPreformed(Direction.DOWN, new Point(coordinates.x, coordinates.y + 1));
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		Enemy enemy = (Enemy) obstacleGrid[y + 1][x];
		enemy.takeDamage(player.getDamage());
		if (enemy.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y + 1][x] = player;
			return killEnemy(enemy);
		}
		else {
			try {
				enemyHitHandler.actionPreformed();
				for (int i = 0; i < player.getKnockback() && obstacleGrid[y + 2 + i][x] == null; i++) {
					obstacleGrid[y + 2 + i][x] = enemy;
					obstacleGrid[y + 1 + i][x] = null;
					Point enemyCoordinates = enemy.getCoordinates();
					enemyCoordinates.translate(0, 1);
				}
			}catch (ArrayIndexOutOfBoundsException e) {}
			return true;
		}
	}
	private boolean attackEnemyLeft() {
		Point coordinates = player.getCoordinates();
		attackListener.actionPreformed(Direction.LEFT, new Point(coordinates.x - 1, coordinates.y));
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		Enemy enemy = (Enemy) obstacleGrid[y][x - 1];
		enemy.takeDamage(player.getDamage());
		if (enemy.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y][x - 1] = player;
			return killEnemy(enemy);
		}
		else {
			enemyHitHandler.actionPreformed();
			try {
				for (int i = 0; i < player.getKnockback() && obstacleGrid[y][x - 2 - i] == null; i++) {
					obstacleGrid[y][x - 2 - i] = enemy;
					obstacleGrid[y][x - 1 - i] = null;
					Point enemyCoordinates = enemy.getCoordinates();
					enemyCoordinates.translate(-1, 0);
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			return true;
		}
	}
	private boolean attackEnemyRight() {
		Point coordinates = player.getCoordinates();
		attackListener.actionPreformed(Direction.RIGHT, new Point(coordinates.x + 1, coordinates.y));
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		Enemy enemy = (Enemy) obstacleGrid[y][x + 1];
		enemy.takeDamage(player.getDamage());
		if (enemy.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y][x + 1] = player;
			return killEnemy(enemy);
		}
		else {
			enemyHitHandler.actionPreformed();
			try {
				for (int i = 0; i < player.getKnockback() && obstacleGrid[y][x + 2 + i] == null; i++) {
					obstacleGrid[y][x + 2 + i] = enemy;
					obstacleGrid[y][x + 1 + i] = null;
					Point enemyCoordinates = enemy.getCoordinates();
					enemyCoordinates.translate(1, 0);
				}
			}
			catch (ArrayIndexOutOfBoundsException e) {}
			return true;
		}
	}
	/**
	 * Precondition: obstacle is only instance of Obstacle
	 * @param obstacle
	 * @return
	 */
	private boolean destroyObstacle(Obstacle obstacle) {
		for (int i = 0; i < obstacles.size(); i++) {
			if (obstacle == obstacles.get(i)) {
				obstacles.remove(i);
				return true;
			}
		}
		return false;
	}
	//kills enemy by removing it from any lists
	private boolean killEnemy(Enemy enemy) {
		for (int i = 0; i < movingEnemies.size(); i++) {
			if (enemy == movingEnemies.get(i)) {
				movingEnemies.remove(i);
			}
		}
		for (int i = 0; i < enemies.size(); i++) {
			if (enemy == enemies.get(i)){
				enemies.remove(i);
				enemyKilledtHandler.actionPreformed();
				return true;
			}
		}
		return true;
	}

	private boolean removePickUp(PickUp pickUp) {
		for (int i = 0; i < pickUps.size(); i++) {
			if (pickUp == pickUps.get(i)) {
				pickUps.remove(i);
				return true;
			}
		}
		return false;
	}

	private boolean removeProjectile(Projectile projectile) {
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectile == projectiles.get(i)) {
				projectiles.remove(i);
				return true;
			}
		}
		return false;
	}
	//methods used to place obstacle
	public void placeObstacle(int x, int y) throws ArrayIndexOccupiedException {
		if (obstacleGrid[y][x] == null && grid[y][x] == null) {
			Obstacle obstacle = new Obstacle(x, y);
			obstacles.add(obstacle);
			obstacleGrid[y][x] = obstacle;
		}
		else {
			throw new ArrayIndexOccupiedException();
		}
	}

	public void placeEnemy(int x, int y, int hp, int damage) throws ArrayIndexOccupiedException {
		if (obstacleGrid[y][x] == null && grid[y][x] == null) {
			Enemy enemy = new Enemy(x, y, hp, damage);
			enemies.add(enemy);
			obstacleGrid[y][x] = enemy;
		}
		else {
			throw new ArrayIndexOccupiedException();
		}
	}

	public void placeHealthPack(int x, int y, int hp)  throws ArrayIndexOccupiedException{
		if (obstacleGrid[y][x] == null && grid[y][x] == null) {
			PickUp pickUp = new HealthPack(x, y, hp);
			pickUps.add(pickUp);
			grid[y][x] = pickUp;
		}
		else {
			throw new ArrayIndexOccupiedException();
		}
	}


	public void placeMovingEnemy(int x, int y, int hp, int damage) throws ArrayIndexOccupiedException {
		if (obstacleGrid[y][x] == null && grid[y][x] == null) {
			MovingEnemy enemy = new MovingEnemy(x, y, hp, damage);
			enemies.add(enemy);
			movingEnemies.add(enemy);
			obstacleGrid[y][x] = enemy;
		}
		else {
			throw new ArrayIndexOccupiedException();
		}
	}
	/**
	 * Precondition: arrow is placed in spot of shooter
	 * arrow is not placed into grid until it is moved for a first time
	 * @param x
	 * @param y
	 * @param speedIn
	 * @param damageIn
	 * @param knockbackIn
	 * @param directionIn
	 */
	public void shootArrow(int x, int y, int speedIn, int damageIn, int knockbackIn, Direction directionIn) {
		Projectile arrow = new Arrow(x, y, speedIn, damageIn, knockbackIn, directionIn);
		projectiles.add(arrow);
		grid[y][x] = arrow;
	}

	public boolean playerShootArrow(Direction direction) {
		if (direction != Direction.INVALID) {
			Point coordinates = player.getCoordinates();
			int x = coordinates.x;
			int y = coordinates.y;
			shootArrow(x, y, 1, player.getDamage(), 0, direction);
			return true;
		}
		return false;
	}

	public void moveProjectiles() {
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile projectile = projectiles.get(i);
			switch (projectile.getDirection()) {
			case UP: 
				if (moveProjectileUp(projectile)) {
					i--;
				}
				break;
			case DOWN: 
				if (moveProjectileDown(projectile)) {
					i--;
				}
				break;
			case LEFT:  
				if (moveProjectileLeft(projectile)) {
					i--;
				}
				break;
			case RIGHT:
				if (moveProjectileRight(projectile)) {
					i--;
				}
				break;
			default: return;
			}
		}
	}

	public boolean moveProjectileUp(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (obstacleGrid[y - 1][x] == null && grid[y - 1][x] == null) {
					coordinates.translate(0, -1);
					grid[y][x] = null;
					grid[y - 1][x] = projectile;
				}
				else if (obstacleGrid[y - 1][x] instanceof Enemy) {
					attackListener.actionPreformed(Direction.UP, new Point(x, y - 1));
					Enemy enemy = (Enemy) obstacleGrid[y - 1][x];
					projectile.useAbility(enemy);
					enemy.takeDamage(projectile.getDamage() / 2);
					removeProjectile(projectile);
					grid[y][x] = null;
					if (enemy.isDead()) {
						obstacleGrid[y - 1][x] = null;
						killEnemy(enemy);
					}
					return true;
				}
				else if (obstacleGrid[y - 1][x] instanceof Obstacle) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y - 1][x];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y - 1][x] = null;

					}
					removeProjectile(projectile);
					grid[y][x] = null;
					return true;
				}
				else if (grid[y - 1][x] instanceof PickUp) {
					PickUp pickUp = (PickUp) grid[y - 1][x];
					if (projectile.useAbility(pickUp)) {
						removePickUp(pickUp);
						grid[y - 1][x] = null;
					}
					removeProjectile(projectile);
					grid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			grid[y][x] = null;
			removeProjectile(projectile);
			return true;
		}
		return false;
	}
	public boolean moveProjectileDown(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (obstacleGrid[y + 1][x] == null && grid[y + 1][x] == null) {
					coordinates.translate(0, 1);
					grid[y][x] = null;
					grid[y + 1][x] = projectile;
				}
				else if (obstacleGrid[y + 1][x] instanceof Enemy) {
					attackListener.actionPreformed(Direction.DOWN, new Point(x, y + 1));
					Enemy enemy = (Enemy) obstacleGrid[y + 1][x];
					projectile.useAbility(enemy);
					enemy.takeDamage(projectile.getDamage() / 2);
					removeProjectile(projectile);
					grid[y][x] = null;
					if (enemy.isDead()) {
						obstacleGrid[y + 1][x] = null;
						killEnemy(enemy);
					}
					return true;
				}
				else if (obstacleGrid[y + 1][x] instanceof Obstacle) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y + 1][x];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y + 1][x] = null;

					}
					removeProjectile(projectile);
					grid[y][x] = null;
					return true;
				}
				else if (grid[y + 1][x] instanceof PickUp) {
					PickUp pickUp = (PickUp) grid[y + 1][x];
					if (projectile.useAbility(pickUp)) {
						removePickUp(pickUp);
						grid[y + 1][x] = null;
					}
					removeProjectile(projectile);
					grid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			grid[y][x] = null;
			removeProjectile(projectile);
			return true;
		}
		return false;
	}
	public boolean moveProjectileLeft(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (obstacleGrid[y][x - 1] == null && grid[y][x - 1] == null) {
					coordinates.translate(-1, 0);
					grid[y][x] = null;
					grid[y][x - 1] = projectile;
				}
				else if (obstacleGrid[y][x - 1] instanceof Enemy) {
					attackListener.actionPreformed(Direction.LEFT, new Point(x - 1, y));
					Enemy enemy = (Enemy) obstacleGrid[y][x - 1];
					projectile.useAbility(enemy);
					enemy.takeDamage(projectile.getDamage() / 2);
					removeProjectile(projectile);
					grid[y][x] = null;
					if (enemy.isDead()) {
						obstacleGrid[y][x - 1] = null;
						killEnemy(enemy);
					}
					return true;
				}
				else if (obstacleGrid[y][x - 1] instanceof Obstacle) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y][x - 1];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y][x - 1] = null;

					}
					removeProjectile(projectile);
					grid[y][x] = null;
					return true;
				}
				else if (grid[y][x - 1] instanceof PickUp) {
					PickUp pickUp = (PickUp) grid[y][x - 1];
					if (projectile.useAbility(pickUp)) {
						removePickUp(pickUp);
						grid[y][x - 1] = null;
					}
					removeProjectile(projectile);
					grid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			grid[y][x] = null;
			removeProjectile(projectile);
			return true;
		}
		return false;
	}
	public boolean moveProjectileRight(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (obstacleGrid[y][x + 1] == null && grid[y][x + 1] == null) {
					coordinates.translate(1, 0);
					grid[y][x] = null;
					grid[y][x + 1] = projectile;
				}
				else if (obstacleGrid[y][x + 1] instanceof Enemy) {
					attackListener.actionPreformed(Direction.RIGHT, new Point(x + 1, y));
					Enemy enemy = (Enemy) obstacleGrid[y][x + 1];
					projectile.useAbility(enemy);
					enemy.takeDamage(projectile.getDamage() / 2);
					removeProjectile(projectile);
					grid[y][x] = null;
					if (enemy.isDead()) {
						obstacleGrid[y][x + 1] = null;
						killEnemy(enemy);
					}
					return true;
				}
				else if (obstacleGrid[y][x + 1] instanceof Obstacle) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y][x + 1];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y][x + 1] = null;

					}
					removeProjectile(projectile);
					grid[y][x] = null;
					return true;
				}
				else if (grid[y][x + 1] instanceof PickUp) {
					PickUp pickUp = (PickUp) grid[y][x + 1];
					if (projectile.useAbility(pickUp)) {
						removePickUp(pickUp);
						grid[y][x + 1] = null;
					}
					removeProjectile(projectile);
					grid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			grid[y][x] = null;
			removeProjectile(projectile);
			return true;
		}
		return false;
	}
	public boolean isFull() {
		boolean isFull = true; 

		for (int y = 0; y < obstacleGrid.length; y++) {
			for (int x = 0; x < obstacleGrid[0].length; x++) {
				if (obstacleGrid[y][x] == null) {
					isFull = false;
				}
			}
		}
		return isFull;
	}
	public static List makeList(Object [] array) {
		ArrayList <Object> list = new ArrayList<Object>();
		for (Object temp : array) {
			list.add(temp);
		}
		return list;
	}

	//methods used to move enemies that have the moving ability
	public void moveEnemies() {
		for (Enemy temp : movingEnemies) {
			boolean enemyMoved = false;

			ArrayList <Direction>  directions  = (ArrayList <Direction>) makeList(Direction.getDirections());
			while(!enemyMoved && directions.size() > 0) {
				Direction direction = Direction.getRandomDirection();
				if (directions.contains(direction)) {
					switch (direction) {
					case UP :
						enemyMoved = moveEnemyUp(temp);
						directions.remove(Direction.UP);
						break;
					case DOWN :
						enemyMoved = moveEnemyDown(temp);
						directions.remove(Direction.DOWN);
						break;
					case LEFT :
						enemyMoved = moveEnemyLeft(temp);
						directions.remove(Direction.LEFT);
						break;
					case RIGHT :
						enemyMoved = moveEnemyRight(temp);
						directions.remove(Direction.RIGHT);
						break;
					}
				}
			}
		}
	}
	private boolean moveEnemyUp(Enemy temp) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y - 1][x] == null && grid[y - 1][x] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y - 1][x] = temp;
				coordinates.translate(0, -1);
				return true;
			}
			else if (obstacleGrid[y - 1][x] instanceof Player) {
				attackListener.actionPreformed(Direction.UP, new Point(x, y - 1));
				attackPlayer(temp, Direction.UP);
				damageTakenHandler.actionPreformed();
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
		return false;
	}
	private boolean moveEnemyDown(Enemy temp) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y + 1][x] == null && grid[y + 1][x] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y + 1][x] = temp;
				coordinates.translate(0, 1);
				return true;
			}
			else if (obstacleGrid[y + 1][x] instanceof Player) {
				attackListener.actionPreformed(Direction.DOWN, new Point(x, y + 1));
				attackPlayer(temp, Direction.DOWN);
				damageTakenHandler.actionPreformed();
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
		return false;
	}
	private boolean moveEnemyLeft(Enemy temp) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y][x - 1] == null && grid[y][x - 1] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y][x - 1] = temp;
				coordinates.translate(-1, 0);
				return true;
			}
			else if (obstacleGrid[y][x - 1] instanceof Player) {
				attackListener.actionPreformed(Direction.LEFT, new Point(x - 1, y));
				attackPlayer(temp, Direction.LEFT);
				damageTakenHandler.actionPreformed();
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
		return false;
	}
	private boolean moveEnemyRight(Enemy temp) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y][x + 1] == null && grid[y][x + 1] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y][x + 1] = temp;
				coordinates.translate(1, 0);
				return true;
			}
			else if (obstacleGrid[y][x + 1] instanceof Player) {
				attackListener.actionPreformed(Direction.RIGHT, new Point(x + 1, y));
				attackPlayer(temp, Direction.RIGHT);
				damageTakenHandler.actionPreformed();
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
		return false;
	}

	public void haveEnemiesAttack() {
		for (Enemy temp : enemies) {
			if (! (temp instanceof MovingEnemy)) {
				Point coordinates = temp.getCoordinates();
				int y = (int) coordinates.getY();
				int x = (int) coordinates.getX();
				Direction direction = Direction.getRandomDirection();
				try {
					switch (direction) {
					case UP :
						if (obstacleGrid[y - 1][x] instanceof Player) {
							moveEnemyUp(temp);
						}
						break;
					case DOWN :
						if (obstacleGrid[y + 1][x] instanceof Player) {
							moveEnemyDown(temp);
						}
						break;
					case LEFT :
						if (obstacleGrid[y][x - 1] instanceof Player) {
							moveEnemyLeft(temp);
						}
						break;
					case RIGHT :
						if (obstacleGrid[y][x + 1] instanceof Player) {
							moveEnemyRight(temp);
						}
						break;
					}
				}catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
	}

	private void attackPlayer(Enemy temp, Direction direction) {
		switch (direction) {
		case UP: attackPlayerUp(temp, direction);
		break;
		case DOWN: attackPlayerDown(temp, direction);
		break;
		case LEFT: attackPlayerLeft(temp, direction);
		break;
		case RIGHT: attackPlayerRight(temp, direction);
		break;
		}
	}
	private void attackPlayerUp(Enemy temp, Direction direction) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		System.out.println("taking damage");
		player.takeDamage(temp.getDamage());
		if (player.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y - 1][x] = temp;
			coordinates.translate(0, -1);
		}
	}
	private void attackPlayerDown(Enemy temp, Direction direction) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		System.out.println("taking damage");
		player.takeDamage(temp.getDamage());
		if (player.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y + 1][x] = temp;
			coordinates.translate(0, 1);
		}
	}
	private void attackPlayerLeft(Enemy temp, Direction direction) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		System.out.println("taking damage");
		player.takeDamage(temp.getDamage());
		if (player.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y][x - 1] = temp;
			coordinates.translate(-1, 0);
		}
	}
	private void attackPlayerRight(Enemy temp, Direction direction) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		System.out.println("taking damage");
		player.takeDamage(temp.getDamage());
		if (player.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y][x + 1] = temp;
			coordinates.translate(1, 0);
		}
	}



	/**
	 * Uses a maze solver to calculate if there is an existing path
	 * @return Whether the player can reach an enemy with the current grid.
	 */
	public boolean areReachableEnemies() {

		if (enemies.size() == 0) {
			levelUpHandler.actionPreformed();
			return false;
		}
		if (AStar.test(obstacleGrid, (ArrayList<Enemy>)enemies, player)){
			return true;
		}
		levelUpHandler.actionPreformed();
		return false;
	}
}