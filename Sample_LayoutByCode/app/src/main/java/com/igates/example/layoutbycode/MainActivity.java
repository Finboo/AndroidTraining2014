package com.igates.example.layoutbycode;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//Create Main Layout View-Group with some parameters
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundColor(Color.BLUE);
		
		//Create the leaf View's with Values 	
		TextView myTextView = new TextView(this);
		myTextView.setText("Enter Text Below");

		EditText myEditText = new EditText(this);
		myEditText.setHint("Text Goes Here!");
		
		//Make Layout parameters to the leaf view's
		int lHeight = LinearLayout.LayoutParams.MATCH_PARENT;
		int lWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
		
		//Adding The view's to the View-Group with parameters
		ll.addView(myTextView, new LinearLayout.LayoutParams(lHeight, lWidth));
		ll.addView(myEditText, new LinearLayout.LayoutParams(lHeight, lWidth));
		
		//Inflate the LinearLayout to the window
		setContentView(ll);
	}
}
