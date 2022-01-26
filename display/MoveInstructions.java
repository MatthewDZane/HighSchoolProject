package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MoveInstructions extends JPanel{
	public BufferedImage moveInstructions = null;
	
	public MoveInstructions() {
		super();
		
		try {
			moveInstructions = ImageIO.read(new File("./pictures/movingInstructions.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g) {
		g.drawImage(moveInstructions, 0, 0, getWidth(), getHeight(), null);
	}
}
