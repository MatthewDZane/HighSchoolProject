package levelDisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import game.Game1;

public class LevelDisplay  extends JPanel{
	private Game1 game;

	private int frameHeight;
	private int frameWidth;

	private GameStatusLabel gameStatusLabel;
	private LevelLabel levelLabel;

	private int height;
	private int width;
	private int distanceFromTopMargin;
	private int distanceFromBottomMargin;
	private int distanceFromLeftMargin;
	private int distanceFromRightMargin;

	public static final double TOP_MARGIN_PERCENT_OF_FRAME = 0.07;
	public static final double BOTTOM_MARGIN_PERCENT_OF_FRAME = 0.85;
	public static final double LEFT_MARGIN_PERCENT_OF_FRAME = 0.40;
	public static final double RIGHT_MARGIN_PERCENT_OF_FRAME = 0.40;
	
	private Timer t = null;
	
	public Game1 getGame() { return game; }
	
	public LevelLabel getLevelLabel() { return levelLabel; }
	public GameStatusLabel getGameStatusLabel() { return gameStatusLabel; }

	public void setFrameHeight(int frameHeightIn) { frameHeight = frameHeightIn; }
	public void setFrameWidth(int frameWidthIn) { frameWidth = frameWidthIn; }

	public void setPlayerDisplayHeight(int heightIn) { height = heightIn; }
	public void setPlayerDisplayWidth(int widthIn) { width = widthIn; }

	public int getDistanceFromTopMargin() { return distanceFromTopMargin; }
	public int getDistanceFromBottonMargin() { return distanceFromBottomMargin; }
	public int getDistanceFromLeftMargins() { return distanceFromLeftMargin; }


	public LevelDisplay(Game1 gameIn, int frameWidthIn, int frameHeightIn) {
		super();

		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		this.setBorder(BorderFactory.createRaisedBevelBorder());

		game = gameIn;

		frameWidth = frameWidthIn;
		frameHeight = frameHeightIn;

		distanceFromTopMargin = (int) Math.round(TOP_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromBottomMargin = (int) Math.round(BOTTOM_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromLeftMargin = (int) Math.round(LEFT_MARGIN_PERCENT_OF_FRAME * frameWidth);
		distanceFromRightMargin = (int) Math.round(RIGHT_MARGIN_PERCENT_OF_FRAME * frameWidth);

		height = frameHeight - (distanceFromTopMargin + distanceFromBottomMargin);
		width = frameWidth - (distanceFromLeftMargin + distanceFromRightMargin);

		setBounds(distanceFromLeftMargin, distanceFromTopMargin, width, height);

		gameStatusLabel = new GameStatusLabel();
		levelLabel = new LevelLabel(game);


		add(levelLabel, BorderLayout.PAGE_START);
		add(gameStatusLabel, BorderLayout.CENTER);
	}


	public void endGame() {
		//remove(gameStatusLabel);
		//gameStatusLabel = new GameStatusLabel();
		//add(gameStatusLabel);
		gameStatusLabel.setText(GameStatusLabel.GAME_OVER);
		t.stop();
		gameStatusLabel.setVisible(true);
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);

		gameStatusLabel.repaint();
		levelLabel.repaint();
	}

	public void resize() {
		distanceFromTopMargin = (int) Math.round(TOP_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromBottomMargin = (int) Math.round(BOTTOM_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromLeftMargin = (int) Math.round(LEFT_MARGIN_PERCENT_OF_FRAME * frameWidth);
		distanceFromRightMargin = (int) Math.round(RIGHT_MARGIN_PERCENT_OF_FRAME * frameWidth);

		height = frameHeight - (distanceFromTopMargin + distanceFromBottomMargin);
		width = frameWidth - (distanceFromLeftMargin + distanceFromRightMargin);

		setBounds(distanceFromLeftMargin, distanceFromTopMargin, width, height);
	}

	public void displayMessage(String message) {
		
		gameStatusLabel.setText(message);
		gameStatusLabel.setVisible(true);
		t = new Timer(1000, new TimerListener(this));
		t.stop();
		t.start();
	}

	class TimerListener implements ActionListener
	{
		int y = 0;
		LevelDisplay display;
		
		public TimerListener(LevelDisplay displayIn) {
			display = displayIn;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			y++;
			if (y > 1 && display.getGame().getIsDone() && y < 4) {
				t = (Timer) arg0.getSource();
				t.stop();
				display.getGameStatusLabel().setVisible(false);
			}
			display.getGameStatusLabel().setVisible(true);
		}

	}
}
