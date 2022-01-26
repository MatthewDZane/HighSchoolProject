package playerDisplay;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import entities.Player;
import game.Game1;

public class PlayerHealthGraphicDisplay extends JPanel{
	private Player player;

	private int frameHeight;
	private int frameWidth;

	private int height;
	private int width;
	private int distanceFromTopMargin;
	private int distanceFromBottomMargin;
	private int distanceFromLeftMargin;
	private int distanceFromRightMargin;

	public static final double TOP_MARGIN_PERCENT_OF_FRAME = 0.10;
	public static final double BOTTOM_MARGIN_PERCENT_OF_FRAME = 0.86;
	public static final double LEFT_MARGIN_PERCENT_OF_FRAME = 0.16;
	public static final double RIGHT_MARGIN_PERCENT_OF_FRAME = 0.61;
	
	public static final int GRID_LINE_WIDTH = 1;
	public int healthPointWidth;

	public void setFrameHeight(int frameHeightIn) { frameHeight = frameHeightIn; }
	public void setFrameWidth(int frameWidthIn) { frameWidth = frameWidthIn; }

	public void setPlayerDisplayHeight(int heightIn) { height = heightIn; }
	public void setPlayerDisplayWidth(int widthIn) { width = widthIn; }

	public int getDistanceFromTopMargin() { return distanceFromTopMargin; }
	public int getDistanceFromBottonMargin() { return distanceFromBottomMargin; }
	public int getDistanceFromLeftMargins() { return distanceFromLeftMargin; }

	public PlayerHealthGraphicDisplay(Game1 gameIn, int frameWidthIn, int frameHeightIn) {
		super();

		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		player = gameIn.getGrid().getPlayer();

		frameWidth = frameWidthIn;
		frameHeight = frameHeightIn;

		distanceFromTopMargin = (int) Math.round(TOP_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromBottomMargin = (int) Math.round(BOTTOM_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromLeftMargin = (int) Math.round(LEFT_MARGIN_PERCENT_OF_FRAME * frameWidth);
		distanceFromRightMargin = (int) Math.round(RIGHT_MARGIN_PERCENT_OF_FRAME * frameWidth);

		height = frameHeight - (distanceFromTopMargin + distanceFromBottomMargin);
		width = frameWidth - (distanceFromLeftMargin + distanceFromRightMargin);

		healthPointWidth = (width - GRID_LINE_WIDTH) / player.getMaxHealth() - GRID_LINE_WIDTH;
		
		setBounds(distanceFromLeftMargin, distanceFromTopMargin, width, height);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		resize();
		g.setColor(Color.red);
		for (int i = 0; i < player.getHP(); i++) {
			g.fillRect(GRID_LINE_WIDTH + (healthPointWidth + GRID_LINE_WIDTH) * i, 
					2 * GRID_LINE_WIDTH, healthPointWidth, height - 4 * GRID_LINE_WIDTH);
		}
	}
	
	public void resize() {

		distanceFromTopMargin = (int) Math.round(TOP_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromBottomMargin = (int) Math.round(BOTTOM_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromLeftMargin = (int) Math.round(LEFT_MARGIN_PERCENT_OF_FRAME * frameWidth);
		distanceFromRightMargin = (int) Math.round(RIGHT_MARGIN_PERCENT_OF_FRAME * frameWidth);

		height = frameHeight - (distanceFromTopMargin + distanceFromBottomMargin);
		width = frameWidth - (distanceFromLeftMargin + distanceFromRightMargin);
		
		healthPointWidth = (width - GRID_LINE_WIDTH) /  player.getMaxHealth() - GRID_LINE_WIDTH;
		
		setBounds(distanceFromLeftMargin, distanceFromTopMargin, width, height);
	}
}
