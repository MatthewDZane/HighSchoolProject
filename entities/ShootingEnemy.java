package entities;

import java.awt.Point;

public class ShootingEnemy extends Enemy{
	
	public ShootingEnemy(int x, int y) {
		super(x, y);
	}
	
	public ShootingEnemy(int x, int y, int hp, int damage) {
		super(x, y, hp, damage);
	}
	
	public String toString() {
		return "Shooting Enemy";
	}
	
	public ShootingEnemy clone() {
		Point coordinates = getCoordinates();
		return new ShootingEnemy(coordinates.x, coordinates.y, getHP(), getDamage());
	}
}
