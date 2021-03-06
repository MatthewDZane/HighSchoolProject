package gridDisplay;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import entities.Enemy;
import entities.HealthPack;
import entities.MovingEnemy;
import entities.Obstacle;
import entities.PickUp;
import entities.Projectile;
import game.Game1;
import game.Grid;
import helpers.AttackListener;

public class GridDisplayer extends JPanel{
	private Game1 game;

	private int frameHeight;
	private int frameWidth;

	private int height;
	private int width;

	private int distanceFromTopMargin;
	private int distanceFromBottomMargin;
	private int distanceFromSideMargins;

	public static final double TOP_MARGIN_PERCENT_OF_FRAME = 0.15;
	public static final double BOTTOM_MARGIN_PERCENT_OF_FRAME = 0.07;
	public static final double SIDE_MARGINS_PERCENT_OF_FRAME = 0.15;
	public static final int GRID_LINE_WIDTH = 2;
	
	public BufferedImage obstacleTexture = null;
	public BufferedImage enemyTexture = null;
	public BufferedImage movingEnemyTexture = null;
	public BufferedImage backGroundTexture = null;
	public BufferedImage playerTexture = null;
	public BufferedImage healthPackTexture = null;
	public BufferedImage arrowUpTexture = null;
	public BufferedImage arrowDownTexture = null;
	public BufferedImage arrowLeftTexture = null;
	public BufferedImage arrowRightTexture = null;

	public void setFrameHeight(int frameHeightIn) { frameHeight = frameHeightIn; }
	public void setFrameWidth(int frameWidthIn) { frameWidth = frameWidthIn; }

	public int getGridHeight() { return height;	}
	public void setGridHeight(int gridHeightIn) { height = gridHeightIn; }
	public int getGridWidth() { return width; }
	public void setGridWidth(int gridWidthIn) { width = gridWidthIn; }
	public int getDistanceFromTopMargin() { return distanceFromTopMargin; }
	public int getDistanceFromBottonMargin() { return distanceFromBottomMargin; }
	public int getDistanceFromSideMargins() { return distanceFromSideMargins; }

