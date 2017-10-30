package hr.foi.air1719.personaltracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import hr.foi.air1719.webservice.WebService;

public class Registration extends AppCompatActivity implements WebService.AsyncResponse {

    private EditText FullName=null;
    private EditText UserName=null;
    private EditText Password= null;
    private TextView RepeatPassword= null;
    private TextView Email= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SharedPreferences settings = getSharedPreferences("Registration", 0);
        if(!settings.getString("UserName", "0").toString().equals("0")) {
            finish();
        }

        FullName=(EditText)findViewById(R.id.txtFullName);
        UserName=(EditText)findViewById(R.id.txtUserName);
        Password=(EditText)findViewById(R.id.txtPassword);
        RepeatPassword=(TextView)findViewById(R.id.txtRepeatPassword);
        Email=(TextView)findViewById(R.id.txtEmail);
    }

    public void onClick_Registration(View v) {


        try {
            JSONObject object = new JSONObject();
            object.put("fullname", FullName.getText().toString());
            object.put("username", UserName.getText().toString());
            object.put("password", Helper.md5(Password.getText().toString()));
            object.put("email", Email.getText().toString());
            object.put("userId", "0");

            WebService ws = new WebService(WebService.RestMethods.PUT,
                    object,"https://tracker-21f6d.firebaseio.com/users/"+ UserName.getText().toString() +".json",this);

            ws.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClick_Cancel(View v) {
        finish();
    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(getBaseContext(),"Response from server:" + output,Toast.LENGTH_LONG).show();
    }
}

