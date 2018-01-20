package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.location.IGPSActivity;
import hr.foi.air1719.location.MyLocation;
import hr.foi.air1719.personaltracker.Helper;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Patricija on 1/19/2018.
 */

public class SavedLocationsDetails extends Fragment implements OnMapReadyCallback, IGPSActivity {
    String activityId = null;
    Double langitude;
    Double longitude ;
    MyLocation myLocation = null;
    GoogleMap googleMap = null;
    Marker mMarker = null;
    boolean  gps_enabled = false;
    List<Address> lokacija;
    String finalAddress;
    List<GpsLocation> gpsLocations ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_details_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            activityId = getArguments().getString("ID");
        }

        fillView(activityId);
        myLocation = new MyLocation();
        myLocation.LocationStart(this);

        MapView mapFragment = (MapView) getView().findViewById(R.id.map);
        mapFragment.onCreate(null);
        mapFragment.onResume();
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        ;
    }
    private void findLocation (double longitude, double latitude) {
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        try {
            lokacija = geocoder.getFromLocation(latitude,longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String addressLine= lokacija.get(0).getAddressLine(0);
        String county = lokacija.get(0).getAdminArea();
        finalAddress = addressLine+", "+county;
    }

    private void fillView(final String activityID) {
        new Thread(new Runnable() {
            public void run() {
                DatabaseFacade dbfacade = new DatabaseFacade(getView().getContext());
                Message message = handler.obtainMessage(1, dbfacade.getLocations(activityID));
                message.sendToTarget();
            }
        }).start();

    }
    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());
            Map<String, GpsLocation> gpsLoc = (Map<String, GpsLocation>)message.obj;
            gpsLocations = new ArrayList();
            gpsLocations.addAll(gpsLoc.values());
            for ( GpsLocation a : gpsLocations) {
               longitude = a.getLongitude();
               langitude = a.getLatitude();
            }
            findLocation(longitude,langitude);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            if (!Helper.isInternetAvailable(this.getActivity())) {
                Toast.makeText(this.getActivity(), "No internet connection right now, please check internet settings and try again", Toast.LENGTH_LONG).show();
                return;
            }

            this.googleMap = googleMap;
           Location location = myLocation.GetLastKnownLocation(this);

            if (location == null) {
                Toast.makeText(getActivity(), "Your GPS is off, please turn on your GPS.", Toast.LENGTH_LONG).show();
                return;
            } /*else {
                Toast.makeText(getActivity(), "Your location is: \nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + "\nAccuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
            }*/

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(langitude, longitude), 15));

            mMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(langitude, longitude))
                    .title(finalAddress));
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }

    }

    @Override
    public void locationChanged(Location location) {
        try {

            if (!Helper.isInternetAvailable(this.getActivity())) {
                Toast.makeText(this.getActivity(), "No internet connection right now, please check internet settings and try again", Toast.LENGTH_LONG).show();
                return;
            }

            if (mMarker != null) {
                mMarker.setPosition(new LatLng(langitude, longitude));
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(mMarker.getPosition()));
            } else {

                mMarker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(langitude, longitude))
                        .title(finalAddress));
            }
            //Toast.makeText(this, "Location changed: \nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + "\nAccuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
        }catch (Exception E)
        {
            E.printStackTrace();
        }
    }
}
