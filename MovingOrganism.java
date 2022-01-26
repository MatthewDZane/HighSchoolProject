/**
 * 
 * @author Matthew Zane
 *
 */
public class MovingOrganism extends Organism {
	private int maxSpeed = 0;
	
	public int getMaxSpeed() { return maxSpeed; }
	
	/**
	 * Default constructor - calls default super constructor and sets
	 * maxSpeed to 1.
	 */
	public MovingOrganism() {
		super();
		maxSpeed = 1;
	}
	/**
	 * Overloaded constructor - calls overloaded super constructor and
	 * sets lifeSpan and maxSpeed
	 * @param lifeSpanIn
	 * @param maxSpeedIn
	 */
	public MovingOrganism(int lifeSpanIn, int maxSpeedIn) {
		super(lifeSpanIn);
		maxSpeed = maxSpeedIn;
	}
	/**
	 * organism chooses a direction to move in
	 * uses getRandomDirection() to select
	 * @return Direction enum of direction to move
	 */
	public Direction chooseDirection() {
		return Direction.getRandomDirection();
	}
	/**
	 * organism chooses how many spaces to move within its max speed
	 * @return number of spaces to move
	 */
	public int chooseDistance() {
		return (int) ((Math.random() * maxSpeed) + 1);
	}
	public MovementVector move() {
		return new MovementVector(chooseDirection(), chooseDistance());
	}
	/**
	 * organism chooses what action it is going to do
	 */
	@Override
	public Object action() {
		int randomInt = (int)((Math.random()*3) + 1);
		switch (randomInt) {
		case 1: return move();
		case 2: return reproduce();
		case 3: return null;

		default: return null;
		}
	}
	/**
	 * organism chooses a direction to reproduce in
	 */
	@Override
	public Object reproduce() {
		return chooseDirection();
	}

}
