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

            if(FullName.getText().toString().equals("")) {Toast.makeText(getBaseContext(), "Wrong name!",Toast.LENGTH_LONG).show(); return;}
            if(UserName.getText().toString().equals("")) {Toast.makeText(getBaseContext(), "Wrong user name!",Toast.LENGTH_LONG).show(); return;}
            if(Password.getText().toString().equals("")) {Toast.makeText(getBaseContext(), "Wrong password!",Toast.LENGTH_LONG).show();  return;}
            if(!Password.getText().toString().equals(RepeatPassword.getText().toString())) {Toast.makeText(getBaseContext(), "Password is not equal!",Toast.LENGTH_LONG).show();  return;}
            if(!Helper.isValidEmail(Email.getText().toString())){Toast.makeText(getBaseContext(), "Wrong e-mail address!",Toast.LENGTH_LONG).show();  return;}

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

        if(output != null && output.indexOf(UserName.getText().toString())>-1 && output.indexOf(Helper.md5(Password.getText().toString()))>-1)
        {
            Toast.makeText(getBaseContext(),"Registration successfully.",Toast.LENGTH_LONG).show();

            SharedPreferences settings = getSharedPreferences("Registration", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("FullName", FullName.getText().toString());
            editor.putString("UserName", UserName.getText().toString());
            editor.putString("Password", Helper.md5(Password.getText().toString()));
            editor.putString("Email",    Email.getText().toString());
            editor.commit();

            finish();
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
        }
    }
}

