package entities;

import java.awt.Point;

public class HealthPack extends PickUp {

	private int healthValue;
	
	public HealthPack(int x, int y, int health) {
		healthValue = health;
		coordinates = new Point(x, y);
	}
	
	public void usePower(Player player) {
		player.heal(healthValue);
		
	}
	
}
