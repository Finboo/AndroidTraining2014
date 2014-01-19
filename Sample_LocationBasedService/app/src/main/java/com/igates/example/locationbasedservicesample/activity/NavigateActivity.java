package com.igates.example.locationbasedservicesample.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.igates.example.locationbasedservicesample.data.RouteLocation;
import com.igates.example.locationbasedservicesample.database.RouteDatabase;
import com.igates.example.locationbasedservicesample.service.RoutingService;
import com.igates.example.locationbasedservicesample.service.RoutingService.LocalBinder;

public class NavigateActivity extends Activity implements LocationListener{

	public static final String DISTANCE = "distance";
	public static final String IS_WIN = "isWin";
	LocationManager mLocationManager;
	private static final String PREFS_NAME = "navigationPreferences";
	ServiceConnection conn;
	GoogleMap mMap;
	final RouteLocation[]  routeLocations = new RouteLocation[1];
	SharedPreferences preferences;
	int routeId;

	private Button checkDistanceButton;
	private Button checkIfDoneButton;
	RoutingService routingService;
	RouteDatabase database ;
	
	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
		
			TextView distanceFromRoute = (TextView)findViewById(R.id.distanceFromRoute);
			
			String distance = msg.getData().getString(DISTANCE);
			String isWin = msg.getData().getString(IS_WIN);
			if(distance!=null){
				distanceFromRoute.setText(distance);
				
			}else if(isWin!=null){
				
				if(Boolean.parseBoolean(isWin)){
					
					routeId = preferences.getInt("routeId",1);
					showLocationOnMap(routeLocations[0].getLatitude(),routeLocations[0].getLongtitude(),routeLocations[0].getHint());
					
					routeId++;
					Editor editor = preferences.edit();
					editor.putInt("routeId",routeId);
					editor.commit();


					getRoute(routeId,routeLocations);
					if(routeLocations[0]!=null){
						TextView hint = (TextView)findViewById(R.id.hintForNavigate);
						hint.setText("The next hint is : " + routeLocations[0].getHint());
						final Dialog d = new Dialog(NavigateActivity.this);
						d.setTitle("Well done!");
						d.setContentView(R.layout.done_stage_dialog);
						d.show();
						
						TextView nextHint= (TextView)d.findViewById(R.id.nextHintOnDoneStage);
						nextHint.setText("The next hint is : \n" + routeLocations[0].getHint());
						Button doneStageButton = (Button)d.findViewById(R.id.doneStageButton);
						doneStageButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								d.dismiss();
								
							}
						});
						
					}else{
						
						TextView hint = (TextView)findViewById(R.id.hintForNavigate);
						hint.setText("Done!!!!!");
						final Dialog d = new Dialog(NavigateActivity.this);
						d.setTitle("Great work!!");
						d.setContentView(R.layout.finished_all_routes_dialog);
						d.show();
						checkDistanceButton.setVisibility(View.GONE);
						checkIfDoneButton.setVisibility(View.GONE);
						
						Button finishedAllStageButton = (Button)d.findViewById(R.id.finishedAllStageButton);
						finishedAllStageButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								d.dismiss();
								
							}
						});
					}

				}else{
					final Dialog d = new Dialog(NavigateActivity.this);
					d.setContentView(R.layout.not_done_stage_dialog);
					d.setTitle("Ooooops!");
					d.show();
					Button notDoneStageButton = (Button)d.findViewById(R.id.notDoneStageButton);
					notDoneStageButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							d.dismiss();
							
						}
					});
				}
			}
			
			
			super.handleMessage(msg);
			
		}
	};
		
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigate);
				
	    preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		routeId = preferences.getInt("routeId", 1);
		database = new RouteDatabase(getBaseContext());
		
		getRoute(routeId,routeLocations);

		TextView hint = (TextView)findViewById(R.id.hintForNavigate);
		
		if(routeLocations[0]!=null){
			hint.setText("The hint is : " + routeLocations[0].getHint());
		}else{
			hint.setText("Done!!!!!");
		}
		
		Button startAgain = (Button) findViewById(R.id.startAgain);
		startAgain.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				routeId = 1;
				Editor editor = preferences.edit();
				editor.putInt("routeId",routeId);
				editor.commit();
				getRoute(routeId,routeLocations);
				TextView hint = (TextView)findViewById(R.id.hintForNavigate);
				hint.setText("The next hint is : " + routeLocations[0].getHint());

				checkDistanceButton.setVisibility(View.VISIBLE);
				checkIfDoneButton.setVisibility(View.VISIBLE);
				
				if(mMap!=null){
					mMap.clear();
				}
				Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				if(location != null){
					showLocationOnMap(location.getLatitude(),location.getLongitude(),"Israel Latitude");
				}
				
			}
		});
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Button zoomToCurrentOnNavigator = (Button) findViewById(R.id.zoomToCurrentOnNavigator);
		zoomToCurrentOnNavigator.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, NavigateActivity.this, NavigateActivity.this.getMainLooper());
			}
		});
		
	    checkDistanceButton = (Button)findViewById(R.id.checkDistance);
	    if(routeLocations[0]==null){
	    	checkDistanceButton.setVisibility(View.GONE);
	    }
		checkDistanceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(isConnectingToInternet()){
					
					Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
					if(location != null){
						final double curLatitude = location.getLatitude();
						final double curLongitude = location.getLongitude();
						
						Toast.makeText(getBaseContext(), "Going to check walking distance ...", Toast.LENGTH_SHORT).show();
						routingService.checkWalkingDistance(myHandler,curLatitude,curLongitude,routeLocations[0]);
											}else{
						Toast.makeText(getBaseContext(), "GPS turned off. Please turn on and try again.", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getBaseContext(), "No internet connection...", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		checkIfDoneButton = (Button)findViewById(R.id.checkDone);
		if(routeLocations[0]==null){
			checkIfDoneButton.setVisibility(View.GONE);
	    }
		checkIfDoneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(isConnectingToInternet()){	
					Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
					if(location!=null){
						final double curLatitude = location.getLatitude();
						final double curLongitude = location.getLongitude();
						routingService.checkDone(myHandler,curLatitude,curLongitude,routeLocations[0]);
					
					}else{
						Toast.makeText(getBaseContext(), "GPS turned off. Please turn on and try again.", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getBaseContext(), "No internet connection...", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		setUpMapIfNeeded();
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigate, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		conn = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.d(((Object) this).getClass().getName(), "disconnected service");
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(((Object) this).getClass().getName(), "connected service");
				IBinder iBinder = service;									
				LocalBinder myService = (LocalBinder)iBinder;
				routingService = myService.getService();
			}
		};
		Intent routingServiceIntent = new Intent(getBaseContext(), RoutingService.class);
		bindService(routingServiceIntent, conn, BIND_AUTO_CREATE);
		
	}
	
	@Override
	protected void onStop() {
		super.onPause();
		unbindService(conn);
		database.close();
	}

	private void getRoute(int routeId, RouteLocation[] routes) {
		
		//RouteLocation routeLocation = database.getRouteById(routeId);
		//routes[0] =  routeLocation;
		
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
	
	private void setUpMapIfNeeded(){
		// Do a null check to confirm that we have not already instantiated the map.
		if(mMap == null)
		{
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapForNavigate)).getMap();
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
		Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if(location != null){
			onLocationChanged(location);
		}
	}
		
	 public boolean isConnectingToInternet(){
	        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null) 
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null) 
	                  for (int i = 0; i < info.length; i++) 
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
	 
	          }
	          return false;
	    }


	@Override
	public void onLocationChanged(Location location) {
		if(location != null)
		{

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
			
		}
		
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
