package com.igates.example.sample_lopperthread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity
{

	protected Handler mThreadHandler;
	TextView myTextView;
	static boolean isRunning = false;

	Looper mFirstLooper;

	/*
	 * Main Handler
	 */
	protected Handler mMainHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if (myTextView != null)
			{
				myTextView.setText(msg.what + "");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myTextView = (TextView) findViewById(R.id.myTextView);
		new Thread("First")
		{
			public void run()
			{
				Looper.prepare();

				/*
				 * Thread Handler
				 */
				mThreadHandler = new Handler()
				{
					public void handleMessage(Message msg)
					{
						Log.e("handleMessage", msg.what + "");
					}
				};
				mFirstLooper = Looper.myLooper();
				Looper.loop();
			};
		}.start();

		new Thread("Second")
		{
			public void run()
			{
				int i = 0;
				isRunning = true;
				while(true)
				{
					if (isRunning)
					{
						try
						{
							sleep(1000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}

						mThreadHandler.sendEmptyMessage(i);
						mMainHandler.sendEmptyMessage(i);
						i++;
					}
				}
			};
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
}
