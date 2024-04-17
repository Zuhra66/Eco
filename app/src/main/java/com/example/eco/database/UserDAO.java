package com.example.eco.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.eco.database.entity.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);
    @Delete
    void delete(User user);
    @Query("SELECT * FROM "+ EcoTrackDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE from " + EcoTrackDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * from "+EcoTrackDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);
    @Query("SELECT * from "+EcoTrackDatabase.USER_TABLE + " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);

    @Query("SELECT * FROM " + EcoTrackDatabase.USER_TABLE + " WHERE id = :userId")
    User getUserByUserIdSync(int userId);

}
