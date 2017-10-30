package hr.foi.air1719.database;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

/**
 * Created by abenkovic on 10/28/17.
 */

public class DatabaseSingleton {

    public static volatile DatabaseSingleton INSTANCE;
    private TrackerDatabase database;

    private DatabaseSingleton(){

    }

    public static synchronized DatabaseSingleton getInstance(){
        if(INSTANCE == null){
            synchronized(DatabaseSingleton.class){
                if(INSTANCE == null){
                    INSTANCE = new DatabaseSingleton();

                }
            }
        }
        return INSTANCE;
    }

    public TrackerDatabase init(Context context){
        if(database == null){
            return database = Room.databaseBuilder(context.getApplicationContext(), TrackerDatabase.class, "TrackerDatabase")
                    .build();
        } else {
            return database;
        }

    }

    public TrackerDatabase getDB() {
            return database;
    }
}
