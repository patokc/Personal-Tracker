package hr.foi.air1719.core.facade;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;

/**
 * Created by abenkovic on 11/29/17.
 */

public class DatabaseFacade implements DataHandler {
    private Database local = null;
    private Database remote = null;

    private DataHandler handler = null;
    private boolean isLocalOnly = false;

    private Map<String, Activity> activities = null;
    private Map<String, GpsLocation> gpsLocations = null;


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
                if(this.activities !=null){
                    combinedData.putAll(this.activities);
                    combinedData.putAll(remoteData);
                }

            } else if (entry.getValue() instanceof GpsLocation){
                if(this.gpsLocations !=null){
                    combinedData.putAll(this.gpsLocations);
                    combinedData.putAll(remoteData);
                }

            }
            this.handler.onDataArrived(combinedData, ok);

        } else if(result instanceof Activity || result instanceof GpsLocation){
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

    public void saveLocation(GpsLocation location){
        local.saveLocation(location);
        if(!this.isLocalOnly){
            remote.saveLocation(location);
        }

    }

    public Map<String, GpsLocation> getLocations(String activityId){
        this.gpsLocations = local.getLocations(activityId);
        if(!this.isLocalOnly){
            remote.getLocations(activityId);
        }

        return this.gpsLocations;
    }



    public void setLocalOnly(boolean localOnly) {
        // ako ne postoji netko tko ce handlati async task, nemoj dozvoliti upite na remote bazu
        if(this.handler != null){
            isLocalOnly = localOnly;
        }
    }


}
