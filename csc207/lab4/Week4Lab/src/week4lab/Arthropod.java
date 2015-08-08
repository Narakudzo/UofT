package week4lab;

import java.util.Random;

public class Arthropod extends Organism {
	
	private int numLegs;
	
	public Arthropod(String name, int xCoord, int yCoord, int speed, 
			String direction, int numLegs) {
		
		super(name, xCoord, yCoord, speed, direction);
		this.numLegs = numLegs;
		
	}

	public int getNumLegs() {
		return numLegs;
	}

	public void setNumLegs(int numLegs) {
		this.numLegs = numLegs;
	}

	@Override
	public String toString() {
		return super.toString() + "\nIt has " + this.numLegs + " legs.";
	}
	
	@Override
    public void move() {
		Random rn = new Random();
		int randomNum =  rn.nextInt(VALID_DIRECTIONS.length);
    	direction = VALID_DIRECTIONS[randomNum];
        super.move();
    }
}
