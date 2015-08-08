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
 * This is the page which can view the client's itineraries information in client mode.
 */
@SuppressLint("SimpleDateFormat")
public class ClientItinerariesInfo extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_itineraries_info);
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		SharedPreferences myPref2 = getSharedPreferences(MainConstants.TRAVELLOGIN, MODE_PRIVATE);
		
		int currentNum = myPref.getInt("currentNum", 0);
		String user = myPref2.getString(MainConstants.keyname, "");
		
        File filedir = this.getApplicationContext().getFilesDir();
	    File clientpath = new File(filedir, MainConstants.CLIENTSAVE);

	    List<List<Flight>> itinerariesResult = null;
	    Client mySelf = null;
		try {
			mySelf = Console.getClient(user, clientpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		itinerariesResult = mySelf.getBookingHistory().getDB();
		if (currentNum == 0) {
			Editor editor = myPref.edit();
			editor.putInt("currentNum", itinerariesResult.size());
			editor.commit();
			currentNum = itinerariesResult.size();
		}
		int currentResult = itinerariesResult.size() - (currentNum - 1);
		
		String numberOfResult = "";
		if (itinerariesResult.size() > 0) {
			numberOfResult = currentResult + " of " + itinerariesResult.size() + "\n";
		} else {
			numberOfResult = "No itineraries found!!\n";
		}
		
		TextView textResult = (TextView) findViewById(R.id.textResult);
		textResult.setText(numberOfResult);
		
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
	 * Stores the update booking information in sharedpreferences and intents to ClientItinerariesInfo.class
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
	    
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		SharedPreferences myPref2 = getSharedPreferences(MainConstants.TRAVELLOGIN, MODE_PRIVATE);
		
		int currentNum = myPref.getInt("currentNum", 0);
		String user = myPref2.getString(MainConstants.keyname, "");
	    
	    List<List<Flight>> itinerariesResult = null;
	    Client mySelf = null;
		try {
			mySelf = Console.getClient(user, clientpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itinerariesResult = mySelf.getBookingHistory().getDB();

		int currentResult = itinerariesResult.size() - (currentNum - 1);
		List<Flight> ftlist = itinerariesResult.get(currentResult - 1);
		
		mySelf.deleteBookingHistory(currentResult - 1);
		
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
			Console.reBuildClientDB(mySelf, mySelf.getEmail(), clientpath.getPath());
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
		builder.setTitle("CANCEL SUCCESSFUL!!");
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(ClientItinerariesInfo.this, ClientItinerariesInfo.class);
				startActivity(intent);
            }
        });
		AlertDialog dialog = builder.create();
		dialog.show();

	}
	
	/**
	 * Clicks the next button and intents to ClientItinerariesInfo.class
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
		
		Intent intent = new Intent(this.getApplicationContext(), ClientItinerariesInfo.class);
		startActivity(intent);
		
	}
	
	/**
	 * Clicks the previous button and intents to ClientItinerariesInfo.class
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
		
		Intent intent = new Intent(this.getApplicationContext(), ClientItinerariesInfo.class);
		startActivity(intent);
	}
	
	/**
	 * Clicks the backtomenu button and intent to MainAdminActivity.class
	 * @param view android.view.View
	 */
	public void backtomenu(View view) {
		
		Intent intent = new Intent(this.getApplicationContext(), MainMenuActivity.class);
		startActivity(intent);
	}

}