package hr.foi.air1719.core.adapter;

import java.util.ArrayList;

import hr.foi.air1719.database.entities.Location;

/**
 * Created by abenkovic on 11/26/17.
 */

public interface DataLoadedListener {
    void onDataLoaded(ArrayList<Location> locations);

    void onDataLoaded(Location location);
}