package com.igates.trainnings.progressbars;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity
{
	
	static ProgressDialog sDialog;
	static Context sCtx;
	static Handler sHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Bundle b = msg.getData();
			int progress = b.getInt("progress");
			if(progress != 100)
			{
				sDialog.setProgress(progress);
			}
			else
			{
				sDialog.dismiss();
				Toast.makeText(sCtx, "progress done", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sCtx = this;
		
		Button b =  (Button) findViewById(R.id.startServiceButton);
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sDialog = new ProgressDialog(MainActivity.this);
				sDialog.setTitle("Donwloading...");
				sDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				sDialog.show();
				
				Intent newService = new Intent();
				newService.setClassName(getApplicationContext(), MyService.class.getName());
				startService(newService);
			}
		});
	}
}
