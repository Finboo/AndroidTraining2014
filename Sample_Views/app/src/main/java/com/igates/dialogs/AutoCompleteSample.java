package com.igates.dialogs;

import java.lang.reflect.Array;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class AutoCompleteSample extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_complete_sample);

		//The Model
		final String[] COUNTRIES = new String[] { "Belgium", "France", "Italy", "Germany", "Spain" };

		//Get The AutoCompleteTextView in code
		final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.myAutoComplete);
		
		//Bind the Model to a controller and View
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.line, COUNTRIES);
		textView.setAdapter(adapter);

		textView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				String s = ((TextView) arg1).getText().toString();
				Log.e("AutoCompleteSample", s + " position: " + getArrayIndex(COUNTRIES, s));
			}
		});
	}

	public final int getArrayIndex(String[] myArray, String myObject)
	{
		int ArraySize = Array.getLength(myArray);// get the size of the array
		for(int i = 0; i < ArraySize; i++)
		{
			if (myArray[i].equals(myObject))
			{
				return (i);
			}
		}
		return (-1);// didn't find what I was looking for
	}
}
