package com.igates.trainnings.progressbars;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class MyService extends Service
{

	static int processCount = 0;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// Normally we would do some work here, like download a file.
		// For our sample, we just sleep for 5 seconds.
		new Thread("DownLoad file")
		{
			public void run()
			{
				for(int i = 0; i < 101; i++)
				{
					try
					{
						sleep(50);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putInt("progress", i);
					msg.setData(b);
					MainActivity.sHandler.sendMessage(msg);
				}
			};
		}.start();
		Toast.makeText(getApplicationContext(), "starting service", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		Toast.makeText(getApplicationContext(), "stopping service - process count " + processCount, Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
