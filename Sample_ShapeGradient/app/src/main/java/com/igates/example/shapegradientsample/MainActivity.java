package com.igates.example.shapegradientsample;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_menu, menu);
		return true;
	}
	AlertDialog ad ;
	@Override
	public void onBackPressed()
	{
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		
		b.setTitle("Are you sure you want to exit?").setNegativeButton("No", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				ad.dismiss();
			}
		}).setPositiveButton("Yes", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				ad.dismiss();
				finish();
			}
		});
		
		ad = b.create();
		ad.show();
	}
}
