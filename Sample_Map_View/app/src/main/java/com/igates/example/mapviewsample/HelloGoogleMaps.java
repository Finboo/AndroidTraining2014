package com.igates.example.mapviewsample;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.gms.maps.MapView;
import com.google.android.maps.OverlayItem;

public class HelloGoogleMaps extends MapActivity
{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
	}

	public void addOverlay(OverlayItem overlay)
	{
		mOverlays.add(overlay);

	//	populate();
	}

	public void HelloItemizedOverlay(Drawable defaultMarker)
	{
		//super(boundCenterBottom(defaultMarker));
	}

	//@Override
	protected boolean onTap(int index)
	{
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
}
