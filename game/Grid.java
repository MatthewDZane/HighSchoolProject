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
import entities.ShootingEnemy;
import game.Game1.LevelCheckTimerListener;
import helpers.AStar;
import helpers.ArrayIndexOccupiedException;
import helpers.AttackListener;
import helpers.Direction;
import helpers.Game1ActionListener;

public class Grid {
	private final int FIRST_LEVEL_GRID_SIZE = 5;

	private Obstacle[][] obstacleGrid;
	private PickUp[][] pickUpGrid;
	private Projectile[][] playerProjectileGrid;
	private Projectile[][] enemyProjectileGrid;

	private Player player;
	private List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private List<Enemy> enemies = new ArrayList<Enemy>();
	private List<MovingEnemy> movingEnemies = new ArrayList<MovingEnemy>();
	private List<ShootingEnemy> shootingEnemies = new ArrayList<ShootingEnemy>();
	private List<PickUp> pickUps = new ArrayList<PickUp>();
	private List<Projectile> playerProjectiles = new ArrayList<Projectile>();
	private List<Projectile> enemyProjectiles = new ArrayList<Projectile>();

	private Game1ActionListener enemyHitHandler;
	private Game1ActionListener enemyKilledHandler;
	private Game1ActionListener damageTakenHandler;
	private Game1ActionListener healedHandler;
	private Game1ActionListener attackUpHandler;
	private Game1ActionListener levelUpHandler;
	private AttackListener attackListener;
	private LevelCheckTimerListener levelCheckTimerListener;

	public Game1ActionListener getEnemyHitHandler() { return enemyHitHandler; }
	public Game1ActionListener getEnemyKilledtHandler() { return enemyKilledHandler; }
	public Game1ActionListener getDamageTakenHandler() { return damageTakenHandler; }
	public Game1ActionListener getHealedHandler() { return healedHandler; }
	public Game1ActionListener getAttackUpHandler() { return attackUpHandler; }
	public Game1ActionListener getLevelUpHandler() { return levelUpHandler; }
	public LevelCheckTimerListener getLevelCheckTimerListener() { return levelCheckTimerListener; }
	public void setLevelCheckTimerListener(LevelCheckTimerListener levelCheckTimerListenerIn) {  levelCheckTimerListener = levelCheckTimerListenerIn; }

	public Obstacle[][] getObstacleGrid() { return obstacleGrid; }
	public PickUp[][] getPickUpGrid() { return pickUpGrid;}
	public Projectile[][] getPlayerProjectileGrid() { return playerProjectileGrid; }
	public Projectile[][] getEnemyProjectileGrid() { return enemyProjectileGrid; }

	public Player getPlayer() { return player; }
	public List<Obstacle> getObstacles() { return obstacles; }
	public List<Enemy> getEnemies() { return enemies; }
	public List<MovingEnemy> getMovingEnemies() { return movingEnemies; }
	public List<ShootingEnemy> getShootingEnemies() { return shootingEnemies; }
	public List<PickUp> getPickUps() { return pickUps; }
	public List<Projectile> getPlayerProjectiles() { return playerProjectiles; }
	public List<Projectile> getEnemyProjectiles() { return enemyProjectiles; }

	public AttackListener getAttackListener() { return attackListener; }
	public void setAttackListener(AttackListener attackUpListenerIn) { attackListener = attackUpListenerIn; }

