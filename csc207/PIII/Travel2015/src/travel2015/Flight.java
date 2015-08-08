package travel2015;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * A flight information of the Flight.
 */
public class Flight implements Serializable {
	
	private static final long serialVersionUID = -9138871063533828860L;
	/** Flight number like AC123 */
	private String flightnumber;
	 /** Departure date and time in YYYY-MM-DD HH:MM format */
	private String departuredatetime;
	 /** Departure date and time in YYYY-MM-DD format */
	private String departuredate;
	 /** Arrival date and time in YYYY-MM-DD HH:MM format */
	private String arrivaldatetime;
	 /** Arrival date and time in YYYY-MM-DD format */
	private String arrivaldate;
	/** The airline of the flight*/
	private String airline;
	/**The String representation of the origin of the flight*/
	private String origin;
	/**The String representation of the destination of the flight*/
	private String destination;
	/**The String representation of the price of the flight*/
	private String price;
	/**The int representation of the available seats of the flight*/
	private int availableseats;
	/**The long representation of the duration of the flight takes*/
	private long duration;
	/**The connectionList of the flight that it can connected to*/
	private ArrayDB<Flight> connectionList;
	/**The uniqueid of the flight*/
	private long uniqueid;
	
	/**
	 * Creates a new empty Flight.
	 */
		// constructor
	public Flight(String flightnumber, String departuredatetime, 
			String arrivaldatetime, String airline, String origin, 
			String destination, String price, String availableseats, long uniqueid) {
		this.flightnumber = flightnumber;
		this.departuredatetime = departuredatetime;
		this.arrivaldatetime = arrivaldatetime;
		this.airline = airline;
		this.origin = origin;
		this.destination = destination;
		this.price = price;
		this.availableseats = Integer.valueOf(availableseats);
		this.connectionList = new ArrayDB<Flight>();
		this.departuredate =  
				String.copyValueOf(this.departuredatetime.toCharArray(), 0, 10);
		this.arrivaldate = 
				String.copyValueOf(this.arrivaldatetime.toCharArray(), 0, 10);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long diff;
		try {
			diff = format.parse(arrivaldatetime).getTime() - 
					format.parse(departuredatetime).getTime();
			this.duration = diff / (60 * 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.uniqueid = uniqueid;
				
	}
	
	/**
	 * set the flight number as given.
	 * @param flightnumber the flightnumber of the flight.
	 */
	public void setFlightnumber(String flightnumber) {
		this.flightnumber = flightnumber;
	}
	
	/**
	 * set the departure date time number as given.
	 * @param departuredatetime the departuredatetime of the flight.
	 */
	public void setDeparturedatetime(String departuredatetime) {
		this.departuredatetime = departuredatetime;
	}
	
	/**
	 * set the departure date as given.
	 * @param departuredate the departure date of the flight.
	 */
	public void setDeparturedate(String departuredate) {
		this.departuredate = departuredate;
	}
	
	/**
	 * set the arrival date time as given.
	 * @param arrivaldatetime the arrival date time of the flight.
	 */
	public void setArrivaldatetime(String arrivaldatetime) {
		this.arrivaldatetime = arrivaldatetime;
	}
	
	/**
	 * set the arrival date as given.
	 * @param arrivaldate the arrivaldate of the flight.
	 */
	public void setArrivaldate(String arrivaldate) {
		this.arrivaldate = arrivaldate;
	}
	
	/**
	 * set the airline as given.
	 * @param airline the company of the flight.
	 */
	public void setAirline(String airline) {
		this.airline = airline;
	}
	
	/**
	 * set the origin as given.
	 * @param origine the origin of the flight.
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	/**
	 * set the destination as given.
	 * @param destination the destination of the flight.
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	/**
	 * set the price as given.
	 * @param price the price of the flight.
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	
	/**
	 * set the duration as given.
	 * @param duration the duration of the flight.
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	/**
	 * set the id as given.
	 * @param uniqueid the uniqueid of the flight.
	 */
	public void setUniqueid(long uniqueid) {
		this.uniqueid = uniqueid;
	}
	
	/**
	 * return the uniqueid.
	 */
	public long getUniqueid() {
		return uniqueid;
	}

	/**
	 * Returns the number of the flight.
	 * @return the number of the flight.
	 */
		// getFlightNumber
	public String getFlightNumber() {
		return this.flightnumber;
	}
	
	/**
	 * Returns the departure date and time.
	 * @return the departure date and time.
	 */
		// getDepartureDateTime
	public String getDepartureDateTime() {
		return this.departuredatetime;
	}
	
	/**
	 * Returns the departure date of the flight.
	 * @return the departure date of the flight.
	 */
		// getDepartureDate
	public String getDepartureDate() {
		return this.departuredate;
	}
	
	/**
	 * Returns the arrival date and time.
	 * @return the arrival date and time.
	 */
		// getArrivalDateTime
	public String getArrivalDateTime() {
		return this.arrivaldatetime;
	}
	
	/**
	 * Returns the arrival date of the flight.
	 * @return the arrival date of the flight.
	 */
		// getArrivalDate
	public String getArrivalDate() {
		return this.arrivaldate;
	}
	
	/**
	 * Returns the airline of the flight.
	 * @return the airline of the flight.
	 */
		// getAirLine
	public String getAirLine() {
		return this.airline;
	}
	
	/**
	 * Returns the origin of the flight.
	 * @return the origin of the flight.
	 */
		// getOrigin
	public String getOrigin() {
		return this.origin;
		
	}
	
	/**
	 * Returns the destination of the flight.
	 * @return the destination of the flight.
	 */
		// getDestination
	public String getDestination() {
		return this.destination;
	}
	
	/**
	 * Returns the price of the flight.
	 * @return the price of the flight.
	 */
		// getPrice
	public String getPrice() {
		return this.price;
	}
	
	/**
	 * Returns the total travel time of the flight trip.
	 * @return the total travel time of the flight trip.
	 */
		// getDuration
	public long getDuration() {
		return this.duration;
	}
	
	/**
	 * Sets the available seats of the flight.
	 * @param the new value of the available seats of the flight.
	 */
		// setAvailableSeats
	public void setAvailableSeats(int availableSeats) {
		this.availableseats = availableSeats;
	}
	
	/**
	 * Returns the available seats of the flight.
	 * @return the available seats of the flight.
	 */
		// getAvailableSests
	public int getAvailableSeats() {
		return availableseats;
	}
	
	/**
	 * Add the list of connecting flight to the connection list.
	 * @param newConnection the connection list add a new connection
	 */
		// addConnectionList
	public void addConnectionList(Flight newConnection) {
		this.connectionList.addObject(newConnection);
	}
	
	/**
	 * Returns the list of flight connection
	 * @return the list of flight connection
	 */
		// getConnectionList
	public ArrayDB<Flight> getConnectionList() {
		return this.connectionList;
	}

	/**
	 * Print Flight information.
	 */
	@Override
	public String toString() {
		return this.getFlightNumber() + "," + 
				this.getDepartureDateTime() + "," +
				this.getArrivalDateTime() + "," + 
				this.getAirLine() + "," + 
				this.getOrigin() + "," + 
				this.getDestination() + "," + 
				this.getPrice();
	}
}
