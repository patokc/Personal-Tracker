package hr.foi.air1719.personaltracker;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.restservice.RestServiceCaller;
import hr.foi.air1719.restservice.RestServiceHandler;


public class Registration extends AppCompatActivity implements RestServiceHandler  {


    private EditText FullName=null;
    private EditText UserName=null;
    private EditText Password= null;
    private TextView RepeatPassword= null;
    private TextView Email= null;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    boolean doublePressedToExit = false;

    public static final Pattern letters_only_check =
            Pattern.compile("^[a-zA-Z]+$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FullName=(EditText)findViewById(R.id.txtFullName);
        UserName=(EditText)findViewById(R.id.txtUserName);
        Password=(EditText)findViewById(R.id.txtPassword);
        RepeatPassword=(TextView)findViewById(R.id.txtRepeatPassword);
        Email=(TextView)findViewById(R.id.txtEmail);



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

    public void onClick_Registration(View v) {

        try {

            if(FullName.getText().toString().equals("")|| !validate_letters(FullName.getText().toString())) {Toast.makeText(getBaseContext(), "Wrong name! Must contain only letters!",Toast.LENGTH_LONG).show(); return;}
            if(UserName.getText().toString().equals("")) {Toast.makeText(getBaseContext(), "Wrong user name!",Toast.LENGTH_LONG).show(); return;}
            if(Password.getText().toString().equals("") || (Password.getText().toString().length()) < 6) {Toast.makeText(getBaseContext(), "Wrong password! Password must contain at least 6 characters!",Toast.LENGTH_LONG).show();  return;}
            if(!Password.getText().toString().equals(RepeatPassword.getText().toString())) {Toast.makeText(getBaseContext(), "Password is not equal!",Toast.LENGTH_LONG).show();  return;}
            if(!Helper.isValidEmail(Email.getText().toString())){Toast.makeText(getBaseContext(), "Wrong e-mail address!",Toast.LENGTH_LONG).show();  return;}

            if (!Helper.isInternetAvailable(this)) {
                Toast.makeText(this, "No internet connection right now, please check internet settings and try again", Toast.LENGTH_LONG).show();
                return;
            }

            RestServiceCaller restServiceCaller = new RestServiceCaller(this);
            restServiceCaller.getUser(UserName.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validate_letters(String letters) {
        Matcher matcher = letters_only_check.matcher(letters);
        return matcher.find();
    }

    public void onClick_Cancel(View v) {
        finish();
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    @Override
    public void onDataArrived(Object data, boolean ok) {

        if (!ok) { return; }


        if (data != null) {
            Toast.makeText(getBaseContext(), "Username already exists please choose another!", Toast.LENGTH_LONG).show();
        } else {

            if (!Helper.isInternetAvailable(this)) {
                Toast.makeText(this, "No internet connection right now, please check internet settings and try again", Toast.LENGTH_LONG).show();
                return;
            }

            RestServiceHandler regHandler = new RestServiceHandler() {
                @Override
                public void onDataArrived(Object result, boolean ok) {
                    if(ok){
                        Toast.makeText(getBaseContext(),"Registration successfully.",Toast.LENGTH_LONG).show();

                        SharedPreferences settings = getSharedPreferences("Registration", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("FullName", FullName.getText().toString());
                        editor.putString("UserName", UserName.getText().toString());
                        editor.putString("Password", Helper.md5(Password.getText().toString()));
                        editor.putString("Email",    Email.getText().toString());
                        editor.commit();

                        finish();
                        Intent intent = new Intent(getApplicationContext(), LogIn.class);
                        startActivity(intent);
                    }

                }
            };

            User u = new User(UserName.getText().toString(), Helper.md5(Password.getText().toString()), FullName.getText().toString());

            RestServiceCaller restServiceCaller = new RestServiceCaller(regHandler);
            restServiceCaller.createUser(u);

        }
    }

    public void onBackPressed() {

        if (doublePressedToExit) {
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


