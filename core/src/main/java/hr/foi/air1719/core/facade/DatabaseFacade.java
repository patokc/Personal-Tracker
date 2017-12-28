package hr.foi.air1719.core.facade;

import android.content.Context;
import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private boolean syncActivities = false;
    private boolean syncLocations = false;

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
    }

    @Override
    public void onDataArrived(Object result, boolean ok) {
        if(ok){
            if(result instanceof Map){
                Map<String, Object> combinedData = new HashMap();
                Map<String, Object> remoteData = (Map) result;
                Map.Entry<String,Object> entry = remoteData.entrySet().iterator().next();

                if(entry.getValue() instanceof Activity){
                    if(this.activities !=null){
                        combinedData.putAll(this.activities);
                        combinedData.putAll(remoteData);

                        if(syncActivities){
                            Runnable syncLocalActivities = new SyncDatabase(this.mapDifference(remoteData, this.activities).values(), "activity", this.local);
                            new Thread(syncLocalActivities).start();

                            Runnable syncRemoteActivities = new SyncDatabase(this.mapDifference(this.activities, remoteData).values(), "activity", this.remote);
                            new Thread(syncRemoteActivities).start();
                            this.syncActivities = false;
                        }
                    }

                } else if (entry.getValue() instanceof GpsLocation){
                    if(this.gpsLocations !=null){
                        combinedData.putAll(this.gpsLocations);
                        combinedData.putAll(remoteData);

                        if(syncLocations){

                            Runnable syncLocalLocations = new SyncDatabase(this.mapDifference(remoteData, this.gpsLocations).values(), "gpsLocation", this.local);
                            new Thread(syncLocalLocations).start();

                            Runnable syncRemoteLocations = new SyncDatabase(this.mapDifference(this.gpsLocations, remoteData).values(), "gpsLocation", this.remote);
                            new Thread(syncRemoteLocations).start();
                            this.syncLocations = false;
                        }
                    }

                }
                if(!this.syncLocations && syncActivities &&!this.isLocalOnly){
                    this.handler.onDataArrived(combinedData, ok);
                }


            } else if(result instanceof Activity || result instanceof GpsLocation){
                this.handler.onDataArrived(result, ok);
            }

        }


    }

    private <K, V> Map<K, V> mapDifference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
        Map<K, V> difference = new HashMap<>();
        difference.putAll(left);
        difference.putAll(right);
        difference.entrySet().removeAll(right.entrySet());
        return difference;
    }

    @Override
    public void saveActivity(Activity activity){
        local.saveActivity(activity);
        remote.saveActivity(activity);
    }

    @Override
    public Activity getActivity(ActivityMode mode, String activityId){
        if(!this.isLocalOnly){
            this.remote.getActivity(mode, activityId);
        }

        Activity data = this.local.getActivity(mode, activityId);
        this.handler.onDataArrived(data, data !=null);
        return data;
    }

    @Override
    public Map<String, Activity> getAllActivities(){
        this.activities = local.getAllActivities();

        if(!this.isLocalOnly){
            remote.getAllActivities();
        }
        this.handler.onDataArrived(this.activities, !this.activities.isEmpty());
        return this.activities;
    }

    @Override
    public List<Activity> getActivityByDate(ActivityMode mode, String activityId, Timestamp date) {
        List<Activity> data = this.local.getActivityByDate(mode, activityId, date);
        this.handler.onDataArrived(data, !data.isEmpty());
        return data;
    }

    @Override
    public List<Activity> getActivityByDateRangeAndMode(ActivityMode mode, Timestamp start, Timestamp end) {
        List<Activity> data = this.local.getActivityByDateRangeAndMode(mode, start, end);
        this.handler.onDataArrived(data, !data.isEmpty());
        return data;
    }

    @Override
    public List<Activity> getActivityByMode(ActivityMode mode) {
        List<Activity> data = this.local.getActivityByMode(mode);
        this.handler.onDataArrived(data, !data.isEmpty());
        return data;

    }

    @Override
    public void saveLocation(GpsLocation location){
        local.saveLocation(location);
        remote.saveLocation(location);
    }

    @Override
    public Map<String, GpsLocation> getLocations(String activityId){
        this.gpsLocations = local.getLocations(activityId);
        if(!this.isLocalOnly){
            remote.getLocations(activityId);
        }
        this.handler.onDataArrived(this.gpsLocations, !this.gpsLocations.isEmpty());
        return this.gpsLocations;
    }

    @Override
    public Map<String, GpsLocation> getAllLocations() {
        if(!this.isLocalOnly){
            this.remote.getAllLocations();
        }

        Map<String, GpsLocation> data = this.local.getAllLocations();
        this.handler.onDataArrived(data, !data.isEmpty());
        return data;
    }

    public void syncData(){
        this.syncActivities = true;
        this.syncLocations = true;
        this.activities = this.local.getAllActivities();
        this.gpsLocations = this.local.getAllLocations();

        this.remote.getAllActivities();
        this.remote.getAllLocations();
    }



    public void setLocalOnly(boolean localOnly) {
        if(this.handler != null){
            isLocalOnly = localOnly;
        }
    }

    @Override
    public String uploadImage(Bitmap image) {
        return this.remote.uploadImage(image);
    }
}
