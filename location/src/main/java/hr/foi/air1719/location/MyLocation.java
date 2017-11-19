package hr.foi.air1719.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.List;

/**
 * Created by DrazenVuk on 16.11.2017...
 */

public class MyLocation implements LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private LocationManager locationManager = null;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    IGPSActivity iGPS = null;
    Activity activity;


    public void LocationStart(IGPSActivity i_gps) {

    }


    public Location GetLastKnownLocation(IGPSActivity i_gps) {

        this.iGPS = i_gps;
        this.activity = (Activity)i_gps;

        LocationManager mLocationManager = (LocationManager)(activity.getSystemService(Context.LOCATION_SERVICE));

        if (ActivityCompat.checkSelfPermission((Activity) this.iGPS, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission((Activity) this.iGPS, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this.iGPS,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(activity, "No PERMISSIONS", Toast.LENGTH_LONG).show();

            } else {

                ActivityCompat.requestPermissions((Activity) this.iGPS,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                Toast.makeText(((Activity) this.iGPS), "Yes PERMISSIONS", Toast.LENGTH_LONG).show();
            }
        }


        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            android.location.Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }

        if(bestLocation != null) {
            return bestLocation;
        }
        else
            return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        iGPS.locationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
