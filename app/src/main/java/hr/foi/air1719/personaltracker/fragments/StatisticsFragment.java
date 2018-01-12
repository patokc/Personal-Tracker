package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
 * Created by Patricija on 1/12/2018.
 */

public class StatisticsFragment extends Fragment {

    public static StatisticsFragment instance;
    private TabLayout allTabs;
    private CaloriesFragment caloriesFragment;
    private MileageFragment mileageFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statistics_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allTabs = (TabLayout) getActivity().findViewById(R.id.statistics_tabs);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container_statistics, new MileageFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        setAllTabs();
        switchWidget();
    }

    private void setAllTabs() {
        mileageFragment = new MileageFragment();
        caloriesFragment = new CaloriesFragment();
        allTabs.addTab(allTabs.newTab().setText("Mileage"), true);
        allTabs.addTab(allTabs.newTab().setText("Calories"));
    }

    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container_statistics, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void changeTabFragment(int tab) {
        switch (tab) {
            case 0:
                replaceFragment(mileageFragment);
                break;
            case 1:
                replaceFragment(caloriesFragment);
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
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public void onResume(){
        super.onResume();
        ((Main) getActivity()).setActionBarTitle("Statistics");
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.statistics_tabs);
    }
}
