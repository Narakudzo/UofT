package e3soln;

import java.util.Observable;


/** A product in a store. **/
public class Product extends Observable {

	/** This Product's name */
	private String name;
	
	/** This Product's price */
	private double price;
	
	/** This Product's store */
	private String store;
	
    /**
     * Creates a {@link e3soln.Product} with the given name, price, and store.
     * @param name name of the new {@link e3soln.Product}
     * @param price price of the new {@link e3soln.Product}
     * @param store store of the new {@link e3soln.Product}
     */
    public Product(String name, double price, String store) {
    	this.name = name;
    	this.price = price;
    	this.store = store;
    }

    /**
     * Returns the name of this {@link e3soln.Product}.
     * @return the name of this {@link e3soln.Product}
     */
    public String getName() {
    	return this.name;
    }

    /**
     * Returns the price of this {@link e3soln.Product}.
     * @return the price of this {@link e3soln.Product} 
     */
    public double getPrice() {
    	return this.price;
    }

    /**
     * Returns the store of this {@link e3soln.Product}.
     * @return the store of this {@link e3soln.Product}
     */
    public String getStore() {
    	return this.store;
    }

    /**
     * Changes the price of this {@link e3soln.Product} to newPrice. All 
     * observers are notified, if the price is changed.
     * @param newPrice the new price of this {@link e3soln.Product}
     */
    public void changePrice(double newPrice) {
    	this.price = newPrice;
    	PriceChange pricechange = new PriceChange(this);
    	setChanged();
    	notifyObservers(pricechange);
    	
    }

    @Override
    public String toString() {
    	return "The price of " + this.name + " at " + this.store 
    			+ " is " + String.format("%.2f", this.price) + ".";
    }
}
