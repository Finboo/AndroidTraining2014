package com.igates.sample.bindservice;

import com.igates.sample.bindservice.LocalService.LocalBinder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity
{
	LocalService mService;
	boolean mBound = false;
	Context mCtx;

	ProgressDialog mDialog;

	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Bundle b = msg.getData();
			int progress = b.getInt("progress");
			if (progress != 100)
			{
				mDialog.setProgress(progress);
			}
			else
			{
				mDialog.dismiss();
				Toast.makeText(mCtx, "progress done", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCtx = this;

		Button b = (Button) findViewById(R.id.getServiceSomething);
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mBound)
				{
					mDialog = new ProgressDialog(MainActivity.this);
					mDialog.setTitle("Donwloading...");
					mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					mDialog.show();

					mService.downloadFile(mHandler);
				}
				else
				{
					Toast.makeText(mCtx, "Download Service is NOT Available!!!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		// Bind to LocalService
		Intent intent = new Intent(this, LocalService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		// Unbind from the service
		if (mBound)
		{
			unbindService(mConnection);
			mBound = false;
		}
	}

	/**
	 * Called when a button is clicked (the button in the layout file attaches to this method with
	 * the android:onClick attribute)
	 */
	public void onButtonClick(View v)
	{
		if (mBound)
		{
			// Call a method from the LocalService.
			// However, if this call were something that might hang, then this request should
			// occur in a separate thread to avoid slowing down the activity performance.
			int num = mService.getRandomNumber();
			Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
		}
	}

	public void bindService(View v)
	{
		// Bind to LocalService
		Intent intent = new Intent(this, LocalService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	public void unBindService(View v)
	{
		// Unbind from the service
		if (mBound)
		{
			unbindService(mConnection);
			mBound = false;
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
			Toast.makeText(mCtx, "Service is Bounded!", Toast.LENGTH_SHORT).show();
		}

		/**
		 * This is called when the connection with the service has been unexpectedly disconnected
		 * that is, its process gets crashed.
		 */
		@Override
		public void onServiceDisconnected(ComponentName arg0)
		{
			mBound = false;
			Toast.makeText(mCtx, "Service is Disconected!", Toast.LENGTH_SHORT).show();
		}
	};
}