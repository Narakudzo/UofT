package team.csc207.travel2015;

import java.io.File;

import travel2015.AndroidControl;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * This is the page of main activity in client mode.
 */
public class MainMenuActivity extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
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
	 * Clicks the searchflight button and intents to ClientSearchFlight.class
	 * @param view android.view.View
	 */
	public void searchflight(View view) {
		Intent i = new Intent(getApplicationContext(), ClientSearchFlight.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the searchitineraries button and intents to ClientSearchItineraries.class
	 * @param view android.view.View
	 */
	public void searchitineraries(View view) {
		Intent i = new Intent(getApplicationContext(), ClientSearchItineraries.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the personalinfo button and intents to ClientPersonalInfo.class
	 * @param view android.view.View
	 */
	public void personalinfo(View view) {
		Intent i = new Intent(getApplicationContext(), ClientPersonalInfo.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the itinerariesinfo button and intents to ClientItinerariesInfo.class
	 * @param view android.view.View
	 */
	public void itinerariesinfo(View view) {
		Intent i = new Intent(getApplicationContext(), ClientItinerariesInfo.class);
		startActivity(i);
	}
	
	/**
	 * Clicks the logout button and intents to MainActivity.class
	 * @param view android.view.View
	 */
    public void logout(View view) {
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
