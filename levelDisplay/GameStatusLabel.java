package levelDisplay;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class GameStatusLabel extends JLabel {

	public static final String GAME_OVER = "Game Over";
	public static final String LEVEL_UP = "Level Up";
	public static final String ENEMY_KILLED = "Enemy Killed";
	public static final String DAMAGE_TAKEN = "Damage Taken";
	public static final String ENEMY_HIT = "Enemy Hit";
	public static final String HEALED = "Healed";
	public static final String HEALTH_UP = "HealthUp";
	public static final String ATTACK_UP = "Attack Up";
	
	public GameStatusLabel() {
		super();

		setFont(this.getFont().deriveFont((float) (this.getFont().getSize() * 2)));
		setHorizontalAlignment(JTextField.CENTER);
		setText("Game Over");
		setForeground(Color.black);
		
		setVisible(false);
	}
	
	public void setVisible(boolean f) {
		super.setVisible(f);
	}
	

}
