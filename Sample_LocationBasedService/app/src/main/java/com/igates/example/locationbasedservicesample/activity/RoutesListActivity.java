package com.igates.example.locationbasedservicesample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.igates.example.locationbasedservicesample.database.RouteDatabase;

public class RoutesListActivity extends Activity {

	RouteDatabase sqlHelper;
	CursorAdapter myAdapter;
	GoogleMap mMap;
	RelativeLayout mapLayout;
	String routeName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routes_list);

		routeName = getIntent().getExtras().getString("routeName");

		final ListView mainListView = (ListView) findViewById(R.id.listView);

	    sqlHelper = new RouteDatabase(this);
	    Cursor cursorForAllRoutes = sqlHelper.getCursorForRouteLocations(routeName);
	    myAdapter = new CustomCursorAdapter(getApplicationContext(), cursorForAllRoutes);
	    
		mainListView.setAdapter(myAdapter);
	    
		
		Button removeAllRoutesButton = (Button)findViewById(R.id.removeAllRoutes);
		removeAllRoutesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				sqlHelper.removeAllRows();
				mainListView.setVisibility(View.GONE);
			}
		});
		
		setUpMap();
		mapLayout = (RelativeLayout)findViewById(R.id.mapLayout);
		
		TextView closeMapTextView = (TextView)findViewById(R.id.closeMap);
		closeMapTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				mapLayout.setVisibility(View.GONE);
				
			}
		});
		
//		Button startNavigate = (Button)findViewById(R.id.startNavigate);
//		
//		startNavigate.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(sqlHelper.isRoutTableNotEmpty()){
//					Intent navigateIntent = new Intent(getBaseContext(), NavigateActivity.class);
//					startActivity(navigateIntent);
//				}else{
//					Toast.makeText(getApplicationContext(), "There are no locations, cant navigate ..." , Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.routes_list_menu, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {

		sqlHelper.close();
		super.onDestroy();
	}

	
	public class CustomCursorAdapter extends CursorAdapter
	{
		LayoutInflater mLayoutInflater;
		
		public CustomCursorAdapter(Context context, Cursor c)
		{
			super(context, c, 0);
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View view, final Context context, Cursor cursor) {


			final long locationId = cursor.getLong(cursor.getColumnIndex(RouteDatabase.KEY_LOCATION_ID ));
			final double longitude = cursor.getDouble(cursor.getColumnIndex(RouteDatabase.KEY_LONGITUDE ));
			final double latitude = cursor.getDouble(cursor.getColumnIndex(RouteDatabase.KEY_LATITUDE ));
			final String hint = cursor.getString(cursor.getColumnIndex(RouteDatabase.KEY_HINT ));
			
			TextView idOfRoute = (TextView) view.findViewById(R.id.routeName);
			idOfRoute.setText(routeName);
			TextView locationIdOfRoute = (TextView) view.findViewById(R.id.locationIdOfRoute);
			locationIdOfRoute.setText("locationId = "+locationId+"");
			TextView hintOfRoute = (TextView) view.findViewById(R.id.hintOfRoute);
			hintOfRoute.setText("hint = "+hint+"");
			
			
			ImageView mapImage =  (ImageView) view.findViewById(R.id.mapImage);
			mapImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/*String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(Label+Name)", latitude, longitude,latitude, longitude);
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
					RoutesListActivity.this.startActivity(intent);*/

					mapLayout.setVisibility(View.VISIBLE);
					showLocationOnMap(latitude, longitude, hint);
					
				}
			});
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			View view =  mLayoutInflater.inflate(R.layout.route_location, parent, false);
			return view;
		}

		
	}
	
	private void setUpMap(){
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
					public void onMapClick(final LatLng latLng) {}
					
				});
			}
		}
	}
	
	protected void showLocationOnMap(double latitude, double longitude, String title){
		

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(new LatLng(latitude, longitude));
		markerOptions.title(title);
		mMap.addMarker(markerOptions).setVisible(true);
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
	    .target(new LatLng(latitude, longitude) )      // Sets the center of the map to Mountain View
	    .zoom(14)                   // Sets the zoom
	    .bearing(0)                // Sets the orientation of the camera to east
	    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
	    .build();                   // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
		/*//String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", latLng.getLongitude(), latLng.getLatitude());
		Toast.makeText(NavigateActivity.this, message, Toast.LENGTH_LONG).show();*/
	

	}
}
