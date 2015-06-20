package com.example.y.travel_diary.Utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Y on 2015-06-20.
 */
public class LocationTracker {
    private static final long POLLING = 1000 * 10;

    private double latitude;
    private double longitude;
    private double accuracy;

    private Location mBestReading;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private boolean avail = false;
    private boolean availGPS = false;
    private boolean availNetwork = false;
    private String provider = null;

    private Context mContext;

    public LocationTracker (Context context) {
        mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                accuracy = location.getAccuracy();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                if (LocationManager.GPS_PROVIDER.equals(provider)) {
                    availGPS = true;
                    provider = LocationManager.GPS_PROVIDER;
                } else {
                    availNetwork = true;
                    provider = LocationManager.NETWORK_PROVIDER;
                }
                locationManager.removeUpdates(locationListener);
                reqLocation();
            }

            @Override
            public void onProviderDisabled(String provider) {
                if (LocationManager.GPS_PROVIDER.equals(provider)) {
                    availGPS = false;
                    if (availNetwork) {
                        provider = LocationManager.NETWORK_PROVIDER;
                        locationManager.removeUpdates(locationListener);
                        reqLocation();
                    } else {
                        provider = null;
                        locationManager.removeUpdates(locationListener);
                    }
                } else {
                    availNetwork = false;
                    if (availGPS) {
                        provider = LocationManager.GPS_PROVIDER;
                        locationManager.removeUpdates(locationListener);
                        reqLocation();
                    } else {
                        provider = null;
                        locationManager.removeUpdates(locationListener);
                    }
                }
            }
        };

        reqLocation();
    }

    public void reqLocation () {
        availGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        availNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (availGPS) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (availNetwork) {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        locationManager.requestLocationUpdates(provider, POLLING, 0, locationListener);
    }

    public Location getLocation () {
        if (availGPS || availNetwork)
            return locationManager.getLastKnownLocation(provider);
        else
            return null;
    }

    public void destroy () {
        locationManager.removeUpdates(locationListener);
    }
}
