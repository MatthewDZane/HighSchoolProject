package playerDisplay;

import javax.swing.JLabel;

import game.Game1;

public class PlayerNameLabel extends JLabel {
		private Game1 game;
		
		public PlayerNameLabel(Game1 gameIn) {
			super();
			
			game = gameIn;
			
			setFont(this.getFont().deriveFont((float) (this.getFont().getSize() * 1.3)));
			setBorder(null);
			setOpaque(true);
			
			setText("Player Name: " + game.getGrid().getPlayer().getName());
		}
		
		public void update() {
			setText("Player Name: " + game.getGrid().getPlayer().getName());
			repaint();
		}
	}