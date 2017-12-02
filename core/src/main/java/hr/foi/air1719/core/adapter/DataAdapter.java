package hr.foi.air1719.core.adapter;

import android.content.Context;

import java.util.ArrayList;

import hr.foi.air1719.database.entities.GpsLocation;

/**
 * Created by abenkovic on 11/26/17.
 */

public class DataAdapter implements IDataAdapter {
    private Context context;

    public ArrayList<GpsLocation> locations;

    protected DataLoadedListener listener;

    private LocalDataAdapter localDataAdapter;
    private RestDataAdapter restDataAdapter;

    public DataAdapter(Context context) {
        this.localDataAdapter = new LocalDataAdapter(context);
        this.restDataAdapter = new RestDataAdapter(context);
    }

    public void getLocation(){
        this.localDataAdapter.getLocation();
        this.restDataAdapter.getLocation();
    }

    @Override
    public void saveLocation(GpsLocation location) {
        this.localDataAdapter.saveLocation(location);
        this.restDataAdapter.saveLocation(location);
    }
}
