package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Patricija on 1/12/2018.
 */

public class CaloriesFragment extends Fragment {

    Float sumCalories =0f;
    Float sumFuel = 0f;
    Float p = 0.0f;
    List<Activity> listActivity = null;
    ArrayList<BarEntry> entries = null;
    BarDataSet dataSet = null;
    BarChart barChart = null;
    View view;
    private static DecimalFormat df2 = new DecimalFormat(".#");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calories_fragment, container, false);
        fillListView();
        return view;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());
            Map<String, Activity> mapList = (Map<String, Activity>)message.obj;
            sumCalories =0f;
            sumFuel = 0f;

            listActivity = new ArrayList();
            listActivity.addAll(mapList.values());
            for (Activity a : listActivity) {

                sumCalories += a.getAvgCal();
                sumFuel +=a.getAvgFuel();
            }
            fillGraph();
        }
    };

    private void fillListView() {
        new Thread(new Runnable() {
            public void run() {
                DatabaseFacade dbfacade = new DatabaseFacade(getActivity().getApplicationContext());
                Message message = mHandler.obtainMessage(1, dbfacade.getAllActivities());
                message.sendToTarget();
            }
        }).start();
    }

    public void fillGraph () {
        barChart = (BarChart) view.findViewById(R.id.graph);
        entries = new ArrayList<>();
        entries.add(new BarEntry(sumCalories,0));
        entries.add(new BarEntry(sumFuel,1));
        dataSet = new BarDataSet(entries,"Calories/Fuel");

        ArrayList<String> date = new ArrayList<>();
        date.add("Calories");
        date.add("Fuel");
        BarData barData = new BarData(date,dataSet);
        barChart.setData(barData);
        barChart.invalidate();


    }


}
