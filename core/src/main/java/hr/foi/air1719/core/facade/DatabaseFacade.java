package hr.foi.air1719.core.facade;

import android.content.Context;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;

/**
 * Created by abenkovic on 11/29/17.
 */

public class DatabaseFacade extends Database implements DataHandler {
    private Database local = null;
    private Database remote = null;

    private DataHandler handler = null;
    private boolean isLocalOnly = true;
    private boolean isSyncOn = false;

    private Map<String, Activity> activities = null;
    private Map<String, GpsLocation> gpsLocations = null;


    public DatabaseFacade(Context context) {
        this.local = new LocalDatabase(context);
        this.remote = new RemoteDatabase(context, this);
        this.handler = this;
    }

    public DatabaseFacade(Context context, DataHandler handler) {
        this.local = new LocalDatabase(context);
        this.remote = new RemoteDatabase(context, handler);
        this.handler = handler;
        isLocalOnly = false;
    }

    @Override
    public void onDataArrived(Object result, boolean ok) {

        if(result instanceof Map){
            Map<String, Object> combinedData = new HashMap();
            Map<String, Object> remoteData = (Map) result;
            Map.Entry<String,Object> entry = remoteData.entrySet().iterator().next();

            if(entry.getValue() instanceof Activity){
                if(this.activities !=null){
                    combinedData.putAll(this.activities);
                    combinedData.putAll(remoteData);



                    if(isSyncOn){
                        // iz zajednickih podataka uklanjam one koji postoje i u lokalnoj bazi
                        combinedData.entrySet().removeAll(this.activities.entrySet());
                        Runnable syncDb = new SyncDatabase(combinedData.values(), "activity", this.local);
                        new Thread(syncDb).start();
                        this.isSyncOn = false;
                    }
                }

            } else if (entry.getValue() instanceof GpsLocation){
                if(this.gpsLocations !=null){
                    combinedData.putAll(this.gpsLocations);
                    combinedData.putAll(remoteData);

                    if(isSyncOn){
                        combinedData.entrySet().removeAll(this.gpsLocations.entrySet());
                        Runnable syncDb = new SyncDatabase(combinedData.values(), "gpsLocation", this.local);
                        new Thread(syncDb).start();
                        this.isSyncOn = false;
                    }
                }

            }
            if(!this.isSyncOn && !this.isLocalOnly){
                this.handler.onDataArrived(combinedData, ok);
            }


        } else if(result instanceof Activity || result instanceof GpsLocation){
            this.handler.onDataArrived(result, ok);
        }
    }

    @Override
    public void saveActivity(Activity activity){
        local.saveActivity(activity);
        if(!this.isLocalOnly){
            remote.saveActivity(activity);
        }

    }

    @Override
    public Activity getActivity(ActivityMode mode, String activityId){
        this.remote.getActivity(mode, activityId);
        return this.local.getActivity(mode, activityId);
    }

    @Override
    public Map<String, Activity> getAllActivities(){
        this.activities = local.getAllActivities();

        if(!this.isLocalOnly){
            remote.getAllActivities();
        }

        return this.activities;
    }

    @Override
    public List<Activity> getActivityByDate(ActivityMode mode, String activityId, Timestamp date) {
        return this.local.getActivityByDate(mode, activityId, date);
    }

    @Override
    public List<Activity> getActivityByDateRangeAndMode(ActivityMode mode, Timestamp start, Timestamp end) {
        return this.local.getActivityByDateRangeAndMode(mode, start, end);
    }

    @Override
    public List<Activity> getActivityByMode(ActivityMode mode) {
        return this.local.getActivityByMode(mode);
    }

    @Override
    public void saveLocation(GpsLocation location){
        local.saveLocation(location);
        if(!this.isLocalOnly){
            remote.saveLocation(location);
        }

    }

    @Override
    public Map<String, GpsLocation> getLocations(String activityId){
        this.gpsLocations = local.getLocations(activityId);
        if(!this.isLocalOnly){
            remote.getLocations(activityId);
        }

        return this.gpsLocations;
    }

    public void syncData(){
        this.isSyncOn = true;
        this.activities = this.local.getAllActivities();
        this.remote.getAllActivities();
    }



    public void setLocalOnly(boolean localOnly) {
        if(this.handler != null){
            isLocalOnly = localOnly;
        }
    }


}
