
public class MovementVector {
	private Direction direction;
	private int distance;
	
	public Direction getDirection() { return direction; }
	public void setDirection(Direction directionIn) { direction = directionIn; }
	public int getDistance() { return distance; }
	public void setDistance(int distanceIn) { distance = distanceIn; }
	
	public MovementVector(Direction directionIn, int distanceIn) {
		setDirection(directionIn);
		setDistance(distanceIn);
	}	
}
