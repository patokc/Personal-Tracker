package hr.foi.air1719.core.facade;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.Location;

/**
 * Created by abenkovic on 11/29/17.
 */

public class DatabaseFacade {
    private final Database local;
    private final Database remote;


    public DatabaseFacade(Context context) {
        local = new LocalDatabase(context);
        remote = new RemoteDatabase(context);
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

    public void saveActivity(Activity activity){
        local.saveActivity(activity);
        remote.saveActivity(activity);
    }

    public void getAllActivities(ActivityMode mode){
        List<Activity> localData = local.getAllActivities(mode);

        for(Activity act: localData){
            System.out.println(act.getActivityId());
        }

        //remote.getAllActivities(mode);

    }
}
