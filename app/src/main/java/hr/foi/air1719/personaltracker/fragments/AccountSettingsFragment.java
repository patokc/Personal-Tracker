package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.content.SharedPreferences;

import android.support.annotation.Nullable;

import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import hr.foi.air1719.personaltracker.Helper;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Timotea on 13.1.2018..
 */

public class AccountSettingsFragment extends Fragment implements View.OnClickListener {

    EditText inputFullName = null;
    EditText inputUserName = null;
    EditText inputEmail = null;


    public AccountSettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        Button button = (Button) view.findViewById(R.id.actionCancelAccount);
        button.setOnClickListener(this);

        inputFullName = (EditText)view.findViewById(R.id.inputFullName);
        inputUserName = (EditText)view.findViewById(R.id.inputUserName);
        inputEmail = (EditText) view.findViewById(R.id.inputEmail);

        SharedPreferences settings = this.getActivity().getSharedPreferences("Registration", 0);
        inputFullName.setText(settings.getString("FullName", ""));
        inputUserName.setText(settings.getString("UserName", ""));
        inputEmail.setText(settings.getString("Email", ""));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {

        Fragment fragment = new AccountSettingsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.layout_account_settings, fragment);
        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }
}
