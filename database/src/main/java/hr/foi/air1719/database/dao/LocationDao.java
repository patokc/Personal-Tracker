package hr.foi.air1719.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import hr.foi.air1719.database.entities.GpsLocation;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM gpsLocation WHERE username =:username AND timestamp BETWEEN :start AND :end")
    GpsLocation findByIdRange(String username, int start, int end);

    @Query("SELECT * FROM gpsLocation WHERE username =:username")
    GpsLocation findByUser(String username);

    @Query("SELECT * FROM gpsLocation WHERE activityId =:activityId")
    GpsLocation findByActivity(int activityId);

    @Query("SELECT * FROM gpsLocation WHERE timestamp BETWEEN :start AND :end")
    GpsLocation findByRange(int start, int end);

    @Insert
    void save(GpsLocation location);

    @Update
    void update(GpsLocation location);

    @Delete
    void delete(GpsLocation location);
}
