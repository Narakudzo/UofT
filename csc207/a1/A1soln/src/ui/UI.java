package ui;

import java.io.IOException;

/** An interface that sets User Interface of the game. */
public interface UI {
	
	/** Starts the game.
	 * @throws IOException executes System.in.read().
	 */
	public void launchGame() throws IOException;
	
	/** Checks if player1 or player2 wins (or both stuck),
	 * and displays the corresponding message. */
	public void displayWinner();

}
