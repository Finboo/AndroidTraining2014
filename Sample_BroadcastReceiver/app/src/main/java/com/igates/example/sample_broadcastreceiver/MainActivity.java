package com.igates.example.sample_broadcastreceiver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		TextView broadCastAnswer = (TextView) findViewById(R.id.broadcastAnswer);
		TextView body = (TextView) findViewById(R.id.body);
		
		Intent in = getIntent();
		Bundle data = in.getExtras();
		
		if(data != null)
		{
			body.setVisibility(View.VISIBLE);
			String answer = data.getString("fromBroadcast", "null");
			broadCastAnswer.setText(answer);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
