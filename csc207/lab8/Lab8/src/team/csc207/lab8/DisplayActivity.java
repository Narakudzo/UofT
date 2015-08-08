package team.csc207.lab8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		
		Intent intent = getIntent();
		String data = (String) intent.getSerializableExtra("dataKey");
		TextView newlySubmitted = (TextView) findViewById(R.id.submitted_data);
		newlySubmitted.setText(data);
	}
}
