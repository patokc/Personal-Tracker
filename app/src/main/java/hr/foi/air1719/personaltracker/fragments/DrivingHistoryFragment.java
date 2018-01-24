package hr.foi.air1719.personaltracker.fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import hr.foi.air1719.core.facade.DataHandler;
import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by DrazenVuk on 12/20/2017.
 */

public class DrivingHistoryFragment extends android.app.Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AlertDialog.Builder builder = null;
    TableLayout tableHistory=null;
    //Button btnDeleteHistory = null;

    private OnFragmentInteractionListener mListener;

    public DrivingHistoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DrivingHistoryFragment newInstance(String param1, String param2) {
        DrivingHistoryFragment fragment = new DrivingHistoryFragment();
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

    static DrivingHistoryFragment myFrame;
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myFrame=this;
        tableHistory = (TableLayout)getView().findViewById(R.id.tableHistory);
        //btnDeleteHistory = (Button)getView().findViewById(R.id.btnDeleteDrivingActivitys);
        builder = new AlertDialog.Builder(getActivity());

        /*btnDeleteHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClick_DeleteHistory(v);
            }
        });*/

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                new Thread(new Runnable() {
                    public void run() {
                        DatabaseFacade dbfacade = new DatabaseFacade(getView().getContext());
                        dbfacade.deleteByActivity(activityForDelete);
                    }
                }).start();

                View row = (View) viewForDelete.getParent();
                ViewGroup container = ((ViewGroup)row.getParent());
                container.removeView(row);
                container.invalidate();

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        fillGrid();
    }


    public void onClick_DeleteHistory(View v) {
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete all Driver activities?");
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void fillGrid() {

        new Thread(new Runnable() {
            public void run() {
                DatabaseFacade dbfacade = new DatabaseFacade(getView().getContext());
                Message message = mHandler.obtainMessage(1, dbfacade.getActivityByModeOrderByStartDESC(ActivityMode.DRIVING));
                message.sendToTarget();
            }
        }).start();
    }


    private Activity activityForDelete = null;
    private View viewForDelete = null;
    public void onClick_DeleteActivity(View v, final Activity activity) {

        activityForDelete=activity;
        viewForDelete=v;
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this activity?");
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void onClick_ShowActivity(View v, String activityID)
    {
        android.app.Fragment fragment = new ActivityMapFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack("Driving Activity", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("activityID", activityID);
        fragment.setArguments(bundle);

        transaction.addToBackStack("Driving Activity");
        transaction.replace(R.id.fragment_container, fragment, "Driving Activity");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driving_history, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());
            int i=0;


            List<Activity> ac = (List<Activity>)message.obj;

            TableRow tbrow0 = new TableRow(getActivity());

            TextView tv0 = new TextView(getActivity());
            tv0.setText(" Start ");
            tv0.setBackgroundColor(Color.LTGRAY);
            tv0.setHeight(100);
            tv0.setTypeface(null, Typeface.BOLD);
            tv0.setGravity(Gravity.CENTER);
            tv0.setTextColor(Color.BLACK);
            tbrow0.addView(tv0);

            TextView tv2 = new TextView(getActivity());
            tv2.setText(" AVG speed ");
            tv2.setTextColor(Color.BLACK);
            tv2.setBackgroundColor(Color.LTGRAY);
            tv2.setHeight(100);
            tv2.setTypeface(null, Typeface.BOLD);
            tv2.setGravity(Gravity.CENTER);
            tbrow0.addView(tv2);

            TextView tv3 = new TextView(getActivity());
            tv3.setText(" Distance ");
            tv3.setTextColor(Color.BLACK);
            tv3.setBackgroundColor(Color.LTGRAY);
            tv3.setHeight(100);
            tv3.setTypeface(null, Typeface.BOLD);
            tv3.setGravity(Gravity.CENTER);
            tbrow0.addView(tv3);


            TextView tv4 = new TextView(getActivity());
            tv4.setText("DEL");
            tv4.setTextColor(Color.BLACK);
            tv4.setBackgroundColor(Color.LTGRAY);
            tv4.setHeight(100);
            tv4.setTypeface(null, Typeface.BOLD);
            tv4.setGravity(Gravity.CENTER);
            tbrow0.addView(tv4);


            TextView tv5 = new TextView(getActivity());
            tv5.setText(" Show ");
            tv5.setTextColor(Color.BLACK);
            tv5.setBackgroundColor(Color.LTGRAY);
            tv5.setHeight(100);
            tv5.setTypeface(null, Typeface.BOLD);
            tv5.setGravity(Gravity.CENTER);
            tbrow0.addView(tv5);


            tableHistory.addView(tbrow0);

            if(ac != null){
                for (Activity a : ac) {

                    TableRow tbrow = new TableRow(getActivity());
                    tbrow.setPadding(0,13,0,13);
                    TextView t1v = new TextView(getActivity());
                    t1v.setText(a.getStart().toString().substring(0, 11));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.CENTER);
                    if(i%2==0) t1v.setBackgroundColor(Color.rgb(236, 236, 236));
                    tbrow.addView(t1v);

                    TextView t3v = new TextView(getActivity());
                    t3v.setText(String.valueOf(String.format("%.1f", a.getAverageSpeed())));
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.CENTER);
                    if(i%2==0) t3v.setBackgroundColor(Color.rgb(236, 236, 236));
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(getActivity());
                    t4v.setText(String.valueOf(String.format("%.1f", a.getDistance())));
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.CENTER);
                    if(i%2==0) t4v.setBackgroundColor(Color.rgb(236, 236, 236));
                    tbrow.addView(t4v);

                    TextView t4_1v = new TextView(getActivity());
                    t4_1v.setText("Delete");
                    t4_1v.setGravity(Gravity.CENTER);
                    t4_1v.setTextColor(Color.RED);
                    if(i%2==0) t4_1v.setBackgroundColor(Color.rgb(236, 236, 236));
                    tbrow.addView(t4_1v);

                    TextView t5v = new TextView(getActivity());
                    t5v.setText("Show");
                    t5v.setGravity(Gravity.CENTER);
                    t5v.setTextColor(Color.BLUE);
                    if(i%2==0) t5v.setBackgroundColor(Color.rgb(236, 236, 236));


                    final String activityId = a.getActivityId();
                    t5v.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            onClick_ShowActivity(v, activityId);
                        }
                    });
                    tbrow.addView(t5v);


                    final Activity activity = a;
                    t4_1v.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            onClick_DeleteActivity(v, activity);
                        }
                    });



                    tableHistory.addView(tbrow);
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


