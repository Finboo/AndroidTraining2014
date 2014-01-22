package com.igates.example.sample_sql_database;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CopyOfMainActivity extends Activity
{
	CursorAdapter mArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// View
		setContentView(R.layout.activity_main);
		final EditText et = (EditText) findViewById(R.id.myEditText);
		ListView lv = (ListView) findViewById(R.id.myListView);

		// Model (link)
		Cursor c = NotesDatabase.getInstance(getApplicationContext()).getAllNotes();

		// Controller
		mArrayAdapter = new myCustomCursorAdapter(getApplicationContext(), c, CursorAdapter.FLAG_AUTO_REQUERY);
		lv.setAdapter(mArrayAdapter);

		et.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
				{
					Note toAdd = new Note(et.getText().toString());

					NotesDatabase.getInstance(getApplicationContext()).addNotes(toAdd);
					Cursor c = NotesDatabase.getInstance(getApplicationContext()).getAllNotes();
					mArrayAdapter.changeCursor(c);

					et.setText("");
					et.setVisibility(View.GONE);
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.add_note:
			{
				findViewById(R.id.myEditText).setVisibility(View.VISIBLE);
				break;
			}
			case R.id.clear_list:
			{
				NotesDatabase.getInstance(getApplicationContext()).deleteAll();
				Cursor c = NotesDatabase.getInstance(getApplicationContext()).getAllNotes();
				mArrayAdapter.changeCursor(c);
				break;
			}
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	class myCustomCursorAdapter extends CursorAdapter
	{
		LayoutInflater mLayoutInflater;

		public myCustomCursorAdapter(Context context, Cursor c, int flags)
		{
			super(context, c, flags);
			mLayoutInflater = LayoutInflater.from(context);
		}
		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{
			((TextView) view.findViewById(R.id.myNote)).setText(cursor.getString(cursor.getColumnIndex(NotesDatabase.KEY_LABEL)));

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy - HH-mm");
			TextView date = (TextView) view.findViewById(R.id.rowDate);
			date.setText(sdf.format(cursor.getLong(cursor.getColumnIndex(NotesDatabase.KEY_TIME_STAMP))));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			View view = mLayoutInflater.inflate(R.layout.note_line, parent, false);
			return view;
		}
	}
}