	public Grid(Game1ActionListener enemyHitHandlerIn,
			Game1ActionListener enemyKilledtHandlerIn,Game1ActionListener damageTakenHandlerIn,
			Game1ActionListener healedHandlerIn, Game1ActionListener attackUpHandlerIn,
			Game1ActionListener levelUpHandlerIn) {

		enemyHitHandler = enemyHitHandlerIn;
		enemyKilledHandler = enemyKilledtHandlerIn;
		damageTakenHandler = damageTakenHandlerIn;
		healedHandler = healedHandlerIn;
		attackUpHandler = attackUpHandlerIn;
		levelUpHandler = levelUpHandlerIn;

		obstacleGrid = new Obstacle[FIRST_LEVEL_GRID_SIZE][FIRST_LEVEL_GRID_SIZE];
		player = new Player(1, 2);
		obstacleGrid[2][1] = player;

		pickUpGrid = new PickUp[FIRST_LEVEL_GRID_SIZE][FIRST_LEVEL_GRID_SIZE];
		playerProjectileGrid = new Arrow[FIRST_LEVEL_GRID_SIZE][FIRST_LEVEL_GRID_SIZE];
		enemyProjectileGrid = new Arrow[FIRST_LEVEL_GRID_SIZE][FIRST_LEVEL_GRID_SIZE];

		obstacles = new ArrayList<Obstacle>();
		pickUps = new ArrayList<PickUp>();
		playerProjectiles = new ArrayList<Projectile>();
		enemyProjectiles = new ArrayList<Projectile>();
	}

	public Grid(int width, int height) {
		obstacleGrid = new Obstacle[height][width];
		pickUpGrid = new PickUp[height][width];
		playerProjectileGrid = new Arrow[height][width];
		enemyProjectileGrid = new Arrow[height][width];
	}

