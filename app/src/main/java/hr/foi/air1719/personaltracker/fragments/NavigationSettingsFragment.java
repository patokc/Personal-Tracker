package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.personaltracker.R;
import hr.foi.air1719.restservice.RestServiceCaller;
import hr.foi.air1719.restservice.RestServiceHandler;

/**
 * Created by Timotea on 13.1.2018..
 */

public class NavigationSettingsFragment extends Fragment {

    TextView lblOutputRefreshRate = null;
    TextView lblOutputMinimalDistance = null;
    SeekBar sbRefreshRate = null;
    SeekBar sbMinimalDistance = null;
    EditText inputFuelConsumption = null;
    EditText inputWeight = null;

    Button actionSaveNavigation = null;
    String logedInUser = null;
    User user = null;

    public NavigationSettingsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_settings, container, false);

        lblOutputRefreshRate = (TextView) view.findViewById(R.id.lblOutputRefreshRate);
        lblOutputMinimalDistance = (TextView) view.findViewById(R.id.lblOutputMinimalDistance);
        sbRefreshRate = (SeekBar) view.findViewById(R.id.sbRefreshRate);
        sbMinimalDistance = (SeekBar) view.findViewById(R.id.sbMinimalDistance);

        inputFuelConsumption = (EditText) view.findViewById(R.id.inputFuelConsumption);
        inputWeight = (EditText) view.findViewById(R.id.inputWeight);

        actionSaveNavigation = (Button) view.findViewById(R.id.actionSaveNavigation);

        SharedPreferences settings = this.getActivity().getSharedPreferences("user", 0);
        logedInUser = settings.getString("username", "");

        RestServiceHandler restServiceHandler =  new RestServiceHandler() {
            @Override
            public void onDataArrived(Object result, boolean ok) {

                user = (User) result;
                inputFuelConsumption.setText(Float.toString(user.getAvgFuel()));
                inputWeight.setText(Float.toString(user.getWeight()));

                lblOutputRefreshRate.setText(sbRefreshRate.getProgress()+ "/" + sbRefreshRate.getMax());
                lblOutputMinimalDistance.setText(sbMinimalDistance.getProgress()+ "/" + sbMinimalDistance.getMax());

            }
        };

        RestServiceCaller restServiceCaller = new RestServiceCaller(restServiceHandler);
        restServiceCaller.getUser(logedInUser.toString());

        sbRefreshRate.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                        progress = progresValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        
                        lblOutputRefreshRate.setText(progress + "/" + seekBar.getMax());
                    }
                });


        sbMinimalDistance.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                        progress = progresValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        lblOutputMinimalDistance.setText(progress + "/" + seekBar.getMax());
                    }
                });


        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
