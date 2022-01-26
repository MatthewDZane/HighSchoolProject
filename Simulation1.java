public class Simulation1 {
	
	public static void main(String [] args) {
		run(50);
		
		System.exit(0);
	}
	
	public static void run(int gridSize){
		Simulation1Grid grid = new Simulation1Grid(gridSize);
		GridRunner runner = new GridRunner(grid);
		GridDisplayer gridDisplay = new GridDisplayer(runner, 1000, 1000);
		
		gridDisplay.repaint();
		while (!runner.getIsDone()) {
			
			if (!runner.getIsPaused()) {
				gridDisplay.takeTurn();
			}
			else {
				
			}
			gridDisplay.repaint();
		}
	}
}
