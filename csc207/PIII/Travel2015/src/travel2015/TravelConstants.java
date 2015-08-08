package travel2015;

public class TravelConstants {
	
	/** Path to data files */
	public static final String PATH = "/home/spencer/Desktop/csc207/group_0361/PIII/sampleTests/";
	
	/** Serialized flight database */
	public static final String FLIGHTDB = PATH + "flightDB.ser";
	
	/** Serialized itinerary database */
	public static final String ITINERARYDB = PATH + "itineraryDB.ser";
	
	/** Serialized client database */
	public static final String CLIENTDB = PATH + "clientDB.ser";
	
	/** Plain passwords.txt file */
	public static final String PASSWORDS = PATH + "passwords.txt";
	
	/** Client type specified in passwords.txt */
	public static final String CLIENTTYEPNAME = "client";
	
	/** Admin type specified in passwords.txt */
	public static final String ADMINTYPENAME = "admin";
	
	/** Duration limit between flights (for connection flights) */
	public static final int CONNECTIONLIMITHOUR = 6;

}
