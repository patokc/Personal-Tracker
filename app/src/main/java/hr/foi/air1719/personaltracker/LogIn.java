package hr.foi.air1719.personaltracker;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hr.foi.air1719.database.entities.Location;
import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.restservice.RestServiceCaller;
import hr.foi.air1719.restservice.RestServiceHandler;


public class LogIn extends AppCompatActivity implements RestServiceHandler {

    private EditText UserName=null;
    private EditText Password= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void onClick_LogIn(View v) {
        UserName=(EditText)findViewById(R.id.txtLogInUserName);
        Password=(EditText)findViewById(R.id.txtLogInPassword);

        RestServiceCaller restServiceCaller = new RestServiceCaller(this);
        restServiceCaller.getUser(UserName.getText().toString());
        Location location = new Location(1, 2, 1.23, 23.21);
        restServiceCaller.saveLocation(location, UserName.getText().toString());
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

            } else {
                Toast.makeText(getBaseContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
            }

        }
    }
}
