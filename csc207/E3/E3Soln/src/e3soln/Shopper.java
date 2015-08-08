package e3soln;

import java.util.Observable;
import java.util.Observer;

public class Shopper implements Observer {
	
	/** This Shipper's name */
	private String name;

    /**
     * Creates a new {@link e3soln.Shopper} with the given name.
     * @param name name of the new {@link e3soln.Shopper}
     */
    public Shopper(String name) {
    	this.name = name;
    }

    /**
     * Returns the name of this {@link e3soln.Shopper}.
     * @return the name of this {@link e3soln.Shopper}
     */
    public String getName() {
    	return this.name;
    }

    /**
     * Prints a message about a price change.
     */
    @Override
    public void update(Observable o, Object arg) {
    	PriceChange pc = (PriceChange) arg;
    	System.out.println(this.name
    			+ " was notified about a price change of "
    			+ pc.getProduct().getName()
    			+ " at " + pc.getProduct().getStore() + " to "
    			+ String.format("%.2f", pc.getProduct().getPrice()) + ".");
    }
}
