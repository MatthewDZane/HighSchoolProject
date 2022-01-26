package helpers;

import java.awt.Point;
import java.util.*;

import entities.Enemy;
import entities.Obstacle;
import entities.Player;

public class AStar {
	public static final int V_H_COST = 10;

	static class Cell{  
		int heuristicCost = 0; 
		int finalCost = 0; 
		int i, j;
		Cell parent; 

		Cell(int i, int j){
			this.i = i;
			this.j = j; 
		}

		public String toString(){
			return "["+this.i+", "+this.j+"]";
		}
	}

	static Cell [][] grid;

	static PriorityQueue<Cell> open;

	static boolean closed[][];
	static int startI, startJ;

	static ArrayList<Point> endCells ;

	public static void setBlocked(int i, int j){
		grid[i][j] = null;
	}

	public static void setStartCell(int i, int j){
		startI = i;
		startJ = j;
	}

	public static void setEndCell(int i, int j){ 
		endCells.add(new Point(i, j));
	}

	static void checkAndUpdateCost(Cell current, Cell t, int cost){
		if(t == null || closed[t.i][t.j])return;
		int t_final_cost = t.heuristicCost+cost;

		boolean inOpen = open.contains(t);
		if(!inOpen || t_final_cost<t.finalCost){
			t.finalCost = t_final_cost;
			t.parent = current;
			if(!inOpen)open.add(t);
		}
	}

	public static void AStar(){ 

		open.add(grid[startJ][startI]);

		Cell current;

		while(true){ 
			current = open.poll();
			if(current==null)break;
			closed[current.i][current.j]=true; 

			if(current.equals(isEndPoint(current.i, current.j))){
				return; 
			} 

			Cell t;  
			if(current.i-1>=0){
				t = grid[current.i-1][current.j];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
			} 

			if(current.j-1>=0){
				t = grid[current.i][current.j-1];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
			}

			if(current.j+1<grid[0].length){
				t = grid[current.i][current.j+1];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
			}

			if(current.i+1<grid.length){
				t = grid[current.i+1][current.j];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
			}
		} 
	}
	
	static Point startingPoint;
	/*
    Params :
    tCase = test case No.
    x, y = Board's dimensions
    si, sj = start location's x and y coordinates
    ei, ej = end location's x and y coordinates
    int[][] blocked = array containing inaccessible cell coordinates
	 */
	public static boolean test(Object [][] gridIn, ArrayList<Enemy> enemies, Player player){
		endCells = new ArrayList<Point>();
		grid = new Cell[gridIn.length + 2][gridIn[0].length + 2];
		closed = new boolean[gridIn.length + 2][gridIn[0].length + 2];
		open = new PriorityQueue<>((Object o1, Object o2) -> {
			Cell c1 = (Cell)o1;
			Cell c2 = (Cell)o2;

			return c1.finalCost<c2.finalCost?-1:
				c1.finalCost>c2.finalCost?1:0;
		});

		for (Enemy enemy : enemies) {
			 Point p = enemy.getCoordinates();
			setEndCell(p.y + 1, p.x + 1);
		}
		
		Point p = player.getCoordinates();
		setStartCell(p.y + 1, p.x + 1);
		startingPoint = new Point(p.x + 1, p.y + 1);
		
		for(int i=0;i<gridIn[0].length + 2;++i){
			for(int j=0;j<gridIn.length + 2;++j){
				Point closestPoint = findClosestEndPoint(i, j);
				int endI = closestPoint.x;
				int endJ = closestPoint.y;
				grid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ);
			}
		}
		
		grid[startingPoint.x][startingPoint.y].finalCost = 0;

		for (int y = 0; y < grid.length; y++) {
			grid[y][0] = null;
			grid[y][grid[0].length - 1] = null;
		}

		for (int x = 0; x < grid[0].length; x++) {
			grid[0][x] = null;
			grid[grid.length - 1][x] = null;
		}
		
		for (int y = 0; y < gridIn.length; y++) {
			for (int x = 0; x < gridIn[0].length; x++) {
				if (gridIn[y][x] instanceof Enemy) {
				}
				else if (gridIn[y][x] instanceof Player) {
					setStartCell(x + 1, y + 1);
					startingPoint = new Point(x + 1, y + 1);
				}
				else if (gridIn[y][x] instanceof Obstacle) {
					grid[y + 1][x + 1] = null;
				}
			}
		}

		AStar(); 
		
		Point endPointFound = endPointMet();
		if (endPointFound != null){
			Cell current = grid[endPointFound.x][endPointFound.y];
			
			while(current.parent!=null){
				current = current.parent;
			} 
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEndPoint(int i , int j) {
		for (Point temp : endCells) {
			if (temp.x == i && temp.y == j) {
				return true;
			}
		}
		return false;
	}
	
	public static Point endPointMet() {
		for (Point temp : endCells) {
			if (closed[temp.x][temp.y]) {
				return temp;
			}
		}
		return null;
	}
	
	public static Point findClosestEndPoint(int i, int j) {
		Point currentPoint = new Point(i, j);
		Point closestPoint = null;
		double closestDistance = Double.MAX_VALUE;
		for (Point temp : endCells) {
			double distance = currentPoint.distance(temp);
			if (distance < closestDistance) {
				closestPoint = temp;
				closestDistance = distance;
			}
		}
		return closestPoint;
	}
}
