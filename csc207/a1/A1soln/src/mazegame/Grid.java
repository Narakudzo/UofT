package mazegame;

/* Generic interface class */

/** Set up the framework of grid that stores sprite object. */
public interface Grid<T> {
	
	/** Sets T object to the cell specified.
	 * @param row cell's position at row.
	 * @param col cell's position at column.
	 * @param item the object to be stored in the cell.
	 */
	public void setCell(int row, int col, T item);
	
	/** Returns T object in the cell specified.
	 * @param row cell's position at row.
	 * @param col cell's position at column.
	 * @return T object stored in the cell.
	 */
	public T getCell(int row, int col);
	
	/** Getters method.
	 * @return number of rows of the grid.
	 */
	public int getNumRows();
	
	/** Getters method.
	 * @return number of columns of the grid.
	 */
	public int getNumCols();
	
	/** Compares one grid to the other.
	 * @param other same Grid<T> object to be compared.
	 * @return true if the other grid has the same
	 * matrix and each element in the cell matches,
	 * or other wise false.
	 */
	public boolean equals(Grid<T> other);
	
	/** Override toString function. */
	public String toString();

}
