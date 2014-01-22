package com.igates.example.sample_urlconnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class MainActivity extends Activity
{

	//Model
	ArrayList<Drawable> myImages = new ArrayList<Drawable>();
	
	//Controller
	ImageAdapter mImageAdapter;

	//View
	Gallery mGallery;
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			mImageAdapter.notifyDataSetChanged();
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		mGallery = (Gallery) findViewById(R.id.myGallery);
		Button load = (Button) findViewById(R.id.loadImages);

		mImageAdapter = new ImageAdapter(getApplicationContext());
		mGallery.setAdapter(mImageAdapter);
		
		AdapterView.OnItemSelectedListener oisl = new Listener();
		mGallery.setOnItemSelectedListener(oisl);

		load.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new Thread()
				{
					public void run()
					{
						getBitmapFromURL("http://www.mugraby-hostel.com/wp-content/uploads/2010/01/Telaviv-City-Beach.jpg");
						getBitmapFromURL("http://gloholiday.com/wp-content/uploads/2013/11/Holiday-Apartments-in-Tel-Aviv.jpg");
					};
				}.start();
			}
		});
	}

	public void getBitmapFromURL(String imageUrl)
	{
		try
		{
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			Drawable d = new BitmapDrawable(getResources(), myBitmap);
			
			myImages.add(d);
			mHandler.sendEmptyMessage(1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class ImageAdapter extends BaseAdapter
	{
		private Context mContext;

		public ImageAdapter(Context c)
		{
			mContext = c;
		}

		public int getCount()
		{
			return myImages.size();
		}

		public Object getItem(int position)
		{
			return myImages.get(position);
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView i = new ImageView(mContext);

			Drawable d = myImages.get(position);
			i.setBackgroundDrawable(d);
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			return i;
		}
	}
	
	/*
	 * listen to the current image shown in the gallery and set it to the Main view
	 */
	class Listener implements AdapterView.OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView parent, View v, int position, long id)
		{
			setMainImage(position);
		}

		@Override
		public void onNothingSelected(AdapterView parent)
		{
		}
	}
	
	public void setMainImage(int position)
	{
		((ImageView) findViewById(R.id.mainImage)).setImageDrawable(myImages.get(mGallery.getSelectedItemPosition()));
	}
}
