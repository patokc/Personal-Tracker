package hr.foi.air1719.core.facade;

import java.util.List;
import java.util.Map;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.Location;

/**
 * Created by abenkovic on 11/29/17.
 */

public abstract class Database {

    public abstract void saveActivity(Activity activity);

    public abstract Activity getActivity(ActivityMode mode, String activityId);

    public abstract Map<String, Activity> getAllActivities(ActivityMode mode);

    public abstract void saveLocation(Location location);

    public abstract List<Location> getLocation();




}
