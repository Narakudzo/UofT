package e3soln;

import java.util.Observable;
import java.util.Observer;

public class PriceWatchWebsite extends Observable implements Observer {

	/** This PriceWatchWebsite's url */
	private String url;
    /**
     * Creates a new {@link e3soln.PriceWatchWebsite} with the given URL.
     * @param url the URL of the new {@link e3soln.PriceWatchWebsite}
     */
    public PriceWatchWebsite(String url) {
    	this.url = url;
    }

    /**
     * Returns the URL of this {@link e3soln.PriceWatchWebsite}.
     * @return the URL of this {@link e3soln.PriceWatchWebsite}
     */
    public String getUrl() {
    	return this.url;
    }

    /**
     * Prints a message about a price change.
     * Notifies all observers of the change in price.
     */
    @Override
    public void update(Observable o, Object arg) {
    	PriceChange pc = (PriceChange) arg;
    	System.out.println("You are subscribed to " + getUrl() +".\n"
    			+ "It was notified about a price change of "
    			+ pc.getProduct().getName()
    			+ " at " + pc.getProduct().getStore() + " to "
    			+ String.format("%.2f", pc.getProduct().getPrice()) + ".");
    	setChanged();
    	notifyObservers(pc);
    	
    }
}