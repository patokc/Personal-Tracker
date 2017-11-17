package hr.foi.air1719.personaltracker;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import hr.foi.air1719.location.IGPSActivity;
import android.location.Location;
import android.widget.Toast;

import hr.foi.air1719.location.MyLocation;


public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , IGPSActivity {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        //ovo sluzi za dohvacanje lokacije. Ovo radi na principu daj mi zadnju poznatu lokaciju, ovo ne mora biti tocna lokacija, nego zadnja poznata
        //tocna lokacija se bude prozivala na drugaciji nacin "myLocation.LocationStart(this)" i imala bude callback metodu
        MyLocation myLocation = new MyLocation();
        Location location = myLocation.GetLastKnownLocation(this);

        if(location != null)
        {
            Toast.makeText(this, "Your location is: \nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + "\nAccuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Your GPS is off, please turn on your GPS.", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.runningMode) {

        } else if (id == R.id.walkingMode) {

        } else if (id == R.id.drivingMode) {

        } else if (id == R.id.settings) {

        } else if (id == R.id.exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void locationChanged(double longitude, double latitude) {

    }

    @Override
    public void displayGPSSettingsDialog() {

    }
}
