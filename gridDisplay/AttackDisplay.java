package gridDisplay;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import helpers.Direction;

public class AttackDisplay extends JPanel{
	private BufferedImage attackTexture = null;
	
	public AttackDisplay(Direction direction) {
		try {
			attackTexture = ImageIO.read(new File("./pictures/attack" + direction + ".png"));
		} catch (IOException e) {}
		
		setOpaque(false);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(attackTexture, 0, 0, getWidth(), getHeight(), null);
		
		Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
	}
}
