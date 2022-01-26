package entities;

import java.awt.Point;

import helpers.Direction;

public abstract class Projectile {
	protected int speed;
	protected int damage;
	protected int knockback;
	protected Point coordinates;
	protected Direction direction;
	
	public int getSpeed() { return speed; }
	public int getDamage() { return damage; }
	public int getKnockback() { return knockback; }
	public Point getCoordinates() { return coordinates; }
	public Direction getDirection() { return direction; }
	
	/**
	 * Returns whether or not the target was killed are destroyed
	 * @param target
	 * @return
	 */
	public boolean hitObstacle(Obstacle target) {
		if (target instanceof Enemy) {
			Enemy enemy = (Enemy) target;
			useAbility(target);
			enemy.takeDamage(damage);
			return enemy.isDead();
		}
		else if (target instanceof Player) {
			Player player = (Player) target;
			useAbility(target);
			player.takeDamage(damage);
			return player.isDead();
		}
		else {
			return useAbility(target);
		}
	}
	
	/**
	 * returns whether or not ability was used successfully
	 * @param target
	 * @return
	 */
	public abstract boolean useAbility(Object target);
}
