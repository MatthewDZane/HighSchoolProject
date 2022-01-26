import javax.swing.JFrame;
/**
 * This class: tests all of the classes in the Simulation1 project
 * @author Matthew Zane
 *
 */
public class Simulation1Tester extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		/*//SquareGrid tester
		Grid grid1 = new Grid(10);
		
		grid1.displayGrid();
		
		System.out.println( "\nWidth: " + grid1.getWidth() + "\nExpected: 10");
		System.out.println( "Heigth: " + grid1.getHeight() + "\nExpected: 10\n");
		
		grid1.setSides(20);
		
		System.out.println( "Width: " + grid1.getWidth() + "\nExpected: 20");
		System.out.println( "Heigth: " + grid1.getHeight() + "\nExpected: 20\n");
		
		System.out.println("The shape is a: " + grid1.getGridShape() + "\nExpected: Square\n");
		
		System.out.println(grid1 + "\nExpected:\nClass: SquareGrid\nWidth: 20\n"
				+ "Height: 20\nGrid Shape: Square\nOrganism ID Counter: 1\nNumber of organisms: 0"
				+ "\n");
		
		grid1.createOrganism(0, 0);
		grid1.displayGrid();
		
		System.out.println("Number of Organisms: " + grid1.getNumOrganisms()
				+ "\n Expected: 1");
		
		grid1.createOrganism(1, 1);
		grid1.displayGrid();
		
		System.out.println("Number of Organisms: " + grid1.getNumOrganisms()
		+ "\nExpected: 2");

		grid1.createOrganism(0, 0);
		System.out.println("Expected: Error. Could not create Organism. Cell already occupied\n");
		grid1.displayGrid();
		System.out.println("Number of Organisms: " + grid1.getNumOrganisms()
		+ "\nExpected: 2");
	
		grid1.killOrganism(2);
		
		System.out.println("Number of Organisms: " + grid1.getNumOrganisms()
		+ "\nExpected: 1");

		grid1.displayGrid();
		
		grid1.growOrganism(1, 1, 0);
		grid1.displayGrid();
		
		grid1.growOrganism(1, 0, 1);
		grid1.displayGrid();
		
		grid1.shrinkOrganism(1, 1, 0);
		grid1.displayGrid();
		
		
		
		//Organism tester
		Organism organism1 = new Organism(1);
		Organism organism2 = new Organism(2, 1);
		
		System.out.println("Organism1 ID: " + organism1.getID() + "\nExpected: 1");
		System.out.println("Organism2 ID: " + organism2.getID() + "\nExpected: 2\n");
		
		System.out.println("\nOrganism1 Max Speed: " + organism1.getMaxSpeed() + "\nExpected: 0");
		System.out.println("Organism2 Max Speed: " + organism2.getMaxSpeed() + "\nExpected: 1");
		
		organism1.setID(3);
		organism1.setMaxSpeed(2);
		
		System.out.println("\nOrganism1 ID: " + organism1.getID() + "\nExpected: 3");
		System.out.println("Organism1 Max Speed: " + organism1.getMaxSpeed() + "\nExpected: 2\n");
		
		System.out.println(organism1 + "\nExpected:\nClass : Organism\nMax Speed: 2 "
				+ "\nID: 3 \n");
		
		
		//Execution Testing
		/*SquareGrid grid2 = new SquareGrid(11);
		grid2.createOrganism(1, 1);
		grid2.displayGrid();
		grid2.takeTurn();
		grid2.displayGrid();*/
		//System.out.println("Grid 3 ----------------------------------");*/
		
	}
		
		
	
}
