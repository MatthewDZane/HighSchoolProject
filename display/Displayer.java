package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import game.Game1;
import game.Grid;
import gridDisplay.GridDisplayer;
import helpers.AttackListener;
import helpers.Direction;
import helpers.Game1ActionListener;
import levelDisplay.GameStatusLabel;
import levelDisplay.LevelDisplay;
import playerDisplay.PlayerDisplayer;
import playerDisplay.PlayerHealthGraphicDisplay;

public class Displayer extends JFrame {
	private Game1 game; 

	private GridDisplayer gridDisplayer;
	private PlayerDisplayer playerDisplayer;
	private LevelDisplay levelDisplay;
	private PlayerHealthGraphicDisplay playerHealthGraphicDisplay;

	private int frameHeight;
	private int frameWidth;

	private ComponentListener componentListener = new ComponentHandler();
	private KeyEventHandler keyEventHandler = new KeyEventHandler();
	private AttackListener attackListener = new AttackListener(this);
	private InstructionDisplayer instructionDisplayer;
	
	private Game1ActionListener enemyHitHandler;
	private Game1ActionListener enemyKilledtHandler;
	private Game1ActionListener damageTakenHandler;
	private Game1ActionListener healedHandler;
	private Game1ActionListener attackUpHandler;
	private Game1ActionListener levelUpHandler;
	
	
	public GridDisplayer getGridDisplayer() { return gridDisplayer; }
	public LevelDisplay getLevelDisplay() { return levelDisplay; }

	public Game1 getGame() { return game; }
	
	public int getFrameHeight() { return frameHeight; }
	public int getFrameWidth() { return frameWidth; }

	public Displayer(int height, int width) {
		super("Game 1");

		setLayout(null);
		
		frameHeight = height;
		frameWidth = width;


		enemyHitHandler = new Game1ActionHandler(GameStatusLabel.ENEMY_HIT);
		enemyKilledtHandler = new Game1ActionHandler(GameStatusLabel.ENEMY_KILLED);
		damageTakenHandler = new Game1ActionHandler(GameStatusLabel.DAMAGE_TAKEN);
		healedHandler = new Game1ActionHandler(GameStatusLabel.HEALED);
		attackUpHandler = new Game1ActionHandler(GameStatusLabel.ATTACK_UP);
		levelUpHandler = new Game1ActionHandler(GameStatusLabel.LEVEL_UP);
		
		game = new Game1(enemyHitHandler, enemyKilledtHandler, 
				damageTakenHandler, healedHandler, attackUpHandler, levelUpHandler);

		addComponentListener(componentListener);
		addKeyListener(keyEventHandler);

		gridDisplayer = new GridDisplayer(game, frameWidth, frameHeight);
		playerDisplayer = new PlayerDisplayer(game, frameWidth, frameHeight);
		levelDisplay = new LevelDisplay(game, frameWidth, frameHeight);
		playerHealthGraphicDisplay = new PlayerHealthGraphicDisplay(game, frameWidth, frameHeight);
		instructionDisplayer = new InstructionDisplayer(frameWidth, frameHeight);
		
		add(levelDisplay);
		add(playerDisplayer);
		add(gridDisplayer);
		add(playerHealthGraphicDisplay);
		add(instructionDisplayer);
		
		game.setAttackListener(attackListener);
		
		setSize(frameWidth, frameHeight);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void endGame() {
		disableKeys();
		levelDisplay.endGame();
		
	}

	public void paint(Graphics g) { 
		super.paint(g);

		levelDisplay.repaint();
		playerHealthGraphicDisplay.repaint();
	}

	public class Game1ActionHandler implements Game1ActionListener {

		private String message = "";

		public Game1ActionHandler(String messageIn) {
			message = messageIn;
		}
		
		@Override
		public void actionPreformed() {
			levelDisplay.displayMessage(message);
		}

	}
	public class ComponentHandler implements ComponentListener {


		public void componentHidden(ComponentEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentResized(ComponentEvent evt) {

			frameHeight = getHeight();
			frameWidth = getWidth();

			try {
				gridDisplayer.setFrameHeight(frameHeight);
				gridDisplayer.setFrameWidth(frameWidth);
				gridDisplayer.resizeGrid();
				playerDisplayer.setFrameHeight(frameHeight);
				playerDisplayer.setFrameWidth(frameWidth);
				playerDisplayer.resize();
				levelDisplay.setFrameHeight(frameHeight);
				levelDisplay.setFrameWidth(frameWidth);
				levelDisplay.resize();
				playerHealthGraphicDisplay.setFrameHeight(frameHeight);
				playerHealthGraphicDisplay.setFrameWidth(frameWidth);
				playerHealthGraphicDisplay.resize();
				instructionDisplayer.setFrameHeight(frameHeight);
				instructionDisplayer.setFrameWidth(frameWidth);
				instructionDisplayer.resize();
			} catch (NullPointerException e) {}
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			// TODO Auto-generated method stub

		}
	}
	public void disableKeys() {
		removeKeyListener(keyEventHandler);
	}
	public void enableKeys() {
		addKeyListener(keyEventHandler);
	}
	public class KeyEventHandler implements KeyListener {

		@Override
		public void keyPressed(KeyEvent evt) {

		}

		@Override
		public void keyReleased(KeyEvent evt) {
			Grid grid = game.getGrid();
			if (!game.getHasMoved()) {
				disableKeys();
				int keyCode = evt.getKeyCode();
				Direction moveDirection = Direction.INVALID;
				switch( keyCode ) { 
				case KeyEvent.VK_UP:
					moveDirection = Direction.UP;
					break;
				case KeyEvent.VK_DOWN:
					moveDirection = Direction.DOWN;
					break;
				case KeyEvent.VK_LEFT:
					moveDirection = Direction.LEFT;
					break;
				case KeyEvent.VK_RIGHT :
					moveDirection = Direction.RIGHT;
					break;

				}
				if (grid.movePlayer(moveDirection)) {
					game.setHasMoved(true);
				}
				else {
					Direction arrowDirection = Direction.INVALID;
					switch ( keyCode) {
					case KeyEvent.VK_W : 
						arrowDirection = Direction.UP;
						break;
					case KeyEvent.VK_S :
						arrowDirection = Direction.DOWN;
						break;
					case KeyEvent.VK_A :
						arrowDirection = Direction.LEFT;
						break;
					case KeyEvent.VK_D :
						arrowDirection = Direction.RIGHT;
						break;
					}
					if (grid.playerShootArrow(arrowDirection)) {
						game.setHasMoved(true);
					}
					else {
						enableKeys();
					}
				}

				repaint();
			}
		}

		@Override
		public void keyTyped(KeyEvent evt) {
			// TODO Auto-generated method stub

		}

	}


}
