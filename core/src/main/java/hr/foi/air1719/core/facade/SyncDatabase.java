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
        this.syncData();
    }

    private void syncData(){
        try{
            if(type.equals("activity")){
                for(Activity activity: (Collection<Activity>) remoteData){
                    try {
                        this.db.saveActivity(activity);
                    } catch (Exception e){
                        System.out.println("Greska prilikom zapisa u bazu: " + e.toString());
                    }
                }
            } else{
                for(GpsLocation location: (Collection<GpsLocation>) remoteData){
                    try {
                        this.db.saveLocation(location);
                    } catch (Exception e){
                        System.out.println("Greska prilikom zapisa u bazu: " + e.toString());
                    }
                }

            }

        }catch(Exception e){
            System.out.println("Greska prilikom sinkronizacije\n" + e.toString());
        }

    }
}
