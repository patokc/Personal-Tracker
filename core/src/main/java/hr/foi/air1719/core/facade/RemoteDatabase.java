package hr.foi.air1719.core.facade;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
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
        this.restServiceCaller.getActivity(this.user, activityId);
        return null;
    }

    @Override
    public List<Activity> getActivityByDate(ActivityMode mode, String activityId, Timestamp date) {
        return null;
    }

    @Override
    public List<Activity> getActivityByDateRangeAndMode(ActivityMode mode, Timestamp start, Timestamp end) {
        return null;
    }

    @Override
    public List<Activity> getActivityByMode(ActivityMode mode) {
        return null;
    }

    @Override
    public Map<String, Activity> getAllActivities() {
        this.restServiceCaller.getAllActivities(this.user);
        return null;
    }

    @Override
    public void saveLocation(GpsLocation location) {
        this.restServiceCaller.saveLocation(location, this.user, location.getActivityId());
    }

    @Override
    public Map<String, GpsLocation> getLocations(String activityId) {
        this.restServiceCaller.getLocations(this.user, activityId);
        return null;
    }


}
