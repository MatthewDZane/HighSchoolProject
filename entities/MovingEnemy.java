package entities;

public class MovingEnemy extends Enemy {
	
	public MovingEnemy(int x, int y) {
		super(x, y);
	}
	public MovingEnemy(int x, int y, int hp, int damage) {
		super(x, y, hp, damage);
	}
	public String toString() {
		return "Moving Enemy";
	}
}
