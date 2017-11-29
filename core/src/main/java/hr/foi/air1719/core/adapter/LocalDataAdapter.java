package hr.foi.air1719.core.adapter;

import android.content.Context;
import android.content.SharedPreferences;

import hr.foi.air1719.database.DatabaseSingleton;
import hr.foi.air1719.database.TrackerDatabase;
import hr.foi.air1719.database.entities.Location;

/**
 * Created by abenkovic on 11/26/17.
 */

public class LocalDataAdapter implements IDataAdapter {
    private TrackerDatabase db;
    private String username;

    public LocalDataAdapter(Context context) {
        this.db = DatabaseSingleton.getInstance().init(context);
        SharedPreferences settings = context.getSharedPreferences("Registration", 0);
        this.username = settings.getString("UserName", "0");
    }

    @Override
    public void getLocation() {
        this.db.locationDao().findByUser(username);
    }

    @Override
    public void saveLocation(Location location) {
        this.db.locationDao().save(location);
    }
}
