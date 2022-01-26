package playerDisplay;

import javax.swing.JLabel;

import game.Game1;

public class PlayerHPLabel extends JLabel {
	private Game1 game;
	
	public PlayerHPLabel(Game1 gameIn) {
		super();
		
		game = gameIn;
		
		setText("Health: " + game.getGrid().getPlayer().getHP());
		
	}
	
	public void update() {
		setText("Health: " + game.getGrid().getPlayer().getHP());
		repaint();
	}
}
