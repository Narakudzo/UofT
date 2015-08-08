package travel2015;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/** A Console used for processing files and saving data */
public class Console {

	static long current= System.currentTimeMillis();
	private static synchronized long getUniqueID(){
		return current++;
	}
	
	/** 
	 * Read client data from the path and create Serialized data.
	 * @param path the path of the file which contains the client info.
	 * @param saveclientdb
	 * @param clienttype the identification of client
	 * @param passfile the path of the file witch contains the information
	 * of client and administrator.
	 * @param admintypethe identification of administrator.
	 * @throws IOException
	 */
	public static void buildNewClientDB(String path, String saveclientdb, 
			String clienttype, String passfile, String admintype) throws IOException {
		
		/** Initiate new ArrayDB to store Client object */
		ArrayDB<Client> newClientDB = new ArrayDB<Client>();
		List<String[]> tempPassDB = new ArrayList<String[]>();
		Client newClient;
		Scanner scanner = new Scanner(new FileInputStream(path));
		String[] csvData;
		while (scanner.hasNextLine()) {
		    csvData = scanner.nextLine().split(",");
		    if (csvData.length == 6) {
		    	//create new client object and add them to ArrayDB
		    	newClient = new Client(csvData[0], csvData[1], csvData[2], csvData[3], csvData[4], csvData[5]);
		    	newClientDB.addObject(newClient);
		    	String[] tempUserInfo = new String[] {clienttype, csvData[2], csvData[0]};
		    	tempPassDB.add(tempUserInfo);
		    }
		}
		scanner.close();
		
		Scanner scanner2 = new Scanner(new FileInputStream(passfile));
		String[] csvData2;
		while (scanner2.hasNextLine()) {
		    csvData2 = scanner2.nextLine().split(",");
		    //store the administrators
		    if (csvData2.length == 3) {
		    	if (csvData2[0].equals(admintype)) {
		    		String[] tempUserInfo = new String[] {csvData2[0], csvData2[1], csvData2[2]};
			    	tempPassDB.add(tempUserInfo);
		    	}
		    }
		}
		scanner2.close();
		
		FileWriter writer = new FileWriter(passfile, false);
		for (String[] passinfo: tempPassDB) {
            writer.write(passinfo[0] + "," + passinfo[1] + "," + passinfo[2]);
            writer.write("\n");
		}
		writer.close();

        OutputStream file = new FileOutputStream(saveclientdb);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(newClientDB);
        output.close();
	}
	
