package com.igates.example.sample_broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver
{
	
	/*
	 * (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 * 
	 * do in shell sample broadcast 
	 * adb shell am broadcast -a com.igates.example.sample_broadcastreceiver.hello
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Toast.makeText(context, "MyReceiver received Broadcast!! ", Toast.LENGTH_SHORT).show();
		Intent in = new Intent();
		in.setClass(context, MainActivity.class);
		in.putExtra("fromBroadcast", intent.getAction());
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(in);
	}

}
