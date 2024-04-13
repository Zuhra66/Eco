package com.example.eco.database.entity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.eco.database.EcoTrackDatabase;


import java.util.List;


@Dao
public interface EcoTrackDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EcoTrackLog ecoTrackLog);
    @Query("SELECT * FROM " + EcoTrackDatabase.ecoTrackLogTable + " ORDER BY date DESC")
    List<EcoTrackLog> getAllRecords();


}
