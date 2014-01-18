package com.igates.trainings.mynotes.customadapters;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyNotesListActivity extends Activity
{
	Context mCtx;

	String FILE_NAME = "mynote_db.txt";

	// Create the model for the list
	ArrayList<NoteItem> mItemsDataBase = new ArrayList<NoteItem>();
	
	int mCurrentItem = -1;
	BaseAdapter mArrayAdapter;
	private EditText myEditText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mCtx = this;
	
		//Model
		loadStringsFromDbFile();

		//View
		setContentView(R.layout.main);
		ListView myListView = (ListView) findViewById(R.id.myListView);
		myEditText = (EditText) findViewById(R.id.myEditText);

		//Controller
		mArrayAdapter = new MyCustomBaseAdapter(getApplicationContext());
		myListView.setAdapter(mArrayAdapter);

		// bind listView to menu
		registerForContextMenu(myListView);

		myEditText.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
				{
					NoteItem toAdd = new NoteItem(myEditText.getText().toString());
					mItemsDataBase.add(0, toAdd);
					mArrayAdapter.notifyDataSetChanged();
					myEditText.setText("");
					myEditText.setVisibility(View.GONE);
					return true;
				}
				return false;
			}
		});
	}

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
		try
		{
			// opening the file
			FileInputStream fis = new FileInputStream(getFilesDir() + "/" + FILE_NAME);
			DataInputStream dis = new DataInputStream(fis);

			int size = dis.readInt();
			for(int i = 0; i < size; i++)
			{
				// get String
				int strLength = dis.readInt();
				byte[] bytes = new byte[strLength];
				dis.readFully(bytes);
				String note = new String(bytes);

				mItemsDataBase.add(note);
			}

			fis.close();
			dis.close();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
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

			dos.writeInt(mItemsDataBase.size());
			for(int i = 0; i < mItemsDataBase.size(); i++)
			{
				dos.writeInt(mItemsDataBase.get(i).task.getBytes().length);
				dos.write(mItemsDataBase.get(i).task.getBytes());
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		finally
		{
			try
			{
				dos.close();
				fos.close();
			}
			catch (IOException e)
			{
			}
		}
	}

	/*
	 * called when create the option menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// create menu item and put id
		MenuItem item = menu.add(0, 10, 0, "Add Note");
		item.setIcon(android.R.drawable.ic_input_add);

		item = menu.add(0, 20, 0, "Clear List");
		item.setIcon(android.R.drawable.btn_minus);

		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * Called when option menu item as been selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == 10)
		{
			findViewById(R.id.myEditText).setVisibility(View.VISIBLE);
		}
		else if (item.getItemId() == 20)
		{
			mItemsDataBase.clear();
			mArrayAdapter.notifyDataSetChanged();
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * Custom Adapter
	 */
	public class MyCustomBaseAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater;
		Context myContext;

		public MyCustomBaseAdapter(Context context)
		{
			mInflater = LayoutInflater.from(context);
			myContext = context;
		}
		public int getCount()
		{
			return mItemsDataBase.size();
		}
		public Object getItem(int position)
		{
			return mItemsDataBase.get(position);
		}
		public long getItemId(int position)
		{
			return position;
		}
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			View view;
			if (convertView == null)
			{
				view = mInflater.inflate(R.layout.note_line, parent, false);
			}
			else
			{
				view = convertView;
			}

			((TextView) view.findViewById(R.id.myNote)).setText(mItemsDataBase.get(position).task);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy - HH-mm");
			TextView date = (TextView) view.findViewById(R.id.rowDate);
			date.setText(sdf.format(mItemsDataBase.get(position).created));

			date.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View textViewCreated)
				{
					mCurrentItem = position;
					showDatePickerDialog(position);
				}
			});

			view.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					String[] menuItems = {"Delete","Share"};
					
					//Build the dialog 
					AlertDialog.Builder adb = new AlertDialog.Builder(mCtx);//we use activity context

					adb.setTitle("Alert Dialog").setItems(menuItems, new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface arg0, int arg1)
						{
							switch (arg1)
							{
							case 0:
							{
								//Updating the model
								mItemsDataBase.remove(position);
								flushStringsToDbFile();
								
								//Updating the view
								mArrayAdapter.notifyDataSetChanged();
								break;
							}
							case 1:
							{
								Intent i = new Intent(Intent.ACTION_SEND);

								i.setType("message/rfc822");

								i.putExtra(Intent.EXTRA_SUBJECT, "My Task");
								i.putExtra(Intent.EXTRA_TEXT, mItemsDataBase.get(position).task);
								try
								{
									startActivity(i);
								}
								catch (android.content.ActivityNotFoundException ex)
								{
									Toast.makeText(mCtx, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
								}
								break;
							}
							default:
							{
								break;
							}
							}
						}
					});

					//The Dialog itself 
					AlertDialog ad = adb.create();
					ad.show();
 				}
			});

			return view;
		}
	}

	// showDatePickerDialog
	private void showDatePickerDialog(int noteIndex)
	{
		int year, month, day;
		Calendar itemDate = null;
		if (noteIndex != -1)
		{
			itemDate = mItemsDataBase.get(noteIndex).created;

			// get the date from the note item
			if (itemDate != null)
			{
				day = itemDate.get(Calendar.DAY_OF_MONTH);
				month = itemDate.get(Calendar.MONTH);
				year = itemDate.get(Calendar.YEAR);
			}
			else
			{
				// set default time zone
				day = 1;
				month = 0;
				year = 2013;
			}

			//Creating the Dialog
			DatePickerDialog dpd = new DatePickerDialog(mCtx, myDateSetListener, year, month, day);
			dpd.setTitle("Please Enter Date");
			dpd.show();
		}
	}

	/*
	 * Listen to the the DatePickerDialog
	 */
	OnDateSetListener myDateSetListener = new OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker datePicker, int year, int month, int day)
		{
			if (mCurrentItem != -1)
			{
				//updating the model
				NoteItem item = mItemsDataBase.get(mCurrentItem);
				Calendar c = Calendar.getInstance();
				c.set(year, month, day);
				item.created = c;
				flushStringsToDbFile();
				
				//updating the view
				mArrayAdapter.notifyDataSetChanged();
				mCurrentItem = -1;
			}
		}
	};
}
