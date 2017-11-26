package hr.foi.air1719.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import hr.foi.air1719.database.entities.Activity;

/**
 * Created by abenkovic on 11/25/17.
 */

@Dao
public interface ActivityDao {
    @Query("SELECT * FROM activity WHERE activityId = :activityId ")
    Activity findById(int activityId);


    @Insert
    void create(Activity activity);

    @Update
    void update(Activity activity);

    @Delete
    void delete(Activity activity);
}
