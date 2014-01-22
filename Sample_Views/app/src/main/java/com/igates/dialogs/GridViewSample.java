package com.igates.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GridViewSample extends Activity
{
	GridView gridView;
	Context mCtx;
	
	// Model. references to our images
	private Integer[] mThumbIds = { R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mCtx = this;

		//View
		// Inflate Resource to window
		setContentView(R.layout.grid_view_sample);
		gridView = (GridView) findViewById(R.id.gridView1);

		//Controller
		ImageAdapter ia = new ImageAdapter(this);
		gridView.setAdapter(ia);

		//Set listener for the gridView we will get each item with position
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				Toast.makeText(mCtx, "" + position, Toast.LENGTH_SHORT).show();
			}
		});

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
			return mThumbIds.length;
		}

		public Object getItem(int position)
		{
			return null;
		}

		public long getItemId(int position)
		{
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView;
			if (convertView == null)
			{ 
				//If it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			}
			else
			{
				//Recycled
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}
	}
}
