package e3soln;

public class Main {

    public static void main(String[] args) {
        Product banana = new Product("banana", 0.59, "Loblaw");
        Product cereal = new Product("cereal", 7.49, "Target");

        Shopper jen = new Shopper("Jen Campbell");
        Shopper steve = new Shopper("Steve Engels");

        PriceWatchWebsite priceWatchRUs = new PriceWatchWebsite("www.pricewatchrus.com");

        banana.addObserver(steve);
        cereal.addObserver(steve);
        cereal.addObserver(jen);
        cereal.addObserver(priceWatchRUs);
        priceWatchRUs.addObserver(jen);

        banana.changePrice(0.55);
        cereal.changePrice(10 / 3.0);
        cereal.changePrice(15.42);    

        /* Output:
            Steve Engels was notified about a price change of banana at Loblaw to 0.55.
            You are subscribed to www.pricewatchrus.com.
            It was notified about a price change of cereal at Target to 3.33.
            Jen Campbell was notified about a price change of cereal at Target to 3.33.
            Jen Campbell was notified about a price change of cereal at Target to 3.33.
            Steve Engels was notified about a price change of cereal at Target to 3.33.
            You are subscribed to www.pricewatchrus.com.
            It was notified about a price change of cereal at Target to 15.42.
            Jen Campbell was notified about a price change of cereal at Target to 15.42.
            Jen Campbell was notified about a price change of cereal at Target to 15.42.
            Steve Engels was notified about a price change of cereal at Target to 15.42.
         */
     }
}
