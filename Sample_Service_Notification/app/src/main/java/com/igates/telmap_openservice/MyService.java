package com.igates.telmap_openservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service
{

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Toast.makeText(getApplicationContext(), "Service started from onStartCommand", Toast.LENGTH_LONG).show();
		
		new Thread("My Thread")
		{
			@Override
			public void run()
			{
				super.run();
				try
				{
					sleep(10000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				
				//Starting the Notification
				passedLimitNotification();
			}
		}.start();
		return super.onStartCommand(intent, flags, startId);
	}

	public void passedLimitNotification()
	{
		Context ctx = getApplicationContext();
		NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

		// ////////////////////////////////
		// setup the notification intent //
		// ////////////////////////////////
		Intent notificationIntent = new Intent();
		notificationIntent.setClassName(ctx, MainActivity.class.getName());

		// create the pending intent that wraps the intent to be fired within the notification //
		// /////////////////////////////////////////////////////////////////////////////////// //
		PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		// setup notification properties //
		// ///////////////////////////// //
		long when = System.currentTimeMillis();

		CharSequence tickerText = "Hi This is my notification";
		CharSequence contentTitle = "iGates";
		CharSequence contentText = "Hello";
		
		// build the notification API 11 and up //
		// //////////////////////////////////// //
		Notification notification = new Notification.Builder(ctx).setWhen(when).setContentText(contentText).setContentIntent(contentIntent)
				.setContentTitle(contentTitle).
				// setAutoCancel(true).
				setSmallIcon(R.drawable.ic_launcher).
				// setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo)).
				setTicker(tickerText).getNotification();

 		//Set Flag for Auto Cancel Notification 
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		// we leave the notificationID as the same value, since we want it to override older with
		// same id
		nm.notify(0, notification);
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
}
