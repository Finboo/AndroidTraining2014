package com.igates.example.sample_startactivityforresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		Button b = (Button) findViewById(R.id.myButton);

		b.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int result = 1;
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", result);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});

		// Intent returnIntent = new Intent();
		// setResult(RESULT_CANCELED, returnIntent);
		// finish();

	}
}
