package team.csc207.travel2015;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * This is the main page of the admin activity in admin mode.
 */
public class MainAdminActivity extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_admin);
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
	 * Clicks the searchflightadmin button and intents to AdminSearchFlight.class
	 * @param view android.view.View
	 */
	public void searchflightadmin(View view) {
		Intent i = new Intent(getApplicationContext(), AdminSearchFlight.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the searchitinerariesadmin and intents to AdminSearchItineraries.class
	 * @param view android.view.View
	 */
	public void searchitinerariesadmin(View view) {
		Intent i = new Intent(getApplicationContext(), AdminSearchItineraries.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the clientinfoadmin button and intents to AdminClientInfo.class
	 * @param view android.view.View
	 */
	public void clientinfoadmin(View view) {
		Intent i = new Intent(getApplicationContext(), AdminClientInfo.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the itinerariesinfoadmin button and intents to AdminItinerariesInfo.class
	 * @param view android.view.View
	 */
	public void itinerariesinfoadmin(View view) {
		Intent i = new Intent(getApplicationContext(), AdminItinerariesInfo.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the editflightinfoadmin button and intents to AdminEditFlightInfo.class
	 * @param view android.view.View
	 */
	public void editflightinfoadmin(View view) {
		Intent i = new Intent(getApplicationContext(), AdminEditFlightInfo.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the uploadclient button and intents to AdminUploadClient.class
	 * @param view android.view.View
	 */
	public void uploadclient(View view) {
		Intent i = new Intent(getApplicationContext(), AdminUploadClient.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the uploadflight button and intents to AdminUploadFlight.class
	 * @param view android.view.View
	 */
	public void uploadflight(View view) {
		Intent i = new Intent(getApplicationContext(), AdminUploadFlight.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the logout button and intents to MainActivity.class
	 * @param view android.view.View
	 */
    public void logout(View view){
	      SharedPreferences sharedpreferences =
	    		  getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
	      Editor editor = sharedpreferences.edit();
	      editor.remove(MainConstants.keyname);
	      editor.remove(MainConstants.keypass);
	      editor.clear();
	      editor.commit();
	      SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
	      Editor editor2 = myPref.edit();
	      editor2.clear();
	      editor2.commit();
	      Intent intent = new Intent(this, MainActivity.class);
	      startActivity(intent);
	   }
}
