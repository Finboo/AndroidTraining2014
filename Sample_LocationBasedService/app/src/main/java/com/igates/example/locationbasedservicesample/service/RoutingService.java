package com.igates.example.locationbasedservicesample.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.igates.example.locationbasedservicesample.activity.NavigateActivity;
import com.igates.example.locationbasedservicesample.data.RouteLocation;

public class RoutingService extends Service
{

	private static final float winningDistanceInKM = 2; 
	
	IBinder mIBinder = new LocalBinder();
	public class LocalBinder extends Binder {
    	
		public RoutingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RoutingService.this;
        }
    }
    
	
	@Override
	public IBinder onBind(Intent arg0)
	{

		return mIBinder;
	}
	
	@Override
	public void onCreate()
	{

		Log.d("RoutingService", "binding!!!");
		super.onCreate();
	}
	

	public void checkWalkingDistance(final Handler handler,final double curLatitude, final double curLongitude,final RouteLocation r) {
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				Bundle data = new Bundle();
				String distance = checkWalkingDistanceInKM(curLatitude, curLongitude, r);
				data.putString(NavigateActivity.DISTANCE, distance);
				msg.setData(data );
				handler.sendMessage(msg );
				
			}
		};
		new Thread(runnable).start();
		
	}

	public void checkDone(final Handler handler,final double curLatitude, final double curLongitude,final RouteLocation r) {
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				Bundle data = new Bundle();
				String distance = checkWalkingDistanceInKM(curLatitude, curLongitude, r);
				
				distance = distance.replaceAll("km","");
				distance = distance.replaceAll(" ","");
				distance = distance.replaceAll(",","");
				
				float distanceFloat = 0;
				boolean res = false;
				try{
					distanceFloat = Float.valueOf(distance);
					res = distanceFloat < winningDistanceInKM;
				}catch(NumberFormatException nfe){
					res = false;
				}
				
				data.putString(NavigateActivity.IS_WIN, String.valueOf(res));
				msg.setData(data );
				handler.sendMessage(msg );
				
			}
		};
		new Thread(runnable).start();
		
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("RoutingService", "un binding!!!");
		return super.onUnbind(intent);
	}
	
	private String checkWalkingDistanceInKM(double curLatitude,  double curLongitude, RouteLocation r){
		
			String url = "http://maps.google.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&mode=walking&sensor=false";
			url = String.format(url,  curLatitude,curLongitude,r.getLatitude(),r.getLongtitude());
			URL obj = null;
			HttpURLConnection con = null ;
			BufferedReader in = null;
			InputStreamReader inputStreamReader = null;
			
			try {
				obj = new URL(url);
				con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				int responseCode = con.getResponseCode(); 
				if(responseCode!=200){
					return  "excpetion...";
				}
				inputStreamReader = new InputStreamReader(con.getInputStream());
				in = new BufferedReader(inputStreamReader);
				String inputLine;
				StringBuffer response = new StringBuffer();
		 
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				String jsonRes = response.toString();
				int start = jsonRes.indexOf("text",jsonRes.indexOf("distance"))+9;
				int end = jsonRes.indexOf("km",jsonRes.indexOf("distance"))+2;					
				String distance = jsonRes.substring(start, end);
				return distance;
			} catch (Exception e) {
				return  "unknown";
			}finally{
				try{
					if(con!=null){
						con.disconnect();
					}
				}catch(Exception e){
					
				}
				try{
					if(in!=null){
						in.close();
					}
				}catch(Exception e){
					
				}
				try{
					if(inputStreamReader!=null){
						inputStreamReader.close();
					}
				}catch(Exception e){
					
				}
			}
		
		
	}
	
	
}
