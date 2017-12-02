package hr.foi.air1719.core.facade;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.Location;

/**
 * Created by abenkovic on 11/29/17.
 */

public class DatabaseFacade implements DataHandler {
    private Database local = null;
    private Database remote = null;

    private DataHandler handler = null;
    private boolean isLocalOnly = false;

    private Map<String, Activity> activities;


    public DatabaseFacade(Context context) {
        this.local = new LocalDatabase(context);
        this.isLocalOnly = true;
    }

    public DatabaseFacade(Context context, DataHandler handler) {
        this.local = new LocalDatabase(context);
        this.remote = new RemoteDatabase(context, this);
        this.handler = handler;
    }

    @Override
    public void onDataArrived(Object result, boolean ok) {

        if(result instanceof Map){
            Map combinedData = new HashMap();
            Map<String, Object> remoteData = (Map) result;
            Map.Entry<String,Object> entry = remoteData.entrySet().iterator().next();

            if(entry.getValue() instanceof Activity){
                combinedData.putAll(this.activities);
                combinedData.putAll(remoteData);
            } else if (entry.getValue() instanceof Location){
                System.out.println("DOBIO LOKACIJE");
            }

            this.handler.onDataArrived(combinedData, ok);
        } else if(result instanceof Activity || result instanceof Location){
            this.handler.onDataArrived(result, ok);
        }
    }

    public void saveActivity(Activity activity){
        local.saveActivity(activity);
        if(!this.isLocalOnly){
            remote.saveActivity(activity);
        }

    }

    public Activity getActivity(ActivityMode mode, String activityId){
        this.remote.getActivity(mode, activityId);
        return this.local.getActivity(mode, activityId);
    }

    public Map<String, Activity> getAllActivities(ActivityMode mode){
        this.activities = local.getAllActivities(mode);

        if(!this.isLocalOnly){
            remote.getAllActivities(mode);
        }

        return this.activities;

    }

    public void saveLocation(Location location){
        local.saveLocation(location);
        remote.saveLocation(location);
    }

    public List<Location> getLocation(){
        List<Location> localData = local.getLocation();
        List<Location> remoteData = remote.getLocation();

        return localData;
    }



    public void setLocalOnly(boolean localOnly) {
        isLocalOnly = localOnly;
    }


}