	/**
	 * Copies the contents from gridIn to this grid starting from point (0, 0)
	 * to the end of gridIn
	 * @param gridIn - Pre-Condition: size must be less than or equal to this
	 */
	public void copyGrid(Grid gridIn) {
		Obstacle[][] smallerObstacleGrid = gridIn.getObstacleGrid();
		PickUp[][] smallerPickUpGrid = gridIn.getPickUpGrid();

		player = gridIn.getPlayer();

		for (int y = 0; y < smallerObstacleGrid.length; y++) {
			for (int x = 0; x < smallerObstacleGrid[0].length; x++) {
				if (smallerObstacleGrid[y][x] != null)
					obstacleGrid[y][x] = smallerObstacleGrid[y][x];
				if (smallerPickUpGrid[y][x] != null) {
					pickUpGrid[y][x] = smallerPickUpGrid[y][x];
				}
			}
		}

		obstacles = gridIn.getObstacles();
		pickUps = gridIn.getPickUps();
		playerProjectiles = gridIn.getPlayerProjectiles();
		enemyProjectiles = gridIn.getEnemyProjectiles();

		enemyHitHandler = gridIn.getEnemyHitHandler();
		enemyKilledHandler =gridIn.getEnemyKilledtHandler();
		damageTakenHandler = gridIn.getDamageTakenHandler();
		healedHandler = gridIn.getHealedHandler();
		attackUpHandler = gridIn.getAttackUpHandler();
		levelUpHandler = gridIn.getLevelUpHandler();
		attackListener = gridIn.getAttackListener();
		levelCheckTimerListener = gridIn.getLevelCheckTimerListener();
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
		boolean success = false;
		Point coordinates = player.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y - 1][x] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y - 1][x] = player;
				success = true;
			} 
			if (pickUpGrid[y - 1][x] != null  && success) {
				PickUp pickUp = pickUpGrid[y - 1][x];
				pickUp.usePower(player);
				pickUps.remove(pickUp);
				pickUpGrid[y - 1][x] = null;
				success = true;
				if (pickUp instanceof HealthPack) {
					healedHandler.actionPreformed();
				}
			}
			if (enemyProjectileGrid[y - 1][x] != null && success) {
				Projectile enemyArrow = enemyProjectileGrid[y - 1][x];
				player.takeDamage(enemyArrow.getDamage());
				enemyProjectiles.remove(enemyArrow);
				enemyProjectileGrid[y - 1][x] = null;
				attackListener.actionPreformed(enemyArrow.getDirection(), new Point(x, y - 1));
				damageTakenHandler.actionPreformed();
				enemyArrow.useAbility(player);
				if (player.isDead()) {
					obstacleGrid[y - 1][x] = null;
				}
				levelCheckTimerListener.actionPerformed();
			}
			if (success) {
				coordinates.translate(0, -1);
			}
			return success;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	private boolean movePlayerDown() {
		boolean success = false;
		Point coordinates = player.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y + 1][x] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y + 1][x] = player;
				success = true;
			}
			if (pickUpGrid[y + 1][x] != null && success) {
				PickUp pickUp = pickUpGrid[y + 1][x];
				pickUp.usePower(player);
				pickUps.remove(pickUp);
				pickUpGrid[y + 1][x] = null;
				success = true;
				if (pickUp instanceof HealthPack) {
					healedHandler.actionPreformed();
				}
			}
			if (enemyProjectileGrid[y + 1][x] != null && success) {
				Projectile enemyArrow = enemyProjectileGrid[y + 1][x];
				player.takeDamage(enemyArrow.getDamage());
				enemyProjectiles.remove(enemyArrow);
				enemyProjectileGrid[y + 1][x] = null;
				attackListener.actionPreformed(enemyArrow.getDirection(), new Point(x, y + 1));
				damageTakenHandler.actionPreformed();
				enemyArrow.useAbility(player);
				if (player.isDead()) {
					obstacleGrid[y + 1][x] = null;
				}
				levelCheckTimerListener.actionPerformed();
			}
			if (success) {
				coordinates.translate(0, 1);
			}
			return success;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	private boolean movePlayerLeft() {
		boolean success = false;
		Point coordinates = player.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y][x - 1] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y][x - 1] = player;
				success = true;
			}
			if (pickUpGrid[y][x - 1] != null && success) {
				PickUp pickUp = pickUpGrid[y][x - 1];
				pickUp.usePower(player);
				pickUps.remove(pickUp);	
				pickUpGrid[y][x - 1] = null;
				if (pickUp instanceof HealthPack) {
					healedHandler.actionPreformed();
				}
			}
			else if (enemyProjectileGrid[y][x - 1] != null && success) {
				Projectile enemyArrow = enemyProjectileGrid[y][x - 1];
				player.takeDamage(enemyArrow.getDamage());
				enemyProjectiles.remove(enemyArrow);
				enemyProjectileGrid[y][x - 1] = null;
				attackListener.actionPreformed(enemyArrow.getDirection(), new Point(x - 1, y));
				damageTakenHandler.actionPreformed();
				enemyArrow.useAbility(player);
				if (player.isDead()) {
					obstacleGrid[y][x - 1] = null;
				}
				//success = true;
				levelCheckTimerListener.actionPerformed();
			}
			if (success) {
				coordinates.translate(-1, 0);
			}
			return success;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	private boolean movePlayerRight() {
		boolean success = false;
		Point coordinates = player.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		try {
			if (obstacleGrid[y][x + 1] == null) {
				obstacleGrid[y][x] = null;
				obstacleGrid[y][x + 1] = player;
				success = true;
			}
			if (pickUpGrid[y][x + 1] != null && success) {
				PickUp pickUp = (PickUp) pickUpGrid[y][x + 1];
				pickUp.usePower(player);
				pickUps.remove(pickUp);
				pickUpGrid[y][x + 1] = null;
				if (pickUp instanceof HealthPack) {
					healedHandler.actionPreformed();
				}
			}
			else if (enemyProjectileGrid[y][x + 1] != null && success) {
				Projectile enemyArrow = enemyProjectileGrid[y][x + 1];
				player.takeDamage(enemyArrow.getDamage());
				enemyProjectiles.remove(enemyArrow);
				enemyProjectileGrid[y][x + 1] = null;
				attackListener.actionPreformed(enemyArrow.getDirection(), new Point(x + 1, y));
				damageTakenHandler.actionPreformed();
				enemyArrow.useAbility(player);
				if (player.isDead()) {
					obstacleGrid[y][x + 1] = null;
				}
				//success = true;
				levelCheckTimerListener.actionPerformed();
			}
			if (success) {
				coordinates.translate(1, 0);
			}
			return success;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
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
			if (obstacleGrid[y - 1][x] == null && playerProjectileGrid[y - 1][x] == null) {
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
			if (obstacleGrid[y + 1][x] == null && playerProjectileGrid[y + 1][x] == null) {
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
			if (obstacleGrid[y][x - 1] == null && playerProjectileGrid[y][x - 1] == null) {
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
			if (obstacleGrid[y][x + 1] == null && playerProjectileGrid[y][x + 1] == null) {
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

	//Removal methods
	private boolean killEnemy(Enemy enemy) {
		if (enemy instanceof MovingEnemy) {
			movingEnemies.remove(enemy);
		}
		if (enemy instanceof ShootingEnemy) {
			shootingEnemies.remove(enemy);
		}
		boolean enemyRemoved = enemies.remove(enemy);
		levelCheckTimerListener.actionPerformed();
		return enemyRemoved;
	}

	//methods used to place obstacle
	public void placeObstacle(int x, int y) throws ArrayIndexOccupiedException {
		if (obstacleGrid[y][x] == null) {
			Obstacle obstacle = new Obstacle(x, y);
			obstacles.add(obstacle);
			obstacleGrid[y][x] = obstacle;
		}
		else {
			throw new ArrayIndexOccupiedException();
		}
	}

	public void placeEnemy(int x, int y, int hp, int damage) throws ArrayIndexOccupiedException {
		if (obstacleGrid[y][x] == null) {
			Enemy enemy = new Enemy(x, y, hp, damage);
			enemies.add(enemy);
			obstacleGrid[y][x] = enemy;
		}
		else {
			throw new ArrayIndexOccupiedException();
		}
	}

	public void placeHealthPack(int x, int y, int hp)  throws ArrayIndexOccupiedException{
		if (pickUpGrid[y][x] == null && obstacleGrid[y][x] == null) {
			PickUp pickUp = new HealthPack(x, y, hp);
			pickUps.add(pickUp);
			pickUpGrid[y][x] = pickUp;
		}
		else {
			throw new ArrayIndexOccupiedException();
		}
	}

	public void placeMovingEnemy(int x, int y, int hp, int damage) throws ArrayIndexOccupiedException {
		if (obstacleGrid[y][x] == null) {
			MovingEnemy enemy = new MovingEnemy(x, y, hp, damage);
			enemies.add(enemy);
			movingEnemies.add(enemy);
			obstacleGrid[y][x] = enemy;
		}
		else {
			throw new ArrayIndexOccupiedException();
		}
	}

	public void placeShootingEnemy(int x, int y, int hp, int damage) throws ArrayIndexOccupiedException {
		if (obstacleGrid[y][x] == null) {
			ShootingEnemy enemy = new ShootingEnemy(x, y, hp, damage);
			enemies.add(enemy);
			shootingEnemies.add(enemy);
			obstacleGrid[y][x] = enemy;
		}
		else {
			throw new ArrayIndexOccupiedException();
		}
	}

	/**
	 * Precondition: arrow is placed in spot of shooter
	 * @param x
	 * @param y
	 * @param speedIn
	 * @param damageIn
	 * @param knockbackIn
	 * @param directionIn
	 * @return whether an arrow was fired
	 */
	public boolean shootPlayerArrow(int x, int y, int speedIn, int damageIn, int knockbackIn, Direction directionIn) {
		try {
			Arrow arrow = new Arrow(x, y, speedIn, damageIn, knockbackIn, directionIn);
			playerProjectiles.add(arrow);
			playerProjectileGrid[y][x] = arrow;
			return true;
		} catch (ArrayIndexOutOfBoundsException e) { return false; }
	}

	public boolean shootEnemyArrow(int x, int y, int speedIn, int damageIn, int knockbackIn, Direction directionIn) {
		try {
			Arrow arrow = new Arrow(x, y, speedIn, damageIn, knockbackIn, directionIn);
			enemyProjectiles.add(arrow);
			enemyProjectileGrid[y][x] = arrow;
			return true;
		} catch (ArrayIndexOutOfBoundsException e) { return false; }
	}

	public boolean playerShootArrow(Direction direction) {
		if (direction != Direction.INVALID) {
			Point coordinates = player.getCoordinates();
			int x = coordinates.x;
			int y = coordinates.y;
			shootPlayerArrow(x, y, 1, player.getDamage(), 0, direction);
			return true;
		}
		return false;
	}

	public boolean enemyShootArrow(Direction direction, Enemy enemy) {
		if (direction != Direction.INVALID) {
			Point coordinates = enemy.getCoordinates();
			int x = coordinates.x;
			int y = coordinates.y;
			return shootEnemyArrow(x, y, 1, enemy.getDamage(), 0, direction);
		}
		return false;
	}

	public void movePlayerProjectiles() {
		for (int i = 0; i < playerProjectiles.size(); i++) {
			Projectile projectile = playerProjectiles.get(i);
			switch (projectile.getDirection()) {
			case UP: 
				if (movePlayerProjectileUp(projectile)) {
					i--;
				}
				break;
			case DOWN: 
				if (movePlayerProjectileDown(projectile)) {
					i--;
				}
				break;
			case LEFT:  
				if (movePlayerProjectileLeft(projectile)) {
					i--;
				}
				break;
			case RIGHT:
				if (movePlayerProjectileRight(projectile)) {
					i--;
				}
				break;
			default: return;
			}
		}
	}

	public boolean movePlayerProjectileUp(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (playerProjectileGrid[y - 1][x] != null) {
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
				}
				else if (obstacleGrid[y - 1][x] == null || obstacleGrid[y - 1][x] instanceof Player) {
					coordinates.translate(0, -1);
					playerProjectileGrid[y][x] = null;
					playerProjectileGrid[y - 1][x] = projectile;
				}
				else if (obstacleGrid[y - 1][x] instanceof Enemy) {
					attackListener.actionPreformed(Direction.UP, new Point(x, y - 1));
					Enemy enemy = (Enemy) obstacleGrid[y - 1][x];
					projectile.useAbility(enemy);
					enemy.takeDamage(projectile.getDamage());
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
					if (enemy.isDead()) {
						obstacleGrid[y - 1][x] = null;
						killEnemy(enemy);
					}
					return true;
				}
				else if (obstacleGrid[y - 1][x] instanceof Obstacle ) {
					Obstacle obstacle = obstacleGrid[y - 1][x];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y - 1][x] = null;
					}
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			playerProjectileGrid[y][x] = null;
			playerProjectiles.remove(projectile);
			return true;
		}
		return false;
	}
	public boolean movePlayerProjectileDown(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (playerProjectileGrid[y + 1][x] != null) {
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
				}
				else if (obstacleGrid[y + 1][x] == null || obstacleGrid[y + 1][x] instanceof Player) {
					coordinates.translate(0, 1);
					playerProjectileGrid[y][x] = null;
					playerProjectileGrid[y + 1][x] = projectile;
				}
				else if (obstacleGrid[y + 1][x] instanceof Enemy) {
					attackListener.actionPreformed(Direction.DOWN, new Point(x, y + 1));
					Enemy enemy = (Enemy) obstacleGrid[y + 1][x];
					projectile.useAbility(enemy);
					enemy.takeDamage(projectile.getDamage());
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
					if (enemy.isDead()) {
						obstacleGrid[y + 1][x] = null;
						killEnemy(enemy);
					}
					return true;
				}
				else if (obstacleGrid[y + 1][x] instanceof Obstacle ) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y + 1][x];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y + 1][x] = null;
					}
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			playerProjectileGrid[y][x] = null;
			playerProjectiles.remove(projectile);
			return true;
		}
		return false;
	}
	public boolean movePlayerProjectileLeft(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (playerProjectileGrid[y][x - 1] != null) {
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
				}
				else if (obstacleGrid[y][x - 1] == null || obstacleGrid[y][x - 1] instanceof Player) {
					coordinates.translate(-1, 0);
					playerProjectileGrid[y][x] = null;
					playerProjectileGrid[y][x - 1] = projectile;
				}
				else if (obstacleGrid[y][x - 1] instanceof Enemy) {
					attackListener.actionPreformed(Direction.LEFT, new Point(x - 1, y));
					Enemy enemy = (Enemy) obstacleGrid[y][x - 1];
					projectile.useAbility(enemy);
					enemy.takeDamage(projectile.getDamage());
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
					if (enemy.isDead()) {
						obstacleGrid[y][x - 1] = null;
						killEnemy(enemy);
					}
					return true;
				}
				else if (obstacleGrid[y][x - 1] instanceof Obstacle ) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y][x - 1];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y][x - 1] = null;
					}
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			playerProjectileGrid[y][x] = null;
			playerProjectiles.remove(projectile);
			return true;
		}
		return false;
	}
	public boolean movePlayerProjectileRight(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (playerProjectileGrid[y][x + 1] != null) {
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
				}
				else if (obstacleGrid[y][x + 1] == null || obstacleGrid[y][x + 1] instanceof Player) {
					coordinates.translate(1, 0);
					playerProjectileGrid[y][x] = null;
					playerProjectileGrid[y][x + 1] = projectile;
				}
				else if (obstacleGrid[y][x + 1] instanceof Enemy) {
					attackListener.actionPreformed(Direction.RIGHT, new Point(x + 1, y));
					Enemy enemy = (Enemy) obstacleGrid[y][x + 1];
					projectile.useAbility(enemy);
					enemy.takeDamage(projectile.getDamage());
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
					if (enemy.isDead()) {
						obstacleGrid[y][x + 1] = null;
						killEnemy(enemy);
					}
					return true;
				}
				else if (obstacleGrid[y][x + 1] instanceof Obstacle ) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y][x + 1];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y][x + 1] = null;
					}
					playerProjectiles.remove(projectile);
					playerProjectileGrid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			playerProjectileGrid[y][x] = null;
			playerProjectiles.remove(projectile);
			return true;
		}
		return false;
	}

	public void moveEnemyProjectiles() {
		for (int i = 0; i < enemyProjectiles.size(); i++) {
			Projectile projectile = enemyProjectiles.get(i);
			switch (projectile.getDirection()) {
			case UP: 
				if (moveEnemyProjectileUp(projectile)) {
					i--;
				}
				break;
			case DOWN: 
				if (moveEnemyProjectileDown(projectile)) {
					i--;
				}
				break;
			case LEFT:  
				if (moveEnemyProjectileLeft(projectile)) {
					i--;
				}
				break;
			case RIGHT:
				if (moveEnemyProjectileRight(projectile)) {
					i--;
				}
				break;
			default: return;
			}
		}
	}
	public boolean moveEnemyProjectileUp(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (enemyProjectileGrid[y - 1][x] != null) {
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
				}
				else if (obstacleGrid[y - 1][x] == null || obstacleGrid[y - 1][x] instanceof Enemy) {
					coordinates.translate(0, -1);
					enemyProjectileGrid[y][x] = null;
					enemyProjectileGrid[y - 1][x] = projectile;
				}
				else if (obstacleGrid[y - 1][x] instanceof Player) {
					attackListener.actionPreformed(Direction.UP, new Point(x, y - 1));
					damageTakenHandler.actionPreformed();
					projectile.useAbility(player);
					player.takeDamage(projectile.getDamage());
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
					if (player.isDead()) {
						obstacleGrid[y - 1][x] = null;
						levelCheckTimerListener.actionPerformed();
					}
					return true;
				}
				else if (obstacleGrid[y - 1][x] instanceof Obstacle ) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y - 1][x];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y - 1][x] = null;
					}
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			enemyProjectileGrid[y][x] = null;
			enemyProjectiles.remove(projectile);
			return true;
		}
		return false;
	}

	public boolean moveEnemyProjectileDown(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (enemyProjectileGrid[y + 1][x] != null) {
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
				}
				else if (obstacleGrid[y + 1][x] == null || obstacleGrid[y + 1][x] instanceof Enemy) {
					coordinates.translate(0, 1);
					enemyProjectileGrid[y][x] = null;
					enemyProjectileGrid[y + 1][x] = projectile;
				}
				else if (obstacleGrid[y + 1][x] instanceof Player) {
					attackListener.actionPreformed(Direction.DOWN, new Point(x, y + 1));
					damageTakenHandler.actionPreformed();
					projectile.useAbility(player);
					player.takeDamage(projectile.getDamage());
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
					if (player.isDead()) {
						obstacleGrid[y + 1][x] = null;
						levelCheckTimerListener.actionPerformed();
					}
					return true;
				}
				else if (obstacleGrid[y + 1][x] instanceof Obstacle ) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y + 1][x];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y + 1][x] = null;
					}
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			enemyProjectileGrid[y][x] = null;
			enemyProjectiles.remove(projectile);
			return true;
		}
		return false;
	}

	public boolean moveEnemyProjectileLeft(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (enemyProjectileGrid[y][x - 1] != null) {
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
				}
				else if (obstacleGrid[y][x - 1] == null || obstacleGrid[y][x - 1] instanceof Enemy) {
					coordinates.translate(-1, 0);
					enemyProjectileGrid[y][x] = null;
					enemyProjectileGrid[y][x - 1] = projectile;
				}
				else if (obstacleGrid[y][x - 1] instanceof Player) {
					attackListener.actionPreformed(Direction.LEFT, new Point(x - 1, y));
					damageTakenHandler.actionPreformed();
					projectile.useAbility(player);
					player.takeDamage(projectile.getDamage());
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
					if (player.isDead()) {
						obstacleGrid[y][x - 1] = null;
						levelCheckTimerListener.actionPerformed();
					}
					return true;
				}
				else if (obstacleGrid[y][x - 1] instanceof Obstacle ) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y][x - 1];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y][x - 1] = null;
					}
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			enemyProjectileGrid[y][x] = null;
			enemyProjectiles.remove(projectile);
			return true;
		}
		return false;
	}

	public boolean moveEnemyProjectileRight(Projectile projectile) {
		Point coordinates = projectile.getCoordinates();
		int x = coordinates.x;
		int y = coordinates.y;
		try {
			for (int i = 0; i < projectile.getSpeed(); i++) {
				if (enemyProjectileGrid[y][x + 1] != null) {
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
				}
				else if (obstacleGrid[y][x + 1] == null || obstacleGrid[y][x + 1] instanceof Enemy) {
					coordinates.translate(1, 0);
					enemyProjectileGrid[y][x] = null;
					enemyProjectileGrid[y][x + 1] = projectile;
				}
				else if (obstacleGrid[y][x + 1] instanceof Player) {
					attackListener.actionPreformed(Direction.RIGHT, new Point(x + 1, y));
					damageTakenHandler.actionPreformed();
					projectile.useAbility(player);
					player.takeDamage(projectile.getDamage());
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
					if (player.isDead()) {
						obstacleGrid[y][x + 1] = null;
						levelCheckTimerListener.actionPerformed();
					}
					return true;
				}
				else if (obstacleGrid[y][x + 1] instanceof Obstacle ) {
					Obstacle obstacle = (Obstacle) obstacleGrid[y][x + 1];
					if (projectile.useAbility(obstacle)) {
						destroyObstacle(obstacle);
						obstacleGrid[y][x + 1] = null;
					}
					enemyProjectiles.remove(projectile);
					enemyProjectileGrid[y][x] = null;
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			enemyProjectileGrid[y][x] = null;
			enemyProjectiles.remove(projectile);
			return true;
		}
		return false;
	}

	public boolean isFull() {

		for (int y = 0; y < obstacleGrid.length; y++) {
			for (int x = 0; x < obstacleGrid[0].length; x++) {
				if (obstacleGrid[y][x] == null) {
					return false;
				}
			}
		}
		return true;
	}

	public static List makeList(Object [] array) {
		ArrayList <Object> list = new ArrayList<Object>();
		for (Object temp : array) {
			list.add(temp);
		}
		return list;
	}

	public void haveEnemiesAttack() {
		for (Enemy temp : enemies) {
			if (! (temp instanceof MovingEnemy)) {
				Point coordinates = temp.getCoordinates();
				int y = (int) coordinates.getY();
				int x = (int) coordinates.getX();

				if (temp instanceof ShootingEnemy) {
					Direction direction = Direction.getRandomDirection();
					switch (direction) {
					case UP :
						enemyShootArrow(direction, temp);
						break;
					case DOWN :
						enemyShootArrow(direction, temp);
						break;
					case LEFT :
						enemyShootArrow(direction, temp);
						break;
					case RIGHT :
						enemyShootArrow(direction, temp);
						break;

					}
				}
				else {

					Direction direction = Direction.getRandomDirection();

					switch (direction) {
					case UP :
						try {
							if (obstacleGrid[y - 1][x] instanceof Player) {
								moveEnemyUp(temp);
							}
						}catch (ArrayIndexOutOfBoundsException e) {}
						break;
					case DOWN :
						try {
							if (obstacleGrid[y + 1][x] instanceof Player) {
								moveEnemyDown(temp);
							}
						}catch (ArrayIndexOutOfBoundsException e) {}
						break;
					case LEFT :
						try {
							if (obstacleGrid[y][x - 1] instanceof Player) {
								moveEnemyLeft(temp);
							}
						}catch (ArrayIndexOutOfBoundsException e) {}
						break;
					case RIGHT :
						try {
							if (obstacleGrid[y][x + 1] instanceof Player) {
								moveEnemyRight(temp);
							}
						}catch (ArrayIndexOutOfBoundsException e) {}
						break;
					}
				}
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
		player.takeDamage(temp.getDamage());
		if (player.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y - 1][x] = temp;
			coordinates.translate(0, -1);
			levelCheckTimerListener.actionPerformed();
		}
	}
	
	private void attackPlayerDown(Enemy temp, Direction direction) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		player.takeDamage(temp.getDamage());
		if (player.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y + 1][x] = temp;
			coordinates.translate(0, 1);
			levelCheckTimerListener.actionPerformed();
		}
	}
	
	private void attackPlayerLeft(Enemy temp, Direction direction) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		player.takeDamage(temp.getDamage());
		if (player.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y][x - 1] = temp;
			coordinates.translate(-1, 0);
			levelCheckTimerListener.actionPerformed();
		}
	}
	
	private void attackPlayerRight(Enemy temp, Direction direction) {
		Point coordinates = temp.getCoordinates();
		int y = (int) coordinates.getY();
		int x = (int) coordinates.getX();
		player.takeDamage(temp.getDamage());
		if (player.isDead()) {
			obstacleGrid[y][x] = null;
			obstacleGrid[y][x + 1] = temp;
			coordinates.translate(1, 0);
			levelCheckTimerListener.actionPerformed();
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
		if (AStar.test(obstacleGrid, hardClone((ArrayList<Enemy>)enemies), player.clone())){
			return true;
		}
		levelUpHandler.actionPreformed();
		return false;
	}

	public ArrayList<Enemy> hardClone(ArrayList<Enemy> arr) {
		ArrayList<Enemy> clone = new ArrayList<Enemy>();
		
		for (Enemy temp : arr) {
			clone.add(temp.clone());
		}
		
		return clone;
	}

}