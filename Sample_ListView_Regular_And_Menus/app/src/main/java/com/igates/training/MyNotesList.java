package com.igates.training;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MyNotesList extends Activity
{
	private static final int CTX_DELETE = 0;
	private static final int CTX_SHARE = 1;
	String FILE_NAME = "mynote_db.txt";

	ArrayList<String> mTodoItems;
	ArrayAdapter<String> mArrayAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ListView myListView = (ListView) findViewById(R.id.myListView);
		final EditText myEditText = (EditText) findViewById(R.id.myEditText);

		loadStringsFromDbFile();
		mTodoItems = new ArrayList<String>();
		for(int i = 0; i < 5; i++)
		{
			mTodoItems.add("Item # " + i);
		}
		mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mTodoItems);
		myListView.setAdapter(mArrayAdapter);

		registerForContextMenu(myListView);

		myEditText.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
				{
					mTodoItems.add(0, myEditText.getText().toString());
					mArrayAdapter.notifyDataSetChanged();
					myEditText.setText("");
					myEditText.setVisibility(View.GONE);
					return true;
				}
				return false;
			}
		});
	}

	@Override
	protected void onPause()
	{
		flushStringsToDbFile();
		super.onPause();
	}

	/*
	 * Load From Data-Base We will call it from on create
	 */
	public void loadStringsFromDbFile()
	{
		FileInputStream fis = null;
		DataInputStream dis = null;
		try
		{
			if (mTodoItems == null)
			{
				mTodoItems = new ArrayList<String>();
			}
			else
			{
				mTodoItems.clear();
			}
			// opening the file
			fis = new FileInputStream(getFilesDir() + "/" + FILE_NAME);
			dis = new DataInputStream(fis);

			// Retrieve file content
			String buff = null;
			while((buff = dis.readLine()) != null)
			{
				mTodoItems.add(buff);
			}

			fis.close();
			dis.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	/*
	 * Write data to file we will call it from on pause so we wont lose information
	 */
	public void flushStringsToDbFile()
	{
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try
		{
			// Open file to write
			fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
			dos = new DataOutputStream(fos);
			for(int i = 0; i < mTodoItems.size(); i++)
			{
				String currentItem = mTodoItems.get(i) + "\n";
				dos.write(currentItem.getBytes());
			}
			dos.close();
			fos.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		finally
		{
			try
			{
				fos.close();
				dos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * Create the Activity menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, CTX_DELETE, 0, getString(R.string.remove));
		menu.add(0, CTX_SHARE, 0, getString(R.string.share));
	}

	/*
	 * Called when an item as benn pressed
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = aInfo.position;

		if (!super.onContextItemSelected(item))
		{
			switch (item.getItemId())
			{
			case CTX_DELETE:
				mTodoItems.remove(position);
				mArrayAdapter.notifyDataSetChanged();
				break;
			case CTX_SHARE:

				Intent i = new Intent(Intent.ACTION_SEND);
				// i.setType("text/plain");
				i.setType("message/rfc822");

				i.putExtra(Intent.EXTRA_SUBJECT, "My Task");
				i.putExtra(Intent.EXTRA_TEXT, mTodoItems.get(position));
				try
				{
					startActivity(i);
					// startActivity(Intent.createChooser(i, "Send mail..."));
				}
				catch (android.content.ActivityNotFoundException ex)
				{
					Toast.makeText(this, getString(R.string.no_email_clients), Toast.LENGTH_SHORT).show();
				}

				break;
			}
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// create menu item and put id
		MenuItem item = menu.add(0, 10, 0, getString(R.string.add_new));
		item.setIcon(android.R.drawable.ic_input_add);
		
		menu.add(0, 11, 0, "Delete All");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == 10)
		{
			findViewById(R.id.myEditText).setVisibility(View.VISIBLE);
		}
		else if(item.getItemId() == 11)
		{
			mTodoItems.clear();
			mArrayAdapter.notifyDataSetChanged();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			Toast.makeText(getApplicationContext(), "Configuration.ORIENTATION_PORTRAIT", Toast.LENGTH_LONG).show();
		}
		else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			Toast.makeText(getApplicationContext(), "Configuration.ORIENTATION_LANDSCAPE", Toast.LENGTH_LONG).show();
		}
	}
}