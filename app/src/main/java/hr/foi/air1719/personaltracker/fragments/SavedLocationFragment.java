package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;
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
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Patricija on 1/17/2018.
 */

public class SavedLocationFragment extends Fragment {

   // String activityID;
    ArrayList savedLocations = new ArrayList();
    ArrayList locationCoordinates =  new ArrayList();

    public SavedLocationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saved_location_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillListView();

        initViews();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.card_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);

        ImageListAdapter adapter = new ImageListAdapter(getActivity().getApplicationContext(), savedLocations, locationCoordinates);
        recyclerView.setAdapter(adapter);

    }


    final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());
            List<Activity> ac = (List<Activity>) message.obj;

            savedLocations.clear();
            for (Activity a : ac) {
                Activity loadImageWalkingMode = new Activity(ActivityMode.WALKING);

                fillView(a.getActivityId());
                loadImageWalkingMode.setDescription(a.getDescription());
                loadImageWalkingMode.setImage("https://benkovic.net/air/img/" + a.getImage());

                savedLocations.add(loadImageWalkingMode);
            }
        }
    };
    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());
            Map<String, GpsLocation> gpsLoc = (Map<String, GpsLocation>)message.obj;
           // List<GpsLocation> gpsLocations = (List<GpsLocation>) message.obj;
            locationCoordinates.clear();
            List<GpsLocation> gpsLocations = new ArrayList();
            gpsLocations.addAll(gpsLoc.values());
           for ( GpsLocation a : gpsLocations) {
                GpsLocation loadCoordinates = new GpsLocation();
                loadCoordinates.setActivityId(a.getActivityId());
                loadCoordinates.setLatitude(a.getLatitude());
                loadCoordinates.setLongitude(a.getLongitude());
                locationCoordinates.add(loadCoordinates);
            }
        }
};
    private void fillListView() {
        new Thread(new Runnable() {
            public void run() {
                DatabaseFacade dbfacade = new DatabaseFacade(getView().getContext());
                Message message = mHandler.obtainMessage(1, dbfacade.getActivityByModeOrderByStartDESC(ActivityMode.WALKING));
                message.sendToTarget();
            }
        }).start();
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
}




