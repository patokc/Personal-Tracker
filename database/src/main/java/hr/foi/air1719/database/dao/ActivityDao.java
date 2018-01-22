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
    @Query("SELECT * FROM activity WHERE activityId = :activityId AND user = :user")
    Activity findById(String activityId, String user);

    @Query("SELECT * FROM activity WHERE user = :user")
    List<Activity> findAll(String user);

    @Query("SELECT * FROM activity WHERE mode = :mode AND user = :user")
    List<Activity> findByMode(ActivityMode mode, String user);

    @Query("SELECT * FROM activity WHERE mode = :mode AND user = :user ORDER BY start DESC LIMIT 50")
    List<Activity> findByModeOrderByStartDESC(ActivityMode mode, String user);

    @Query("SELECT * FROM activity WHERE start >= :date AND user = :user")
    List<Activity> findByDate(Timestamp date, String user);

    @Query("SELECT * FROM activity WHERE start >= :date AND mode = :mode AND user = :user")
    List<Activity> findByDateAndMode(Timestamp date, ActivityMode mode, String user);

    @Query("SELECT * FROM activity WHERE start BETWEEN :start AND :end AND user = :user")
    List<Activity> findByDateRange(Timestamp start, Timestamp end, String user);

    @Query("SELECT * FROM activity WHERE mode=:mode AND start BETWEEN :start AND :end AND user = :user")
    List<Activity> findByDateRangeAndMode(Timestamp start, Timestamp end, ActivityMode mode, String user);

    @Insert
    void create(Activity activity);

    @Update
    void update(Activity activity);

    @Delete
    void delete(Activity activity);

    @Delete
    void deleteByActivity(Activity activity);
}
