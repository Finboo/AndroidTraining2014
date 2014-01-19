package com.igates.demoapps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class ListItemView extends TextView
{
	private Paint mMarginPaint;
	private Paint mLinePaint;
	private int mPaperColor;
	private float mMargin;

	public ListItemView(Context context, AttributeSet ats, int ds)
	{
		super(context, ats, ds);
		init();
	}
	
	public ListItemView(Context context)
	{
		super(context);
		init();
	}

	public ListItemView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		// Get a reference to our resource table.
		Resources myResources = getResources();
		
		// Create the paint brushes we will use in the onDraw method.
		mMarginPaint = new Paint();
		mMarginPaint.setColor(myResources.getColor(R.color.notepad_margin));

		mLinePaint = new Paint();
		mLinePaint.setColor(myResources.getColor(R.color.notepad_lines));
		
		// Get the paper background color and the margin width.
		mPaperColor = myResources.getColor(R.color.notepad_paper);
		mMargin = myResources.getDimension(R.dimen.notepad_margin);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		// Color as paper
		canvas.drawColor(mPaperColor);
		
		// Draw ruled lines
		canvas.drawLine(0, 0, getMeasuredWidth(), 0, mLinePaint);
		canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), mLinePaint);
		
		// Draw margin
		canvas.drawLine(mMargin, 0, mMargin, getMeasuredHeight(), mMarginPaint);

		// Move the text across from the margin
//		canvas.save();
		canvas.translate(mMargin, 0);
		
		// Use the TextView to render the text.
		super.onDraw(canvas);
//		canvas.restore();
	}
}
