package travel2015;

import java.io.Serializable;

public class Client extends User implements Serializable { 
	//private ArrayDB<Client> listofClient ;
	/**
	 * Creates a new Client with the , and Password password.
	 * @param lastName the lastname of Client
	 * @param firstName the firstname of Client
	 * @param email the email of Client
	 * @param address the address of Client
	 * @param creditCardNumber the creditcardnumber of Client
	 * @param expiryDate the epirydate of the creditcard of Client
	 * @param bookingHistory the bookinghistory of Client
	 * @param billingInfo the billing information of Client
	 */

	private static final long serialVersionUID = -6259870987045877144L;
	private String lastName;
	private String firstName;
	private String email;
	private String address;
	private String creditCardNumber;
	private String expiryDate;
	private ArrayDB<Flight> bookingHistory = new ArrayDB<Flight>();
	private String billingInfo;
	public Client(String lastName, String firstName, String email, String address,
			String creditCardNumber, String expiryDate) {
		super(email);
		this.address = address;
		this.creditCardNumber = creditCardNumber;
		this.email = email;
		this.expiryDate = expiryDate;
		this.lastName = lastName;
		this.firstName = firstName;
		this.bookingHistory = new ArrayDB<Flight>();
		this.billingInfo = "";
		
	}
	
	/**
	 * Return a String of the lastname of Client
	 * @param Client
	 * @return Returns a string representation of the Client lastname
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * edit the lastname of a Client
	 * @param lastName the lastname of a Client
	 */
	 
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Return a String of the firstname of Client
	 * @param Client
	 * @return Returns a string representation of the Client firstname
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * edit the firstname of a Client
	 * @param firstName the firstname of a Client
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Return a String of the email of Client
	 * @param Client
	 * @return Returns a string representation of the Client email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * edit the email of a Client
	 * @param email the email of a Client
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Return a String of the address of Client
	 * @param Client
	 * @return Returns a string representation of the Client address
	 */
	public String getAddress(){
		return address;
	}
	
	/**
	 * edit the address of a Client
	 * @param address the address of a Client
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Return the creditcardnumber of Client
	 * @param Client
	 * @return Returns int creditcardnumber of Client
	 */	
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	
	/**
	 * edit the creditcardnumber of a Client
	 * @param creditCardNumber the creditcardnumber of a Client
	 */
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	
	/**
	 * Return the expirydate of the creditcard of Client
	 * @param Client
	 * @return Returns int the expirydate of the creditcard of Client.
	 */	
	public String getExpriDate(){
		return expiryDate;
	}
	
	/**
	 * edit the expirydate of the creditcard
	 * @param expirydate the expirydate of the creditcard of a client
	 */		
	public void setExpriDate(String expirydate) {
		this.expiryDate = expirydate;
	}
	
	/**
	 * Return the bookinghistory of Client
	 * @param Client
	 * @return Returns list of the bookinghistory of Client.
	 */	
	public ArrayDB<Flight> getBookingHistory() {
		return bookingHistory;
	}
	
	/**
	 * add bookinghistory of Client
	 * @param Object Flight
	 */	
	public void addBookingHistory(Flight flight) {
		this.bookingHistory.addObject(flight);
	}
	
	/**
	 * Return the billinginfo of Client
	 * @param Client
	 * @return Returns string of the billinginfo of Client.
	 */	
	public String getBillingInfo(){
		return billingInfo;
	}
	
	/**
	 * edit billing information of Client
	 * @param String billingInfo the billinginfo of Client
	 */	
	public void editBillingInfo(String billinginfo) {
		this.billingInfo = billinginfo;
	}
	
	/**
	 * book a flight
	 * @param Object Flight
	 */	
	public void bookItinerary(Flight flight) {
		/** To be implemented in Phase 3 */
	}
	
	/**
	 * cancel a booking
	 * @param Object Flight
	 */	
	public void cancelItinerary(Flight flight) {
		/** To be implemented in Phase 3 */
	}
	
	/**
	 * edit personal information of Client
	 * @param Client
	 */	
	public void ediitPersonalInfo() {
		/** To be implemented in Phase 3 */
	}

}
