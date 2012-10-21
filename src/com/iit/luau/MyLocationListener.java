package com.iit.luau;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class MyLocationListener implements LocationListener 
{
    public static double latitude;
    public static double longitude;

    public void onLocationChanged(Location loc)
	{
		loc.getLatitude();
		loc.getLongitude();
		latitude=loc.getLatitude();
		longitude=loc.getLongitude();
		
		MainActivity.updateCoordinatesUI(latitude, longitude);
	}

	public void onProviderDisabled(String provider)
	{
		MainActivity.updateCoordinatesUI(-1, -1);
	}
	public void onProviderEnabled(String provider)
	{
		MainActivity.updateCoordinatesUI(1, 1);
	}
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}
}

