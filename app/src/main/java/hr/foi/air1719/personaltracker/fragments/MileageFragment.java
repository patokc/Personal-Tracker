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

    Float zbrojDrive = 0f;
    Float zbrojRun = 0f;
    Float zbrojWalk = 0f;
    List<Activity> listActivity= new ArrayList();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mileage_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillListView();
        BarChart barChart = (BarChart) getView().findViewById(R.id.graph);

        ArrayList<BarEntry> entrie = new ArrayList<>();
        entrie.add(new BarEntry(zbrojRun,0));
        entrie.add(new BarEntry(zbrojDrive,1));
        BarDataSet dataSet = new BarDataSet(entrie,"Modes");


        ArrayList<String> date = new ArrayList<>();
        date.add("RUNNING MODE");
        date.add("DRIVING MODE");
        BarData barData = new BarData(date,dataSet);
        barChart.setData(barData);

    }

    final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());

            Map<String, Activity> mapList = (Map<String, Activity>)message.obj;
            listActivity = new ArrayList();
            listActivity.addAll(mapList.values());
            for (Activity a : listActivity) {
                if (a.getMode().equals("RUNNING")) {
                    zbrojRun += a.getDistance();
                }
                else if(a.getMode().equals("DRIVING")){
                    zbrojDrive +=a.getDistance();
                }
                else {
                    zbrojWalk+=a.getDistance();
                }

            }
        }
    };

    private void fillListView() {
        new Thread(new Runnable() {
            public void run() {
                DatabaseFacade dbfacade = new DatabaseFacade(getView().getContext());
                Message message = mHandler.obtainMessage(1, dbfacade.getAllActivities());
                message.sendToTarget();
            }
        }).start();
    }


}
