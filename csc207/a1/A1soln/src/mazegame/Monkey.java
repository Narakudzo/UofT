package mazegame;

/** One of the sprite objects of game.
 * This is the player who moves around and chase Banana and MobileBanana.
 */
public class Monkey extends Sprite implements Moveable {
	
	/** Current score of Monkey. */
	private int score;
	
	/** How many moves Monkey made so far. */
	private int numMoves;

	/** Constructs Monkey object. Initial score and numMoves are zero.
	 * @param symbol the player1 or player2 character sets at MazeConstants.
	 * @param row the row position of Monkey.
	 * @param col the column position of Monkey.
	 */
	public Monkey(char symbol, int row, int col) {
		super(symbol, row, col);
		this.score = 0;
		this.numMoves = 0;
	}

	@Override
	public void move(int row, int col) {
		this.row = row;
		this.column = col;
		this.numMoves += 1;
	}
	
	/** Updates Monkey's score when the Monkey eats Banana or MobileBanana.
	 * @param score the value Monkey gets.
	 */
	public void eatBanana(int score) {
		this.score += score;
		
	}
	
	/** Getters for score instance.
	 * @return the current score of Monkey.
	 */
	public int getScore(){
		return this.score;
	}
	
	/** Getters for numMoves instance.
	 * @return number of moves made by Monkey.
	 */
	public int getNumMoves() {
		return this.numMoves;
	}

}
