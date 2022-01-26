
public class Organism {
	private final int ID;
	private int lifeSpan;
	private int turnsRemaining;
	private boolean isNewBorn = true;

	public boolean getIsNewBorn() { return isNewBorn; }
	public void setTurnsLeft(int turnsLeft) { turnsRemaining = turnsLeft; }
	public int getTurnsLeft() {return turnsRemaining; }
	public int getLifeSpan() {return lifeSpan; }
	public int getID() { return ID; }

	/**
	 * Default constructor for an organism - sets lifeSpan to 1, turnsRemaining 
	 * is set to lifeSpan, and ID is set to next available value
	 */
	public Organism() {
		lifeSpan = 1;
		turnsRemaining = lifeSpan;
		ID = IDCounter.getNextValue();
	}
	/**
	 * Overloaded constructor for an organism - sets lifeSpan to input, turnsRemaining
	 * is set to lifeSpan, and ID is set to next available value
	 * @param lifeSpanIn
	 */
	public Organism(int lifeSpanIn) {
		lifeSpan = lifeSpanIn;
		turnsRemaining = lifeSpanIn;
		ID = IDCounter.getNextValue();
	}
	/**
	 * decrements the turnsRemaining and sets inNewBorn to false
	 */
	public void age() {
		turnsRemaining--;
		isNewBorn = false;
	}

	public boolean canDie() {
		return turnsRemaining == 0;
	}
	/**
	 * To String of Organism
	 */
	public String toString() {
		return "Class: Organism\nID: " + ID + "\nTurns left: " + turnsRemaining + "\n";
	}
	/**
	 * Does the action that the organism chooses
	 * @return Object related to action.
	 */
	public Object action() {
		return reproduce();
	}

	/**
	 * organism chooses a direction to reproduce in
	 */
	public Object reproduce() {
		int randomInt = (int)((Math.random()*2) + 1);
		switch (randomInt) {
		case 1: return Direction.getRandomDirection();
		case 2: return null;

		default: return null;
		}
	}
}
