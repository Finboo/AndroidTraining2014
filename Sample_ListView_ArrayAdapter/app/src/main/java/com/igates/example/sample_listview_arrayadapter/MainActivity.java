package com.igates.example.sample_listview_arrayadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView myListView = (ListView) findViewById(R.id.myListView);

		final EditText myEditText = (EditText) findViewById(R.id.myEditText);

		final ArrayList<String> todoItems = new ArrayList<String>();
		final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, todoItems);

		myListView.setAdapter(myArrayAdapter);

		myEditText.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
				{
					todoItems.add(myEditText.getText().toString());
					myArrayAdapter.notifyDataSetChanged();
					myEditText.setText("");
				}
				return false;
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}
}
