package team.csc207.travel2015;

import java.io.File;
import java.io.IOException;
import java.util.List;

import travel2015.Client;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * This is the edit flight information page in admin mode.
 */
public class AdminEditFlightInfo extends Activity {
	
	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_edit_flight_info);
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		String mySearchKeywords = myPref.getString("searchkey", "");
		
		TextView searchResultText = (TextView) findViewById(R.id.searchResultText);
		Spinner searchResultSpinner = (Spinner) findViewById(R.id.searchResultSpinner);
		
		if (mySearchKeywords.length() > 0) {
			TextView flightSearchText =(TextView) findViewById(R.id.searchText);
			flightSearchText.setVisibility(View.GONE);
			EditText flightSearchKeywords = (EditText) findViewById(R.id.searchKeywords);
			flightSearchKeywords.setVisibility(View.GONE);
			Button searchButton = (Button) findViewById(R.id.searchButton);
			searchButton.setVisibility(View.GONE);
			
			File filedir = this.getApplicationContext().getFilesDir();
			File flightpath = new File(filedir, MainConstants.FLIGHTSAVE);
			
			String arrayKeywords[] = mySearchKeywords.split("\\s+");
			
			List<Flight> flightResult = null;
			try {
				flightResult = Console.searchFlightByKeywords(arrayKeywords, flightpath.getPath());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (flightResult.size() > 0) {
				String message = "Search results are shown below. Select flight's ID (the unique " +
						"id numbers shown in the bottom of each flight) and tap Edit to edit the flight.";
				searchResultText.setText(message);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
				for (Flight ft: flightResult) {
					adapter.add(ft.getAirLine() + ": " + ft.getFlightNumber());
					adapter.add(ft.getOrigin() + " -> " + ft.getDestination());
					adapter.add("D: " + ft.getDepartureDateTime());
					adapter.add("A: " + ft.getArrivalDateTime());
					adapter.add("P: $" + ft.getPrice() + "  (unique id below)");
					adapter.add(String.valueOf(ft.getUniqueid()));
					adapter.add(" ");
				}
				
				searchResultSpinner.setAdapter(adapter);
			} else {
				String message = "No results found!! Try your search again!!";
				searchResultText.setText(message);
				searchResultText.setTextSize(14);
				searchResultSpinner.setVisibility(View.GONE);
				Button searchResultButton = (Button) findViewById(R.id.searchResultButton);
				searchResultButton.setVisibility(View.GONE);
			}
			
			
		} else {
			searchResultText.setVisibility(View.GONE);
			searchResultSpinner.setVisibility(View.GONE);
			Button searchResultButton = (Button) findViewById(R.id.searchResultButton);
			searchResultButton.setVisibility(View.GONE);
			Button searchResultBackButton = (Button) findViewById(R.id.searchResultBackButton);
			searchResultBackButton.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Stores the keyword in sharedpreferences and intents to AdminEditFlightInfo.class
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
		
		EditText flightSearchKeywords = (EditText) findViewById(R.id.searchKeywords);
		String searchValue = flightSearchKeywords.getText().toString();
		
		if (searchValue.length() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Keywords missing!! Try again!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
		
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		Editor editor = myPref.edit();
		editor.putString("searchkey", searchValue);
		editor.commit();
		
		Intent intent = new Intent(this, AdminEditFlightInfo.class);
	    startActivity(intent);
		
	}
	
	/**
	 * Stores the the id of flight in sharedpreferences and intents to AdminEditFlightInfoAction.class
	 * @param view android.view.View
	 */
	public void editflight(View view) {
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
		
		Spinner searchResultSpinner = (Spinner) findViewById(R.id.searchResultSpinner);
		String uniqueFlightId = String.valueOf(searchResultSpinner.getSelectedItem());
		try {
			if(Console.flightIsExist(uniqueFlightId, flightpath.getPath())) {
				SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
				Editor editor = myPref.edit();
				editor.remove("searchkey");
				editor.putString("flightid", uniqueFlightId);
				editor.commit();
				Intent intent = new Intent(this.getApplicationContext(), AdminEditFlightInfoAction.class);
				startActivity(intent);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setTitle("Select the flight's unique ID!!");
				builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					@Override
		            public void onClick(DialogInterface dialog, int which) {
		            }
		        });
				AlertDialog dialog = builder.create();
				dialog.show();
				return;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * Clicks the backtosearch button and intents to AdminEditFlightInfo.class
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
		editor.remove("searchkey");
		editor.commit();
		Intent intent = new Intent(this.getApplicationContext(), AdminEditFlightInfo.class);
		startActivity(intent);
	}
	
	/**
	 * Clicks the backtomeun button and intends to MainAdminActivity.class.
	 * @param view android.view.View
	 */
	public void backtomenu(View view) {
		SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		Editor editor = myPref.edit();
		editor.remove("searchkey");
		editor.commit();
		Intent intent = new Intent(this.getApplicationContext(), MainAdminActivity.class);
		startActivity(intent);
	}
}
