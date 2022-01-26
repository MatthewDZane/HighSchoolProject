package helpers;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import display.Displayer;
import gridDisplay.AttackDisplay;
import gridDisplay.GridDisplayer;

public class AttackListener {
	Displayer panel;
	
	public AttackListener(Displayer panelIn) {
		panel = panelIn;
	}
	
	public void actionPreformed(Direction direction, Point coordinates) {
		Rectangle rect = getRect(coordinates);
		AttackDisplay attackDisplay = new AttackDisplay(direction);
		panel.add(attackDisplay , 1);
		attackDisplay.setBounds(rect.x, rect.y, rect.width, rect.height);
		panel.setVisible(true);
		Timer t = new Timer(100, new TimerListener(attackDisplay, panel));
		t.start();
		panel.repaint();
	}
	
	public Rectangle getRect(Point p) {
		GridDisplayer gridDisplayer = panel.getGridDisplayer();
		Rectangle rect  = gridDisplayer.getRect(p);
		
		rect.width  = (int) Math.round(panel.getFrameWidth() * (1 -  (2 * GridDisplayer.SIDE_MARGINS_PERCENT_OF_FRAME)) 
				* rect.width / gridDisplayer.getGridWidth());
		rect.height = (int) Math.round(panel.getFrameHeight() * rect.height * 
				(1 - GridDisplayer.BOTTOM_MARGIN_PERCENT_OF_FRAME - GridDisplayer.TOP_MARGIN_PERCENT_OF_FRAME) 
				/ gridDisplayer.getGridHeight());
		
		rect.x = rect.width * p.x + gridDisplayer.getDistanceFromSideMargins();
		rect.y = rect.height * p.y + gridDisplayer.getDistanceFromTopMargin();
		
		return rect;
	}
	
	public class TimerListener implements ActionListener{
		int y = 0;
		AttackDisplay display;
		Displayer panel;
		
		public TimerListener(AttackDisplay displayIn, Displayer panelIn) {
			display = displayIn;
			panel = panelIn;
		}
		
		public void actionPerformed(ActionEvent e) {
			y++;
			if (y > 1)
			panel.remove(display);
			panel.repaint();
		}
	}
}
