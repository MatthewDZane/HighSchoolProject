package levelDisplay;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

import game.Game1;

public class LevelLabel extends JLabel {
	private Game1 game;
	
	public LevelLabel(Game1 gameIn) {
		super("Level: " + gameIn.getLevel(), JLabel.CENTER);

		game = gameIn;	
		
		setOpaque(true);
		setBackground(Color.white);
	}

	public void paint(Graphics g) {
		super.paint(g);
		setText("Level: " + game.getLevel());

	}
}