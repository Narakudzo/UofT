package team.csc207.travel2015;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.EditText;

/**
 * This is the page of viewing personal information in client mode.
 */
public class ClientPersonalInfo extends Activity {

	/**
	 * Calls when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_personal_info);
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		SharedPreferences myPref2 = getSharedPreferences(MainConstants.TRAVELLOGIN, MODE_PRIVATE);
		String user = myPref2.getString(MainConstants.keyname, "");
		File filedir = this.getApplicationContext().getFilesDir();
		File clientpath = new File(filedir, MainConstants.CLIENTSAVE);
		
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
		
		EditText lastname = (EditText) findViewById(R.id.editLastName);
		EditText firstname = (EditText) findViewById(R.id.editFirstName);
		EditText email = (EditText) findViewById(R.id.editEmail);
		EditText address = (EditText) findViewById(R.id.editAddress);
		EditText creditcard = (EditText) findViewById(R.id.editCreditCard);
		EditText expiry = (EditText) findViewById(R.id.editExpiry);
		lastname.setText(mySelf.getLastName());
		firstname.setText(mySelf.getFirstName());
		email.setText(mySelf.getEmail());
		address.setText(mySelf.getAddress());
		creditcard.setText(mySelf.getCreditCardNumber());
		expiry.setText(mySelf.getExpriDate());
		
	}
	
	/**
	 * Stores the update information and intents to ClientPersonalInfo.class
	 * @param view android.view.View
	 */
	public void update(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		EditText lastname = (EditText) findViewById(R.id.editLastName);
		EditText firstname = (EditText) findViewById(R.id.editFirstName);
		EditText email = (EditText) findViewById(R.id.editEmail);
		EditText address = (EditText) findViewById(R.id.editAddress);
		EditText creditcard = (EditText) findViewById(R.id.editCreditCard);
		EditText expiry = (EditText) findViewById(R.id.editExpiry);
		String editLastName = lastname.getText().toString();
		String editFirstName = firstname.getText().toString();
		String editEmail = email.getText().toString();
		String editAddress = address.getText().toString();
		String editCreditCard = creditcard.getText().toString();
		String editExpiry = expiry.getText().toString();
		
		// Check field if all fields are filled
		if (editLastName.length() == 0 || editFirstName.length() == 0 || editEmail.length() == 0 ||
				editAddress.length() == 0 || editCreditCard.length() == 0 || editExpiry.length() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Some fields are empty!! Try again!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
		
		// Check email address in regular experession
		final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(emailPattern);
	    Matcher matcher = pattern.matcher(editEmail);
		if (!matcher.find()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Invalid email format!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}

		SharedPreferences myPref2 = getSharedPreferences(MainConstants.TRAVELLOGIN, MODE_PRIVATE);
		String user = myPref2.getString(MainConstants.keyname, "");
		File filedir = this.getApplicationContext().getFilesDir();
		File clientpath = new File(filedir, MainConstants.CLIENTSAVE);
		File passwords = new File(filedir, MainConstants.PASSWORDS);
		
		// Check if the email address is already in use
		List<Client> clientList = null;
		try {
			clientList = Console.getClientDB(clientpath.getPath()).getDB();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<String> tempEmailList = new ArrayList<String>();
		for (Client tempCheckClient: clientList) {
			if (!tempCheckClient.getEmail().equals(user)) {
				tempEmailList.add(tempCheckClient.getEmail());
			}
		}
		if (tempEmailList.contains(editEmail)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Email already in use!!");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
		
		
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
		String setEmail = mySelf.getEmail();
		mySelf.setLastName(editLastName);
		mySelf.setFirstName(editFirstName);
		mySelf.setEmail(editEmail);
		mySelf.setAddress(editAddress);
		mySelf.setCreditCardNumber(editCreditCard);
		mySelf.setExpriDate(editExpiry);
		
		if (!editEmail.equals(user)) {
			mySelf.setUsername(editEmail);
			try {
				AndroidControl.setUsername(setEmail, editEmail, passwords);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Console.reBuildClientDB(mySelf, setEmail, clientpath.getPath());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Editor editor = myPref2.edit();
			editor.clear();
		    editor.commit();
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setIcon(android.R.drawable.ic_dialog_info);
			builder1.setTitle("UPDATE SUCCESSFUL!! Please re-login!!");
			builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog1 = builder1.create();
			dialog1.show();
			
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);

			
		} else {
			try {
				Console.reBuildClientDB(mySelf, setEmail, clientpath.getPath());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setIcon(android.R.drawable.ic_dialog_info);
			builder2.setTitle("UPDATE SUCCESSFUL!!");
			builder2.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(ClientPersonalInfo.this, ClientPersonalInfo.class);
					startActivity(i);
	            }
	        });
			AlertDialog dialog2 = builder2.create();
			dialog2.show();
			
			
		}
			
	}
	
	/**
	 * Stores the update password and intents to MainActivity.class
	 * @param view android.view.View
	 */
	public void updatepassword(View view) {
		SharedPreferences userDetails = 
				getSharedPreferences(MainConstants.TRAVELLOGIN, Context.MODE_PRIVATE);
		String userDetail1 = userDetails.getString(MainConstants.keyname, "");
		if (userDetail1.length() == 0) {
			Intent intent = new Intent(this, MainActivity.class);
		      startActivity(intent);
		      return;
		}
		
		EditText password1 = (EditText) findViewById(R.id.editPassword1);
		EditText password2 = (EditText) findViewById(R.id.editPassword2);
		String editPassword1 = password1.getText().toString();
		String editPassword2 = password2.getText().toString();

		if (editPassword1.length() == 0 || editPassword2.length() == 0 || !editPassword1.equals(editPassword2)) {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setIcon(android.R.drawable.ic_dialog_alert);
			builder1.setTitle("Password update unsuccessful!! Try again!!");
			builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
			AlertDialog dialog1 = builder1.create();
			dialog1.show();
			return;
		}
		
		SharedPreferences myPref2 = getSharedPreferences(MainConstants.TRAVELLOGIN, MODE_PRIVATE);
		String user = myPref2.getString(MainConstants.keyname, "");
		File filedir = this.getApplicationContext().getFilesDir();
		File clientpath = new File(filedir, MainConstants.CLIENTSAVE);
		File passwords = new File(filedir, MainConstants.PASSWORDS);
		
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
		
		mySelf.setPassword(editPassword1);
		
		try {
			AndroidControl.setPassword(user, editPassword1, passwords);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Console.reBuildClientDB(mySelf, user, clientpath.getPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Editor editor = myPref2.edit();
		editor.clear();
	    editor.commit();
	    
		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
		builder2.setIcon(android.R.drawable.ic_dialog_info);
		builder2.setTitle("Password changed!! Please re-login!!");
		builder2.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(ClientPersonalInfo.this, MainActivity.class);
				startActivity(i);
            }
        });
		AlertDialog dialog2 = builder2.create();
		dialog2.show();

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
