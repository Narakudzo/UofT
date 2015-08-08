package mazegame;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

/**
 * A class that represents the basic functionality of the maze game.
 * This class is responsible for performing the following operations:
 * 1. At creation, it initializes the instance variables used to store the
 *        current state of the game.
 * 2. When a move is specified, it checks if it is a legal move and makes the
 *        move if it is legal.
 * 3. It reports information about the current state of the game when asked.
 */
public class MazeGame {

    /** A random number generator to move the MobileBananas. */
    private Random random;
    
    /** The maze grid. */
    private Grid<Sprite> maze;

    /** The first player. */
    private Monkey player1;
    
    /** The second player. */
    private Monkey player2;

    /** The bananas to eat. */
    private List<Banana> bananas;
    
    /**
     * Creates a new MazeGame that corresponds to the maze in the file
     * named layoutFileName.
     * @param layoutFileName the path to the input maze file
     */
    public MazeGame(String layoutFileName) throws IOException {
        random = new Random();
        bananas = new ArrayList<>();
        
        int[] dimensions = getDimensions(layoutFileName);
        maze = new ArrayGrid<Sprite>(dimensions[0], dimensions[1]);
               
        Scanner sc = new Scanner(new File(layoutFileName));

        /* INITIALIZE THE GRID HERE */

        String nextLine = sc.nextLine();
        
        int numCols = nextLine.length();
        int numRows = 0;
        
        for(int i = 0; i < numCols; i++) {
        	setGrid(nextLine.charAt(i), numRows, i);
        }
        // find the number of rows
        while (sc.hasNext()) {
            numRows++;
            nextLine = sc.nextLine();
            for(int i = 0; i < numCols; i++) {
            	setGrid(nextLine.charAt(i), numRows, i);
            }
        }
        sc.close();

    }
    
    /**
     * Returns the dimensions of the maze in the file named layoutFileName.
     * @param layoutFileName the path of the input maze file
     * @return an array [numRows, numCols], where numRows is the number
     * of rows and numCols is the number of columns in the maze that
     * corresponds to the given input maze file
     * @throws IOException
     */    
    private int[] getDimensions(String layoutFileName) throws IOException {       
        
        Scanner sc = new Scanner(new File(layoutFileName));

        // find the number of columns
        String nextLine = sc.nextLine();
        int numCols = nextLine.length();

        int numRows = 1;

        // find the number of rows
        while (sc.hasNext()) {
            numRows++;
            nextLine = sc.nextLine();
        }

        sc.close();
        return new int[]{numRows, numCols};
    }
    
    /** Getters for maze instance. */
    public Grid<Sprite> getMaze() {
    	
    	return maze;
    }
    
    /** Getters for player1 instance. */
    public Monkey getPlayerOne() {
    	
    	return player1;
    }
    
    /** Getters for player2 instance. */
    public Monkey getPlayerTwo() {
    	
    	return player2;
    }
    
    /** Find how many rows in maze instance.
     * @return number of rows in maze.
     */
    public int getNumRows() {
    	
    	return maze.getNumRows();
    }
    /** Find how many columns in maze instance.
     * @return number of columns in maze.
     */
    public int getNumCols() {
    	
    	return maze.getNumCols();
    }
    
    /** Retrieves a selected Sprite object in maze.
     * @param i rows position in maze.
     * @param j columns position in maze.
     * @return Sprite object at rows and columns in maze.
     */
    public Sprite get(int i, int j) {
    	
    	return maze.getCell(i, j);
    }
    
    /** Controls how player1 and player2 move.
     * Depending on commands that are set in MazeConstants,
     * passes information "who tries to move where" to 
     * executeMonkey() method.
     * If the command is not found, this method only executes
     * randomBanana() method to make sure mobile banana moves
     * randomly.
     * @param nextMove several command set at MazeConstants.
     */
    public void move(char nextMove) {
    	
    	switch (nextMove) {
    	case MazeConstants.P1_LEFT:
    		executeMonkey(0, MazeConstants.LEFT, player1);
    		break;
    	case MazeConstants.P1_DOWN:
    		executeMonkey(MazeConstants.DOWN, 0, player1);
    		break;
    	case MazeConstants.P1_RIGHT:
    		executeMonkey(0, MazeConstants.RIGHT, player1);
    		break;
    	case MazeConstants.P1_UP:
    		executeMonkey(MazeConstants.UP, 0, player1);
    		break;
    	case MazeConstants.P2_LEFT:
    		executeMonkey(0, MazeConstants.LEFT, player2);
    		break;
    	case MazeConstants.P2_DOWN:
    		executeMonkey(MazeConstants.DOWN, 0, player2);
    		break;
    	case MazeConstants.P2_RIGHT:
    		executeMonkey(0, MazeConstants.RIGHT, player2);
    		break;
    	case MazeConstants.P2_UP:
    		executeMonkey(MazeConstants.UP, 0, player2);
    		break;
    	default:
    		randomBanana();
    		break;

    	}
    }
    
