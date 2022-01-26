package gameRunner;

import game.Game1;

public class Game1Runner {
	public static void main(String [] args) {
		while(true) {
			Game1.run().dispose();
		}
	}
}
