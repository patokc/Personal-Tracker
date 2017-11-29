package hr.foi.air1719.core.facade;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air1719.database.entities.Location;

/**
 * Created by abenkovic on 11/29/17.
 */

public class DatabaseFacade {
    private final List<Database> databases;

    public DatabaseFacade(Context context) {
        databases = new ArrayList<>();
        databases.add(new LocalDatabase(context));
        databases.add(new RemoteDatabase(context));
    }

    public void saveLocation(Location location){
        for(Database db: databases){
            db.saveLocation(location);
        }

    }

    public void getLocation(){

    }
}
