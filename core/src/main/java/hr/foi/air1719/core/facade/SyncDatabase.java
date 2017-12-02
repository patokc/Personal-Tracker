package hr.foi.air1719.core.facade;

import java.util.Collection;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.GpsLocation;

/**
 * Created by abenkovic on 12/2/17.
 */

public class SyncDatabase implements Runnable {
    private Collection remoteData = null;
    private String type = null;
    private Database db = null;


    public SyncDatabase(Collection remoteData, String type, Database db){
        this.remoteData = remoteData;
        this.type = type;
        this.db = db;
    }

    @Override
    public void run() {
        this.syncLocal();
    }

    public void syncLocal(){
        if(type.equals("activity")){
            for(Activity activity: (Collection<Activity>) remoteData){
                this.db.saveActivity(activity);
            }
        } else{
            for(GpsLocation location: (Collection<GpsLocation>) remoteData){
                this.db.saveLocation(location);
            }

        }
    }
}
