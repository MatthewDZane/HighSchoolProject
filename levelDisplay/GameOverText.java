package levelDisplay;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class GameOverText extends JLabel {

	public GameOverText() {
		super();

		setFont(this.getFont().deriveFont((float) (this.getFont().getSize() * 3)));
		setHorizontalAlignment(JTextField.CENTER);
		setText("Game Over");
		setForeground(Color.black);
		
		setVisible(false);
	}
	

}
