package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
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

import hr.foi.air1719.location.IGPSActivity;
import hr.foi.air1719.location.MyLocation;
import hr.foi.air1719.personaltracker.Helper;
import hr.foi.air1719.personaltracker.Main;
import hr.foi.air1719.personaltracker.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Patricija on 11/21/2017.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, IGPSActivity {
    private FragmentManager mFragmentManager;
    MyLocation myLocation = null;
    GoogleMap googleMap = null;
    Marker mMarker = null;
    FloatingActionButton floatingActionButton;
    boolean  gps_enabled = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        floatingActionButton = (FloatingActionButton) getView().findViewById(R.id.floatingActionButton);


        myLocation = new MyLocation();
        myLocation.LocationStart(this);

        MapView mapFragment = (MapView) getView().findViewById(R.id.map);
        mapFragment.onCreate(null);
        mapFragment.onResume();
        mapFragment.getMapAsync((OnMapReadyCallback) this);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CheckLocationService();
                if (gps_enabled) {
                    LocationManualFragment locationManual = new LocationManualFragment();
                    mFragmentManager = getFragmentManager();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, locationManual)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }
                else {
                    Helper.PushNotificationGPS(getActivity().getWindow().getContext());
                  //  Toast.makeText(getActivity(), "Your GPS is off, please turn on your GPS.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void  CheckLocationService () {
        LocationManager lm = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception E) {
            E.printStackTrace();

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            if (!Helper.isInternetAvailable(this.getActivity())) {
                Helper.PushNotificationInternet(getActivity().getWindow().getContext());
                Toast.makeText(this.getActivity(), "No internet connection right now, please check internet settings and try again", Toast.LENGTH_LONG).show();
                return;
            }

            this.googleMap = googleMap;
            Location location = myLocation.GetLastKnownLocation(this);

            if (location == null) {
                Helper.PushNotificationGPS(getActivity().getWindow().getContext());
                Toast.makeText(getActivity(), "Your GPS is off, please turn on your GPS.", Toast.LENGTH_LONG).show();
                return;
            } /*else {
                Toast.makeText(getActivity(), "Your location is: \nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + "\nAccuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
            }*/

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

            mMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("My location"));
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
                Helper.PushNotificationInternet(getActivity().getWindow().getContext());
                Toast.makeText(this.getActivity(), "No internet connection right now, please check internet settings and try again", Toast.LENGTH_LONG).show();
                return;
            }

            if (mMarker != null) {
                mMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(mMarker.getPosition()));
            } else {
                mMarker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("My location"));
            }
            //Toast.makeText(this, "Location changed: \nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + "\nAccuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
        }catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    public void onResume(){
        super.onResume();

        ((Main) getActivity())
                .setActionBarTitle("Walking mode");
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.walkingMode);
    }
}