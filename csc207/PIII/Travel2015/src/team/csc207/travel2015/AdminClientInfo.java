package team.csc207.travel2015;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import travel2015.AndroidControl;
import travel2015.Client;
import travel2015.Console;
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
 * This is the client information page in Admin mode.
 */
public class AdminClientInfo extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_client_info);
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
		adapter.add("SELECT CLIENT TO VIEW/EDIT");
		adapter.add("*Choose email, not his/her name.");
		for (Client tempClient: myClientList) {
			adapter.add(" ");
			adapter.add(tempClient.getFirstName() + " " + tempClient.getLastName());
			adapter.add("ADDR: " + tempClient.getAddress());
			adapter.add("CC: " + tempClient.getCreditCardNumber());
			adapter.add("EXP: " + tempClient.getExpriDate());
			adapter.add(tempClient.getEmail());
		}
		adapter.add(" ");
		clientlist.setAdapter(adapter);
		
	}
	
	/**
	 * Stores email address in sharedpreference and intents to AdminClientInfoEdit.class.
	 * @param view android.view.View
	 */
	public void view(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		    startActivity(intent);
		    return;
		}
		
		// First, check email is selected.
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
        
        // Register email address to SharedPreferences and intent to AdminClientInfoEdit.class
        SharedPreferences myPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        Editor editor = myPref.edit();
		editor.putString("myclient", clientemail);
		editor.commit();
		Intent intent = new Intent(this.getApplicationContext(), AdminClientInfoEdit.class);
		startActivity(intent);
 
	}
	
	/**
	 * Clicks the backtomeun button and intends to MainAdminActivity.class.
	 * @param view android.view.View
	 */
	public void backtomenu(View view) {
		Intent intent = new Intent(this.getApplicationContext(), MainAdminActivity.class);
		startActivity(intent);
	}
}
