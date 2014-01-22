package com.igates.telmap_openservice;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button openServiceExplicit = (Button) findViewById(R.id.myButtonExplicit);
		Button openServiceImplicit = (Button) findViewById(R.id.myButtonImplicit);
		
		//If There are notification pending kill it
		NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(0);
		
		openServiceExplicit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				//Create intent with explicit class 
				Intent in = new Intent();
				in.setClass(getApplicationContext(), MyService.class);
				
				//start the service  
				startService(in);
			}
		});
		
		openServiceImplicit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				//Create intent with implicit Action 
				Intent in = new Intent("shay_telmap");
				startService(in);
			}
		});
		
	}
}
