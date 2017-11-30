package hr.foi.air1719.core.facade;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air1719.database.DatabaseSingleton;
import hr.foi.air1719.database.TrackerDatabase;
import hr.foi.air1719.database.entities.Location;

/**
 * Created by abenkovic on 11/29/17.
 */

public class LocalDatabase implements Database {
    private Context context;
    private TrackerDatabase db;
    private SharedPreferences settings;
    private String user;

    public LocalDatabase(Context context) {
        this.context = context;
        this.db = DatabaseSingleton.getInstance().init(context);
        this.settings = context.getSharedPreferences("user", 0);
        this.user = settings.getString("username", "0");

    }

    @Override
    public void saveLocation(Location location) {
        this.db.locationDao().save(location);
    }

    @Override
    public List<Location> getLocation() {
        return this.db.locationDao().findByUser(user);


    }
}
