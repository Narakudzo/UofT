package ui;

import mazegame.MazeGame;
import java.io.*;

/** A console based UI for the game. */
public class TextUI implements UI {

	/** The game object. */
	private MazeGame game;
	
	/** Initializes the MazeGame. */
	public TextUI(MazeGame game) {
		this.game = game;
	}
	
	@Override
	public void launchGame() throws IOException {
        
		/** Reads command from players, and ignores extra newlines from the input. */
        char command, ignore;

        while(game.hasWon() == 0 && !game.isBlocked()) {
	        
	        System.out.print(game.getMaze().toString());
	        System.out.print("Enter next move: ");
	        command = (char) System.in.read();
	        game.move(command);
	        
	        do {
	        	ignore = (char) System.in.read();
	        } while(ignore != '\n');
	        
        }
	}

	@Override
	public void displayWinner() {
		
        int won = game.hasWon();        
        String message = null;
        
        if (game.isBlocked()) { // no winners
            message = "Game over! Both players are stuck.";
        } else {
            if (won == 1) {
                message = "Congratulations Player 1! You won the maze in " + 
                          game.getPlayerOne().getNumMoves() + " moves.";
            } else if (won == 2) { 
                message = "Congratulations Player 2! You won the maze in " + 
                          game.getPlayerTwo().getNumMoves() + " moves.";
            } else { // it's a tie
                message = "It's a tie!";
            }
        }
        System.out.print(game.getMaze().toString());
        System.out.println(message);
		
	}


}
