package com.example.attendance_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {
	Button viewButton;
	Button manageButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		viewButton = (Button) findViewById(R.id.button_view);
		manageButton = (Button) findViewById(R.id.button_manage);
		
		
		viewButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						View_Attendance_Activity.class);
				
				startActivity(intent);

				// data=database_helper.getAllData();
				// Message.message(MainMenu.this, data);

			}
		});
		manageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Message.message(MainMenu.this, "hello");
				Intent intent = new Intent(MainMenu.this,
						Mark_login.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
