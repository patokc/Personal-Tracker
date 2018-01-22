package hr.foi.air1719.walking;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.core.SharingManager;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.location.IGPSActivity;
import hr.foi.air1719.location.MyLocation;

/**
 * Created by DrazenVuk on 12/27/2017.
 */
public class WalkingMode extends android.app.Fragment  implements IGPSActivity, SharingManager {

    MyLocation myLocation = null;
    TextView txtSpeed=null;
    TextView txtAvgSpeed=null;
    TextView txtTotalKm=null;
    TextView txtTodayTotalKm=null;
    TextView txtAddress=null;
    TextView txtBurnedCalories=null;
    Button btnDrivingStart = null;
    Button btnShowTrip = null;
    Button btnShowHistory = null;
    Geocoder geocoder = null;
    Location lastPoint = null;
    float totalDistance = 0;
    Date startDate = null;
    SharedPreferences userSP = null;
    DatabaseFacade dbCurrentFacade = null;
    Activity currentActivity = null;
    float weight = 70.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_walking_mode, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        userSP = getActivity().getSharedPreferences("user", 0);

        txtSpeed = (TextView) getView().findViewById(R.id.txtSpeedInfo);
        txtBurnedCalories = (TextView) getView().findViewById(R.id.txtBurnedCalories);
        txtTotalKm = (TextView) getView().findViewById(R.id.txtTotalKm);
        txtAvgSpeed = (TextView) getView().findViewById(R.id.txtAvgSpeed);
        txtTodayTotalKm = (TextView) getView().findViewById(R.id.txtTodayTotalKm);
        txtAddress = (TextView) getView().findViewById(R.id.txtAddressDM);
        dbCurrentFacade = new DatabaseFacade(getView().getContext());

        btnDrivingStart = (Button) getView().findViewById(R.id.btnDrivingStart);
        btnDrivingStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClick_Start(v);
            }
        });

        btnShowTrip = (Button) getView().findViewById(R.id.btnShowTrip);
        btnShowTrip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClick_ShowTrip(v);
            }
        });

        btnShowHistory = (Button) getView().findViewById(R.id.btnShowHistory);
        btnShowHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClick_ShowHistory(v);
            }
        });


        btnShowTrip.setVisibility(View.INVISIBLE);

        txtAddress.setText("Waiting for GPS fix...");
        weight=userSP.getFloat("weight",70.0f);
        if(weight<5)weight=70.0f;
    }

    public void onClick_Start(View v) {

        if(myLocation ==null)
        {
            totalDistance = 0;
            lastPoint=null;
            startDate = null;
            currentActivity = new Activity(ActivityMode.WALKING_A);
            currentActivity.setActivityId(currentActivity.getActivityId());
            currentActivity.setStart(new Timestamp(new Date().getTime()));

            Toast.makeText(this.getActivity(), "Start walking mode", Toast.LENGTH_SHORT).show();
            if(startDate==null) startDate= new Date();
            myLocation = new MyLocation();
            myLocation.LocationStart(this);
            btnDrivingStart.setText("Stop");
            btnShowTrip.setVisibility(View.INVISIBLE);
        }
        else
        {

            new Thread(new Runnable() {
                public void run() {

                    currentActivity.setDistance(totalDistance);
                    currentActivity.setAverageSpeed((float)CalculateAvgSpeed(startDate, new Date(), (double)totalDistance));
                    currentActivity.setFinish(new Timestamp(new Date().getTime()));
                    currentActivity.setUser(userSP.getString("username", ""));
                    currentActivity.setAvgCal(CalculateCalories(weight, totalDistance));
                    dbCurrentFacade.saveActivity(currentActivity);
                }
            }).start();


            Toast.makeText(this.getActivity(), "Stop walking mode", Toast.LENGTH_SHORT).show();
            myLocation.LocationListenerStop();
            myLocation = null;
            btnDrivingStart.setText("Start");
            btnShowTrip.setVisibility(View.VISIBLE);
        }
    }

    public void onClick_ShowTrip(View v) {

        ActivityMapFragment fragment = new ActivityMapFragment();
        fragment.share(activity,fragmentTransaction, fragment_container);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack("Walking Map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("Walking Map");

        Bundle bundle = new Bundle();
        bundle.putString("activityID", currentActivity.getActivityId());
        fragment.setArguments(bundle);

        transaction.replace(fragment_container, fragment, "Walking Map");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    public void onClick_ShowHistory(View v) {

        WalkingHistoryFragment fragment = new WalkingHistoryFragment();
        fragment.share(activity,fragmentTransaction, fragment_container);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack("Walking History", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(fragment_container, fragment, "Walking History");
        transaction.addToBackStack("Walking History");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    Location tempLocation=null;
    @Override
    public void locationChanged(Location location) {

        try {
            List<Address> lokacija = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            tempLocation=location;

            int speed = (int) ((location.getSpeed() * 3600) / 1000);
            txtSpeed.setText(speed + " km/h");

            if(lastPoint==null)lastPoint = location;
            if(startDate==null) startDate= new Date();

            totalDistance += CalculateDistance(lastPoint, location);

            lastPoint=location;

            txtTotalKm.setText(String.format("%.2f", totalDistance) + " km");

            txtAvgSpeed.setText(String.format("%.2f", CalculateAvgSpeed(startDate, new Date(), totalDistance)) + " km/h");

            txtTodayTotalKm.setText(String.format("%.2f", totalDistance) + " km");

            txtBurnedCalories.setText(String.format("%.2f", CalculateCalories(weight, totalDistance))+ " kcal");

            if(lokacija.size()>0)
                txtAddress.setText(lokacija.get(0).getAddressLine(0));


            new Thread(new Runnable() {
                public void run() {

                    DatabaseFacade dbfacade = new DatabaseFacade(getView().getContext());
                    dbfacade.saveLocation(new GpsLocation(currentActivity.getActivityId(), tempLocation.getLongitude(), tempLocation.getLatitude(), tempLocation.getAccuracy()));
                }
            }).start();

        }catch (Exception E)
        {
            Toast.makeText(this.getActivity(), E.toString(), Toast.LENGTH_SHORT).show();
            E.printStackTrace();
        }
    }


    public float CalculateCalories(float w, float d)
    {
        return ((float)1.036)*w*d;
    }


    FragmentTransaction fragmentTransaction;
    android.app.Activity activity;
    int fragment_container;
    @Override
    public void share(android.app.Activity activity, FragmentTransaction fragmentTransaction, int fragment_container) {
        this.fragmentTransaction=fragmentTransaction;
        this.activity=activity;
        this.fragment_container=fragment_container;
    }

    public static float CalculateDistance(Location startPoint, Location endPoint)
    {
        return (startPoint.distanceTo(endPoint) / ((float)1000));
    }

    public static float CalculateAvgSpeed(Date startDate, Date endDate, double KM)
    {
        try {
            long different = endDate.getTime() - startDate.getTime();

            float diffHours = (float)((float)different / (float)(60 * 60 * 1000));
            if(diffHours==0) return 0;

            return (float)(KM / diffHours);
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
        return -1;
    }
}
