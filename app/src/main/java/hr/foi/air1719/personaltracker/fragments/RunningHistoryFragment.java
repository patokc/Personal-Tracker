package hr.foi.air1719.personaltracker.fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.personaltracker.R;


/**
 * Created by Nikolina on 14.01.2018..
 */

public class RunningHistoryFragment extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TableLayout tableRunningHistory = null;

    private OnFragmentInteractionListener mListener;

    public RunningHistoryFragment() {
        // Required empty public constructor
    }


    public static RunningHistoryFragment newInstance(String param1, String param2) {
        RunningHistoryFragment fragment = new RunningHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    static RunningHistoryFragment myFrame;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myFrame = this;
        tableRunningHistory = (TableLayout) getView().findViewById(R.id.tableRunningHistory);
        fillTable();

    }

    private void fillTable() {

        new Thread(new Runnable() {
            public void run() {
                DatabaseFacade dbfacade = new DatabaseFacade(getView().getContext());
                Message message = mHandler.obtainMessage(1, dbfacade.getActivityByModeOrderByStartDESC
                        (ActivityMode.RUNNING));
                message.sendToTarget();
            }
        }).start();

    }


    private void onClick_ShowActivity(View v, String  activityID) {

        android.app.Fragment fragment = new ActivityMapFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack("Running Activity", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("activityID", activityID);
        fragment.setArguments(bundle);

        transaction.addToBackStack("Running Activity");
        transaction.replace(R.id.fragment_container, fragment, "Running Activity");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_running_history, container, false);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public float CalculateCalories(float w, float d)
    {
        return ((float)1.036)*w*d;
    }


    final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());
            int i = 0;


            List<Activity> ac = (List<Activity>) message.obj;

            TableRow firstrow = new TableRow(getActivity());

            TextView tv1 = new TextView(getActivity());
            tv1.setText("Date");
            tv1.setBackgroundColor(Color.LTGRAY);
            tv1.setHeight(100);
            tv1.setTypeface(null, Typeface.BOLD);
            tv1.setGravity(Gravity.CENTER);
            tv1.setTextColor(Color.BLACK);
            firstrow.addView(tv1);

            TextView tv2 = new TextView(getActivity());
            tv2.setText("Distance");
            tv2.setTextColor(Color.BLACK);
            tv2.setBackgroundColor(Color.LTGRAY);
            tv2.setHeight(100);
            tv2.setTypeface(null, Typeface.BOLD);
            tv2.setGravity(Gravity.CENTER);
            firstrow.addView(tv2);

            TextView tv3 = new TextView(getActivity());
            tv3.setText("Calories");
            tv3.setTextColor(Color.BLACK);
            tv3.setBackgroundColor(Color.LTGRAY);
            tv3.setHeight(100);
            tv3.setTypeface(null, Typeface.BOLD);
            tv3.setGravity(Gravity.CENTER);
            firstrow.addView(tv3);
            //tableRunningHistory.addView(firstrow);

            TextView tv4 = new TextView(getActivity());
            tv4.setText("Map View");
            tv4.setTextColor(Color.BLACK);
            tv4.setBackgroundColor(Color.LTGRAY);
            tv4.setHeight(100);
            tv4.setTypeface(null, Typeface.BOLD);
            tv4.setGravity(Gravity.CENTER);
            firstrow.addView(tv4);
            tableRunningHistory.addView(firstrow);


            if(ac != null){
                for (Activity a : ac) {

                    TableRow tablerow = new TableRow(getActivity());
                    tablerow.setPadding(0,13,0,13);
                    TextView t1v = new TextView(getActivity());
                    t1v.setText(a.getStart().toString().substring(0, 11));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.CENTER);
                    if (i % 2 == 0) t1v.setBackgroundColor(Color.rgb(236, 236, 236));
                    tablerow.addView(t1v);


                    TextView t2v = new TextView(getActivity());
                    t2v.setText(String.valueOf(String.format("%.1f", a.getDistance())));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.CENTER);
                    if (i % 2 == 0) t2v.setBackgroundColor(Color.rgb(236, 236, 236));
                    tablerow.addView(t2v);

                    TextView t3v = new TextView(getActivity());
                    t3v.setText(String.valueOf(String.format("%.1f", a.getAvgCal()==0?CalculateCalories(77.0f,a.getDistance()):a.getAvgCal())));
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.CENTER);
                    if(i%2==0) t3v.setBackgroundColor(Color.rgb(236, 236, 236));
                    tablerow.addView(t3v);


                    TextView t4v = new TextView(getActivity());
                    t4v.setText("Map View");
                    t4v.setTextColor(Color.BLUE);
                    t4v.setGravity(Gravity.CENTER);
                    if (i % 2 == 0) t4v.setBackgroundColor(Color.rgb(236, 236, 236));

                    final String activityId = a.getActivityId();
                    t4v.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            onClick_ShowActivity(v, activityId);

                        }
                    });
                    tablerow.addView(t4v);

                    tableRunningHistory.addView(tablerow);
                    i++;

                }

            }

        }
    };



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}