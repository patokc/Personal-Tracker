package hr.foi.air1719.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import hr.foi.air1719.database.entities.GpsLocation;

@Dao
public interface GpsLocationDao {

    @Query("SELECT * FROM gpslocation WHERE username =:username")
    List<GpsLocation> findByUser(String username);

    @Query("SELECT * FROM gpslocation WHERE activityId =:activityId")
    List<GpsLocation> getGpsLocations(String activityId);

    @Query("SELECT * FROM gpslocation WHERE timestamp BETWEEN :start AND :end")
    List<GpsLocation> findByRange(int start, int end);

    @Insert
    void save(GpsLocation location);

    @Update
    void update(GpsLocation location);

    @Delete
    void delete(GpsLocation location);
}
