package team.csc207.travel2015;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import android.widget.EditText;
import android.widget.Spinner;

/**
 * This is the search itineraries page in admin mode.
 */
public class AdminSearchItineraries extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_search_itineraries);
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		File filedir = this.getApplicationContext().getFilesDir();
		File clientpath = new File(filedir, MainConstants.CLIENTSAVE);
		
		List<Client> myClientList = null;
		try {
			myClientList = Console.getClientDB(clientpath.getPath()).getDB();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		Spinner clientlist = (Spinner) findViewById(R.id.myclient);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.add("Please choose one client's email.");
		adapter.add("*If you are searching for yourself,");
		adapter.add("choose one from below randomly.");
		adapter.add("*Choose email, not his/her name.");
		for (Client tempClient: myClientList) {
			adapter.add(" ");
			adapter.add(tempClient.getFirstName() + " " + tempClient.getLastName());
			adapter.add(tempClient.getEmail());
		}
		adapter.add(" ");
		clientlist.setAdapter(adapter);
	}
	
	/**
	 * Stores the departure date, origin, destination, sorted method and client's email in sharedpreferences and
	 * intents to AdminSearchItinerariesBook.class
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
		
		// First check email is selected.
		File filedir = this.getApplicationContext().getFilesDir();
		File clientpath = new File(filedir, MainConstants.CLIENTSAVE);
		
		List<Client> myClientList = null;
		try {
			myClientList = Console.getClientDB(clientpath.getPath()).getDB();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Spinner myclient = (Spinner) findViewById(R.id.myclient);
        String clientemail = String.valueOf(myclient.getSelectedItem());
        
        List<String> tempClientEmailList = new ArrayList<String>();
        for (Client tempClient: myClientList) {
        	tempClientEmailList.add(tempClient.getEmail());
        }
        if (!tempClientEmailList.contains(clientemail)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Please choose valid client's email!!");
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
		
		EditText editOrigin = (EditText) findViewById(R.id.editOrigin);
        String origin = editOrigin.getText().toString();
        EditText editDestination = (EditText) findViewById(R.id.editDestination);
        String destination = editDestination.getText().toString();
        Spinner year = (Spinner) findViewById(R.id.year);
        Spinner month = (Spinner) findViewById(R.id.month);
        Spinner date = (Spinner) findViewById(R.id.date);
        Spinner sorting = (Spinner) findViewById(R.id.sortby);
        String sortby = String.valueOf(sorting.getSelectedItem());
        String departuredate = String.valueOf(year.getSelectedItem()) + 
        		"-" + String.valueOf(month.getSelectedItem()) +
        		"-" + String.valueOf(date.getSelectedItem());
		Editor editor = myPref.edit();
		editor.putString("origin", origin);
		editor.putString("destination", destination);
		editor.putString("departuredate", departuredate);
		editor.putString("myclient", clientemail);
		editor.putString("sortby", sortby);
		editor.commit();

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
        
        if (itinerariesResult.isEmpty()) {
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
		editor2.putInt("searchresult", itinerariesResult.size());
		editor2.commit();
		
		Intent intent = new Intent(this.getApplicationContext(), AdminSearchItinerariesBook.class);
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