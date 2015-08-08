package team.csc207.lab8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EntryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry);
	}
	
	public void submitData(View view) {
		Intent intent = new Intent(this, DisplayActivity.class);
		
		EditText dataField = (EditText) findViewById(R.id.data_field);
		String data = dataField.getText().toString();
		
		intent.putExtra("dataKey", data);
		startActivity(intent);
		
	}
}
