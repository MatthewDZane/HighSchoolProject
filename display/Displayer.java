package display;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
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
	private JButton pausePlayButton;
	private JButton retryButton;

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
		super();

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
		
		pausePlayButton = new JButton("Pause");
		pausePlayButton.addActionListener(new PausePlayButtonListener(this));
		
		pausePlayButton.setBounds((int)(0.28 * frameWidth), (int)(0.01 * frameHeight), (int)(0.10 * frameWidth), (int)(0.05 * frameHeight));
		add(pausePlayButton);
		
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
		requestFocus();
	}

	public void endGame() {
		remove(pausePlayButton);
		Game1.setIsPaused(true);
		levelDisplay.endGame();
		addRetryButton();
	}

	public void addRetryButton() {
		retryButton = new JButton("Retry");
		retryButton.setBounds((int)(0.45 * frameWidth), (int)(0.01 * frameHeight), (int)(0.10 * frameWidth), (int)(0.05 * frameHeight));
		retryButton.addActionListener(new RetryButtonListener(this));
		add(retryButton);
	}
	
	public class RetryButtonListener implements ActionListener {
		
		Displayer display;
		
		public RetryButtonListener(Displayer displayIn) {
			display = displayIn;
		}
		
		public void actionPerformed(ActionEvent arg0) {

			display.remove(retryButton);

			game = new Game1(enemyHitHandler, enemyKilledtHandler, 
					damageTakenHandler, healedHandler, attackUpHandler, levelUpHandler);

			remove(levelDisplay);
			remove(playerDisplayer);
			remove(gridDisplayer);
			remove(playerHealthGraphicDisplay);
			remove(instructionDisplayer);

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

			Game1.setRestarted(true);	
		}
	}
	
	public class PausePlayButtonListener implements ActionListener {
		
		Displayer display;
		
		public PausePlayButtonListener(Displayer displayIn) {
			display = displayIn;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			if (Game1.getIsPaused()) {
				Game1.setIsPaused(false);
				pausePlayButton.setText("Pause");
				requestFocus();
			}
			else {
				Game1.setIsPaused(true);
				pausePlayButton.setText("Play");
			}
		}

	}
	
	public void paint(Graphics g) { 
		super.paint(g);
		componentListener.componentResized(null);
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

		public void componentHidden(ComponentEvent arg0) {}

		public void componentMoved(ComponentEvent arg0) {}

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
				pausePlayButton.setBounds((int)(0.28 * frameWidth), (int)(0.01 * frameHeight), (int)(0.10 * frameWidth), (int)(0.05 * frameHeight));
			} catch (NullPointerException e) {}
		}

		public void componentShown(ComponentEvent arg0) {}
	}
	
	public class KeyEventHandler implements KeyListener {

		public void keyPressed(KeyEvent evt) {
			if(!Game1.getIsPaused()) {
				Grid grid = game.getGrid();
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

				if (!grid.movePlayer(moveDirection)) {
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

					grid.playerShootArrow(arrowDirection);
				}

				repaint();
			}
		}

		public void keyReleased(KeyEvent evt) {}

		public void keyTyped(KeyEvent evt) {}
	}
}
