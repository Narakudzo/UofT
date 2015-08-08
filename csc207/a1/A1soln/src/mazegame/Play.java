package mazegame;

import java.io.IOException;

import ui.GUI;
import ui.TextUI;
import ui.UI;

/** A class that initializes game to play */
public class Play {

	/** Opens game grid file and initialize GUI game or TextUI game. */
    public static void main(String[] args) throws IOException {
         
        MazeGame game = new MazeGame(MazeConstants.FILENAME);
        UI gameUI;
        
        if (MazeConstants.UI_TYPE.equals("text")) {
            gameUI = new TextUI(game);
        }
        else {
            gameUI = new GUI(game);
        }
        
        gameUI.launchGame(); 
        gameUI.displayWinner();
    }
}
