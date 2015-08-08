package travel2015;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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


public class Console {

	
	/** Read client data from the path and create Serialized data.
	 * Save the Serialized data to local drive.
	 * @param path File path to CSV file
	 * @throws IOException File not found / FileOutputStream
	 */
	public static void buildNewClientDB(String path) throws IOException {
		
		/** Initiate new ArrayDB to store Client object */
		ArrayDB<Client> newClientDB = new ArrayDB<Client>();
		Client newClient;
		Scanner scanner = new Scanner(new FileInputStream(path));
		String[] csvData;
		while (scanner.hasNextLine()) {
		    csvData = scanner.nextLine().split(",");
		    if (csvData.length == 6) {
		    	newClient = new Client(csvData[0], csvData[1], csvData[2], csvData[3], csvData[4], csvData[5]);
		    	newClientDB.addObject(newClient);
		    }
		}
		scanner.close();

        OutputStream file = new FileOutputStream(TravelConstants.CLIENTDBPATH);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(newClientDB);
        output.close();
	}

	/**
	 * Read flight data from the path and create Serialized data.
	 * Save the Serialized data to local drive.
	 * @param path CSV Path
	 * @throws IOException File not found / FileOutput Stream
	 */
	public static void buildNewFlightDB(String path) throws IOException {
		
		/** Initiate new ArrayDB to store Flight and Itinerary object */
		ArrayDB<Flight> newFlightDB = new ArrayDB<Flight>();
		ArrayDB<List<Flight>> newItineraryDB = new ArrayDB<List<Flight>>();
		List<Flight> tempFlightList;
		Scanner scanner = new Scanner(new FileInputStream(path));
		String[] csvData;
		while (scanner.hasNextLine()) {
		    csvData = scanner.nextLine().split(",");
		    if (csvData.length == 7) {
		    	Flight newFlight = new Flight(csvData[0], csvData[1], csvData[2], csvData[3], 
		    			csvData[4], csvData[5], csvData[6]);
		    
				/** Add possible connection flights. */
			    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				for (Flight temp: newFlightDB.getDB()) {
					if (temp.getOrigin().equals(newFlight.getDestination())) {
						long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
						try {
							diff = format.parse(temp.getDepartureDateTime()).getTime() -
									format.parse(newFlight.getArrivalDateTime()).getTime();
							diff = diff / (60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (diff <= TravelConstants.CONNECTIONLIMITHOUR) {
							newFlight.addConnectionList(temp);
							tempFlightList = new ArrayList<Flight>();
							tempFlightList.add(newFlight);
							tempFlightList.add(temp);
							newItineraryDB.addObject(tempFlightList);
						}
					}
					if (temp.getDestination().equals(newFlight.getOrigin())) {
						long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
						try {
							diff = format.parse(newFlight.getDepartureDateTime()).getTime() - 
									format.parse(temp.getArrivalDateTime()).getTime();
							diff = diff / (60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (diff <= TravelConstants.CONNECTIONLIMITHOUR) {
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
					if (templist.get(0).getOrigin().equals(newFlight.getDestination())) {
						long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
						try {
							diff = format.parse(templist.get(0).getDepartureDateTime()).getTime() -
									format.parse(newFlight.getArrivalDateTime()).getTime();
							diff = diff / (60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (diff <= TravelConstants.CONNECTIONLIMITHOUR) {
							tempFlightList = new ArrayList<Flight>();
							tempFlightList.add(newFlight);
							for (Flight tempflight: templist) {
								tempFlightList.add(tempflight);
							}
							newItineraryDB.addObject(tempFlightList);
						}
					}
					if (templist.get(templist.size() - 1).getDestination().equals(newFlight.getOrigin())) {
						long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
						try {
							diff = format.parse(newFlight.getDepartureDateTime()).getTime() - 
									format.parse(templist.get(templist.size() - 1).getArrivalDateTime()).getTime();
							diff = diff / (60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (diff <= TravelConstants.CONNECTIONLIMITHOUR) {
							tempFlightList = new ArrayList<Flight>();
							for (Flight tempflight: templist) {
								tempFlightList.add(tempflight);
							}
							tempFlightList.add(newFlight);
							newItineraryDB.addObject(tempFlightList);
						}
					}
				}
				newFlightDB.addObject(newFlight);
		    }
		}
		scanner.close();
		
        OutputStream file = new FileOutputStream(TravelConstants.FLIGHTDBPATH);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(newFlightDB);
        output.close();
        
        OutputStream file2 = new FileOutputStream(TravelConstants.ITINERARYDBPATH);
        OutputStream buffer2 = new BufferedOutputStream(file2);
        ObjectOutput output2 = new ObjectOutputStream(buffer2);

        output2.writeObject(newItineraryDB);
        output2.close();
	}

	/**
	 * Read admin data from the path and create Serialized data.
	 * Save the Serialized data to local drive.
	 * @param path CSV Path
	 * @throws IOException File not found / FileOutput Stream
	 */
	public static void buildNewAdminDB(String path) throws IOException {
		//TODO To be implmented 
	}
	
	/** Read Serialized ArrayDB of Client data from the CLIENTDBPATH
	 * and return the ArrayDB
	 * @return ArrayDB<Client> from local drive
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayDB<Client> getClientDB() throws ClassNotFoundException, IOException {

        InputStream file = new FileInputStream(TravelConstants.CLIENTDBPATH);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        // Deserialize the ArrayDB
        ArrayDB<Client> clientDB = (ArrayDB<Client>) input.readObject();
        input.close();

		return clientDB;
	}
	
	/** Read Serialized ArrayDB of Flight data from the FLIGHTDBPATH
	 * and return the ArrayDB
	 * @return ArrayDB<Flight> from local drive
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayDB<Flight> getFlightDB() throws ClassNotFoundException, IOException {

        InputStream file = new FileInputStream(TravelConstants.FLIGHTDBPATH);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        // Deserialize the ArrayDB
        ArrayDB<Flight> flightDB = (ArrayDB<Flight>) input.readObject();
        input.close();

		return flightDB;
	}

	/** Read Serialized ArrayDB of Itinerary data from the ITINERARYDBPATH
	 * and return the ArrayDB
	 * @return ArrayDB<List<Flight>> from local drive
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayDB<List<Flight>> getItineraryDB() throws ClassNotFoundException, IOException {

        InputStream file = new FileInputStream(TravelConstants.ITINERARYDBPATH);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        // Deserialize the ArrayDB
        ArrayDB<List<Flight>> itineraryDB = (ArrayDB<List<Flight>>) input.readObject();
        input.close();

		return itineraryDB;
	}
	
	/** Read Serialized ArrayDB of Admin data from the ADMINDBPATH
	 * and return the ArrayDB
	 * @return ArrayDB<Administrator> from local drive
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayDB<Administrator> getAdminDB() throws ClassNotFoundException, IOException {

        InputStream file = new FileInputStream(TravelConstants.ADMINDBPATH);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        // Deserialize the ArrayDB
        ArrayDB<Administrator> adminDB = (ArrayDB<Administrator>) input.readObject();
        input.close();

		return adminDB;
	}
	
	/**
	 * Returns all flights that depart from origin and arrive at destination on
	 * the given date. 
	 * @param date Departure Date
	 * @param origin Departure City
	 * @param destination Destination City
	 * @return the flights that depart from origin and arrive at destination
	 *  on the given date formatted with one flight per line in exactly this
	 *  format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,Price
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static String searchFlight(String date, String origin, String destination) throws ClassNotFoundException, IOException {
		List<Flight> templist = new ArrayList<Flight>();
		for (Flight temp: getFlightDB().getDB()) {
			if (temp.getDepartureDate().equals(date) && temp.getOrigin().equals(origin) && 
					temp.getDestination().equals(destination)) {
				templist.add(temp);
			}
		}
		
		String result = "";
		for (Flight temp: templist) {
			result += temp.getFlightNumber() + "," + temp.getDepartureDateTime() + "," + 
					temp.getArrivalDateTime() + "," + temp.getAirLine() + "," + temp.getOrigin() + "," +
					temp.getDestination()  + "," + 
					String.format("%.2f", Double.parseDouble(temp.getPrice())) + "\n";
		}
		return result;
	}

	
	/**
	 * Returns all itineraries that depart from origin and arrive at
	 * destination on the given date.
	 * @param date a departure date (in the format YYYY-MM-DD)
	 * @param origin a flight original
	 * @param destination a flight destination
	 * @return itineraries that depart from origin and arrive at
	 * destination on the given date with stopovers at or under 6 hours.
	 * Each itinerary in the output should contain one line per flight,
	 * in the format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
	 * followed by total price (on its own line, exactly two decimal places),
	 * followed by total duration (on its own line, in format HH:MM)
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static String searchItineraries(String date, String origin, String destination) throws ClassNotFoundException, IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String result = "";
		for (Flight temp: getFlightDB().getDB()) {
			if (temp.getDepartureDate().equals(date) && temp.getOrigin().equals(origin) && 
					temp.getDestination().equals(destination)) {
				result += temp.getFlightNumber() + "," + temp.getDepartureDate() + "," +
					temp.getArrivalDate() + "," + temp.getAirLine() + "," + temp.getOrigin() + "," +
					temp.getDestination()  + "\n" +
					String.format("%.2f", Double.parseDouble(temp.getPrice())) + "\n" + 
					String.format("%02d", temp.getDuration() / 60) + ":" +
					String.format("%02d", temp.getDuration() % 60) + "\n";
			}
		}
		for (List<Flight> tempflight: getItineraryDB().getDB()) {
			if (tempflight.get(0).getDepartureDate().equals(date) && 
					tempflight.get(0).getOrigin().equals(origin) && 
					tempflight.get(tempflight.size() - 1).getDestination().equals(destination)) {
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
				for (Flight temp: tempflight) {
					result += temp.getFlightNumber() + "," + temp.getDepartureDateTime() + "," +
						temp.getArrivalDateTime() + "," + temp.getAirLine() + "," + temp.getOrigin() + "," +
						temp.getDestination()  + "\n";
				}
				result += String.format("%.2f", tempPriceValue) + "\n" + 
						String.format("%02d", tempDurationValue / 60) + ":" +
						String.format("%02d", tempDurationValue % 60) + "\n";
			}
		}
		return result;
	}
	
	/**
	 * Returns the same itineraries as getItineraries produces, but sorted according
	 * to total itinerary cost, in non-decreasing order.
	 * @param date a departure date (in the format YYYY-MM-DD)
	 * @param origin a flight original
	 * @param destination a flight destination
	 * @return itineraries (sorted in non-decreasing order of total itinerary cost) 
	 * that depart from origin and arrive at
	 * destination on the given date with stopovers at or under 6 hours.
	 * Each itinerary in the output should contain one line per flight,
	 * in the format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
	 * followed by total price (on its own line, exactly two decimal places),
	 * followed by total duration (on its own line, in format HH:MM).
	 */
	public static String searchItinerariesPrice(String date, String origin, String destination) throws ClassNotFoundException, IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<List<Flight>> sortList = new ArrayList<List<Flight>>();
		List<Double> sortPrice = new ArrayList<Double>();
		List<Long> sortDuration = new ArrayList<Long>();
		List<Flight> newTempList;
		String result = "";
		for (Flight temp: getFlightDB().getDB()) {
			if (temp.getDepartureDate().equals(date) && temp.getOrigin().equals(origin) && 
					temp.getDestination().equals(destination)) {
				sortPrice.add(Double.valueOf(temp.getPrice()));
				sortDuration.add(temp.getDuration());
				newTempList = new ArrayList<Flight>();
				newTempList.add(temp);
				sortList.add(newTempList);
			}
	
		}
		for (List<Flight> tempflight: getItineraryDB().getDB()) {
			if (tempflight.get(0).getDepartureDate().equals(date) && 
					tempflight.get(0).getOrigin().equals(origin) && 
					tempflight.get(tempflight.size() - 1).getDestination().equals(destination)) {
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
		
		int k = 0;
		for (List<Flight> sortedTempFlight: sortList) {
			for (Flight temp: sortedTempFlight) {
				result += temp.getFlightNumber() + "," + temp.getDepartureDateTime() + "," +
					temp.getArrivalDateTime() + "," + temp.getAirLine() + "," + temp.getOrigin() + "," +
					temp.getDestination() + "\n";
			}
			result += String.format("%.2f", sortPrice.get(k)) + "\n" +
					String.format("%02d", sortDuration.get(k) / 60) + ":" +
					String.format("%02d", sortDuration.get(k) % 60) + "\n";
			k++;
		}

		return result;
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
	public static String searchItinerariesDuration(String date, String origin, String destination) throws ClassNotFoundException, IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<List<Flight>> sortList = new ArrayList<List<Flight>>();
		List<Double> sortPrice = new ArrayList<Double>();
		List<Long> sortDuration = new ArrayList<Long>();
		List<Flight> newTempList;
		String result = "";
		for (Flight temp: getFlightDB().getDB()) {
			if (temp.getDepartureDate().equals(date) && temp.getOrigin().equals(origin) && 
					temp.getDestination().equals(destination)) {
				sortPrice.add(Double.valueOf(temp.getPrice()));
				sortDuration.add(temp.getDuration());
				newTempList = new ArrayList<Flight>();
				newTempList.add(temp);
				sortList.add(newTempList);
			}
		}
		for (List<Flight> tempflight: getItineraryDB().getDB()) {
			if (tempflight.get(0).getDepartureDate().equals(date) && 
					tempflight.get(0).getOrigin().equals(origin) && 
					tempflight.get(tempflight.size() - 1).getDestination().equals(destination)) {
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
		
		int k = 0;
		for (List<Flight> sortedTempFlight: sortList) {
			for (Flight temp: sortedTempFlight) {
				result += temp.getFlightNumber() + "," + temp.getDepartureDateTime() + "," +
					temp.getArrivalDateTime() + "," + temp.getAirLine() + "," + temp.getOrigin() + "," +
					temp.getDestination()  + "\n";
			}
			result += String.format("%.2f", sortPrice.get(k)) + "\n" +
					String.format("%02d", sortDuration.get(k) / 60) + ":" +
					String.format("%02d", sortDuration.get(k) % 60) + "\n";
			k++;
		}
		return result;
	}
	
	
	/** Add new flight to the existing database.
	 * @param flightnumber
	 * @param departuredatetime
	 * @param arrivaldatetime
	 * @param airline
	 * @param origin
	 * @param destination
	 * @param price
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void addFlight(String flightnumber, String departuredatetime, String arrivaldatetime,
			String airline, String origin, String destination, String price) throws IOException, ClassNotFoundException {
		
		/** Get existing ArrayDB to store Flight and Itinerary object */
		ArrayDB<Flight> addFlightDB = getFlightDB();
		ArrayDB<List<Flight>> addItineraryDB = getItineraryDB();
		List<Flight> tempFlightList;

    	Flight newFlight = new Flight(flightnumber, departuredatetime, arrivaldatetime, airline,
    			origin, destination, price);
    
		/** Add possible connection flights. */
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (Flight temp: addFlightDB.getDB()) {
			if (temp.getOrigin().equals(newFlight.getDestination())) {
				long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
				try {
					diff = format.parse(temp.getDepartureDateTime()).getTime() -
							format.parse(newFlight.getArrivalDateTime()).getTime();
					diff = diff / (60 * 60 * 1000);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (diff <= TravelConstants.CONNECTIONLIMITHOUR) {
					newFlight.addConnectionList(temp);
					tempFlightList = new ArrayList<Flight>();
					tempFlightList.add(newFlight);
					tempFlightList.add(temp);
					addItineraryDB.addObject(tempFlightList);
				}
			}
			if (temp.getDestination().equals(newFlight.getOrigin())) {
				long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
				try {
					diff = format.parse(newFlight.getDepartureDateTime()).getTime() - 
							format.parse(temp.getArrivalDateTime()).getTime();
					diff = diff / (60 * 60 * 1000);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (diff <= TravelConstants.CONNECTIONLIMITHOUR) {
					temp.addConnectionList(newFlight);
					tempFlightList = new ArrayList<Flight>();
					tempFlightList.add(temp);
					tempFlightList.add(newFlight);
					addItineraryDB.addObject(tempFlightList);
				}
			}
		}
		
		/** Searching for possible itinerary */
		List<List<Flight>> tempItineraryDB = new ArrayList<List<Flight>>();
		tempItineraryDB.addAll(addItineraryDB.getDB());
		for (List<Flight> templist: tempItineraryDB) {
			if (templist.get(0).getOrigin().equals(newFlight.getDestination())) {
				long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
				try {
					diff = format.parse(templist.get(0).getDepartureDateTime()).getTime() -
							format.parse(newFlight.getArrivalDateTime()).getTime();
					diff = diff / (60 * 60 * 1000);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (diff <= TravelConstants.CONNECTIONLIMITHOUR) {
					tempFlightList = new ArrayList<Flight>();
					tempFlightList.add(newFlight);
					for (Flight tempflight: templist) {
						tempFlightList.add(tempflight);
					}
					addItineraryDB.addObject(tempFlightList);
				}
			}
			if (templist.get(templist.size() - 1).getDestination().equals(newFlight.getOrigin())) {
				long diff = TravelConstants.CONNECTIONLIMITHOUR + 1;
				try {
					diff = format.parse(newFlight.getDepartureDateTime()).getTime() - 
							format.parse(templist.get(templist.size() - 1).getArrivalDateTime()).getTime();
					diff = diff / (60 * 60 * 1000);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (diff <= TravelConstants.CONNECTIONLIMITHOUR) {
					tempFlightList = new ArrayList<Flight>();
					for (Flight tempflight: templist) {
						tempFlightList.add(tempflight);
					}
					tempFlightList.add(newFlight);
					addItineraryDB.addObject(tempFlightList);
				}
			}
		}
		addFlightDB.addObject(newFlight);

		
        OutputStream file = new FileOutputStream(TravelConstants.FLIGHTDBPATH);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(addFlightDB);
        output.close();
        
        OutputStream file2 = new FileOutputStream(TravelConstants.ITINERARYDBPATH);
        OutputStream buffer2 = new BufferedOutputStream(file2);
        ObjectOutput output2 = new ObjectOutputStream(buffer2);

        output2.writeObject(addItineraryDB);
        output2.close();
	}

}
