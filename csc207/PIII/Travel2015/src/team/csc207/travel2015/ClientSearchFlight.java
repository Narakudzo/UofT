package team.csc207.travel2015;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
import android.widget.Spinner;
import travel2015.*;

/**
 * This is the page which can search the flight information in client mode.
 */
public class ClientSearchFlight extends Activity {
	
	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_search_flight);
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
	}
	
	/**
	 * Stores the departure date, origin and destination in the sharedpreference and intents
	 * to ClientSearchFlightBook.class
	 * @param view android.view.View
	 */
	public void searchflight(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		
		EditText editOrigin = (EditText) findViewById(R.id.editOrigin);
        String origin = editOrigin.getText().toString();
        EditText editDestination = (EditText) findViewById(R.id.editDestination);
        String destination = editDestination.getText().toString();
        Spinner year = (Spinner) findViewById(R.id.year);
        Spinner month = (Spinner) findViewById(R.id.month);
        Spinner date = (Spinner) findViewById(R.id.date);
        String departuredate = String.valueOf(year.getSelectedItem()) + 
        		"-" + String.valueOf(month.getSelectedItem()) +
        		"-" + String.valueOf(date.getSelectedItem());
		Editor editor = myPref.edit();
		editor.putString("origin", origin);
		editor.putString("destination", destination);
		editor.putString("departuredate", departuredate);
		editor.commit();

        File filedir = this.getApplicationContext().getFilesDir();
	    File flightpath = new File(filedir, MainConstants.FLIGHTSAVE);

        ArrayList<Flight> flightResult = null;
		try {
			flightResult = Console.searchFlight(departuredate, origin, destination, flightpath.getPath());
		} catch (ClassNotFoundException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Error!! Flight file not found!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		} catch (IOException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("System Error!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
        
        if (flightResult.isEmpty()) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("No flight is available in your criteria. Try again.");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
        }
        
        Editor editor2 = myPref.edit();
		editor2.putInt("searchresult", flightResult.size());
		editor2.commit();
		
		Intent intent = new Intent(this.getApplicationContext(), ClientSearchFlightBook.class);
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
