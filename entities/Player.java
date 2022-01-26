package entities;
public class Player extends Obstacle{
	private String name = "Player 1";
	private int damage = 2;
	private int hp = 3;
	private int maxHealth = 6;
	private int knockback = 0;

	public String getName() { return name; }
	public int getDamage() { return damage; }
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
		int num = (int) (Math.random() * 3);
		switch (num) {
		case 0 : 
			damage++;
			break;
		case 1 :
			knockback++;
			break;
		case 2 : 
			maxHealth++;
			hp++;
			break;
		}
	}
	public String toString() {
		return "Player";
	}
}