	/** 
	 * Save changes of a client and write it to the file.
	 * @param myself a client object.
	 * @param email the email of a client.
	 * @param clientsave the database of the clients.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void reBuildClientDB(Client myself, String email, String clientsave) throws ClassNotFoundException, IOException {
		ArrayDB<Client> newClientDB = new ArrayDB<Client>();
		List<Client> currentClientDB = getClientDB(clientsave).getDB();
		
		for (Client tempClient: currentClientDB) {
			if (tempClient.getEmail().equals(email)) {
				newClientDB.addObject(myself);
			} else {
				newClientDB.addObject(tempClient);
			}
		}
		
		OutputStream file = new FileOutputStream(clientsave);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(newClientDB);
        output.close();
	}

	/**
	 * Read flight data from the path and create Serialized data
	 * Build Flight object and save them to ArrayDB.
	 * @param path the path to the file which contains the flight info.
	 * @param saveflight the database of the flight objects .
	 * @param saveitineraries the database of the itineraries.
	 * @throws IOException
	 */
	public static void buildNewFlightDB(String path, String saveflight, String saveitineraries) throws IOException {
		
		/** Initiate new ArrayDB to store Flight and Itinerary object */
		ArrayDB<Flight> newFlightDB = new ArrayDB<Flight>();
		ArrayDB<List<Flight>> newItineraryDB = new ArrayDB<List<Flight>>();
		List<Flight> tempFlightList;
		Scanner scanner = new Scanner(new FileInputStream(path));
		String[] csvData;
		while (scanner.hasNextLine()) {
		    csvData = scanner.nextLine().split(",");
		    //build new flight object and save them to ArrayDB.
		    if (csvData.length == 8) {
		    	Flight newFlight = new Flight(csvData[0], csvData[1], csvData[2], csvData[3], 
		    			csvData[4], csvData[5], csvData[6], csvData[7], getUniqueID());
		    
				/** Add possible connection flights. */
			    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				for (Flight temp: newFlightDB.getDB()) {
					if (temp.getOrigin().equals(newFlight.getDestination()) && !temp.getDestination().equals(newFlight.getOrigin())) {
						long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
						try {
							diff = format.parse(temp.getDepartureDateTime()).getTime() -
									format.parse(newFlight.getArrivalDateTime()).getTime();
							diff = diff / (60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (diff <= TravelConstants.CONNECTIONLIMITHOUR && diff > 0) {
							newFlight.addConnectionList(temp);
							tempFlightList = new ArrayList<Flight>();
							tempFlightList.add(newFlight);
							tempFlightList.add(temp);
							newItineraryDB.addObject(tempFlightList);
						}
					}
					if (temp.getDestination().equals(newFlight.getOrigin()) && !temp.getOrigin().equals(newFlight.getDestination())) {
						long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
						try {
							diff = format.parse(newFlight.getDepartureDateTime()).getTime() - 
									format.parse(temp.getArrivalDateTime()).getTime();
							diff = diff / (60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (diff <= TravelConstants.CONNECTIONLIMITHOUR && diff > 0) {
							temp.addConnectionList(newFlight);
							tempFlightList = new ArrayList<Flight>();
							tempFlightList.add(temp);
							tempFlightList.add(newFlight);
							newItineraryDB.addObject(tempFlightList);
						}
					}
				}
				
				/** Searching for possible itinerary */
				List<List<Flight>> tempItineraryDB = new ArrayList<List<Flight>>();
				tempItineraryDB.addAll(newItineraryDB.getDB());
				for (List<Flight> templist: tempItineraryDB) {
					int firstCheck = 0;
					if (templist.get(0).getOrigin().equals(newFlight.getDestination())) {
						for (Flight checkFlight: templist) {
							if (checkFlight.getDestination().equals(newFlight.getOrigin())) {
								firstCheck++;
							}
						}
						if (firstCheck == 0) {
							long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
							try {
								diff = format.parse(templist.get(0).getDepartureDateTime()).getTime() -
										format.parse(newFlight.getArrivalDateTime()).getTime();
								diff = diff / (60 * 60 * 1000);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							if (diff <= TravelConstants.CONNECTIONLIMITHOUR && diff > 0) {
								tempFlightList = new ArrayList<Flight>();
								tempFlightList.add(newFlight);
								for (Flight tempflight: templist) {
									tempFlightList.add(tempflight);
								}
								newItineraryDB.addObject(tempFlightList);
							}
						}
					}
					int secondCheck = 0;
					if (templist.get(templist.size() - 1).getDestination().equals(newFlight.getOrigin())) {
						for (Flight checkFlight: templist) {
							if (checkFlight.getOrigin().equals(newFlight.getDestination())) {
								secondCheck++;
							}
						}
						if (secondCheck == 0) {
							long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
							try {
								diff = format.parse(newFlight.getDepartureDateTime()).getTime() - 
										format.parse(templist.get(templist.size() - 1).getArrivalDateTime()).getTime();
								diff = diff / (60 * 60 * 1000);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							if (diff <= TravelConstants.CONNECTIONLIMITHOUR && diff > 0) {
								tempFlightList = new ArrayList<Flight>();
								for (Flight tempflight: templist) {
									tempFlightList.add(tempflight);
								}
								tempFlightList.add(newFlight);
								newItineraryDB.addObject(tempFlightList);
							}
						}
					}
				}
				newFlightDB.addObject(newFlight);
		    }
		}
		scanner.close();
		
        OutputStream file = new FileOutputStream(saveflight);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(newFlightDB);
        output.close();
        
        OutputStream file2 = new FileOutputStream(saveitineraries);
        OutputStream buffer2 = new BufferedOutputStream(file2);
        ObjectOutput output2 = new ObjectOutputStream(buffer2);

        output2.writeObject(newItineraryDB);
        output2.close();
	}
	
	/**
	 * Change the available seats on a flight and save the changed flight.
	 * @param flight the flight object.
	 * @param saveflight the database of the flight objects .
	 * @param saveitineraries the database of the itineraries.
	 * @param updateseats the new number of seats on the flight.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void reBuildFlightDB(Flight flight, String saveflight, String saveitineraries, int updateseats) throws ClassNotFoundException, IOException {
		List<Flight> currentFlightList = getFlightDB(saveflight).getDB();
		ArrayDB<Flight> newFlightDB = new ArrayDB<Flight>();
		for (Flight tempFlight: currentFlightList) {
			if (tempFlight.getUniqueid() == flight.getUniqueid()) {
				int tempAvailableSeats = tempFlight.getAvailableSeats();
				tempFlight.setAvailableSeats(tempAvailableSeats + updateseats);
				newFlightDB.addObject(tempFlight);
			} else {
				newFlightDB.addObject(tempFlight);
			}
		}
		
		OutputStream file = new FileOutputStream(saveflight);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(newFlightDB);
        output.close();
        
        reBuildItinerariesDB(saveitineraries, saveflight);
		
	}
	
	/**
	 * Updates flight in the database and save the changes.
	 * @param flight the flight object.
	 * @param saveflight the database of the flight objects .
	 * @param saveitineraries the database of the itineraries.
	 * @param saveclient the database of clients.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void reBuildFlightDB2(Flight flight, String saveflight, String saveitineraries, String saveclient) throws ClassNotFoundException, IOException {
		List<Flight> currentFlightList = getFlightDB(saveflight).getDB();
		ArrayDB<Flight> newFlightDB = new ArrayDB<Flight>();
		for (Flight tempFlight: currentFlightList) {
			if (tempFlight.getUniqueid() == flight.getUniqueid()) {
				newFlightDB.addObject(flight);
			} else {
				newFlightDB.addObject(tempFlight);
			}
		}
		
		OutputStream file = new FileOutputStream(saveflight);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(newFlightDB);
        output.close();
        
        reBuildItinerariesDB(saveitineraries, saveflight);
        
        ArrayDB<Client> newClientDB = new ArrayDB<Client>();
		List<Client> currentClientDB = getClientDB(saveclient).getDB();
		
		for (Client tempClient: currentClientDB) {
			ArrayDB<List<Flight>> tempFlights = tempClient.getBookingHistory();
			ArrayDB<List<Flight>> newFlights = new ArrayDB<List<Flight>>();
			for (List<Flight> tempLists: tempFlights.getDB()) {
				List<Flight> newLists = new ArrayList<Flight>();
				for (Flight tempFlight: tempLists) {
					if (tempFlight.getUniqueid() == flight.getUniqueid()) {
						newLists.add(flight);
					} else {
						newLists.add(tempFlight);
					}
				}
				newFlights.addObject(newLists);
			}
			tempClient.setBookingHistory(newFlights);
			newClientDB.addObject(tempClient);
		}
		
		OutputStream file2 = new FileOutputStream(saveclient);
        OutputStream buffer2 = new BufferedOutputStream(file2);
        ObjectOutput output2 = new ObjectOutputStream(buffer2);

        output2.writeObject(newClientDB);
        output2.close();
		
	}
	
	/**
	 * Make possible flights for each itinerary.
	 * @param saveflight the database of the flight objects .
	 * @param saveitineraries the database of the itineraries.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void reBuildItinerariesDB(String saveitineraries, String saveflight) throws IOException, ClassNotFoundException {

		ArrayList<Flight> myFlightList = (ArrayList<Flight>) getFlightDB(saveflight).getDB();
		ArrayDB<Flight> newFlightDB = new ArrayDB<Flight>();
		ArrayDB<List<Flight>> newItineraryDB = new ArrayDB<List<Flight>>();
		List<Flight> tempFlightList;
		for (Flight newFlight: myFlightList) {
		    if (newFlight.getAvailableSeats() > 0) {
		    
				/** Add possible connection flights. */
			    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				for (Flight temp: newFlightDB.getDB()) {
					if (temp.getOrigin().equals(newFlight.getDestination()) && !temp.getDestination().equals(newFlight.getOrigin())) {
						long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
						try {
							diff = format.parse(temp.getDepartureDateTime()).getTime() -
									format.parse(newFlight.getArrivalDateTime()).getTime();
							diff = diff / (60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (diff <= TravelConstants.CONNECTIONLIMITHOUR && diff > 0) {
							newFlight.addConnectionList(temp);
							tempFlightList = new ArrayList<Flight>();
							tempFlightList.add(newFlight);
							tempFlightList.add(temp);
							newItineraryDB.addObject(tempFlightList);
						}
					}
					if (temp.getDestination().equals(newFlight.getOrigin()) && !temp.getOrigin().equals(newFlight.getDestination())) {
						long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
						try {
							diff = format.parse(newFlight.getDepartureDateTime()).getTime() - 
									format.parse(temp.getArrivalDateTime()).getTime();
							diff = diff / (60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (diff <= TravelConstants.CONNECTIONLIMITHOUR && diff > 0) {
							temp.addConnectionList(newFlight);
							tempFlightList = new ArrayList<Flight>();
							tempFlightList.add(temp);
							tempFlightList.add(newFlight);
							newItineraryDB.addObject(tempFlightList);
						}
					}
				}
				
				/** Searching for possible itinerary */
				List<List<Flight>> tempItineraryDB = new ArrayList<List<Flight>>();
				tempItineraryDB.addAll(newItineraryDB.getDB());
				for (List<Flight> templist: tempItineraryDB) {
					int firstCheck = 0;
					if (templist.get(0).getOrigin().equals(newFlight.getDestination())) {
						for (Flight checkFlight: templist) {
							if (checkFlight.getDestination().equals(newFlight.getOrigin())) {
								firstCheck++;
							}
						}
						if (firstCheck == 0) {
							long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
							try {
								diff = format.parse(templist.get(0).getDepartureDateTime()).getTime() -
										format.parse(newFlight.getArrivalDateTime()).getTime();
								diff = diff / (60 * 60 * 1000);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							if (diff <= TravelConstants.CONNECTIONLIMITHOUR && diff > 0) {
								tempFlightList = new ArrayList<Flight>();
								tempFlightList.add(newFlight);
								for (Flight tempflight: templist) {
									tempFlightList.add(tempflight);
								}
								newItineraryDB.addObject(tempFlightList);
							}
						}
					}
					int secondCheck = 0;
					if (templist.get(templist.size() - 1).getDestination().equals(newFlight.getOrigin())) {
						for (Flight checkFlight: templist) {
							if (checkFlight.getOrigin().equals(newFlight.getDestination())) {
								secondCheck++;
							}
						}
						if (secondCheck == 0) {
							long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
							try {
								diff = format.parse(newFlight.getDepartureDateTime()).getTime() - 
										format.parse(templist.get(templist.size() - 1).getArrivalDateTime()).getTime();
								diff = diff / (60 * 60 * 1000);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							if (diff <= TravelConstants.CONNECTIONLIMITHOUR && diff > 0) {
								tempFlightList = new ArrayList<Flight>();
								for (Flight tempflight: templist) {
									tempFlightList.add(tempflight);
								}
								tempFlightList.add(newFlight);
								newItineraryDB.addObject(tempFlightList);
							}
						}
					}
				}
				newFlightDB.addObject(newFlight);
		    }
		}
        
        OutputStream file2 = new FileOutputStream(saveitineraries);
        OutputStream buffer2 = new BufferedOutputStream(file2);
        ObjectOutput output2 = new ObjectOutputStream(buffer2);

        output2.writeObject(newItineraryDB);
        output2.close();
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * return the ArrayDB of clients.
	 * @param path the path to the file which contains the clients info.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static ArrayDB<Client> getClientDB(String path) throws ClassNotFoundException, IOException {

        InputStream file = new FileInputStream(path);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        // Deserialize the ArrayDB
        ArrayDB<Client> clientDB = (ArrayDB<Client>) input.readObject();
        input.close();

		return clientDB;
	}
	/**
	 * return the client that matches the given email, return null otherwise.
	 * @param path the path to the file which contains the clients info.
	 * @param email the email of the client.
	 * @throws IOException
	 */
	public static Client getClient(String email, String path) throws ClassNotFoundException, IOException {
		List<Client> clientList = getClientDB(path).getDB();
		for (Client tempClient: clientList) {
			if (tempClient.getEmail().equals(email)) {
				return tempClient;
			}
		}
		return null;
	}
	
	/** 
	 * Read Serialized ArrayDB
	 * return the ArrayDB of flights.
	 * @param path the path to the file which contains the flight info.
	 * @throws ClassNotFoundException.
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayDB<Flight> getFlightDB(String path) throws ClassNotFoundException, IOException {

        InputStream file = new FileInputStream(path);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        // Deserialize the ArrayDB
        ArrayDB<Flight> flightDB = (ArrayDB<Flight>) input.readObject();
        input.close();

		return flightDB;
	}
	


	/** 
	 * Read Serialized ArrayDB
	 * return the ArrayDB of list of flight.
	 * @param path the path to the itinerary database.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayDB<List<Flight>> getItineraryDB(String path) throws ClassNotFoundException, IOException {

        InputStream file = new FileInputStream(path);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        // Deserialize the ArrayDB
        ArrayDB<List<Flight>> itineraryDB = (ArrayDB<List<Flight>>) input.readObject();
        input.close();

		return itineraryDB;
	}
	
	
	/**
	 * Returns all flights that depart from origin and arrive at destination on that date
	 * @param date the departure date.
	 * @param origin the origin.
	 * @param destination the destination.
	 * @param path the path to the flight database.
	 * @throws IOException
	 */
	public static ArrayList<Flight> searchFlight(String date, String origin, String destination, String path) throws ClassNotFoundException, IOException {
		ArrayList<Flight> templist = new ArrayList<Flight>();
		for (Flight temp: getFlightDB(path).getDB()) {
			if (temp.getDepartureDate().equals(date) && temp.getOrigin().toLowerCase().contains(origin.toLowerCase()) && 
					temp.getDestination().toLowerCase().contains(destination.toLowerCase())) {
				templist.add(temp);
			}
		}
		return templist;
	}

	/**
	 * return the list of flights that matches the keywors.
	 * @param date
	 * @param origin
	 * @param destination
	 * @param path
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static List<Flight> searchFlightByKeywords(String[] keywords, String path) throws ClassNotFoundException, IOException {
		List<Flight> templist = new ArrayList<Flight>();
		for (Flight temp: getFlightDB(path).getDB()) {
			int searchresult = 0;
			for (String key: keywords) {
				if (temp.getAirLine().toLowerCase().contains(key.toLowerCase()) || 
						temp.getArrivalDateTime().toLowerCase().contains(key.toLowerCase()) ||
						temp.getDepartureDateTime().toLowerCase().contains(key.toLowerCase()) || 
						temp.getDestination().toLowerCase().contains(key.toLowerCase()) ||
						temp.getFlightNumber().toLowerCase().contains(key.toLowerCase()) || 
						temp.getOrigin().toLowerCase().contains(key.toLowerCase())) {
					searchresult++;
				}
			}
			if(searchresult == keywords.length) {
				templist.add(temp);
			}
		}
		return templist;
	}
	/**
	 * 
	 * @param uniqueid
	 * @param path
	 * @return true if the given flight exit, false otherwise.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static boolean flightIsExist(String uniqueid, String path) throws ClassNotFoundException, IOException {
		for (Flight temp: getFlightDB(path).getDB()) {
			if (String.valueOf(temp.getUniqueid()).equals(uniqueid)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param uniqueid
	 * @param path
	 * @return the flight object of the uniqueid.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Flight getFlightbyID(String uniqueid, String path) throws ClassNotFoundException, IOException {
		for (Flight temp: getFlightDB(path).getDB()) {
			if (String.valueOf(temp.getUniqueid()).equals(uniqueid)) {
				return temp;
			}
		}
		return null;
	}
	

	/**
	 * search Itineraries in the list of Itineraries that fit the date, origin, destination, flightpath ,and itinerariespath.
	 * NOT USED IN ANDROID, Only for Driver.java
	 * @param date the date of the flight
	 * @param origin the origin of the flights, where start off.
	 * @param destination the last destination of the flight, where end up last.
	 * @param flightpath the many flights 
	 * @param itinerariespath the many itineraries
	 * @return sorted list possible itineraries
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static ArrayList<List<Flight>> searchItineraries(String date, String origin, String destination, 
			String flightpath, String itinerariespath) throws ClassNotFoundException, IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<List<Flight>> sortList = new ArrayList<List<Flight>>();
		List<Double> sortPrice = new ArrayList<Double>();
		List<Long> sortDuration = new ArrayList<Long>();
		List<Flight> newTempList;
		
		for (Flight temp: getFlightDB(flightpath).getDB()) {
			if (temp.getDepartureDate().equals(date) && temp.getOrigin().toLowerCase().contains(origin.toLowerCase()) && 
					temp.getDestination().toLowerCase().contains(destination.toLowerCase()) && temp.getAvailableSeats() > 0) {
				sortPrice.add(Double.valueOf(temp.getPrice()));
				sortDuration.add(temp.getDuration());
				newTempList = new ArrayList<Flight>();
				newTempList.add(temp);
				sortList.add(newTempList);
			}
	
		}
		for (List<Flight> tempflight: getItineraryDB(itinerariespath).getDB()) {
			if (tempflight.get(0).getDepartureDate().equals(date) && 
					tempflight.get(0).getOrigin().toLowerCase().contains(origin.toLowerCase()) && 
					tempflight.get(tempflight.size() - 1).getDestination().toLowerCase().contains(destination.toLowerCase())) {
				long tempDurationValue = 0;
				double tempPriceValue = 0;
				for (int i = 0; i < tempflight.size(); i++) {
					tempPriceValue += Double.valueOf(tempflight.get(i).getPrice());
				}
				try {
					tempDurationValue = format.parse(tempflight.get(tempflight.size() - 1).getArrivalDateTime()).getTime() - 
							format.parse(tempflight.get(0).getDepartureDateTime()).getTime();
					tempDurationValue = tempDurationValue / (60 * 1000);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				sortPrice.add(tempPriceValue);
				sortDuration.add(tempDurationValue);
				sortList.add(tempflight);
			}
		}
		
		return (ArrayList<List<Flight>>) sortList;
		
	}
	
	/**
	 * Returns the same itineraries as getItineraries produces, but sorted according price.
	 * @param date the departure date
	 * @param origin the origin
	 * @param destination the destination
	 * @param flightpath the path to the flight database.
	 * @param itinerarypath the path to the itinerary database. 
	 * @throws IOException
	 */
	public static ArrayList<List<Flight>> searchItinerariesPrice(String date, String origin, String destination, 
			String flightpath, String itinerariespath) throws ClassNotFoundException, IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<List<Flight>> sortList = new ArrayList<List<Flight>>();
		List<Double> sortPrice = new ArrayList<Double>();
		List<Long> sortDuration = new ArrayList<Long>();
		List<Flight> newTempList;
		
		for (Flight temp: getFlightDB(flightpath).getDB()) {
			if (temp.getDepartureDate().equals(date) && temp.getOrigin().toLowerCase().contains(origin.toLowerCase()) && 
					temp.getDestination().toLowerCase().contains(destination.toLowerCase()) && temp.getAvailableSeats() > 0) {
				sortPrice.add(Double.valueOf(temp.getPrice()));
				sortDuration.add(temp.getDuration());
				newTempList = new ArrayList<Flight>();
				newTempList.add(temp);
				sortList.add(newTempList);
			}
	
		}
		for (List<Flight> tempflight: getItineraryDB(itinerariespath).getDB()) {
			if (tempflight.get(0).getDepartureDate().equals(date) && 
					tempflight.get(0).getOrigin().toLowerCase().contains(origin.toLowerCase()) && 
					tempflight.get(tempflight.size() - 1).getDestination().toLowerCase().contains(destination.toLowerCase())) {
				long tempDurationValue = 0;
				double tempPriceValue = 0;
				for (int i = 0; i < tempflight.size(); i++) {
					tempPriceValue += Double.valueOf(tempflight.get(i).getPrice());
				}
				try {
					tempDurationValue = format.parse(tempflight.get(tempflight.size() - 1).getArrivalDateTime()).getTime() - 
							format.parse(tempflight.get(0).getDepartureDateTime()).getTime();
					tempDurationValue = tempDurationValue / (60 * 1000);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				sortPrice.add(tempPriceValue);
				sortDuration.add(tempDurationValue);
				sortList.add(tempflight);
			}
		}
		
		for (int i = 0; i < sortPrice.size(); i ++) {
			for (int j = 1; j < sortPrice.size(); j++) {
				if (sortPrice.get(j - 1) > sortPrice.get(j)) {
					Collections.swap(sortPrice, j- 1, j);
					Collections.swap(sortDuration, j - 1, j);
					Collections.swap(sortList, j - 1, j);
				}
			}
		}

		return (ArrayList<List<Flight>>) sortList;
		
	}
	
	/**
	 * Returns the same itineraries as getItineraries produces, but sorted according
	 * to total itinerary travel time, in non-decreasing order.
	 * @param date a departure date (in the format YYYY-MM-DD)
	 * @param origin a flight original
	 * @param destination a flight destination
	 * @return itineraries (sorted in non-decreasing order of travel itinerary travel time) 
	 * that depart from origin and arrive at
	 * destination on the given date with stopovers at or under 6 hours.
	 * Each itinerary in the output should contain one line per flight,
	 * in the format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
	 * followed by total price (on its own line, exactly two decimal places),
	 * followed by total duration (on its own line, in format HH:MM).
	 */
	public static ArrayList<List<Flight>> searchItinerariesDuration(String date, String origin, String destination, 
			String flightpath, String itinerariespath) throws ClassNotFoundException, IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<List<Flight>> sortList = new ArrayList<List<Flight>>();
		List<Double> sortPrice = new ArrayList<Double>();
		List<Long> sortDuration = new ArrayList<Long>();
		List<Flight> newTempList;
		for (Flight temp: getFlightDB(flightpath).getDB()) {
			if (temp.getDepartureDate().equals(date) && temp.getOrigin().toLowerCase().contains(origin.toLowerCase()) && 
					temp.getDestination().toLowerCase().contains(destination.toLowerCase()) && temp.getAvailableSeats() > 0) {
				sortPrice.add(Double.valueOf(temp.getPrice()));
				sortDuration.add(temp.getDuration());
				newTempList = new ArrayList<Flight>();
				newTempList.add(temp);
				sortList.add(newTempList);
			}
		}
		for (List<Flight> tempflight: getItineraryDB(itinerariespath).getDB()) {
			if (tempflight.get(0).getDepartureDate().equals(date) && 
					tempflight.get(0).getOrigin().toLowerCase().contains(origin.toLowerCase()) && 
					tempflight.get(tempflight.size() - 1).getDestination().toLowerCase().contains(destination.toLowerCase())) {
				long tempDurationValue = 0;
				double tempPriceValue = 0;
				for (int i = 0; i < tempflight.size(); i++) {
					tempPriceValue += Double.valueOf(tempflight.get(i).getPrice());
				}
				try {
					tempDurationValue = format.parse(tempflight.get(tempflight.size() - 1).getArrivalDateTime()).getTime() - 
							format.parse(tempflight.get(0).getDepartureDateTime()).getTime();
					tempDurationValue = tempDurationValue / (60 * 1000);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				sortPrice.add(tempPriceValue);
				sortDuration.add(tempDurationValue);
				sortList.add(tempflight);
			}
		}
		for (int i = 0; i < sortDuration.size(); i ++) {
			for (int j = 1; j < sortDuration.size(); j++) {
				if (sortDuration.get(j - 1) > sortDuration.get(j)) {
					Collections.swap(sortPrice, j- 1, j);
					Collections.swap(sortDuration, j - 1, j);
					Collections.swap(sortList, j - 1, j);
				}
			}
		}
		return (ArrayList<List<Flight>>) sortList;
	}
}

