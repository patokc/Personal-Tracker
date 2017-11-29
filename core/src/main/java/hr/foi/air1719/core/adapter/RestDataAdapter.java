package hr.foi.air1719.core.adapter;


import android.content.Context;
import android.content.SharedPreferences;

import hr.foi.air1719.database.entities.Location;
import hr.foi.air1719.restservice.RestServiceCaller;
import hr.foi.air1719.restservice.RestServiceHandler;

/**
 * Created by abenkovic on 11/26/17.
 */

public class RestDataAdapter implements IDataAdapter, RestServiceHandler {
    private RestServiceCaller restServiceCaller;
    private String username;

    public RestDataAdapter(Context context) {
        restServiceCaller = new RestServiceCaller(this);
        SharedPreferences settings = context.getSharedPreferences("Registration", 0);
        this.username = settings.getString("UserName", "0");
    }

    @Override
    public void getLocation() {

    }

    @Override
    public void saveLocation(Location location) {
        this.restServiceCaller.saveLocation(location, this.username);
    }

    @Override
    public void onDataArrived(Object result, boolean ok) {

    }
}
