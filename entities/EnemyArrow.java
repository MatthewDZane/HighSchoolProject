package entities;

import java.awt.Point;

import helpers.Direction;

public class EnemyArrow extends Projectile{

	public EnemyArrow(int x, int y, int speedIn, int damageIn, int knockbackIn, Direction directionIn) {
		speed = speedIn;
		damage = damageIn;
		knockback = knockbackIn;
		direction = directionIn;
		coordinates = new Point(x, y);
	}
	
	public boolean useAbility(Object target) {
		return false;
	}

}