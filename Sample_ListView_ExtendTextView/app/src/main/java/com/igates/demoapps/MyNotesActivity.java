package com.igates.demoapps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class MyNotesActivity extends Activity
{
	private static final int CTX_DELETE = 0;
	private static final int CTX_SHARE = 1;
	String FILE_NAME = "mynote_db.txt";

	// Create the modle for the list
	ArrayList<String> mTodoItems = new ArrayList<String>();
	ArrayAdapter<String> mArrayAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			loadStringsFromDbFile();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}

		// inflate your view
		// Get reference to UI widgets
		setContentView(R.layout.main);
		ListView myListView = (ListView) findViewById(R.id.myListView);
		final EditText myEditText = (EditText) findViewById(R.id.myEditText);

		mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listviewitem, mTodoItems);
		myListView.setAdapter(mArrayAdapter);

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
					return true;
				}
				return false;
			}
		});

		// bind model to listview
		registerForContextMenu(myListView);
		
		final SharedPreferences sp = getSharedPreferences("MyPref", MODE_PRIVATE);
		long timestamp = sp.getLong("iAgreeTimestamp", 0);
		if (timestamp > 0)
		{
			// user signed terms&conditions
			// go on to app
		}
		else
		{
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			
			adb.setTitle("Register To My Application").setNegativeButton("No", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					finish();
				}
			}).setPositiveButton("I Agree", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					
					SharedPreferences.Editor spe = sp.edit();
					spe.putLong("iAgreeTimestamp", System.currentTimeMillis());// get current time
					spe.commit(); // save to the disk
				}
			}).create().show();
		}
	}

	protected void onPause()
	{
		try
		{
			flushStringsToDbFile();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		super.onPause();
	}

	/*
	 * Load From Data-Base 
	 * We will call it from on create
	 * 
	 */
	public void loadStringsFromDbFile() throws Throwable
	{
		//opening the file
		FileInputStream fis = new FileInputStream(getFilesDir() + "/" + FILE_NAME);
		DataInputStream dis = new DataInputStream(fis);

		//Retrieve file content 
		String buff = null;
		while((buff = dis.readLine()) != null)
		{
			mTodoItems.add(buff);
		}

		fis.close();
		dis.close();
	}
	/*
	 * Write data to file 
	 * we will call it from on pause so we wont lose information
	 */
	public void flushStringsToDbFile() throws Throwable
	{
		//Open file to write 
		FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
		DataOutputStream dos = new DataOutputStream(fos);
		for(int i = 0; i < mTodoItems.size(); i++)
		{
			String currentItem = mTodoItems.get(i) + "\n";
			dos.write(currentItem.getBytes());
		}
		dos.close();
		fos.close();
	}

	
	
	/*
	 * Create the Activity menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, CTX_DELETE, 0, "Delete");
		menu.add(0, CTX_SHARE, 0, "Share");
	}

	/*
	 * Called when an item as been pressed
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
					Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}

				break;
			}
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//create menu item and put id
		MenuItem item = menu.add(0, 10, 0, "Add Note");
		item.setIcon(android.R.drawable.ic_input_add);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == 10)
		{
			findViewById(R.id.myEditText).setVisibility(View.VISIBLE);
		}
		return super.onOptionsItemSelected(item);
	}

}
