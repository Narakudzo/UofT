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
 * This is the itineraries information page in admin mode.
 */
@SuppressLint("SimpleDateFormat")
public class AdminItinerariesInfo extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_itineraries_info);
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);

		int currentNum = myPref.getInt("currentNum", 0);

        File filedir = this.getApplicationContext().getFilesDir();
	    File clientpath = new File(filedir, MainConstants.CLIENTSAVE);

	    List<List<Flight>> itinerariesResult = new ArrayList<List<Flight>>();
	    List<String[]> clientIndex = new ArrayList<String[]>();
	    List<Client> myClientList = new ArrayList<Client>();
		try {
			myClientList = Console.getClientDB(clientpath.getPath()).getDB();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Client tempClient: myClientList) {
			if (tempClient.getBookingHistory().getDB().size() > 0) {
				int count = 0;
				for (List<Flight> tempFlightList: tempClient.getBookingHistory().getDB()) {
					itinerariesResult.add(tempFlightList);
					String[] nameAndEmail = 
						new String[] {tempClient.getFirstName() + " " + tempClient.getLastName(), tempClient.getEmail(), String.valueOf(count)};
					clientIndex.add(nameAndEmail);
					count++;
				}
			}
		}

		if (currentNum == 0) {
			Editor editor = myPref.edit();
			editor.putInt("currentNum", itinerariesResult.size());
			editor.commit();
			currentNum = itinerariesResult.size();
		}
		int currentResult = itinerariesResult.size() - (currentNum - 1);
		
		String numberOfResult = "";
		String showClientName = "";
		String showClientEmail = "";
		String showClientFlightIndex = "";
		if (itinerariesResult.size() > 0) {
			numberOfResult = currentResult + " of " + itinerariesResult.size() + "\n";
			showClientName = "(" + clientIndex.get(currentResult - 1)[0] + ")";
			showClientEmail = clientIndex.get(currentResult - 1)[1];
			showClientFlightIndex = clientIndex.get(currentResult - 1)[2];
					
		} else {
			numberOfResult = "No itineraries found!!\n";
		}
		
		TextView textResult = (TextView) findViewById(R.id.textResult);
		textResult.setText(numberOfResult);
		TextView textResult3 = (TextView) findViewById(R.id.textResult3);
		textResult3.setText(showClientName);
		TextView clientEmail = (TextView) findViewById(R.id.clientemail);
		clientEmail.setText(showClientEmail);
		TextView clientFlightIndex = (TextView) findViewById(R.id.clientflightindex);
		clientFlightIndex.setText(showClientFlightIndex);
		
		String myResult = "";
		
		if (itinerariesResult.size() > 0) {
			
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
					ft.getAirLine() + ": " + ft.getFlightNumber() + "\n\n";
				totalprice = totalprice + Double.valueOf(ft.getPrice());
			}
			myResult += "Total Price: $" + String.format("%.2f", totalprice) + "\n";
			myResult += "Total Duration: " + (diff / 60) + " H(s) " + (diff % 60) + " M(s)\n";
		}
		
		TextView textResult2 = (TextView) findViewById(R.id.textResult2);
		textResult2.setText(myResult);
		
		if (currentResult == 1 || itinerariesResult.size() == 0) {
			Button previous = (Button) findViewById(R.id.previous);
			previous.setVisibility(View.GONE);
		}
		
		if (currentResult == itinerariesResult.size() || itinerariesResult.size() == 0) {
			Button next = (Button) findViewById(R.id.next);
			next.setVisibility(View.GONE);
		}
		
		if (itinerariesResult.size() == 0) {
			Button cancel = (Button) findViewById(R.id.cancel);
			cancel.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Stores the update itineraries information in sharedpreferences and intents to AdminItinerariesInfo.class
	 * @param view android.view.View
	 */
	public void cancel(View view) {
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
	    File clientpath = new File(filedir, MainConstants.CLIENTSAVE);
	    
		TextView textResult3 = (TextView) findViewById(R.id.textResult3);
		String showClientName = textResult3.getText().toString();
		TextView clientEmail = (TextView) findViewById(R.id.clientemail);
		String showClientEmail = clientEmail.getText().toString();
		TextView clientFlightIndex = (TextView) findViewById(R.id.clientflightindex);
		String showClientFlightIndex = clientFlightIndex.getText().toString();
	    
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		
		//int currentNum = myPref.getInt("currentNum", 0);

	    Client myClient = null;
		try {
			myClient = Console.getClient(showClientEmail, clientpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Flight> ftlist = myClient.getBookingHistory().getDB().get(Integer.valueOf(showClientFlightIndex));
		myClient.deleteBookingHistory(Integer.valueOf(showClientFlightIndex));
		
		try {
			for (Flight ft: ftlist) {
				Console.reBuildFlightDB(ft, flightpath.getPath(), itinerariespath.getPath(), 1);
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
		editor.putInt("currentNum", 0);
		editor.commit();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("CANCELED for " + showClientName + " !!");
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(AdminItinerariesInfo.this, AdminItinerariesInfo.class);
				startActivity(intent);
            }
        });
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Clicks the next button and intents to AdminItinerariesInfo.class
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
		int currentNum = myPref.getInt("currentNum", 0);
		Editor editor = myPref.edit();
		currentNum -= 1;
		editor.putInt("currentNum", currentNum);
		editor.commit();
		
		Intent intent = new Intent(this.getApplicationContext(), AdminItinerariesInfo.class);
		startActivity(intent);
		
	}
	
	/**
	 * Clicks the pervious button and intents to AdminItinerariesInfo.class
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
		int currentNum = myPref.getInt("currentNum", 0);
		Editor editor = myPref.edit();
		currentNum += 1;
		editor.putInt("currentNum", currentNum);
		editor.commit();
		
		Intent intent = new Intent(this.getApplicationContext(), AdminItinerariesInfo.class);
		startActivity(intent);
	}
	
	/**
	 * Clicks the backtomenu button and intents to MainAdminActivity.class
	 * @param view android.view.View
	 */
	public void backtomenu(View view) {
		
		Intent intent = new Intent(this.getApplicationContext(), MainAdminActivity.class);
		startActivity(intent);
	}

}