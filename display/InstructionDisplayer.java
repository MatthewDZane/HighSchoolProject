package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.Timer;

import game.Game1;

public class InstructionDisplayer  extends JPanel{

	private int frameHeight;
	private int frameWidth;

	private int height;
	private int width;
	
	private int distanceFromTopMargin;
	private int distanceFromBottomMargin;
	private int distanceFromLeftMargin;
	private int distanceFromRightMargin;

	public static final double TOP_MARGIN_PERCENT_OF_FRAME = 0.01;
	public static final double BOTTOM_MARGIN_PERCENT_OF_FRAME = 0.86;
	public static final double LEFT_MARGIN_PERCENT_OF_FRAME = 0.63;
	public static final double RIGHT_MARGIN_PERCENT_OF_FRAME = 0.05;
	
	MoveInstructions move = new MoveInstructions();
	ShootInstructions shoot = new ShootInstructions();

	public void setFrameHeight(int frameHeightIn) { frameHeight = frameHeightIn; }
	public void setFrameWidth(int frameWidthIn) { frameWidth = frameWidthIn; }

	public void setPlayerDisplayHeight(int heightIn) { height = heightIn; }
	public void setPlayerDisplayWidth(int widthIn) { width = widthIn; }

	public int getDistanceFromTopMargin() { return distanceFromTopMargin; }
	public int getDistanceFromBottonMargin() { return distanceFromBottomMargin; }
	public int getDistanceFromLeftMargins() { return distanceFromLeftMargin; }


	public InstructionDisplayer(int frameWidthIn, int frameHeightIn) {
		super();
		//setLayout(new SpringLayout());
		setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(Color.WHITE);

		frameWidth = frameWidthIn;
		frameHeight = frameHeightIn;

		distanceFromTopMargin = (int) Math.round(TOP_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromBottomMargin = (int) Math.round(BOTTOM_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromLeftMargin = (int) Math.round(LEFT_MARGIN_PERCENT_OF_FRAME * frameWidth);
		distanceFromRightMargin = (int) Math.round(RIGHT_MARGIN_PERCENT_OF_FRAME * frameWidth);

		height = frameHeight - (distanceFromTopMargin + distanceFromBottomMargin);
		width = frameWidth - (distanceFromLeftMargin + distanceFromRightMargin);

		add(move);
		add(shoot);
		
		shoot.setBounds(0,  0, width / 2, height);
		move.setBounds(width / 2 + 1, 0, width / 2, height);
		
		setBounds(distanceFromLeftMargin, distanceFromTopMargin, width, height);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		resize();
	}

	public void resize() {
		distanceFromTopMargin = (int) Math.round(TOP_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromBottomMargin = (int) Math.round(BOTTOM_MARGIN_PERCENT_OF_FRAME * frameHeight);
		distanceFromLeftMargin = (int) Math.round(LEFT_MARGIN_PERCENT_OF_FRAME * frameWidth);
		distanceFromRightMargin = (int) Math.round(RIGHT_MARGIN_PERCENT_OF_FRAME * frameWidth);

		height = frameHeight - (distanceFromTopMargin + distanceFromBottomMargin);
		width = frameWidth - (distanceFromLeftMargin + distanceFromRightMargin);

		shoot.setBounds(0,  0, width / 2, height);
		move.setBounds(width / 2 + 1, 0, width / 2, height);
		
		
		setBounds(distanceFromLeftMargin, distanceFromTopMargin, width, height);
	}
}
