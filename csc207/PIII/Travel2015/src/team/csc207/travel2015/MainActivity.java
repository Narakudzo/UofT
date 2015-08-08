package team.csc207.travel2015;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import travel2015.*;

/**
 * This is the login page of the main activity.
 */
public class MainActivity extends Activity {
   
	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Stores the login information in sharedpreference and intents to MainAdminActivity.class 
	 * or MainMenuActivity.class
	 * @param view android.view.View
	 */
	public void login(View view) {
		EditText username = (EditText) findViewById(R.id.editTextUsername);
		EditText password = (EditText) findViewById(R.id.editTextPassword);
		String user = username.getText().toString();
		String pass = password.getText().toString();
	    File filedir = this.getApplicationContext().getFilesDir();
	    File passfile = new File(filedir, MainConstants.PASSWORDS);
	    SharedPreferences sharedpreferences = getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		if (AndroidControl.Login(user, pass, passfile)) {
			Editor editor = sharedpreferences.edit();
			editor.putString(MainConstants.keyname, user);
			editor.putString(MainConstants.keypass, pass);
			editor.commit();
			if (AndroidControl.getLoginType(user, pass, passfile).equals(MainConstants.CLIENT)) {
				Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
				startActivity(i);
			} else if (AndroidControl.getLoginType(user, pass, passfile).equals(MainConstants.ADMIN)) {
				Intent i = new Intent(getApplicationContext(), MainAdminActivity.class);
				startActivity(i);
			}
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Login failed. Please try again.");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		
	}
}
