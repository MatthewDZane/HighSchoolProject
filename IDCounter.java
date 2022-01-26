
public final class IDCounter {
	private static int count = 1;
	
	public static int getNextValue() {return count++;}
	
	private IDCounter() {}
	
	public static void resetCount() {
		count = 1;
	}
}
