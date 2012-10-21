package com.iit.luau;

import java.util.List;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
	
	public static Location getLocation(Context ctx) {
        LocationManager lm = (LocationManager) ctx
                .getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;
        for (int i = providers.size() - 1; i >= 0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null)
                break;
        }
        return l;
    }

}

