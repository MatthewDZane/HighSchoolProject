package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ShootInstructions extends JPanel{
	
	public BufferedImage shootInstructions = null;
	
	public ShootInstructions() {
		super();
		
		try {
			shootInstructions = ImageIO.read(new File("./pictures/shootingInstructions.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g) {
		g.drawImage(shootInstructions, 0, 0, getWidth(), getHeight(),  null);
	}
}
