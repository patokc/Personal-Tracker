package hr.foi.air1719.core.facade;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import hr.foi.air1719.database.entities.Location;
import hr.foi.air1719.restservice.RestServiceCaller;
import hr.foi.air1719.restservice.RestServiceHandler;

/**
 * Created by abenkovic on 11/29/17.
 */

public class RemoteDatabase implements Database, RestServiceHandler {
    private Context context;
    private RestServiceCaller restServiceCaller;
    private SharedPreferences settings;
    private String user;

    public RemoteDatabase(Context context) {
        this.context = context;
        this.restServiceCaller = new RestServiceCaller(this);
        this.settings = context.getSharedPreferences("user", 0);
        this.user = settings.getString("username", "0");
    }

    @Override
    public void saveLocation(Location location) {
        this.restServiceCaller.saveLocation(location, this.user);
    }

    @Override
    public List<Location> getLocation() {
        return null;
    }

    @Override
    public void onDataArrived(Object result, boolean ok) {

    }
}
