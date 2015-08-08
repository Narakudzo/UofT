package ui;

import java.awt.event.*;

/** A listener for the GUI window. */
public class GUIListener extends KeyAdapter {
  
    /** A GUI for the maze game. */
    private GUI window;

    /**
     * Creates a listener for the GUI window.
     * @param window the GUI to listen to
     */
    public GUIListener(GUI window) {
        this.window = window;
    }
  
    @Override
    public void keyPressed(KeyEvent event) {
    }
  
    @Override
    public void keyTyped(KeyEvent event) {
        char nextMove = event.getKeyChar();
        this.window.getGame().move(nextMove);
        this.window.updateLabels();
    }

    @Override
    public void keyReleased(KeyEvent event) {
    } 
}
