package hr.foi.air1719.personaltracker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by DrazenVuk on 12/27/2017.
 */
public class ActivityMapFragment extends android.app.Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap = null;
    private Polyline line;
    private List<LatLng> Lat_Lng;
    private OnFragmentInteractionListener mListener;
    String activityId=null;

    public ActivityMapFragment() {
        // Required empty public constructor
    }


    private void ShowTrip()
    {
        try {

            new Thread(new Runnable() {
                public void run() {
                    DatabaseFacade dbfacade = new DatabaseFacade(getView().getContext());
                    Message message = mHandler.obtainMessage(1, dbfacade.getLocations(activityId));
                    //Message message = mHandler.obtainMessage(1, dbfacade.getAllLocations());
                    message.sendToTarget();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            try
            {
                Lat_Lng = new ArrayList();
                Map<String, GpsLocation> loc = (Map<String, GpsLocation>)message.obj;

                List<GpsLocation> lGpsLocation = locationSortMap(loc);

                for(GpsLocation a: lGpsLocation) {
                    Lat_Lng.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    System.out.println("Unsort......" + a.getTimestamp());
                }


                if(line!=null)
                    line.remove();

                if(Lat_Lng.size()>0) {
                    line = googleMap.addPolyline(new PolylineOptions().geodesic(true).addAll(Lat_Lng));
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(Lat_Lng.get(0), 14);
                    googleMap.animateCamera(yourLocation);
                }
            }
            catch (Exception E)
            {
                E.printStackTrace();
            }
        }
    };

    public static List<GpsLocation> locationSortMap(Map<String, GpsLocation> loc)
    {
        if (loc != null) {
            List<GpsLocation> values = new ArrayList();
            values.addAll(loc.values());
            Collections.sort(values, new Comparator<GpsLocation>() {
                public int compare(GpsLocation o1, GpsLocation o2) {
                    return o1.getTimestamp().compareTo(o2.getTimestamp());
                }
            });

            return values;
        }
        return null;
    }


    public static ActivityMapFragment newInstance(String param1, String param2) {
        ActivityMapFragment fragment = new ActivityMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        MapView mapFragment = (MapView) getView().findViewById(R.id.googleMapFragment);
        mapFragment.onCreate(null);
        mapFragment.onResume();
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        if(activityId!=null)
        {
            ShowTrip();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activityId = getArguments().getString("activityID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_map, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
