package com.igates.trainings.mynotes.customadapters;


public class NoteItem
{
	public String task;
	public long created;

	public NoteItem(String _task)
	{
		task = _task;
		created = java.lang.System.currentTimeMillis();
	}

	public NoteItem(String _task, long _created)
	{
		task = _task;
		created = _created;
	}
}