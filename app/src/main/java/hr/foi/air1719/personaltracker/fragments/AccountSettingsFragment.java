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

    String logInUser = null;
    User korisnik = null;
    User checkUser = null;
    Button actionSaveAccount = null;
    boolean check = false;


    public AccountSettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        check = false;

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
        logInUser = settings.getString("username", "");


        RestServiceHandler restServiceHandler =  new RestServiceHandler() {
            @Override
            public void onDataArrived(Object result, boolean ok) {

                korisnik = (User) result;
                inputFullName.setText(korisnik.getFullname().toString());
                inputUserName.setText(korisnik.getUsername().toString());
                inputEmail.setText(korisnik.getEmail().toString());

            }
        };

        RestServiceCaller restServiceCaller = new RestServiceCaller(restServiceHandler);
        restServiceCaller.getUser(logInUser.toString());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onClick_Save(View v) {


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user", 0);
        logInUser = sharedPreferences.getString("username", "");


        RestServiceHandler restServiceHandler2 =  new RestServiceHandler() {
            @Override
            public void onDataArrived(Object result, boolean ok) {

                korisnik = (User) result;

            }
        };

        RestServiceCaller restServiceCaller2 = new RestServiceCaller(restServiceHandler2);
        restServiceCaller2.getUser(logInUser.toString());

        try {

            if(inputFullName.getText().toString().equals("")) {
                Toast.makeText(this.getActivity(), "Wrong full name!",Toast.LENGTH_LONG).show();
                return;
            }

            if(inputUserName.getText().toString().equals("")) {
                Toast.makeText(this.getActivity(), "Wrong user name!",Toast.LENGTH_LONG).show();
                return;
            }

            RestServiceHandler restServiceHandler3 =  new RestServiceHandler() {
                @Override
                public void onDataArrived(Object result, boolean ok) {

                    checkUser = (User) result;

                }
            };

            RestServiceCaller restServiceCaller3 = new RestServiceCaller(restServiceHandler3);
            restServiceCaller3.getUser(inputUserName.getText().toString());

            if(checkUser != null) {
                Toast.makeText(this.getActivity(), "Username already exists please choose another!",Toast.LENGTH_LONG).show();
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

                if(korisnik.getPassword().equals(Helper.md5(inputOldPassword.getText().toString()))) {
                    Toast.makeText(this.getActivity(), "Your current password is incorrect!",Toast.LENGTH_LONG).show();
                    return;
                }

                if(inputPassword.getText().toString().equals("") || (inputPassword.getText().toString().length()) < 6) {
                    Toast.makeText(this.getActivity(), "Wrong password! Password must contain at least 6 characters!",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!inputPassword.getText().toString().equals(inputRepeatPassword.getText().toString())) {
                    Toast.makeText(this.getActivity(), "Password is not equal!",Toast.LENGTH_LONG).show();
                    return;
                }

                check = true;
            }

            restServiceCaller2.deleteUser(korisnik.getUsername().toString());

            korisnik.setFullname(inputFullName.getText().toString());
            korisnik.setUsername(inputUserName.getText().toString());
            korisnik.setEmail(inputEmail.getText().toString());

            if (check) {
                korisnik.setPassword(Helper.md5(inputPassword.getText().toString()));
            }

            if(korisnik!=null){
                restServiceCaller2.createUser(korisnik);
            }


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", inputUserName.getText().toString());
            editor.commit();

            Toast.makeText(this.getActivity(), "Saved", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
