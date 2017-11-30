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
import hr.foi.air1719.database.entities.Location;
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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Toast.makeText(fragment.getActivity(), "No PERMISSIONS", Toast.LENGTH_LONG).show();

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // Toast.makeText(fragment.getActivity(), "Yes PERMISSIONS", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onClick_LogIn(View v) {
        UserName=(EditText)findViewById(R.id.txtLogInUserName);
        Password=(EditText)findViewById(R.id.txtLogInPassword);

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
                        Location loc = new Location(UserName.getText().toString(), 1,2.5, 6.1);
                        DatabaseFacade dbfacade = new DatabaseFacade(getApplicationContext());
                        dbfacade.saveLocation(loc);
                        dbfacade.getLocation();
                    }
                });
                thread.start();


            } else {
                Toast.makeText(getBaseContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
            }

        }
    }
}
