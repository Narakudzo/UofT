package mazegame;

/** An abstract class that is the prototype of sprite. */
public abstract class Sprite {
	
	/** Sprite character. */
	protected char symbol;
	
	/** Sprite's row position. */
	protected int row;
	
	/** Sprite's column position. */
	protected int column;
	
	/** Constructs Sprite */
	public Sprite(char symbol, int row, int col) {	
		this.symbol = symbol;
		this.row = row;
		this.column = col;
	}

	/** Converts char symbol to String and returns the symbol.
	 * @return String type of char symbol.
	 */
	@Override
	public String toString() {
		return Character.toString(this.symbol);
		
	}
	
	/** Getters for symbol. */
	public char getSymbol() {
		return this.symbol;
	}

	/** Getters for row. */
	public int getRow() {
		return this.row;
	}

	/** Getters for column. */
	public int getColumn() {
		return this.column;
	}
}
