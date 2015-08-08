package travel2015;

import java.io.Serializable;

/**
 * The abstract class of the User of the program
 * This class is not really used in the application. 
 * The reason for having this class is to send notification to each client.
 */
public abstract class User implements Serializable {

	/**
	 * Serial version unique ID
	 */
	private static final long serialVersionUID = -5686849657087251875L;

	/**this is the username of the user, to differentiate users*/
	protected String username;
	
	/**this is the password of the user, to identify the user */
	protected String password;
	
	/**
	 * Creates a new User with the Username username, and Password password
	 * @param username the Username of the user
	 * @param password the Password of the user
	 */
	public User(String username, String password){
		this.username = username;
		this.password = password;			
	}


	/** Get username for verification.
	 * @return User's username.
	 */
	public String getUsername() {
		return username;
	}

	/** Set username when client changes its email.
	 * @param username new username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/** Get password for verification.
	 * @return password User's password.
	 */
	public String getPassword() {
		return password;
	}

	/** Set password when User changes it.
	 * @param password new password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
