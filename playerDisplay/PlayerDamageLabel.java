package playerDisplay;
import java.awt.Color;

import javax.swing.JLabel;

import game.Game1;

public class PlayerDamageLabel extends JLabel{
	private Game1 game;

	public PlayerDamageLabel(Game1 gameIn) {
		super();

		game = gameIn;

		setText("Damage: " + game.getGrid().getPlayer().getDamage());

		setOpaque(true);
		setBackground(Color.white);
	}
	
	public void update() {
		setText("Damage: " + game.getGrid().getPlayer().getDamage());
		repaint();
	}
}
