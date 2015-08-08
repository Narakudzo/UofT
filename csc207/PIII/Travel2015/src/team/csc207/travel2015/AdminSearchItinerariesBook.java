package team.csc207.travel2015;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import travel2015.Client;
import travel2015.Console;
import travel2015.Flight;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is the searching and booking itineraries page in admin mode.
 */
@SuppressLint("SimpleDateFormat")
public class AdminSearchItinerariesBook extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_search_itineraries_book);
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		
        File filedir = this.getApplicationContext().getFilesDir();

	    String origin = myPref.getString("origin", "");
	    String departuredate = myPref.getString("departuredate", "");
	    String destination = myPref.getString("destination", "");
	    String sortby = myPref.getString("sortby", "");
	    String clientemail = myPref.getString("myclient", "");
	    int searchresult = myPref.getInt("searchresult", 0);

	    // First, get client's object
		File clientpath = new File(filedir, MainConstants.CLIENTSAVE);
		Client myClient = null;
		try {
			myClient = Console.getClient(clientemail, clientpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextView clientName =(TextView) findViewById(R.id.clientname);
		clientName.setText("(" + myClient.getFirstName() + " " + myClient.getLastName() + ")");
		
	    File flightpath = new File(filedir, MainConstants.FLIGHTSAVE);
	    File itinerariespath = new File(filedir, MainConstants.ITINERARIESSAVE);
	    ArrayList<List<Flight>> itinerariesResult = null;
		try {
			if (sortby.equals("Sort by Price")) {
				itinerariesResult = Console.searchItinerariesPrice(departuredate, origin, destination,
					flightpath.getPath(), itinerariespath.getPath());
			} else if (sortby.equals("Sort by Duration")) {
				itinerariesResult = Console.searchItinerariesDuration(departuredate, origin, destination,
						flightpath.getPath(), itinerariespath.getPath());
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int currentResult = itinerariesResult.size() - (searchresult - 1);
		String numberOfResult = currentResult + " of " + itinerariesResult.size() + "\n" + sortby;
		TextView textResult = (TextView) findViewById(R.id.textResult);
		textResult.setText(numberOfResult);
		
		String myResult = "";
		List<Flight> ftlist = itinerariesResult.get(currentResult - 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long diff = 0;
		try {
			diff = format.parse(ftlist.get(ftlist.size() - 1).getArrivalDateTime()).getTime() -
					format.parse(ftlist.get(0).getDepartureDateTime()).getTime();
			diff = diff / (60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		double totalprice = 0.0F;
		
		for (Flight ft: ftlist) {
			myResult += ft.getOrigin() + " -> " + ft.getDestination() + "\n" +
				"Departure: " + ft.getDepartureDateTime() + "\n" +
				"Arrival:       " + ft.getArrivalDateTime() + "\n" +
				ft.getAirLine() + ": " + ft.getFlightNumber() + "\n" +
				"Seats remaining: " + ft.getAvailableSeats() + "\n\n";
			totalprice = totalprice + Double.valueOf(ft.getPrice());
		}
		myResult += "Total Price: $" + String.format("%.2f", totalprice) + "\n";
		myResult += "Total Duration: " + (diff / 60) + " H(s) " + (diff % 60) + " M(s)\n";
		
		TextView textResult2 = (TextView) findViewById(R.id.textResult2);
		textResult2.setText(myResult);
		
		if (currentResult == 1) {
			Button previous = (Button) findViewById(R.id.previous);
			previous.setVisibility(View.GONE);
		}
		
		if (currentResult == itinerariesResult.size()) {
			Button next = (Button) findViewById(R.id.next);
			next.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Stores the booking information in sharedpreferences and intent to AdminSearchItineraries.class
	 * @param view android.view.View
	 */
	public void book(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		File filedir = this.getApplicationContext().getFilesDir();
	    File flightpath = new File(filedir, MainConstants.FLIGHTSAVE);
	    File itinerariespath = new File(filedir, MainConstants.ITINERARIESSAVE);
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		String origin = myPref.getString("origin", "");
	    String departuredate = myPref.getString("departuredate", "");
	    String destination = myPref.getString("destination", "");
	    String clientemail = myPref.getString("myclient", "");
	    String sortby = myPref.getString("sortby", "");
	    int searchresult = myPref.getInt("searchresult", 0);
	    
	    ArrayList<List<Flight>> itinerariesResult = null;
		try {
			if (sortby.equals("Sort by Price")) {
				itinerariesResult = Console.searchItinerariesPrice(departuredate, origin, destination,
					flightpath.getPath(), itinerariespath.getPath());
			} else if (sortby.equals("Sort by Duration")) {
				itinerariesResult = Console.searchItinerariesDuration(departuredate, origin, destination,
						flightpath.getPath(), itinerariespath.getPath());
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int currentResult = itinerariesResult.size() - (searchresult - 1);
		List<Flight> ftlist = itinerariesResult.get(currentResult - 1);
		
		File clientpath = new File(filedir, MainConstants.CLIENTSAVE);
		Client myClient = null;
		try {
			myClient = Console.getClient(clientemail, clientpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myClient.addBookingHistory(ftlist);
		
		try {
			for (Flight ft: ftlist) {
				Console.reBuildFlightDB(ft,	flightpath.getPath(), itinerariespath.getPath(), -1);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Console.reBuildClientDB(myClient, myClient.getEmail(), clientpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Editor editor = myPref.edit();
		editor.remove("origin");
		editor.remove("destination");
		editor.remove("departuredate");
		editor.remove("searchresult");
		editor.remove("myclient");
		editor.remove("sortby");
		editor.commit();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("BOOKED for " + myClient.getFirstName() + " " + myClient.getLastName() + "!!");
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(AdminSearchItinerariesBook.this, AdminSearchItineraries.class);
				startActivity(intent);
            }
        });
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Clicks the next button and intents to AdminSearchItinerariesBook.class
	 * @param view android.view.View
	 */
	public void next(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		int searchresult = myPref.getInt("searchresult", 0);
		Editor editor = myPref.edit();
		searchresult -= 1;
		editor.putInt("searchresult", searchresult);
		editor.commit();
		
		Intent intent = new Intent(this.getApplicationContext(), AdminSearchItinerariesBook.class);
		startActivity(intent);
		
	}
	
	/**
	 * Clicks the previous button and intents to AdminSearchItinerariesBook.class
	 * @param view android.view.View
	 */
	public void previous(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		int searchresult = myPref.getInt("searchresult", 0);
		Editor editor = myPref.edit();
		searchresult += 1;
		editor.putInt("searchresult", searchresult);
		editor.commit();
		
		Intent intent = new Intent(this.getApplicationContext(), AdminSearchItinerariesBook.class);
		startActivity(intent);
		
	}
	
	/**
	 * Clicks the backtosearch button and intent to AdminSearchItineraries.class
	 * @param view android.view.View
	 */
	public void backtosearch(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		Editor editor = myPref.edit();
		editor.remove("origin");
		editor.remove("destination");
		editor.remove("departuredate");
		editor.remove("searchresult");
		editor.remove("sortby");
		editor.commit();
		
		Intent intent = new Intent(this.getApplicationContext(), AdminSearchItineraries.class);
		startActivity(intent);
	}

}
