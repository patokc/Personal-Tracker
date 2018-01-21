package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;

import android.os.Bundle;
import android.content.SharedPreferences;

import android.support.annotation.Nullable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import hr.foi.air1719.database.entities.User;

import hr.foi.air1719.personaltracker.Helper;
import hr.foi.air1719.personaltracker.R;
import hr.foi.air1719.restservice.RestServiceCaller;
import hr.foi.air1719.restservice.RestServiceHandler;

/**
 * Created by Timotea on 13.1.2018..
 */

public class AccountSettingsFragment extends Fragment  {

    EditText inputFullName = null;
    EditText inputUserName = null;
    EditText inputEmail = null;
    EditText inputOldPassword = null;
    EditText inputPassword = null;
    EditText inputRepeatPassword = null;

    User korisnik = null;
    Button actionSaveAccount = null;
    boolean check = false;

    float fuelConsumption = 0;
    float weight = 0;

    public AccountSettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        inputFullName = (EditText)view.findViewById(R.id.inputFullName);
        inputUserName = (EditText)view.findViewById(R.id.inputUserName);
        inputEmail = (EditText) view.findViewById(R.id.inputEmail);

        inputOldPassword = (EditText) view.findViewById(R.id.inputOldPassword);
        inputPassword = (EditText) view.findViewById(R.id.inputPassword);
        inputRepeatPassword = (EditText) view.findViewById(R.id.inputRepeatPassword);

        actionSaveAccount = (Button) view.findViewById(R.id.actionSaveAccount);
        actionSaveAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClick_Save(v);
            }
        });

        SharedPreferences settings = this.getActivity().getSharedPreferences("user", 0);

        inputFullName.setText(settings.getString("fullName", ""));
        inputUserName.setText(settings.getString("username", ""));
        inputUserName.setEnabled(false);
        inputEmail.setText(settings.getString("email", ""));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onClick_Save(View v) {

        SharedPreferences settings = this.getActivity().getSharedPreferences("user", 0);
        check = false;

        try {

            if(inputFullName.getText().toString().equals("")) {
                Toast.makeText(this.getActivity(), "Wrong full name!",Toast.LENGTH_LONG).show();
                return;
            }

            if(!Helper.isValidEmail(inputEmail.getText().toString())){
                Toast.makeText(this.getActivity(), "Wrong e-mail address!",Toast.LENGTH_LONG).show();
                return;
            }

            if(!inputOldPassword.getText().toString().equals("") || !inputPassword.getText().toString().equals("") || !inputRepeatPassword.getText().toString().equals("")) {

                if(inputOldPassword.getText().toString().equals("")) {
                    Toast.makeText(this.getActivity(), "Enter your current password!",Toast.LENGTH_LONG).show();
                    return;
                }

                String password = settings.getString("password", "");

                if(!password.equals(Helper.md5(inputOldPassword.getText().toString()))) {
                    Toast.makeText(this.getActivity(), "Your current password is incorrect!",Toast.LENGTH_LONG).show();
                    return;
                }

                if(inputPassword.getText().toString().equals("") || (inputPassword.getText().toString().length()) < 6) {
                    Toast.makeText(this.getActivity(), "Wrong password! Password must contain at least 6 characters!",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!inputPassword.getText().toString().equals(inputRepeatPassword.getText().toString())) {
                    Toast.makeText(this.getActivity(), "Passwords are not equal!",Toast.LENGTH_LONG).show();
                    return;
                }

                check = true;

            }

            RestServiceHandler restServiceHandler =  new RestServiceHandler() {
                @Override
                public void onDataArrived(Object result, boolean ok) {

                    korisnik = (User) result;

                }
            };

            RestServiceCaller restServiceCaller = new RestServiceCaller(restServiceHandler);
            restServiceCaller.getUser(settings.getString("username", ""));

            korisnik.setFullname(inputFullName.getText().toString());
            korisnik.setEmail(inputEmail.getText().toString());

            if(check){
                korisnik.setPassword(Helper.md5(inputPassword.getText().toString()));
            }

            if(korisnik!=null){

                restServiceCaller.createUser(korisnik);

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("fullName", korisnik.getFullname().toString());
                editor.putString("email", korisnik.getEmail().toString());
                editor.putString("password", korisnik.getPassword().toString());
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
