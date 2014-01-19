package com.igates.example.locationbasedservicesample.database;

import java.util.ArrayList;
import java.util.List;

import com.igates.example.locationbasedservicesample.data.RouteLocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RouteDatabase extends SQLiteOpenHelper
{

	// Database Version
    private static final int DATABASE_VERSION = 6;
	public static final String KEY_ID = "_id";

	public static final String KEY_ROUTE_NAME = "route_name";
	public static final String KEY_LOCATION_ID = "location_id";
	public static final String KEY_HINT = "hint";	
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	
	private static final String ROUTE_TABLE_NAME = "route_table";
	private static final String ROUTE_TABLE_CREATE =
            "CREATE TABLE " + ROUTE_TABLE_NAME + " (" +
            		KEY_ID + " integer primary key autoincrement, " +
            		KEY_ROUTE_NAME + " TEXT, "+
            		KEY_LOCATION_ID + " integer, "+
            		KEY_LATITUDE + " DOBLE, " +
            		KEY_LONGITUDE + " DOBLE, " +
            		KEY_HINT + " TEXT);";
	
	private static final String COLUMNS [] = {KEY_ID,KEY_ROUTE_NAME,KEY_LOCATION_ID,KEY_LATITUDE,KEY_LONGITUDE,KEY_HINT};
	SQLiteDatabase db;
	
	public RouteDatabase(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		
	}
	
	public RouteDatabase(Context context) {
        super(context, ROUTE_TABLE_NAME, null, DATABASE_VERSION);  
    }

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(ROUTE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(RouteDatabase.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + ROUTE_TABLE_NAME);
		    db.execSQL(ROUTE_TABLE_CREATE);
	}

	
	public boolean isRoutTableNotEmpty(){
		
		db = this.getReadableDatabase();
		Cursor cursor = 
                db.query(ROUTE_TABLE_NAME, // a. table
                new String[]{KEY_ID}, // b. column names
                null, // c. selections 
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
		
		 if (cursor != null && cursor.getCount()>0){
		        return true;
		 }else{
			 return false;
		 }
	}
	

	public List<RouteLocation> getAllRoutes(){
		
		List<RouteLocation> routesList = null;
		db = this.getReadableDatabase();
		Cursor cursor = 
                db.query(ROUTE_TABLE_NAME, // a. table
                COLUMNS, // b. column names
                null, // c. selections 
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
		
		 if (cursor != null && cursor.getCount()>0){
			 routesList = new ArrayList<RouteLocation>();
			 while(cursor.moveToNext()){
				 
			        long id  = cursor.getLong(cursor.getColumnIndex(KEY_ID));
			        String routeName = cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAME));
			        long locationId = cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID));
					double latitude  = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
					double longitutde  = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE));
					String hint  = cursor.getString(cursor.getColumnIndex(KEY_HINT));

					routesList.add( new RouteLocation(id,routeName,locationId,latitude,longitutde,hint));
			 }
		     return routesList;
		 }else{
			 return null;
		 }
	}
	
	public List<RouteLocation> getAllRouteLocations(String selectedRouteName){
		List<RouteLocation> routesList = new ArrayList<RouteLocation>();
		db = this.getReadableDatabase();
		Cursor cursor = 
                db.query(ROUTE_TABLE_NAME, // a. table
                COLUMNS, // b. column names
                KEY_ROUTE_NAME +" = ?", // c. selections 
                new String[]{selectedRouteName}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
		
		if (cursor != null && cursor.moveToFirst()!=false){
	        cursor.moveToFirst();
	        long id  = cursor.getLong(cursor.getColumnIndex(KEY_ID));
	        String routeName = cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAME));
	        long locationId = cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID));
			double latitude  = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
			double longitutde  = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE));
			String hint  = cursor.getString(cursor.getColumnIndex(KEY_HINT));

			routesList.add( new RouteLocation(id,routeName,locationId,latitude,longitutde,hint));
		}else{
			return null;
		}
		return routesList;

		
	}
	
	public RouteLocation getRouteLocation(String selectedRouteName,long selectedLocationId){
		
		RouteLocation routeLocation = null;
		
		db = this.getReadableDatabase();
		Cursor cursor = 
                db.query(ROUTE_TABLE_NAME, // a. table
                COLUMNS, // b. column names
                KEY_ROUTE_NAME +" = ? and " + KEY_LOCATION_ID +" = ?", // c. selections 
                new String[]{selectedRouteName,selectedLocationId+""}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
		
		if (cursor != null && cursor.moveToFirst()!=false){
	        cursor.moveToFirst();
	        long id  = cursor.getLong(cursor.getColumnIndex(KEY_ID));
	        String routeName = cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAME));
	        long locationId = cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID));
			double latitude  = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
			double longitutde  = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE));
			String hint  = cursor.getString(cursor.getColumnIndex(KEY_HINT));

			routeLocation= new RouteLocation(id,routeName,locationId,latitude,longitutde,hint);
		}else{
			return null;
		}
		return routeLocation;

		
	}
	
	public RouteLocation getRouteLocation(long routeId){
		
		RouteLocation routeLocation = null;
		
		db = this.getReadableDatabase();
		Cursor cursor = 
                db.query(ROUTE_TABLE_NAME, // a. table
                COLUMNS, // b. column names
                KEY_ID+ " = ?", // c. selections 
                new String[]{routeId+""}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
		
		if (cursor != null && cursor.moveToFirst()!=false){
	        cursor.moveToFirst();
	        long id  = cursor.getLong(cursor.getColumnIndex(KEY_ID));
	        String routeName = cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAME));
	        long locationId = cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID));
			double latitude  = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
			double longitutde  = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE));
			String hint  = cursor.getString(cursor.getColumnIndex(KEY_HINT));

			routeLocation= new RouteLocation(id,routeName,locationId,latitude,longitutde,hint);
		}else{
			return null;
		}
		return routeLocation;

		
	}
	
	public long addRoute(RouteLocation routeLocation){
		//check what is max location id for route 
		int locationId = 1;
		db = this.getReadableDatabase();
		Cursor cursor = 
                db.query(ROUTE_TABLE_NAME, // a. table
                new String[]{KEY_ROUTE_NAME,KEY_LOCATION_ID}, // b. column names
                KEY_ROUTE_NAME + "= ?", // c. selections 
                new String[]{routeLocation.getRouteName()}, // d. selections args
                null, // e. group by
                null, // f. having
                KEY_LOCATION_ID + " DESC ", // g. order by
                null); // h. limit
		
		if(cursor.moveToFirst()){
			locationId = cursor.getInt(cursor.getColumnIndex(KEY_LOCATION_ID))+1 ;
		}
		
		db = this.getWritableDatabase();
		 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ROUTE_NAME, routeLocation.getRouteName()); 
        values.put(KEY_LOCATION_ID, locationId); 
        values.put(KEY_LATITUDE, routeLocation.getLatitude()); 
        values.put(KEY_LONGITUDE, routeLocation.getLongtitude()); 
        values.put(KEY_HINT, routeLocation.getHint()); 
 
        // 3. insert
        long rowId = db.insert(ROUTE_TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close(); 
        return rowId;
	}
	
	/**
	 * 
	 * @param routeName
	 * @param locationId
	 * @return numberOfDeletedRows
	 */
	public int removeRouteLocation(String routeName,long locationId){
		
		db = this.getWritableDatabase();
		int numberOfDeletedRows = db.delete(
				ROUTE_TABLE_NAME, 
				KEY_ROUTE_NAME +" = ? and " + KEY_LOCATION_ID +" = ? ", 
				new String[]{routeName,locationId+""});
		db.close(); 
		return numberOfDeletedRows;
	}
	
	/**
	 * 
	 * @param routeName
	 * @return numberOfDeletedRows
	 */
	public int removeAllRouteLocations(String routeName){
		
		db = this.getWritableDatabase();
		int numberOfDeletedRows = db.delete(
				ROUTE_TABLE_NAME, 
				KEY_ROUTE_NAME + " = ? " , 
				new String[]{routeName});
		db.close(); 
		return numberOfDeletedRows;
	}
	
	public void removeAllRows(){
		db = this.getWritableDatabase();
		db.delete(ROUTE_TABLE_NAME, null, null);
		db.close(); 
	}
	
	public Cursor getCursorForRouteLocations(String routeName){
		
		String sqlQuery = "SELECT *" + " FROM " + ROUTE_TABLE_NAME  +" WHERE " + KEY_ROUTE_NAME +" = '"+routeName+"' ORDER BY "+KEY_LOCATION_ID+" ASC;";
		db= getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlQuery, null);
		return cursor;
	}
	
	/**
	 * @return all routes names cursor
	 */
	public Cursor getAllRoutesNames(){
		
		db = this.getReadableDatabase();
		Cursor cursor = 
                db.query(ROUTE_TABLE_NAME, // a. table
                new String[]{KEY_ROUTE_NAME,KEY_ID}, // b. column names
                null, // c. selections 
                null, // d. selections args
                KEY_ROUTE_NAME, // e. group by
                null, // f. having
                KEY_ROUTE_NAME + " ASC ", // g. order by
                null); // h. limit
		
		
	        return cursor;

	}
}
