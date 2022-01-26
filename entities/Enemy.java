package entities;

public class Enemy extends Obstacle{
	private int hp = 2;
	private int damage = 1;
	
	public int getDamage() { return damage; }
	
	public int getHP() { return hp; }
	
	public Enemy(int x, int y) {
		super(x, y);
	}
	public Enemy(int x, int y, int hpIn, int damageIn) {
		super(x, y);
		hp = hpIn;
		damage = damageIn;
	}
	public void takeDamage(int damage) {
		hp = hp - damage;
	}
	public boolean isDead() {
		return hp <= 0;
	}
	public String toString() {
		return "Enemy";
	}
}
