package entities;
import java.awt.Point;

public class Obstacle {
	private Point coordinates;
	
	public Point getCoordinates() { return coordinates; }
	
	public Obstacle(int x, int y) {
		coordinates = new Point(x, y);
	}
	
	public String toString() {
		return "Obstacle";
	}
}
