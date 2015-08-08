package travel2015;

import java.io.Serializable;
import java.util.List;

public class Client extends User implements Serializable { 
	//private ArrayDB<Client> listofClient ;
	/**The serialVersionUID of the Clients */
	private static final long serialVersionUID = -6259870987045877144L;
	/**The String representation of the lastName of the client*/
	private String lastName;
	/**The String representation of the firstName of the client*/
	private String firstName;
	/**The String representation of the email of the client*/
	private String email;
	/**The String representation of the of address the client*/
	private String address;
	/**The String representation of the creditCardNumber the client*/
	private String creditCardNumber;
	/**The String representation of the expiryDate of credit Card of the client*/
	private String expiryDate;
	/**The Array list representation of the bookingHistory of the client*/
	private ArrayDB<List<Flight>> bookingHistory;
	/**The String representation of the billingInfo the client*/
	private String billingInfo;
	/**
	 * Creates a new Client with the , and Password password.
	 * @param lastName the last name of Client
	 * @param firstName the first name of Client
	 * @param email the email of Client
	 * @param address the address of Client
	 * @param creditCardNumber the credit card number of Client
	 * @param expiryDate the epirydate of the credit card of Client
	 * @param bookingHistory the booking history of Client
	 * @param billingInfo the billing information of Client
	 */
	public Client(String lastName, String firstName, String email, String address,
			String creditCardNumber, String expiryDate) {
		super(email, lastName);
		this.address = address;
		this.creditCardNumber = creditCardNumber;
		this.email = email;
		this.expiryDate = expiryDate;
		this.lastName = lastName;
		this.firstName = firstName;
		this.bookingHistory = new ArrayDB<List<Flight>>();
		this.billingInfo = "";
		
	}
	
	/**
	 * Return a String of the last name of Client
	 * @param Client
	 * @return Returns a string representation of the Client last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Edit the last name of a Client
	 * @param lastName the last name of a Client
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
	 * Edit the first name of a Client
	 * @param firstName the first name of a Client
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
	 * Edit the email of a Client
	 * @param email the email of a Client
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Return a String of the address of Client
	 * @return Returns a string representation of the Client address
	 */
	public String getAddress(){
		return address;
	}
	
	/**
	 * Edit the address of a Client
	 * @param address the address of a Client
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Return the credit card number of Client
	 * @return Returns int credit card number of Client
	 */	
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	
	/**
	 * Edit the credit card number of a Client
	 * @param creditCardNumber the credit card number of a Client
	 */
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	
	/**
	 * Return the expirydate of the credit card of Client
	 * @return Returns int the expirydate of the credit card of Client.
	 */	
	public String getExpriDate(){
		return expiryDate;
	}
	
	/**
	 * edit the expirydate of the credit card
	 * @param expirydate the expirydate of the credit card of a client
	 */		
	public void setExpriDate(String expirydate) {
		this.expiryDate = expirydate;
	}
	
	/**
	 * Return the booking history of Client
	 * @return Returns list of the booking history of Client.
	 */	
	public ArrayDB<List<Flight>> getBookingHistory() {
		return bookingHistory;
	}
	
	/**
	 * add booking history of Client
	 * @param Object Flight the flight that is chosen to book
	 */	
	public void addBookingHistory(List<Flight> flight) {
		this.bookingHistory.addObject(flight);
	}
	/**
	 * delete booking history of Client
	 * @param num the index position of the book want to remove
	 */
	public void deleteBookingHistory(int num) {
		this.bookingHistory.removeObject(num);
	}
	/**
	 * Change the bookingHistory to a different bookingHistory
	 * @param bookingHistory
	 */
	public void setBookingHistory(ArrayDB<List<Flight>> bookingHistory) {
		this.bookingHistory = bookingHistory;
	}

	/**
	 * Return the billing information of Client
	 * @return Returns string of the billing information of Client.
	 */	
	public String getBillingInfo(){
		return billingInfo;
	}
	
	/**
	 * edit billing information of Client
	 * @param String billingInfo the billing information of Client
	 */	
	public void editBillingInfo(String billinginfo) {
		this.billingInfo = billinginfo;
	}
}
