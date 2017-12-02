package hr.foi.air1719.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.sql.Timestamp;
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

    @Query("SELECT * FROM activity")
    List<Activity> findAll();

    @Query("SELECT * FROM activity WHERE mode = :mode")
    List<Activity> findByMode(ActivityMode mode);

    @Query("SELECT * FROM activity WHERE start >= :date")
    List<Activity> findByDate(Timestamp date);

    @Query("SELECT * FROM activity WHERE start >= :date AND mode = :mode")
    List<Activity> findByDateAndMode(Timestamp date, ActivityMode mode);

    @Query("SELECT * FROM activity WHERE start BETWEEN :start AND :end")
    List<Activity> findByDateRange(Timestamp start, Timestamp end);

    @Query("SELECT * FROM activity WHERE mode=:mode AND start BETWEEN :start AND :end")
    List<Activity> findByDateRangeAndMode(Timestamp start, Timestamp end, ActivityMode mode);

    @Insert
    void create(Activity activity);

    @Update
    void update(Activity activity);

    @Delete
    void delete(Activity activity);
}
