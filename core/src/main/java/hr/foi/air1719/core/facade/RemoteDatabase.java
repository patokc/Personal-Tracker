package hr.foi.air1719.core.facade;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.Location;
import hr.foi.air1719.restservice.RestServiceCaller;
import hr.foi.air1719.restservice.RestServiceHandler;

/**
 * Created by abenkovic on 11/29/17.
 */

public class RemoteDatabase  extends Database implements RestServiceHandler {
    private RestServiceCaller restServiceCaller;
    private DataHandler dataHandler;
    private SharedPreferences settings;
    private String user;

    public RemoteDatabase(Context context, DataHandler handler) {
        this.restServiceCaller = new RestServiceCaller(this);
        this.settings = context.getSharedPreferences("user", 0);
        this.user = settings.getString("username", "0");
        this.dataHandler = handler;
    }

    @Override
    public void onDataArrived(Object result, boolean ok) {
        this.dataHandler.onDataArrived(result, ok);
    }

    @Override
    public void saveActivity(Activity activity) {
        activity.setUser(this.user);
        this.restServiceCaller.saveActivity(activity);
    }

    @Override
    public Activity getActivity(ActivityMode mode, String activityId) {
        this.restServiceCaller.getActivity(this.user, mode, activityId);
        return null;
    }

    @Override
    public Map<String, Activity> getAllActivities(ActivityMode mode) {
        this.restServiceCaller.getAllActivities(this.user, mode);
        return null;
    }

    @Override
    public void saveLocation(Location location) {
        this.restServiceCaller.saveLocation(location, this.user);
    }

    @Override
    public List<Location> getLocation() {
        return null;
    }


}
