package mazegame;

/* Generic interface class */

/** Creates two dimensional array (game screen) for MazeGame
 * and responsible for the following operations:
 * 1. Add sprite object to each cell on the array.
 * 2. Retrieve the object from the cell.
 * 3. Compares two dimensional arrays. 
 */
public class ArrayGrid<T> implements Grid<T> {
	
	/** Number of rows in the grid. */
	private int numRows;
	
	/** Number of columns in the grid. */
	private int numCols;
	
	/** Two dimensional array to store sprite object. */
	private T[][] gameTable;
	
	/** Temporary String variable for passing toString() values. */
	private String gameArray;
	
	/** Constructs two dimensional array for sprite objects.
	 * @param numRows number of rows to be set on the array
	 * @param numCols number of columns to be set on the array */
	@SuppressWarnings("unchecked")
	public ArrayGrid(int numRows, int numCols){
		this.numRows = numRows;
		this.numCols = numCols;
		this.gameTable = (T[][]) new Object[numRows][numCols];
	}

	/** Adds sprite object to the cell specified on gameTable. */
	@Override
	public void setCell(int row, int col, T item) {
		this.gameTable[row][col] = item;
	}

	/** Retrieve sprite object from gameTable and returns it. */
	@Override
	public T getCell(int row, int col) {
		return this.gameTable[row][col];
	}

	@Override
	public int getNumRows() {
		return this.numRows;
	}

	@Override
	public int getNumCols() {
		return this.numCols;
	}

	@Override
	public boolean equals(Grid<T> other) {
		for(int i = 0; i < this.numRows; i++) {
			for(int j = 0; j < this.numCols; j++) {
				if(!(getCell(i, j).equals(other.getCell(i, j)))) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**	Returns each toString() in gameTable[][] arrays.
	 * @return multiple toString() from each T in gameTable[][].
	 */
	@Override
	public String toString() {
		this.gameArray = "";
		for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                this.gameArray += getCell(i, j).toString();
            }
            this.gameArray += "\n";
        }
		return this.gameArray;
	}

}
