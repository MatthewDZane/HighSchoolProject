package playerDisplay;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import game.Game1;

public class PlayerDisplayer extends JPanel {

	private int frameHeight;
	private int frameWidth;

	private PlayerNameLabel playerNameLabel;
	private PlayerHPLabel playerHPLabel;
	private PlayerDamageLabel playerDamageLabel;

	private int height;
	private int width;
	private int distanceFromTopMargin;
	private int distanceFromBottomMargin;
	private int distanceFromLeftMargin;
	private int distanceFromRightMargin;
	
	public static final double TOP_MARGIN_PERCENT_OF_FRAME = 0.01;
	public static final double BOTTOM_MARGIN_PERCENT_OF_FRAME = 0.92;
	public static final double LEFT_MARGIN_PERCENT_OF_FRAME = 0.02;
	public static final double RIGHT_MARGIN_PERCENT_OF_FRAME = 0.75;

	public void setFrameHeight(int frameHeightIn) { frameHeight = frameHeightIn; }
	public void setFrameWidth(int frameWidthIn) { frameWidth = frameWidthIn; }

	public void setPlayerDisplayHeight(int heightIn) { height = heightIn; }
	public void setPlayerDisplayWidth(int widthIn) { width = widthIn; }

	public int getDistanceFromTopMargin() { return distanceFromTopMargin; }
	public int getDistanceFromBottonMargin() { return distanceFromBottomMargin; }
	public int getDistanceFromLeftMargins() { return distanceFromLeftMargin; }


	public PlayerDisplayer(Game1 gameIn, int frameWidthIn, int frameHeightIn) {
		super();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.white);
		setBorder(BorderFactory.createRaisedBevelBorder());

		frameWidth = frameWidthIn;
		frameHeight = frameHeightIn;

		distanceFromTopMargin = (int) Math.round(TOP_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromBottomMargin = (int) Math.round(BOTTOM_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromLeftMargin = (int) Math.round(LEFT_MARGIN_PERCENT_OF_FRAME * frameWidth);
		distanceFromRightMargin = (int) Math.round(RIGHT_MARGIN_PERCENT_OF_FRAME * frameWidth);

		height = frameHeight - (distanceFromTopMargin + distanceFromBottomMargin);
		width = frameWidth - (distanceFromLeftMargin + distanceFromRightMargin);

		setBounds(distanceFromLeftMargin, distanceFromTopMargin, width, height);

		playerNameLabel = new PlayerNameLabel(gameIn);
		playerHPLabel = new PlayerHPLabel(gameIn);
		playerDamageLabel = new PlayerDamageLabel(gameIn);
		
		add(playerNameLabel);
		add(playerHPLabel);
		add(playerDamageLabel);
		
	}

	public void paint(Graphics g) {
		super.paint(g);
		
		playerNameLabel.update();
		playerHPLabel.update();
		playerDamageLabel.update();
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
}
