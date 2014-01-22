package com.igates.sample.bindservice;

import java.util.Random;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class LocalService extends Service
{
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();
	// Random number generator
	private final Random mGenerator = new Random();

	/**
	 * Class used for the client Binder. Because we know this service always runs in the same
	 * process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder
	{
		LocalService getService()
		{
			// Return this instance of LocalService so clients can call public methods
			return LocalService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return mBinder;
	}

	/** method for clients */
	public int getRandomNumber()
	{
		return mGenerator.nextInt(100);
	}
	
	
	/** method for clients */
	public void downloadFile(final Handler activityHandler)
	{
		new Thread("download file")
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
					activityHandler.sendMessage(msg);
				}
			};
			
		}.start();
	}
}