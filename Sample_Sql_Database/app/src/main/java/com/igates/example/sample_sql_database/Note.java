package com.igates.example.sample_sql_database;

public class Note
{
	public String mLabel;
	public Long mTimeStamp;
	
	public Note(String labelString)
	{
		mLabel = labelString;
		mTimeStamp = System.currentTimeMillis();
	}

	public Note(String labelString, long lo)
	{
		mLabel = labelString;
		mTimeStamp = lo;
	}
}
