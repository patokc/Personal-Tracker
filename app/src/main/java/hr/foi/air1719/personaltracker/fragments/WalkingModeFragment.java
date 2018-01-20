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

import hr.foi.air1719.personaltracker.Main;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Patricija on 1/15/2018.
 */

public class WalkingModeFragment extends Fragment {
    public static WalkingModeFragment instance;
    private TabLayout allTabs;
    private MapFragment mapFragment;
    private SavedLocationFragment savedLocationFragment;

    public WalkingModeFragment () {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.walking_mode_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allTabs = (TabLayout) getActivity().findViewById(R.id.walkingMode_tabs);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container_walking_mode, new MapFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        setAllTabs();
        switchWidget();
    }

    private void setAllTabs() {
        mapFragment= new MapFragment();
        savedLocationFragment= new SavedLocationFragment();
        allTabs.addTab(allTabs.newTab().setText("Map"), true);
        allTabs.addTab(allTabs.newTab().setText("Saved locations"));
    }

    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container_walking_mode, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void changeTabFragment(int tab) {
        switch (tab) {
            case 0:
                replaceFragment(mapFragment);
                break;
            case 1:
                replaceFragment(savedLocationFragment);
                break;
        }
    }

    private void switchWidget() {
        allTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {changeTabFragment(tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {changeTabFragment(tab.getPosition());

            }
        });
    }
    public void onResume(){
        super.onResume();
        ((Main) getActivity()).setActionBarTitle("Walking mode");
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.walkingMode);
    }
}
