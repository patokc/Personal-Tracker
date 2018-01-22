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

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Patricija on 1/12/2018.
 */

public class MileageFragment extends Fragment {

    Float sumRun =0f;
    Float sumDrive = 0f;
    Float sumWalk = 0f;
    List<Activity> listActivity = null;
    ArrayList<BarEntry> entries = null;
    BarDataSet dataSet = null;
    BarChart barChart = null;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mileage_fragment, container, false);
        fillListView();
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());

            Map<String, Activity> mapList = (Map<String, Activity>)message.obj;
            sumRun =0f;
            sumDrive = 0f;
            sumWalk = 0f;
            listActivity = new ArrayList();
            listActivity.addAll(mapList.values());
            for (Activity a : listActivity) {
                if (a.getMode().toString() == "RUNNING") {
                    sumRun += a.getDistance();
                }
                else if(a.getMode().toString() == "DRIVING"){
                    sumDrive +=a.getDistance();
                }
                else {
                    sumWalk+=a.getDistance();
                }
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
        entries.add(new BarEntry(sumRun, 0));
        entries.add(new BarEntry(sumDrive, 1));
        entries.add(new BarEntry(sumWalk, 2));
        dataSet = new BarDataSet(entries, "Modes");

        ArrayList<String> date = new ArrayList<>();
        date.add("Running mode");
        date.add("Driving mode");
        date.add("Walking mode");
        BarData barData = new BarData(date, dataSet);
        barChart.setData(barData);
        barChart.invalidate();

    }
}
