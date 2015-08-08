package mazegame;

/** One of the Sprite class. Mobile Banana is capable of moving to a new position. */
public class MobileBanana extends Banana implements Moveable{
	
	/** Constructs MobileBanana object.
	 * @param symbol character sets in MazeConstants.MOBILE_BANANA.
	 * @param row the row position of MobileBanana.
	 * @param col the column position of MobileBanana.
	 * @param value the score when MobileBanana is eaten by Monkey.
	 */
	public MobileBanana(char symbol, int row, int col, int value) {
		super(symbol, row, col, value);
	}

	@Override
	public void move(int row, int col) {
		this.row = row;
		this.column = col;
		
	}

}
