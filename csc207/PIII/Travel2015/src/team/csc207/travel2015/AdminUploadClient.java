package team.csc207.travel2015;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import travel2015.Console;

/**
 * This is the page which can upload the client information in admin mode.
 */
public class AdminUploadClient extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_upload_client);
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		File clientdir = this.getApplicationContext().getFilesDir();
	    File clientfile = new File(clientdir, MainConstants.CLIENTUPLOAD);
		TextView textElement = (TextView) findViewById(R.id.clientpath);
		textElement.setText(clientfile.getPath());
	}
	
	/**
	 * Stores the client information in sharedpreference and intents to MainAdminActivity.class
	 * @param view android.view.View
	 */
	public void upload(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		File clientdir = this.getApplicationContext().getFilesDir();
	    File clientfile = new File(clientdir, MainConstants.CLIENTUPLOAD);
	    File clientsave = new File(clientdir, MainConstants.CLIENTSAVE);
	    File passwordfile = new File(clientdir, MainConstants.PASSWORDS);
	    
	    try {
			Console.buildNewClientDB(clientfile.getPath(), clientsave.getPath(), 
					MainConstants.CLIENT, passwordfile.getPath(), MainConstants.ADMIN);
		} catch (IOException e) {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setIcon(android.R.drawable.ic_dialog_alert);
			builder1.setTitle("Upload failed!! Try again!!");
			builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder1.create();
			dialog.show();
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("Upload success!!");
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(AdminUploadClient.this, MainAdminActivity.class);
				startActivity(intent);
            }
        });
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Clicks the backtomenu button and intent to MainAdminActivity.class
	 * @param view android.view.View
	 */
	public void backtomenu(View view) {
		Intent intent = new Intent(this.getApplicationContext(), MainAdminActivity.class);
		startActivity(intent);
	}
}
