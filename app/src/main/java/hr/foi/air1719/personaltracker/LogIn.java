package hr.foi.air1719.personaltracker;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.restservice.RestServiceCaller;
import hr.foi.air1719.restservice.RestServiceHandler;


public class LogIn extends AppCompatActivity implements RestServiceHandler {

    private EditText UserName=null;
    private EditText Password= null;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {

            if (!Helper.isInternetAvailable(this)) {
                Toast.makeText(this, "No internet connection right now, please check internet settings and try again", Toast.LENGTH_LONG).show();
            }


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    Toast.makeText(this, "Not allowed to use GPS location", Toast.LENGTH_LONG).show();

                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                    Toast.makeText(this, "Allowed to use GPS location", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    public void onClick_LogIn(View v) {
        UserName=(EditText)findViewById(R.id.txtLogInUserName);
        Password=(EditText)findViewById(R.id.txtLogInPassword);

        if (!Helper.isInternetAvailable(this)) {
            Toast.makeText(this, "No internet connection right now, please check internet settings and try again", Toast.LENGTH_LONG).show();
            return;
        }

        RestServiceCaller restServiceCaller = new RestServiceCaller(this);
        restServiceCaller.getUser(UserName.getText().toString());
    }

    public void onClick_Registration(View v) {
        finish();
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    @Override
    public void onDataArrived(Object data, boolean ok) {
        if (ok) {
            User user = (User) data;

            if (user.getUsername().equals(UserName.getText().toString())
                    && user.getPassword().equals(Helper.md5(Password.getText().toString()))) {
                finish();
                Toast.makeText(getBaseContext(), "Login is successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Main.class);
                startActivity(intent);

                SharedPreferences settings = getSharedPreferences("user", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", UserName.getText().toString());
                editor.commit();

                Thread thread = new Thread(new Runnable(){

                    @Override
                    public void run(){
                        //Activity act = new Activity(ActivityMode.DRIVING);
                        DatabaseFacade dbfacade = new DatabaseFacade(getApplicationContext());
                        //dbfacade.saveActivity(act);


                        /*for(Activity a: dbfacade.getAllActivities().values()){
                            System.out.println(a.getActivityId());
                        }

                        GpsLocation loc = new GpsLocation("1274e380-5f2e-407c-b0cc-a1785ee47b43",12.3243243,21.556345, 1);
                        dbfacade.saveLocation(loc);

                        dbfacade.getLocations("1274e380-5f2e-407c-b0cc-a1785ee47b43");*/

                        dbfacade.syncData();
                    }
                });
                thread.start();

            } else {
                Toast.makeText(getBaseContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
            }
        }
    }
}
