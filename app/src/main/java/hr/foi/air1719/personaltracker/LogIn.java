package hr.foi.air1719.personaltracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import hr.foi.air1719.webservice.WebService;

public class LogIn extends AppCompatActivity implements WebService.AsyncResponse {

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

        WebService ws = new WebService(WebService.RestMethods.GET,
                null,"https://tracker-21f6d.firebaseio.com/users/"+ UserName.getText().toString() +".json",
                this);

        ws.execute();
    }

    public void onClick_Registration(View v) {
        finish();
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(String output) {

        if(output != null && output.indexOf(UserName.getText().toString())>-1 &&
                output.indexOf(Helper.md5(Password.getText().toString()))>-1)
        {
            finish();
            Toast.makeText(getBaseContext(),"Login is successful",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Username or password is incorrect",Toast.LENGTH_LONG).show();
        }
    }
}
