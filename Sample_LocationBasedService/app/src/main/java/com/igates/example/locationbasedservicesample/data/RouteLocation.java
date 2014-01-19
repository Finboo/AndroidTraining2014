package com.igates.example.locationbasedservicesample.data;

public class RouteLocation {


	private long id;
	private String routeName;
	private long locationId;
	private double latitude;
	private double longtitude;
	private String hint;
	
	public RouteLocation(long id, String routeName, long locationId, double latitude,
			double longtitude, String hint) {
		super();
		this.id = id;
		this.routeName = routeName;
		this.locationId = locationId;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.hint = hint;
	}
	
	public RouteLocation( String routeName, double latitude,
			double longtitude, String hint) {
		super();
		this.routeName = routeName;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.hint = hint;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	
}
