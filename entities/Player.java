package entities;

import java.awt.Point;

public class Player extends Obstacle{
	private String name = "Player 1";
	private double damage = 1;
	private int hp = 6;
	private int maxHealth = 6;
	private int knockback = 0;

	public String getName() { return name; }
	public int getDamage() { return (int)damage; }
	public int getHP() { return hp; }
	public int getMaxHealth() { return maxHealth; }
	public int getKnockback() { return knockback; }

	public Player(int x, int y) {
		super(x, y);
	}

	public Player(String nameIn, int x, int y) {
		super(x, y);
		name = nameIn;
	}

	public Player(String nameIn, int x, int y, double damageIn, int hpIn, int maxHealthIn, int knockbackIn) {
		super(x, y);
		name = nameIn;
		damage = damageIn;
		hp = hpIn;
		maxHealth = maxHealthIn;
		knockback = knockbackIn;
	}
	
	public void takeDamage(int damage) {
		hp = hp - damage;
	}
	
	public boolean isDead() {
		return hp <= 0;
	}
	
	public void heal(int h) {
		if (hp + h < maxHealth) {
			hp += h;
		}
		else {
			hp = maxHealth;
		}
	}
	
	/**
	 * Randomly increases an attribute of the player by one.
	 */
	public void upgrade() {
		int num = (int) (Math.random() * 2);
		switch (num) {
		case 0 : 
			damage += .25;
			break;
		case 1 :
			maxHealth++;
			hp++;
			break; 
		}
	}
	
	public String toString() {
		return "Player";
	}
	
	public Player clone() {
		Point coordinates = getCoordinates();
		return new Player(name, coordinates.x, coordinates.y, damage, hp, maxHealth, knockback);
	}
}
