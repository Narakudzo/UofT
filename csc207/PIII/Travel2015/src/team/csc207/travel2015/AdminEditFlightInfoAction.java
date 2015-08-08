package team.csc207.travel2015;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import travel2015.Console;
import travel2015.Flight;
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
import android.widget.EditText;

/**
 * This is the activity page of editing flight information in admin mode.
 */
public class AdminEditFlightInfoAction extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_edit_flight_info_action);
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		String flightid = myPref.getString("flightid", "");
		if(flightid.length() == 0) {
			Intent intent = new Intent(this.getApplicationContext(), AdminEditFlightInfo.class);
			startActivity(intent);
			return;
		}
		
		File filedir = this.getApplicationContext().getFilesDir();
		File flightpath = new File(filedir, MainConstants.FLIGHTSAVE);
		
		Flight myFlight = null;
		try {
			myFlight = Console.getFlightbyID(flightid, flightpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EditText editNumber = (EditText) findViewById(R.id.editNumber);
		EditText editDeparturedatetime = (EditText) findViewById(R.id.editDeparturedatetime);
		EditText editArrivaldatetime = (EditText) findViewById(R.id.editArrivaldatetime);
		EditText editAirline = (EditText) findViewById(R.id.editAirline);
		EditText editOrigin = (EditText) findViewById(R.id.editOrigin);
		EditText editDestination = (EditText) findViewById(R.id.editDestination);
		EditText editPrice = (EditText) findViewById(R.id.editPrice);
		EditText editNumberofseats = (EditText) findViewById(R.id.editNumberofseats);
		editNumber.setText(myFlight.getFlightNumber());
		editDeparturedatetime.setText(myFlight.getDepartureDateTime());
		editArrivaldatetime.setText(myFlight.getArrivalDateTime());
		editAirline.setText(myFlight.getAirLine());
		editOrigin.setText(myFlight.getOrigin());
		editDestination.setText(myFlight.getDestination());
		editPrice.setText(myFlight.getPrice());
		editNumberofseats.setText(String.valueOf(myFlight.getAvailableSeats()));
		
	}
	
	/**
	 * Stores the update flight information and intents to AdminEditFlightInfoAction.class
	 * @param view android.view.View
	 */
	public void update(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		String flightid = myPref.getString("flightid", "");
		if(flightid.length() == 0) {
			Intent intent = new Intent(this.getApplicationContext(), AdminEditFlightInfo.class);
			startActivity(intent);
			return;
		}
		
		EditText editNumber = (EditText) findViewById(R.id.editNumber);
		String Number = editNumber.getText().toString();
		EditText editDeparturedatetime = (EditText) findViewById(R.id.editDeparturedatetime);
		String Departuredatetime = editDeparturedatetime.getText().toString();
		EditText editArrivaldatetime = (EditText) findViewById(R.id.editArrivaldatetime);
		String Arrivaldatetime = editArrivaldatetime.getText().toString();
		EditText editAirline = (EditText) findViewById(R.id.editAirline);
		String Airline = editAirline.getText().toString();
		EditText editOrigin = (EditText) findViewById(R.id.editOrigin);
		String Origin = editOrigin.getText().toString();
		EditText editDestination = (EditText) findViewById(R.id.editDestination);
		String Destination = editDestination.getText().toString();
		EditText editPrice = (EditText) findViewById(R.id.editPrice);
		String Price = editPrice.getText().toString();
		EditText editNumberofseats = (EditText) findViewById(R.id.editNumberofseats);
		String NumberofseatsString = editNumberofseats.getText().toString();
		
		// Check empty fields
		if (Number.length() == 0 || Departuredatetime.length() == 0 || Airline.length() == 0 ||
				Arrivaldatetime.length() == 0 || Origin.length() == 0 || Destination.length() == 0 ||
				Price.length() == 0 || NumberofseatsString.length() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Some fields are empty!! Try again!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
		
		// Check price value
		Price = String.format("%.2f", Double.valueOf(Price));
		double checkPrice = Double.valueOf(Price);
		if (checkPrice <= 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Price must be positive value!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
		
		// Check number of seats value
		int Numberofseats = Integer.valueOf(NumberofseatsString);
		if (Numberofseats < 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Number of seats cannot be negative!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
		
		// Check date/time format
		long tempDeparture = 0;
		long tempArrival = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");		
		try {
			tempDeparture = format.parse(Departuredatetime).getTime();
		} catch (ParseException e) {
		}
		try {
			tempArrival = format.parse(Arrivaldatetime).getTime();
		} catch (ParseException e) {
		}
		if (tempDeparture <= 0 || tempArrival <= 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Check Departure/Arrival date/time format!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
		
		if (tempDeparture >= tempArrival) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Departure must be earlier than Arrival!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
		
		// Set date format correctly.
		Departuredatetime = df.format(tempDeparture);
		Arrivaldatetime = df.format(tempArrival);
		
		File filedir = this.getApplicationContext().getFilesDir();
		File flightpath = new File(filedir, MainConstants.FLIGHTSAVE);
		File itinerariespath = new File(filedir, MainConstants.ITINERARIESSAVE);
		File clientpath = new File(filedir, MainConstants.CLIENTSAVE);
		
		Flight ft = null;
		try {
			ft = Console.getFlightbyID(flightid, flightpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// set new flight info
		ft.setFlightnumber(Number);
		ft.setDeparturedatetime(Departuredatetime);
		ft.setArrivaldatetime(Arrivaldatetime);
		ft.setAirline(Airline);
		ft.setOrigin(Origin);
		ft.setDestination(Destination);
		ft.setPrice(Price);
		ft.setAvailableSeats(Numberofseats);
		
		String Departuredate[] = Departuredatetime.split("\\s+");
		String Arrivaldate[] = Arrivaldatetime.split("\\s+");
		ft.setDeparturedate(Departuredate[0]);
		ft.setArrivaldate(Arrivaldate[0]);
		
		try {
			Console.reBuildFlightDB2(ft, flightpath.getPath(), itinerariespath.getPath(), clientpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("UPDATE SUCCESSFUL!!");
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(AdminEditFlightInfoAction.this, AdminEditFlightInfoAction.class);
				startActivity(intent);
            }
        });
		AlertDialog dialog = builder.create();
		dialog.show();

	}
	
	/**
	 * Clicks the backtosearch button and intents to AdminEditFlightInfo.class
	 * @param view android.view.View
	 */
	public void backtosearch(View view) {
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		Editor editor = myPref.edit();
		editor.remove("searchkey");
		editor.remove("flightid");
		editor.commit();
		Intent intent = new Intent(this.getApplicationContext(), AdminEditFlightInfo.class);
		startActivity(intent);
	}
	
}
