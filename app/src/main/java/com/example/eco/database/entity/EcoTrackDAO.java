package com.example.eco.database.entity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.eco.database.EcoTrackDatabase;


import java.util.ArrayList;
import java.util.List;


@Dao
public interface EcoTrackDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EcoTrackLog ecoTrackLog);
    @Query("SELECT * FROM " + EcoTrackDatabase.ecoTrackLogTable + " ORDER BY date DESC")
    List<EcoTrackLog> getAllRecords();

    @Query("SELECT * FROM " + EcoTrackDatabase.ecoTrackLogTable + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    List<EcoTrackLog> getRecordsByUserId(int loggedInUserId);

    @Query("SELECT * FROM " + EcoTrackDatabase.ecoTrackLogTable + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    LiveData<List<EcoTrackLog>> getRecordsByUserIdLiveData(int loggedInUserId);
    @Delete
    void delete(EcoTrackLog ecoTrackLog);

}
