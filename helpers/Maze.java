package helpers;
import java.awt.Point;

import entities.Enemy;
import entities.Obstacle;
import entities.Player;

public class Maze {
	
	private int[][] grid;

	public static final int END_POINT = 1;
	public Point startingPoint;
	public static final int WALL = 2;
	public static final int EMPTY_POINT = 0;
	public static final int VISITED_POINT = 3;

	public Maze(Object[][] gridIn) {
		

		grid = new int[gridIn.length + 2][gridIn[0].length + 2];
		for (int y = 0; y < grid.length; y++) {
			grid[y][0] = WALL;
			grid[y][grid[0].length - 1] = WALL;
		}
		for (int x = 0; x < grid[0].length; x++) {
			grid[0][x] = WALL;
			grid[grid.length - 1][x] = WALL;
		}
		for (int y = 0; y < gridIn.length; y++) {
			for (int x = 0; x < gridIn[0].length; x++) {
				if (gridIn[y][x] instanceof Enemy) {
					grid[y + 1][x + 1] = END_POINT;
				}
				else if (gridIn[y][x] instanceof Player) {
					grid[y + 1][x + 1] = VISITED_POINT;
					startingPoint = new Point(x + 1, y + 1);
				}
				else if (gridIn[y][x] instanceof Obstacle) {
					grid[y + 1][x + 1] = WALL;
				}
				else if (gridIn[y][x] == null) {
					grid[y + 1][x + 1] = EMPTY_POINT;
				}
			}
		}
	}
	public boolean isSolvable() {
		return findSolution(grid.clone(), new Point((int) startingPoint.getX(), (int) startingPoint.getY()));
	}

	public boolean findSolution(int[][] arrIn, Point lastPt) {

		int x = (int) lastPt.getX();
		int y = (int) lastPt.getY();
		if (arrIn[y][x] == 1) {
			return true;
		}
		arrIn[y][x] = VISITED_POINT;

		grid = cloneGrid(arrIn);

		
		for (Direction temp : Direction.getDirections()) {
			Point newPoint = new Point();

			switch (temp) {
			case UP: newPoint = new Point(x, y - 1);
			break;
			case DOWN:newPoint = new Point(x, y + 1);
			break;
			case LEFT:newPoint = new Point(x - 1, y);
			break;
			case RIGHT:newPoint = new Point(x + 1, y);
			break;
			default:
				break;
			}
			int newX = newPoint.x;
			int newY = newPoint.y;

			if (newX > 0 && newX < arrIn[0].length - 1 &&
					newY > 0 && newY < arrIn.length - 1 &&
					arrIn[newY][newX] != WALL && arrIn[newY][newX] != VISITED_POINT) {
				boolean isSolution =  findSolution(grid, newPoint);
				if (isSolution) {
					return true;
				}
				else {
					grid = cloneGrid(arrIn);
				}
			}

		}
		return false;
	}

	public static int[][] cloneGrid(int[][] grid) {
		int[][] arr = new int[grid.length][grid[0].length];
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[0].length; x++) {
				arr[y][x] = grid[y][x];
			}
		}
		return arr;
	}

	public void display() {
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[0].length; x++) {
				System.out.print(grid[y][x] + " ");
			}
			System.out.println();
		}
	}
}
