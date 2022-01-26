package entities;

import java.awt.Point;

public abstract class PickUp {
	public Point coordinates = new Point();
	
	public Point getCoordinates() { return coordinates; }
	
	public abstract void usePower(Player player);
}