	public GridDisplayer(Game1 gameIn, int frameWidthIn, int frameHeightIn) {
		super();

		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setOpaque(true);
		game = gameIn;

		frameWidth = frameWidthIn;
		frameHeight = frameHeightIn;

		distanceFromTopMargin = (int) Math.round(TOP_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromBottomMargin = (int) Math.round(BOTTOM_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromSideMargins = (int) Math.round(SIDE_MARGINS_PERCENT_OF_FRAME * frameWidth);

		height = frameHeight - (distanceFromTopMargin + distanceFromBottomMargin) + 1;
		width = frameWidth - (2 * distanceFromSideMargins) + 1;

		setBounds(distanceFromSideMargins, distanceFromTopMargin, width + 2, height + 2);

		try {
			obstacleTexture = ImageIO.read(new File("./pictures/obstacle.png"));
			enemyTexture = ImageIO.read(new File("./pictures/enemy.png"));
			movingEnemyTexture = ImageIO.read(new File("./pictures/movingEnemy.png"));
			backGroundTexture = ImageIO.read(new File("./pictures/dirt.png"));
			playerTexture = ImageIO.read(new File("./pictures/player.png"));
			healthPackTexture = ImageIO.read(new File("./pictures/healthPack.png"));
			arrowUpTexture = ImageIO.read(new File("./pictures/arrowUp.png"));
			arrowDownTexture = ImageIO.read(new File("./pictures/arrowDown.png"));;
			arrowLeftTexture = ImageIO.read(new File("./pictures/arrowLeft.png"));;
			arrowRightTexture = ImageIO.read(new File("./pictures/arrowRight.png"));;
		} catch (IOException e) {

			System.out.println("Picture files not found");
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		resizeGrid();
		Grid grid = game.getGrid();
		for (int i = 0; i < grid.getObstacleGrid().length; i++) {
			for (int j = 0; j < grid.getObstacleGrid()[0].length; j++) {
				Rectangle gridRect = getRect(new Point(j, i));
				g.drawImage(backGroundTexture, gridRect.x, gridRect.y, gridRect.width + GRID_LINE_WIDTH, gridRect.height + GRID_LINE_WIDTH, null);
			}
		}
		//g.drawImage(backGroundTexture, 0, 0, width + GRID_LINE_WIDTH, height + GRID_LINE_WIDTH, null);
		g.drawRect(0, 0, width + 1 , height + 1);


		List<Obstacle> obstacles = grid.getObstacles();
		int numObstacles = obstacles.size();

		for (int i = 0; i < numObstacles; i++) {
			Rectangle obstacleRect = getRect(obstacles.get(i).getCoordinates());
			g.drawImage(obstacleTexture, obstacleRect.x, obstacleRect.y, obstacleRect.width +GRID_LINE_WIDTH, 
					obstacleRect.height + GRID_LINE_WIDTH, null);
		}



		List <PickUp> pickUps = grid.getPickUps();
		int numPickUps = pickUps.size();
		for (int i = 0; i < numPickUps; i++) {
			if (pickUps.get(i) instanceof HealthPack) {
				Rectangle healthPackRect = getRect(pickUps.get(i).getCoordinates());
				g.drawImage(healthPackTexture, healthPackRect.x, healthPackRect.y, healthPackRect.width, healthPackRect.height, null);
			}
		}

		List <Projectile> projectiles = grid.getProjectiles();
		int numProjectiles = projectiles.size();
		for (int i = 0; i < numProjectiles; i++) {
			Rectangle projectileRect = getRect(projectiles.get(i).getCoordinates());
			switch (projectiles.get(i).getDirection()) {
			case UP: 
				g.drawImage(arrowUpTexture, projectileRect.x, projectileRect.y, projectileRect.width, projectileRect.height, null) ;
				break;
			case DOWN: 
				g.drawImage(arrowDownTexture, projectileRect.x, projectileRect.y, projectileRect.width, projectileRect.height, null) ;
				break;
			case LEFT:
				g.drawImage(arrowLeftTexture, projectileRect.x, projectileRect.y, projectileRect.width, projectileRect.height, null) ;
				break;
			case RIGHT: 
				g.drawImage(arrowRightTexture, projectileRect.x, projectileRect.y, projectileRect.width, projectileRect.height, null) ;
				break;
			}
		}
		List <Enemy> enemies = grid.getEnemies();
		int numEnemies = enemies.size();
		for (int i = 0; i < numEnemies; i++) {
			if (enemies.get(i) instanceof MovingEnemy ) {
				Rectangle enemyRect = getRect(enemies.get(i).getCoordinates());
				g.drawImage(movingEnemyTexture, enemyRect.x, enemyRect.y, enemyRect.width, enemyRect.height, null);
			}
			else {
				Rectangle enemyRect = getRect(enemies.get(i).getCoordinates());
				g.drawImage(enemyTexture, enemyRect.x, enemyRect.y, enemyRect.width, enemyRect.height, null);
			}
		}
		List<MovingEnemy> movingEnemies = grid.getMovingEnemies();
		int numMovingEnemies = movingEnemies.size();
		for (int i = 0; i < numMovingEnemies; i++) {
			//paint moving enemy design
		}

		if (!game.getGrid().getPlayer().isDead()) {
			Rectangle playerRect = getRect(game.getGrid().getPlayer().getCoordinates());
			g.drawImage(playerTexture, playerRect.x, playerRect.y, playerRect.width, playerRect.height, null);
		}
	}

	public void resizeGrid() {

		distanceFromTopMargin = (int) Math.round(TOP_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromBottomMargin = (int) Math.round(BOTTOM_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromSideMargins = (int) Math.round(SIDE_MARGINS_PERCENT_OF_FRAME * frameWidth);

		height = frameHeight - (distanceFromTopMargin + distanceFromBottomMargin) + 1;
		width = frameWidth - (2 * distanceFromSideMargins) + 1;

		setBounds(distanceFromSideMargins, distanceFromTopMargin, width + 2, height + 2);
	}

	public Rectangle getRect(Point p) {
		int y = (int) p.getY();
		int x = (int) p.getX();
		int yCord1 = height * y / game.getGrid().getObstacleGrid().length;
		int xCord1 = width * x / game.getGrid().getObstacleGrid()[0].length;
		int yCord2 = height * (y + 1) / game.getGrid().getObstacleGrid().length;
		int xCord2 = width * (x + 1) / game.getGrid().getObstacleGrid()[0].length;
		return new Rectangle(xCord1, yCord1, xCord2 - xCord1, yCord2 - yCord1);
	}
	
}
