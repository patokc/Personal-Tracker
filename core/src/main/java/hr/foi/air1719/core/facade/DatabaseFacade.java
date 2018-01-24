package hr.foi.air1719.core.facade;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.sql.Timestamp;
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
    private Database databaseModule = null;
    private DataHandler handler = null;

    private boolean isLocalOnly = true;
    private boolean syncActivities = false;
    private boolean syncLocations = false;
    private boolean isSyncChecked = false;

    private Map<String, Activity> activities = null;
    private Map<String, GpsLocation> gpsLocations = null;

    Context context = null;


    public DatabaseFacade(Context context) {
        SharedPreferences settings = context.getSharedPreferences("user", 0);
        SharedPreferences.Editor editor = settings.edit();
        this.isLocalOnly = settings.getBoolean("localOnlyData", false);

        if(isLocalOnly){
            this.databaseModule = new LocalDatabase(context);
        }else {
            this.databaseModule = new RemoteDatabase(context, this);
        }

        this.handler = this;
        this.context = context;
    }

    public DatabaseFacade(Context context, DataHandler handler) {
        SharedPreferences settings = context.getSharedPreferences("user", 0);
        SharedPreferences.Editor editor = settings.edit();
        this.isLocalOnly = settings.getBoolean("localOnlyData", false);

        if(isLocalOnly){
            this.databaseModule = new LocalDatabase(context);
        }else {
            this.databaseModule = new RemoteDatabase(context, handler);

        }
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void onDataArrived(Object result, boolean ok) {
        if(ok){
            if(result instanceof Map){
                Map<String, Object> combinedData = new HashMap();
                Map<String, Object> remoteData = (Map) result;
                try{
                    Map.Entry<String,Object> entry = remoteData.entrySet().iterator().next();
                    if(entry.getValue() instanceof Activity){
                        if(this.activities !=null){
                            combinedData.putAll(this.activities);
                            combinedData.putAll(remoteData);

                            if(syncActivities){
                                Runnable syncLocalActivities = new SyncDatabase(this.mapDifference(remoteData, this.activities).values(), "activity", this.databaseModule);
                                new Thread(syncLocalActivities).start();

                                Runnable syncRemoteActivities = new SyncDatabase(this.mapDifference(this.activities, remoteData).values(), "activity", this.databaseModule);
                                new Thread(syncRemoteActivities).start();
                                this.syncActivities = false;
                            }
                        }

                    } else if (entry.getValue() instanceof GpsLocation){
                        if(this.gpsLocations !=null){
                            combinedData.putAll(this.gpsLocations);
                            combinedData.putAll(remoteData);

                            if(syncLocations){

                                Runnable syncLocalLocations = new SyncDatabase(this.mapDifference(remoteData, this.gpsLocations).values(), "gpsLocation", this.databaseModule);
                                new Thread(syncLocalLocations).start();

                                Runnable syncRemoteLocations = new SyncDatabase(this.mapDifference(this.gpsLocations, remoteData).values(), "gpsLocation", this.databaseModule);
                                new Thread(syncRemoteLocations).start();
                                this.syncLocations = false;
                            }
                        }

                    }

                } catch(Exception e){
                    System.err.print("Greska kod dohvata elementa");
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
        databaseModule.saveActivity(activity);
    }

    @Override
    public Activity getActivity(ActivityMode mode, String activityId){
        syncCheck();

        this.databaseModule.getActivity(mode, activityId);
        Activity data = this.databaseModule.getActivity(mode, activityId);
        this.handler.onDataArrived(data, data !=null);
        return data;
    }

    @Override
    public Map<String, Activity> getAllActivities(){
        syncCheck();
        this.activities = databaseModule.getAllActivities();

        this.handler.onDataArrived(this.activities, true);
        return this.activities;
    }

    @Override
    public List<Activity> getActivityByDate(ActivityMode mode, String activityId, Timestamp date) {
        syncCheck();
        List<Activity> data = this.databaseModule.getActivityByDate(mode, activityId, date);
        this.handler.onDataArrived(data, !data.isEmpty());
        return data;
    }

    @Override
    public List<Activity> getActivityByDateRangeAndMode(ActivityMode mode, Timestamp start, Timestamp end) {
        syncCheck();
        List<Activity> data = this.databaseModule.getActivityByDateRangeAndMode(mode, start, end);
        this.handler.onDataArrived(data, !data.isEmpty());
        return data;
    }

    @Override
    public List<Activity> getActivityByMode(ActivityMode mode) {
        syncCheck();
        List<Activity> data = this.databaseModule.getActivityByMode(mode);
        this.handler.onDataArrived(data, !data.isEmpty());
        return data;

    }

    @Override
    public List<Activity> getActivityByModeOrderByStartDESC(ActivityMode mode) {
        syncCheck();
        List<Activity> data = this.databaseModule.getActivityByModeOrderByStartDESC(mode);
        if(data != null){
            this.handler.onDataArrived(data, !data.isEmpty());
        }

        return data;

    }

    @Override
    public void saveLocation(GpsLocation location){
        databaseModule.saveLocation(location);
    }

    @Override
    public void deleteByActivity(Activity activity) {
        databaseModule.deleteByActivity(activity);
    }

    @Override
    public Map<String, GpsLocation> getLocations(String activityId){
        syncCheck();
        this.gpsLocations = databaseModule.getLocations(activityId);

        this.handler.onDataArrived(this.gpsLocations, true);
        return this.gpsLocations;
    }

    @Override
    public Map<String, GpsLocation> getAllLocations() {
        syncCheck();
        Map<String, GpsLocation> data = this.databaseModule.getAllLocations();
        this.handler.onDataArrived(data, !data.isEmpty());
        return data;
    }

    public void syncData(){
        this.syncActivities = true;
        this.syncLocations = true;
        this.activities = this.databaseModule.getAllActivities();
        this.gpsLocations = this.databaseModule.getAllLocations();

        this.databaseModule.getAllActivities();
        this.databaseModule.getAllLocations();
    }



    public void setLocalOnly(boolean localOnly) {
        if(this.handler != null){
            isLocalOnly = localOnly;
        }
    }

    @Override
    public String uploadImage(Bitmap image) {
        return this.databaseModule.uploadImage(image);
    }

    private void syncCheck(){
        if(!isSyncChecked){
            SharedPreferences settings = context.getSharedPreferences("user", 0);
            SharedPreferences.Editor editor = settings.edit();
            this.isLocalOnly = settings.getBoolean("localOnlyData", false);
            if(!settings.getBoolean("isSynced", false)&&!isLocalOnly){
                this.syncData();
                editor.putBoolean("isSynced", true);
                editor.apply();
            }
            isSyncChecked = true;
        }

    }
}