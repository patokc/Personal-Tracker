package hr.foi.air1719.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air1719.database.entities.Location;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM location WHERE username =:username AND timestamp BETWEEN :start AND :end")
    List<Location> findByIdRange(String username, int start, int end);

    @Query("SELECT * FROM location WHERE username =:username")
    List<Location> findByUser(String username);

    @Query("SELECT * FROM location WHERE activityId =:activityId")
    List<Location> findByActivity(int activityId);

    @Query("SELECT * FROM location WHERE timestamp BETWEEN :start AND :end")
    List<Location> findByRange(int start, int end);

    @Insert
    void save(Location location);

    @Update
    void update(Location location);

    @Delete
    void delete(Location location);
}
