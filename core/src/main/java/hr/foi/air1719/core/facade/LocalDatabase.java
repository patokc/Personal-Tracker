package hr.foi.air1719.core.facade;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.foi.air1719.database.DatabaseSingleton;
import hr.foi.air1719.database.TrackerDatabase;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;

/**
 * Created by abenkovic on 11/29/17.
 */

public class LocalDatabase extends Database {
    private TrackerDatabase db;
    private SharedPreferences settings;
    private String user;

    public LocalDatabase(Context context) {
        this.db = DatabaseSingleton.getInstance().init(context);
        this.settings = context.getSharedPreferences("user", 0);
        this.user = settings.getString("username", "0");
    }

    @Override
    public void saveActivity(Activity activity) {
        activity.setUser(this.user);
        this.db.activityDao().create(activity);
    }

    @Override
    public Activity getActivity(ActivityMode mode, String activityId) {
        return this.db.activityDao().findById(activityId);
    }

    @Override
    public List<Activity> getActivityByDate(ActivityMode mode, String activityId, Timestamp date) {
        return null;
    }

    @Override
    public List<Activity> getActivityByDateRangeAndMode(ActivityMode mode, Timestamp start, Timestamp end) {
        return this.db.activityDao().findByDateRangeAndMode(start, end, mode);
    }

    @Override
    public List<Activity> getActivityByMode(ActivityMode mode) {
        return this.db.activityDao().findByMode(mode);
    }

    @Override
    public Map<String, Activity> getAllActivities() {
        Map<String, Activity> map = new HashMap<>();
        for(Activity activity: this.db.activityDao().findAll()){
            map.put(activity.getActivityId(), activity);
        }

        return map;
    }

    @Override
    public void saveLocation(GpsLocation location) {
        location.setUsername(this.user);
        this.db.gpsLocationDao().save(location);
    }

    @Override
    public Map<String, GpsLocation> getLocations(String activityId) {
        Map<String, GpsLocation> map = new HashMap<>();
        for(GpsLocation gps: this.db.gpsLocationDao().getGpsLocations(activityId)){
            map.put(gps.getLocationId(), gps);
        }

        return map;
    }

    @Override
    public Map<String, GpsLocation> getAllLocations() {
        Map<String, GpsLocation> map = new HashMap<>();
        for(GpsLocation gps: this.db.gpsLocationDao().findByUser(this.user)){
            map.put(gps.getLocationId(), gps);
        }
        return map;
    }

    @Override
    public String uploadImage(Bitmap image) {
        return "Not Implemented Yet";
    }
}
