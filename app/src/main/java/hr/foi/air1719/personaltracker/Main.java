package hr.foi.air1719.personaltracker;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import hr.foi.air1719.personaltracker.fragments.DrivingModeFragment;

import hr.foi.air1719.personaltracker.fragments.MapFragment;
import hr.foi.air1719.personaltracker.fragments.Settings;


public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {


    NavigationView navigationView;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;
    ActionBarDrawerToggle toggle;
    boolean doublePressedToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getFragmentManager().addOnBackStackChangedListener(this);
        toolbar.setNavigationOnClickListener(navigationClick);

        navigationView.setCheckedItem(R.id.walkingMode);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new hr.foi.air1719.personaltracker.fragments.MapFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            onNavigationItemSelected(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectedFragment(int itemId) {
        Fragment fragment = null;
        String tag = null;
        switch (itemId) {
            case R.id.walkingMode:
                fragment = new MapFragment();
                tag = "WalkingMode";
                break;
            case R.id.runningMode:
                tag = "RunningMode";
                break;
            case R.id.drivingMode:
                fragment = new DrivingModeFragment();
                tag = "DrivingMode";
                break;
            case R.id.settings:
                fragment = new Settings();
                tag = "Settings";
                break;
            case R.id.action_settings:
                fragment = new Settings();
                tag = "Settings";
                break;
            case R.id.logout:
                tag= "Logout";
                break;
        }

        if (fragment != null) {

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment, tag);
            if (tag != "WalkingMode" ) {
                transaction.addToBackStack(null);
            }


            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();

        }

        if(tag == "Logout") {
            new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_Dialog)
                    .setMessage("Are you sure you want to log out?")
                    .setCancelable(false)
                    .setTitle("Log out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            Intent intent = new Intent(getApplicationContext(), LogIn.class);
                            startActivity(intent);
                            SharedPreferences settings = getSharedPreferences("user", 0);
                            SharedPreferences.Editor editor = settings.edit().clear();
                            editor.commit();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        selectedFragment(id);
        if (id == R.id.logout) {
            return false;
        }
        else {
        return true;
        }
    }

    @Override
    public void onBackStackChanged() {

        FragmentManager fragmentManager = getFragmentManager();
        toggle.setDrawerIndicatorEnabled(fragmentManager.getBackStackEntryCount() == 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(fragmentManager.getBackStackEntryCount() > 0);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() != 0) {

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                fragmentManager.popBackStack();
                navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.walkingMode);
            }
        } else {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            } else if (doublePressedToExit) {
                super.onBackPressed();

            } else {
                this.doublePressedToExit = true;
                Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doublePressedToExit = false;
                    }
                }, 2000);
            }
        }
    }

    View.OnClickListener navigationClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    drawer.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
            }
    };
}
