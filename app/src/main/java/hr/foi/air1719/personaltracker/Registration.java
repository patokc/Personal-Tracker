package hr.foi.air1719.personaltracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.restservice.RestServiceCaller;
import hr.foi.air1719.restservice.RestServiceHandler;


public class Registration extends AppCompatActivity  {

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

            User user = new User(UserName.getText().toString(), Helper.md5(Password.getText().toString()), FullName.getText().toString());

            RestServiceCaller restServiceCaller = new RestServiceCaller(regHandler);
            restServiceCaller.createUser(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_Cancel(View v) {
        finish();
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

}