    /** Checks if the ongoing game is finished or not by
     * examining the number of bananas left in bananas.
     * @return 1 if player1 wins, 2 if player2 wins,
     * 3 if it's tie, and 0 if the game is still ongoing
     * (that means bananas is not empty yet).
     */
    public int hasWon() {
    	
    	if(bananas.isEmpty()) {
    		if (player1.getScore() > player2.getScore()) {
    			return 1;
    		}
    		else if (player1.getScore() < player2.getScore()) {
    			return 2;
    		}
    		else {
    			return 3;
    		}
    	}
    	else {
    		return 0;
    	}
    }
    
    /** Checks if player1 and player2 have no place to move anymore.
     * @return true if both players are stuck, false otherwise.
     */
    public boolean isBlocked() {
    	
    	if(stuckPlayer1() && stuckPlayer2()) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /** Helper method of MazeGame().
     * The method plays the role of creating each Sprite and
     * inserting the Sprite to a selected cell at row/col.
     * @param symbol Sprite symbol specified in MazeConstants.
     * @param row the row position of maze grid.
     * @param col the column position of maze grid.
     */
    private void setGrid(char symbol, int row, int col){
    	
        switch (symbol) {
        case MazeConstants.WALL:
        	maze.setCell(row, col,
        			new Wall(MazeConstants.WALL, row, col));
        	break;
        case MazeConstants.P1:
        	player1 = new Monkey(MazeConstants.P1, row, col);
        	maze.setCell(row, col, player1);
        	break;
        case MazeConstants.P2:
        	player2 = new Monkey(MazeConstants.P2, row, col);
        	maze.setCell(row, col, player2);
        	break;
        case MazeConstants.BANANA:
        	Banana newBanana = new Banana(MazeConstants.BANANA,
					row, col, MazeConstants.BANANA_SCORE);
        	maze.setCell(row, col, newBanana);
        	bananas.add(newBanana);
        	break;
        case MazeConstants.MOBILE_BANANA:
        	MobileBanana newMobileBanana = new MobileBanana(MazeConstants.MOBILE_BANANA,
					row, col, MazeConstants.MOBILE_BANANA_SCORE);
        	maze.setCell(row, col, newMobileBanana);
        	bananas.add(newMobileBanana);
        	break;
        default:
        	maze.setCell(row, col,
        			new UnvisitedHallway(MazeConstants.VACANT, row, col));
        	break;
        }
    }
    
    /** Helper method of move().
     * The method handles to move Monkey a new position.
     * If the next position is available, it updates Monkey's instances (row, columns and numMoves).
     * Also it overwrites the grid of new position to Monkey, overwrites the grid of previous Monkey
     * to VisitedHallway.
     * It also checks if Monkey ate banana. If so, it updates bananas list, and Monkey's score.
     * At last, it executes randomBanana() to move mobile banana randomly.
     * @param row the next row where Monkey wants to move to.
     * @param col the next column where Monkey wants to move to.
     * @param M Monkey's object, either player1 or player2.
     */
    private void executeMonkey(int row, int col, Monkey M) {
    	int curRow = M.getRow();
    	int curColumn = M.getColumn();
    	int nextRow = curRow + row;
    	int nextColumn = curColumn + col;
    	char nextSymbol = maze.getCell(nextRow, nextColumn).getSymbol();
    	if(nextSymbol == MazeConstants.VACANT) {
    		M.move(nextRow, nextColumn);
    		maze.setCell(nextRow, nextColumn, M);
    		maze.setCell(curRow, curColumn,
    				new VisitedHallway(MazeConstants.VISITED, curRow, curColumn));
    	}
    	else if(nextSymbol == MazeConstants.BANANA ||
    			nextSymbol == MazeConstants.MOBILE_BANANA){
    		int value = 0;
    		int counter = 0;
    		int remove = 0;
    		for(Banana tempBanana: bananas){
    			if(tempBanana.row == nextRow && tempBanana.column == nextColumn) {
    				value = tempBanana.value;
    				remove = counter;
    			}
    			counter++;
    		}
    		bananas.remove(remove);
    		M.eatBanana(value);
    		M.move(nextRow, nextColumn);
    		maze.setCell(nextRow, nextColumn, M);
    		maze.setCell(curRow, curColumn,
    				new VisitedHallway(MazeConstants.VISITED, curRow, curColumn));
    	}
    	randomBanana();
    }
    
    /** Be responsible for moving mobile bananas randomly.
     * Reads bananas list, and if its symbol is mobile banana,
     * it moves mobile banana to a new possible position available.
     */
    private void randomBanana() {

		for(Banana tempBanana: bananas){
			if(tempBanana.symbol == MazeConstants.MOBILE_BANANA) {
				int i[] = {-1, 0, 1};
				int curRow = tempBanana.row;
				int curColumn = tempBanana.column;
				int nextRow =  curRow + i[random.nextInt(i.length)];
				int nextColumn = curColumn;
				if(nextRow == curRow) {
					nextColumn += i[random.nextInt(i.length)];
				}
				char nextSymbol = maze.getCell(nextRow, nextColumn).getSymbol();
		    	if(nextSymbol == MazeConstants.VACANT) {
		    		((MobileBanana) tempBanana).move(nextRow, nextColumn);
		    		maze.setCell(nextRow, nextColumn, (MobileBanana) tempBanana);
		    		maze.setCell(curRow, curColumn,
		    				new UnvisitedHallway(MazeConstants.VACANT,
		    						curRow, curColumn));
		    	}
			}
		}
		
    }
    
    /** Helper method of isBlocked().
     * Checks all four cells around the player1's position,
     * and if the symbols are not among VACANT, MOBILE_BANANA or BANANA,
     * it returns true.
     * @return true if player1 is stuck, false otherwise.
     */
	private boolean stuckPlayer1() {
		
		char left = maze.getCell(player1.getRow(),
				player1.getColumn()  + MazeConstants.LEFT).getSymbol();
		
		char right = maze.getCell(player1.getRow(),
				player1.getColumn()  + MazeConstants.RIGHT).getSymbol();
		
		char down = maze.getCell(player1.getRow() + MazeConstants.DOWN,
				player1.getColumn()).getSymbol();
		
		char up = maze.getCell(player1.getRow() + MazeConstants.UP,
				player1.getColumn()).getSymbol();
		
		return left != MazeConstants.VACANT && left != MazeConstants.BANANA &&
				left != MazeConstants.MOBILE_BANANA &&
				right != MazeConstants.VACANT && right != MazeConstants.BANANA &&
				right != MazeConstants.MOBILE_BANANA &&
				down != MazeConstants.VACANT && down != MazeConstants.BANANA &&
				down != MazeConstants.MOBILE_BANANA &&
				up != MazeConstants.VACANT && up != MazeConstants.BANANA &&
				up != MazeConstants.MOBILE_BANANA;
		
	}
	
    /** Helper method of isBlocked().
     * Checks all four cells around the player2's position,
     * and if the symbols are not among VACANT, MOBILE_BANANA or BANANA,
     * it returns true.
     * @return true if player2 is stuck, false otherwise.
     */
	private boolean stuckPlayer2() {
		
		char left = maze.getCell(player2.getRow(),
				player2.getColumn()  + MazeConstants.LEFT).getSymbol();
		
		char right = maze.getCell(player2.getRow(),
				player2.getColumn()  + MazeConstants.RIGHT).getSymbol();
		
		char down = maze.getCell(player2.getRow() + MazeConstants.DOWN,
				player2.getColumn()).getSymbol();
		
		char up = maze.getCell(player2.getRow() + MazeConstants.UP,
				player2.getColumn()).getSymbol();
		
		return left != MazeConstants.VACANT && left != MazeConstants.BANANA &&
				left != MazeConstants.MOBILE_BANANA &&
				right != MazeConstants.VACANT && right != MazeConstants.BANANA &&
				right != MazeConstants.MOBILE_BANANA &&
				down != MazeConstants.VACANT && down != MazeConstants.BANANA &&
				down != MazeConstants.MOBILE_BANANA &&
				up != MazeConstants.VACANT && up != MazeConstants.BANANA &&
				up != MazeConstants.MOBILE_BANANA;
		
	}

}
