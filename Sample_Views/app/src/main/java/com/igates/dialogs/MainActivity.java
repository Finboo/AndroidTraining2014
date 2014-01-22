package com.igates.dialogs;

import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.DateTimeKeyListener;
import android.util.TimeUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnDateSetListener
{
	Dialog m_dialog;
	MainActivity myInstance;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		myInstance = this;
		m_dialog = new Dialog(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Custom Dialog
		Button b = (Button) findViewById(R.id.StartButton);
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				m_dialog.setContentView(R.layout.custom_dialog);
				m_dialog.setTitle("Custom Dialog");
				
				TextView text = (TextView) m_dialog.findViewById(R.id.text);
				ImageView image = (ImageView) m_dialog.findViewById(R.id.image);

				text.setText(Html.fromHtml("Hello, this is a custom dialog! <a href=\"www.igates.co.il\">igates</a>"));
				image.setImageResource(R.drawable.icon);

				m_dialog.show();
			}
		});

		// Date Picker Dialog
		b = (Button) findViewById(R.id.DatePicker);
		b.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showDialog(1);
			}
		});

		// Switch to Activity AutoComplete
		b = (Button) findViewById(R.id.switchToB);
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent in = new Intent();
				in.setClass(myInstance, AutoCompleteSample.class);
				startActivity(in);
			}
		});
		
		// Switch to Activity GridView
		b = (Button) findViewById(R.id.switchToGridView);
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent in = new Intent();
				in.setClass(myInstance, GridViewSample.class);
				startActivity(in);
			}
		});
		
		// Switch to Activity WebView
		b = (Button) findViewById(R.id.switchToWebView);
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent in = new Intent();
				in.setClass(myInstance, WebViewSample.class);
				startActivity(in);
			}
		});
		
		// Progress Dialog for 3 seconds
		b = (Button) findViewById(R.id.ProgresButton);
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final ProgressDialog dialog = ProgressDialog.show(myInstance, "", "Loading... please wait....", true);
				new Thread()
				{
					public void run()
					{
						try
						{
							Thread.sleep(3000);
						}
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				}.start();
				
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		if (id == 1)
		{
			Date d = new Date(System.currentTimeMillis());
			DatePickerDialog picker = null;
			picker = new DatePickerDialog(this, odl, 2011, 3, 17);
			return picker;
		}
		return super.onCreateDialog(id);
	}

	OnDateSetListener odl = new OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			Toast.makeText(myInstance, "Date set To: "+ year + "," + monthOfYear + 1 +"," + dayOfMonth, Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
	{
	}
}