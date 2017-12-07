package hr.foi.air1719.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
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
    Fragment fragment;


    public void LocationStart(IGPSActivity i_gps) {
        try {
            //TODO ovo treba staviti u postavke
            long BrzinaOsvjezavanjaGps = 10;
            float MinimalnaUdaljenostGps = 10;
            long BrzinaOsvjezavanjaNetwork = 10;
            float MinimalnaUdaljenostNetwork = 100;
            Boolean isGpsEnabled = true;

            this.iGPS = i_gps;
            this.fragment = (Fragment)i_gps;

            if(locationManager != null) return;

            locationManager = (LocationManager) fragment.getActivity().getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    Toast.makeText(fragment.getActivity(), "Not allowed to use GPS location", Toast.LENGTH_LONG).show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(fragment.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                    Toast.makeText(fragment.getActivity(), "Allowed to use GPS location", Toast.LENGTH_LONG).show();
                }
            }


            if (gps_enabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, BrzinaOsvjezavanjaGps, MinimalnaUdaljenostGps, this);
                //Toast.makeText(fragment.getActivity(), "GpsPrecision", Toast.LENGTH_LONG).show();
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, BrzinaOsvjezavanjaNetwork, MinimalnaUdaljenostNetwork, this);
                //Toast.makeText(fragment.getActivity(), "NetworkPrecision", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LocationListenerStop() {
        try
        {
            locationManager.removeUpdates(this);
            locationManager = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Location GetLastKnownLocation(IGPSActivity i_gps) {

        try
        {
            this.iGPS = i_gps;
            this.fragment = (Fragment)i_gps;

            LocationManager mLocationManager = (LocationManager)(fragment.getActivity().getSystemService(Context.LOCATION_SERVICE));

            if (ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    Toast.makeText(fragment.getActivity(), "Not allowed to use GPS location", Toast.LENGTH_LONG).show();

                } else {

                    ActivityCompat.requestPermissions(fragment.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                    Toast.makeText(fragment.getActivity(), "Allowed to use GPS location", Toast.LENGTH_LONG).show();
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

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
