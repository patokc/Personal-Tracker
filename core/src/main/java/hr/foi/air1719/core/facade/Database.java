package hr.foi.air1719.core.facade;

import java.util.List;

import hr.foi.air1719.database.entities.Location;

/**
 * Created by abenkovic on 11/29/17.
 */

public interface Database {

    public void saveLocation(Location location);

    public List<Location> getLocation();


}
