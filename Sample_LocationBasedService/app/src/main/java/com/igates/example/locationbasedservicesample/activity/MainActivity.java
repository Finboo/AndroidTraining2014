package com.igates.example.locationbasedservicesample.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.igates.example.locationbasedservicesample.R;
import com.igates.example.locationbasedservicesample.data.RouteLocation;
import com.igates.example.locationbasedservicesample.database.RouteDatabase;

public class MainActivity extends Activity implements LocationListener
{

	private static final String ROUTE_INDEX = "routeIndex";
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000000; // in Milliseconds
	private static final String ROUTE_PREFERENCES = "route_preferences";
	String routeName;
	protected LocationManager mLocationManager;
	GoogleMap mMap;
	RouteDatabase sqlHelper;

	@Override
	public void onCreate(Bundle savedInstanceState){
		routeName = getIntent().getExtras().getString("routeName");
		super.onCreate(savedInstanceState);
	    sqlHelper = new RouteDatabase(this);
		

		setContentView(R.layout.activity_main);
	
		Button zoomToCurrent = (Button) findViewById(R.id.zoomToCurrent);
		Button navigateButton = (Button) findViewById(R.id.navigate);

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		setUpMapIfNeeded();
		
		
		zoomToCurrent.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				showCurrentLocation();
			}
		});
		
		
		navigateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(sqlHelper.isRoutTableNotEmpty()){
					Intent navigateIntent = new Intent(getBaseContext(), NavigateActivity.class);
					startActivity(navigateIntent);
				}else{
					Toast.makeText(getApplicationContext(), "There are no locations, cant navigate ..." , Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	
	
	protected void showCurrentLocation()
	{
		
		mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, this.getMainLooper());
		

	}


	private void setUpMapIfNeeded(){
		// Do a null check to confirm that we have not already instantiated the map.
		if(mMap == null)
		{
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if(mMap != null)
			{
				// The Map is verified. It is now safe to manipulate the map.
				mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener()
				{
					@Override
					public void onMyLocationChange(Location arg0)
					{

					}
				});
				
				mMap.setOnMapClickListener(new OnMapClickListener() {
					
					@Override
					public void onMapClick(final LatLng latLng) {
						
						final Dialog d = new Dialog(MainActivity.this);
						d.setContentView(R.layout.dialog);
						d.setTitle("Enter new point");
						d.show();
						
						Button myButton = (Button)d.findViewById(R.id.dialogButton);

						Button cancelButton = (Button)d.findViewById(R.id.cancelButton);
						
						myButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								EditText hintEditText = (EditText)d.findViewById(R.id.hint);
								String hint = hintEditText.getText().toString();
								RouteLocation routeLocation = new RouteLocation(routeName, latLng.latitude, latLng.longitude, hint);
								long rowId = sqlHelper.addRoute(routeLocation );
								d.dismiss();
								Toast.makeText(MainActivity.this, "Added new route " + rowId, Toast.LENGTH_LONG).show();;
								
							}
						});
						cancelButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {

								d.dismiss();
								
							}
						});
					}
					
				});
			}
		}
		
		Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if(location != null){
			onLocationChanged(location);
		}
	}
	
	@Override
	protected void onDestroy() {
		sqlHelper.close();
		super.onDestroy();
	}



	@Override
	public void onLocationChanged(Location location) {

			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
			markerOptions.title("Israel Latitude");
			mMap.addMarker(markerOptions).setVisible(true);
			
			
			LatLng myLayLan = new LatLng(location.getLatitude(), location.getLongitude());
			CameraPosition cameraPosition = new CameraPosition.Builder()
		    .target(myLayLan )      // Sets the center of the map to Mountain View
		    .zoom(17)                   // Sets the zoom
		    .bearing(90)                // Sets the orientation of the camera to east
		    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
		    .build();                   // Creates a CameraPosition from the builder
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
			String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
		
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}