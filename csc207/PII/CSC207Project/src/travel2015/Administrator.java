package travel2015;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Administrator class for User who is authorized to access Flight and Client data.
 */

public class Administrator extends User implements Serializable {
	
	/**
	 * Serial unique ID for Administrator.
	 */
	private static final long serialVersionUID = 6578124824187245283L;

	//private ArrayDB<Administrator> listofClient ;
	/**
	 * Creates a new Administrator with the Username username, and Password password.
	 * @param username the Username of Administrator
	 * @param password the Password Administrator
	 */
	public Administrator (String username, String password){
		super(username,password);
	}
	
	/**
	 * Return a String of the information about the Client, given the unique email of the Client.
	 * @param email the unique email of the Client
	 * @return Returns a string representation of the Client Information in order of lastname,firstname ...
	 */
	public String checkClientInfo(String email){
		try {
			for (Client temp: Console.getClientDB().getDB()) {
				if (email.equals(temp.getEmail())) {
					return temp.getLastName() + "," + temp.getFirstName() + "," + temp.getEmail() + "," + 
								temp.getAddress() + "," + temp.getCreditCardNumber() + "," + 
								temp.getExpriDate() + "\n";
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	
	}
	
	/**
	 * Return a List<Client> object of the information about the Client.
	 * @return Returns List<Client>
	 */
	public List<Client> checkClientInfo(){
		try {
			return Console.getClientDB().getDB();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Edit Client Information of the Client,given the unique email of the Client.
	 * @param email the unique email of the Client
	 * @param newClientInfo The list all the Client
	 */
	public void editClientInfo(String email, Client newClientInfo){
		/** TO BE implemented in Phase 3 */
	}
	
	/**
	 * Show Itinerary Information.
	 * @return List of list of flights.
	 */
	public List<List<Flight>> accessItineraryInfo(){
		try {
			return Console.getItineraryDB().getDB();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
