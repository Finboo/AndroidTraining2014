package com.igates.example.sample_startactivityforresult;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button a = (Button) findViewById(R.id.goToB);
		a.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(getApplicationContext(), SecondActivity.class);
				startActivityForResult(i, 1);
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == 1)
		{
			if(resultCode == RESULT_OK)
			{
				String result = data.getStringExtra("result");
			}
			if(resultCode == RESULT_CANCELED)
			{
				// Write your code if there's no result
			}
		}
	}// onActivityResult

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
