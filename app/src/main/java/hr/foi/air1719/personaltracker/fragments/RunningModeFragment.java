package hr.foi.air1719.personaltracker.fragments;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Timestamp;
import java.util.Date;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.location.IGPSActivity;
import hr.foi.air1719.location.MyLocation;
import hr.foi.air1719.personaltracker.Helper;
import hr.foi.air1719.personaltracker.Main;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Nikolina on 13.12.2017..
 */

public class RunningModeFragment extends Fragment implements IGPSActivity
{

    MyLocation myLocation=null;
    Button btnRunningModeStart=null;
    EditText txtWeight=null;
    TextView txtTotalDistance=null;
    TextView txtCalories=null;
    Button btnShowRunningHistory = null;
    Button btnShowRoute=null;
    float totalDistance = 0;
    Date startDate = null;
    Location lastPoint=null;


    DatabaseFacade dbCurrentFacade = null;
    Activity currentActivity = null;


    //String weight;
    //Float weightInKg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_running_mode, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        txtTotalDistance=(TextView) getView().findViewById(R.id.txtTotalDistance);
        txtWeight=(EditText) getView().findViewById(R.id.txtWeight);
        txtCalories=(TextView) getView().findViewById(R.id.txtCalories);
        dbCurrentFacade = new DatabaseFacade(getView().getContext());
        currentActivity = new Activity(ActivityMode.RUNNING);

        btnRunningModeStart = (Button) getView().findViewById(R.id.btnRunningModeStart);
        btnRunningModeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onClick_StartRunningMode(v);
            }
        });

        btnShowRunningHistory = (Button) getView().findViewById(R.id.btnShowRunningHistory);
        btnShowRunningHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClick_ShowRunningHistory(v);
            }
        });


        btnShowRoute=(Button)getView().findViewById(R.id.btnShowRoute);
        btnShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onClick_ShowLastRoute(v);

            }
        });
    }

    private void onClick_ShowLastRoute(View v)
    {
        android.app.Fragment fragment = new ActivityMapFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack("Running Activity", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("Running Activity");

        Bundle bundle = new Bundle();
        bundle.putString("activityID", currentActivity.getActivityId());
        fragment.setArguments(bundle);

        transaction.replace(R.id.fragment_container, fragment, "Running Activity");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

    }

    private void onClick_ShowRunningHistory(View v) {

        Fragment fragment = new RunningHistoryFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, "Running History");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }


    private void onClick_StartRunningMode(View v)

    {
        if(myLocation ==null)
        {
            totalDistance = 0;
            startDate = null;
            currentActivity.setActivityId(currentActivity.getActivityId());
            currentActivity.setStart(new Timestamp(new Date().getTime()));

            Toast.makeText(this.getActivity(), "Running mode started", Toast.LENGTH_SHORT).show();
            if(startDate==null) startDate= new Date();
            myLocation = new MyLocation();
            myLocation.LocationStart(this);
            btnRunningModeStart.setText("Stop");
        }

        else
        {
            new Thread(new Runnable() {
                public void run() {


                    currentActivity.setDistance(totalDistance);
                    currentActivity.setFinish(new Timestamp(new Date().getTime()));
                    dbCurrentFacade.saveActivity(currentActivity);

                }
            }).start();

            Toast.makeText(this.getActivity(), "Running mode stopped", Toast.LENGTH_SHORT).show();
            myLocation.LocationListenerStop();
            myLocation = null;
            btnRunningModeStart.setText("Start");

            /*if (txtWeight.getText().toString().isEmpty())
            {
                return;
            }
            else {
                weight = txtWeight.getText().toString();
                weightInKg = Float.parseFloat(weight);
            }
            */
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }


    //float caloriesBurned=0;
    /*public static float CalculateCalories(float a, float b)
    {
        return ((float)1.036)*a*b;
    }*/


    Location tempLocation=null;

    @Override
    public void locationChanged(Location location)
    {
        try
        {
            tempLocation=location;

            if(lastPoint==null)lastPoint = location;
            if(startDate==null) startDate= new Date();
            totalDistance += Helper.CalculateDistance(lastPoint, location);
            lastPoint=location;
            txtTotalDistance.setText(String.format("%.2f", totalDistance) + " km");
            //caloriesBurned=CalculateCalories(totalDistance,weightInKg);
            //txtCalories.setText(caloriesBurned);

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


    public void onResume(){
        super.onResume();
        ((Main) getActivity()).setActionBarTitle("Running mode");
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        //  navigationView.setNavigationItemSelectedListener((Main) getActivity());
        navigationView.setCheckedItem(R.id.runningMode);

    }
}


