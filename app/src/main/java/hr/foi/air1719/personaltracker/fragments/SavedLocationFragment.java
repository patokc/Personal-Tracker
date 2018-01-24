package hr.foi.air1719.personaltracker.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;


import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.personaltracker.LogIn;
import hr.foi.air1719.personaltracker.R;
import hr.foi.air1719.personaltracker.RecyclerView.SavedLocationsAdapter;
import hr.foi.air1719.personaltracker.RecyclerView.RecyclerItemClickListener;

/**
 * Created by Patricija on 1/17/2018.
 */

public class SavedLocationFragment extends Fragment {


    ArrayList savedLocations = new ArrayList();
    ArrayList locationCoordinates =  new ArrayList();
    List<GpsLocation> gpsLocations = new ArrayList();
    RecyclerView recyclerView;


    public SavedLocationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saved_location_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= (RecyclerView) getActivity().findViewById(R.id.card_recycler_view);
        fillListView();

        recyclerView.addOnItemTouchListener(

                new RecyclerItemClickListener(getActivity().getApplicationContext(),recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Activity activity = (Activity) savedLocations.get(position);
                        Fragment fragment = new SavedLocationsDetails();
                        Bundle bundle = new Bundle();
                        bundle.putString("ID",activity.getActivityId());
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment, "Details");
                        transaction.addToBackStack(null);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.commit();
                    }

                    @Override
                    public void onLongClick(View view, final int position) {
                        final Activity activity = (Activity) savedLocations.get(position);
                        new AlertDialog.Builder(getActivity().getWindow().getContext(),android.R.style.Theme_DeviceDefault_Light_Dialog)
                                .setMessage("Are you sure you want delete this item?")
                                .setCancelable(false)
                                .setTitle("Confirm?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new Thread(new Runnable() {
                                            public void run() {
                                                DatabaseFacade dbfacade = new DatabaseFacade(getActivity().getApplicationContext());
                                                dbfacade.deleteByActivity(activity);
                                            }
                                        }).start();
                                        fillListView();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .create()
                                .show();
                    }

                })
        );
    }

    private void initViews() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        SavedLocationsAdapter adapter = new SavedLocationsAdapter(getActivity().getApplicationContext(), savedLocations, locationCoordinates);
        recyclerView.setAdapter(adapter);

    }

    final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());
            List<Activity> ac = (List<Activity>) message.obj;
            locationCoordinates.clear();
            savedLocations.clear();
            if(ac != null){
                for (Activity a : ac) {
                    Activity loadImageWalkingMode = new Activity(ActivityMode.WALKING);
                    fillView(a.getActivityId().toString());
                    loadImageWalkingMode.setActivityId(a.getActivityId().toString());
                    loadImageWalkingMode.setDescription(a.getDescription().toString());
                    loadImageWalkingMode.setImage("https://benkovic.net/air/img/" + a.getImage().toString());

                    savedLocations.add(loadImageWalkingMode);
                }
            }

        }
    };
    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());
            Map<String, GpsLocation> gpsLoc = (Map<String, GpsLocation>)message.obj;

            gpsLocations = new ArrayList();
            gpsLocations.addAll(gpsLoc.values());
            for ( GpsLocation a : gpsLocations) {
                GpsLocation loadCoordinates = new GpsLocation();
                loadCoordinates.setActivityId(a.getActivityId().toString());
                loadCoordinates.setLatitude(a.getLatitude());
                loadCoordinates.setLongitude(a.getLongitude());
                loadCoordinates.setTimestamp(a.getTimestamp());
                locationCoordinates.add(loadCoordinates);
            }

            initViews();
        }
    };
    private void fillListView() {
        new Thread(new Runnable() {
            public void run() {
                DatabaseFacade dbfacade = new DatabaseFacade(getActivity().getApplicationContext());
                Message message = mHandler.obtainMessage(1, dbfacade.getActivityByModeOrderByStartDESC(ActivityMode.WALKING));
                message.sendToTarget();
            }
        }).start();
    }
    private void fillView(final String activityID) {
        new Thread(new Runnable() {
            public void run() {
                DatabaseFacade dbfacade = new DatabaseFacade(getActivity().getApplicationContext());
                Message message = handler.obtainMessage(1, dbfacade.getLocations(activityID));
                message.sendToTarget();
            }
        }).start();

    }
}



