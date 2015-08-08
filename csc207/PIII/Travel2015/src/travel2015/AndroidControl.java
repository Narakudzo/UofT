package travel2015;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
/** An Android Control class used for process the csv file */
public class AndroidControl {
	/**
     * Return true if the username and password matches the information
     * in the csv file, return false otherwise.
     * @param username the username of a client/administrator
     * @param password the password of a client/administrator 
     * @param file the file of client/administrator information with
     * lines in the format: 
     * identification, username, password.
     */
	public static boolean Login (String username, String password, File file) {
		
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(file.getPath()));
		} catch (FileNotFoundException e) {
			return false;
		}
		String[] csvData;
		while (scanner.hasNextLine()) {
		    csvData = scanner.nextLine().split(",");
		    if (csvData.length != 3) { break; }
		    if (username.equals(csvData[1]) && password.equals(csvData[2])) {
		    	scanner.close();
		    	return true;
		    }

		}
		scanner.close();
		return false;
	}
	
	/**
     * Return the identification of a user, either client or administrator.
     * @param username the username of a client/administrator
     * @param password the password of a client/administrator 
     * @param file the file of client/administrator information with
     * lines in the format: 
     * identification ,username, password.
     */
	public static String getLoginType (String username, String password, File file) {
		
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(file.getPath()));
		} catch (FileNotFoundException e) {
			return null;
		}
		String[] csvData;
		while (scanner.hasNextLine()) {
		    csvData = scanner.nextLine().split(",");
		    if (csvData.length != 3) { break; }
		    if (username.equals(csvData[1]) && password.equals(csvData[2])) {
		    	scanner.close();
		    	return csvData[0];
		    }

		}
		scanner.close();
		return null;
	}
	
	/**
     * Change the login password of a client/administrator.
     * @param username the username of a client/administrator
     * @param password the password of a client/administrator 
     * @param file the file of client/administrator information with
     * lines in the format: 
     * identification ,username, password.
     */
	public static void setPassword (String username, String password, File file) throws IOException {
		
		Scanner scanner = new Scanner(new FileInputStream(file.getPath()));
		String[] csvData;
		String newPasswords = "";
		while (scanner.hasNextLine()) {
		    csvData = scanner.nextLine().split(",");
		    if (csvData.length != 3) { break; }
		    if (username.equals(csvData[1])) {
		    	newPasswords += csvData[0] + "," + csvData[1] + "," + password + "\n";
		    } else {
		    	newPasswords += csvData[0] + "," + csvData[1] + "," + csvData[2] + "\n";
		    }
		}
		scanner.close();
		FileWriter writer = new FileWriter(file.getPath(), false);
    	writer.write(newPasswords);
		writer.close();
		
	}
	
	/**
     * Change the login uername of a client/administrator.
     * @param oldusername the oldusername of a client/administrator
     * @param newusername the newusername of a client/administrator 
     * @param file the file of client/administrator information with
     * lines in the format: 
     * identification ,username, password.
     */
	public static void setUsername (String oldusername, String newusername, File file) throws IOException {
		
		Scanner scanner = new Scanner(new FileInputStream(file.getPath()));
		String[] csvData;
		String newPasswords = "";
		while (scanner.hasNextLine()) {
		    csvData = scanner.nextLine().split(",");
		    if (csvData.length != 3) { break; }
		    if (oldusername.equals(csvData[1])) {
		    	newPasswords += csvData[0] + "," + newusername + "," + csvData[2] + "\n";
		    } else {
		    	newPasswords += csvData[0] + "," + csvData[1] + "," + csvData[2] + "\n";
		    }
		}
		scanner.close();
		FileWriter writer = new FileWriter(file.getPath(), false);
    	writer.write(newPasswords);
		writer.close();
	}

}
