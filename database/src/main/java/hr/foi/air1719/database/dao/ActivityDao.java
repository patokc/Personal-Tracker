package hr.foi.air1719.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;

/**
 * Created by abenkovic on 11/25/17.
 */

@Dao
public interface ActivityDao {
    @Query("SELECT * FROM activity WHERE activityId = :activityId ")
    Activity findById(String activityId);

    @Query("SELECT * FROM activity WHERE user = :user")
    Activity findByUser(String user);

    @Query("SELECT * FROM activity WHERE user = :user AND mode = :mode")
    List<Activity> findByUserAndMode(String user, ActivityMode mode);

    @Insert
    void create(Activity activity);

    @Update
    void update(Activity activity);

    @Delete
    void delete(Activity activity);
}
