package hr.foi.air1719.database;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;

import hr.foi.air1719.database.dao.LocationDao;
import hr.foi.air1719.database.dao.UserDao;
import hr.foi.air1719.database.entities.Location;
import hr.foi.air1719.database.entities.User;


/**
 * Created by abenkovic on 10/28/17.
 */
@Database(entities = {User.class, Location.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class TrackerDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract LocationDao locationDao();
}
