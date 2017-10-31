package hr.foi.air1719.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import hr.foi.air1719.database.entities.User;

@Dao
public interface UserDao {


    @Query("SELECT * FROM user WHERE username LIKE :name LIMIT 1")
    User findByName(String name);

    @Query("SELECT * FROM user WHERE userId LIKE :id LIMIT 1")
    User findById(int id);

    @Query("SELECT password FROM user WHERE username LIKE :name LIMIT 1")
    String getPassword(String name);

    @Insert
    void create(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
