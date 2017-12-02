package hr.foi.air1719.database;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;

import hr.foi.air1719.database.converters.*;
import hr.foi.air1719.database.dao.*;
import hr.foi.air1719.database.entities.*;


@Database(entities = {User.class, GpsLocation.class, Activity.class}, version = 1)
@TypeConverters({ActivityModeConverter.class, DateConverter.class})
public abstract class TrackerDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract GpsLocationDao gpsLocationDao();
    public abstract ActivityDao activityDao();
}
