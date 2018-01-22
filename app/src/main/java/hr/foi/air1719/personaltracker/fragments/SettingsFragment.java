package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import hr.foi.air1719.personaltracker.Main;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Timotea on 5.12.2017..
 */

public class SettingsFragment extends Fragment {

    public static SettingsFragment instance;
    private TabLayout allTabs;
    private AccountSettingsFragment accountSettingsFragment;
    private NavigationSettingsFragment navigationSettingsFragment;


    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        allTabs = (TabLayout) getView().findViewById(R.id.settings_tabs);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container_settings, new AccountSettingsFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        setAllTabs();
        switchWidget();
    }

    private void setAllTabs(){

        accountSettingsFragment = new AccountSettingsFragment();
        navigationSettingsFragment = new NavigationSettingsFragment();
        allTabs.addTab(allTabs.newTab().setText("Account"), true);
        allTabs.addTab(allTabs.newTab().setText("Navigation"));
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container_settings, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void changeTabFragment(int tab){

        switch (tab){
            case 0:
                replaceFragment(accountSettingsFragment);
                break;
            case 1:
                replaceFragment(navigationSettingsFragment);
                break;
        }
    }

    private void switchWidget(){

        allTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void onResume(){
        super.onResume();

        ((Main) getActivity())
                .setActionBarTitle("Settings");
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.settings_tabs);

    }

}
