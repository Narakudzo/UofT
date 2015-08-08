package mazegame;

/** One of the sprite objects of game.
 * When player eats Banana, the player gains the score specified.
 */
public class Banana extends Sprite {

	/** Banana's score value. */
	protected int value;

	/** Constructs Banana object.
	 * @param symbol Banana object to be shown on the game grid.
	 * @param row the current row position on the game grid.
	 * @param col the current column position on the game grid.
	 * @param value the score that a player gets when they get it.
	 */
	public Banana(char symbol, int row, int col, int value){
		super(symbol, row, col);
		this.value = value;
	}
	
	/** Getters method
	 * @return Banana's score value.
	 */
	public int getValue() {
		return this.value;
	}
	
}
