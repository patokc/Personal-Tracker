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
import android.widget.Toast;

import hr.foi.air1719.database.entities.User;

import hr.foi.air1719.personaltracker.Helper;
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
    User korisnik = null;

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
        actionSaveNavigation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClick_Save(v);
            }
        });

        SharedPreferences settings = this.getActivity().getSharedPreferences("user", 0);

        inputFuelConsumption.setText(Float.toString(settings.getFloat("fuelConsumption", 0)));
        inputWeight.setText(Float.toString(settings.getFloat("weight", 0)));

        sbRefreshRate.setProgress(settings.getInt("refreshRate", 0));
        sbMinimalDistance.setProgress(settings.getInt("minimalDistance", 0));

        lblOutputRefreshRate.setText(sbRefreshRate.getProgress() + "/" + sbRefreshRate.getMax());
        lblOutputMinimalDistance.setText(sbMinimalDistance.getProgress()+ "/" + sbMinimalDistance.getMax());

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

        lblOutputRefreshRate.setText(sbRefreshRate.getProgress() + "/" + sbRefreshRate.getMax());
        lblOutputMinimalDistance.setText(sbMinimalDistance.getProgress()+ "/" + sbMinimalDistance.getMax());
    }


    public void onClick_Save(View v) {

        SharedPreferences settings = this.getActivity().getSharedPreferences("user", 0);


        RestServiceHandler restServiceHandler =  new RestServiceHandler() {
            @Override
            public void onDataArrived(Object result, boolean ok) {

                korisnik = (User) result;

            }
        };

        RestServiceCaller restServiceCaller = new RestServiceCaller(restServiceHandler);
        restServiceCaller.getUser(settings.getString("username", ""));

        try {

            if(inputFuelConsumption.getText().toString().equals("")) {
                Toast.makeText(this.getActivity(), "Fuel consumption is not inserted!",Toast.LENGTH_LONG).show();
                return;
            }

            if(Helper.validateLetters(inputFuelConsumption.getText().toString())) {
                Toast.makeText(this.getActivity(), "Inserted fuel consumption is not a number!",Toast.LENGTH_LONG).show();
                return;
            }

            if(inputFuelConsumption.getText().toString().contains(",")) {
                Toast.makeText(this.getActivity(), "Fuel consumption is not in right format! Hint: use dot instead of comma!",Toast.LENGTH_LONG).show();
                return;
            }

            if(inputFuelConsumption.getText().toString().matches("[0-9]+") || inputFuelConsumption.getText().toString().matches("[0-9]+\\.[0-9]+")) {


            }

            else{
                Toast.makeText(this.getActivity(), "Fuel consumption is not in right format!",Toast.LENGTH_LONG).show();
                return;
            }

            if(inputWeight.getText().toString().equals("")) {
                Toast.makeText(this.getActivity(), "Weight is not inserted!",Toast.LENGTH_LONG).show();
                return;
            }

            if(Helper.validateLetters(inputWeight.getText().toString())) {
                Toast.makeText(this.getActivity(), "Inserted weight is not a number!",Toast.LENGTH_LONG).show();
                return;
            }

            if(inputWeight.getText().toString().contains(",")) {
                Toast.makeText(this.getActivity(), "Weight is not in right format! Hint: use dot instead of comma!",Toast.LENGTH_LONG).show();
                return;
            }

            if(inputWeight.getText().toString().matches("[0-9]+") || inputWeight.getText().toString().matches("[0-9]+\\.[0-9]+")) {


            }

            else{
                Toast.makeText(this.getActivity(), "Weight is not in right format!",Toast.LENGTH_LONG).show();
                return;
            }

            korisnik.setAvgFuel(Float.parseFloat(inputFuelConsumption.getText().toString()));
            korisnik.setWeight(Float.parseFloat(inputWeight.getText().toString()));

            if(korisnik!=null){

                restServiceCaller.createUser(korisnik);

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("refreshRate", sbRefreshRate.getProgress());
                editor.putInt("minimalDistance", sbMinimalDistance.getProgress());
                editor.putFloat("fuelConsumption", korisnik.getAvgFuel());
                editor.putFloat("weight", korisnik.getWeight());
                editor.commit();

                Toast.makeText(this.getActivity(), "Saved", Toast.LENGTH_SHORT).show();

            }

            else {
                Toast.makeText(this.getActivity(), "Problem with saving data!",Toast.LENGTH_LONG).show();
                return;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
