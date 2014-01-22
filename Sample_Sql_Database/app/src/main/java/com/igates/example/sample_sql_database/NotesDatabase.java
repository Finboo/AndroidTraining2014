package com.igates.example.sample_sql_database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDatabase extends SQLiteOpenHelper
{
	private static NotesDatabase sNotesDatabase;

	public static NotesDatabase getInstance(Context ctx)
	{
		if(sNotesDatabase == null)
		{
			sNotesDatabase = new NotesDatabase(ctx);
		}
		return sNotesDatabase;
	}

	// Database Version
	public static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "NotesManager";

	// Notes table name
	public static final String TABLE_NOTES = "Notes";

	// Note Keys
	public static final String KEY_ID = "_id";
	public static final String KEY_TIME_STAMP = "time_stamp";
	public static final String KEY_LABEL = "label";

	private NotesDatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_Notes_TABLE = "CREATE TABLE " + TABLE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
																														 KEY_LABEL + " TEXT," +
																														 KEY_TIME_STAMP + " BIG INT" + ")";
		db.execSQL(CREATE_Notes_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);

		// Create tables again
		onCreate(db);
	}

	public Cursor getAllNotes()
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES + " ORDER BY " + KEY_ID + " DESC", null);
		return cursor;
	}

	public Boolean addNotes(Note noteItem)
	{
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LABEL, noteItem.mLabel);
		values.put(KEY_TIME_STAMP, noteItem.mTimeStamp);

		// Inserting Row
		db.insert(TABLE_NOTES, null, values);
		db.close(); // Closing database connection

		return true;
	}

	public void deleteAll()
	{
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NOTES, null, null);
		db.close();
		// TODO Auto-generated method stub

	}
}
