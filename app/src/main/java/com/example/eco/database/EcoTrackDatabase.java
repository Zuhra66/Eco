package com.example.eco.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.eco.MainActivity;
import com.example.eco.database.entity.EcoTrackDAO;
import com.example.eco.database.entity.EcoTrackLog;
import com.example.eco.database.entity.User;
import com.example.eco.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {EcoTrackLog.class, User.class}, version = 2, exportSchema = false)
public abstract class EcoTrackDatabase extends RoomDatabase {
    public static final String USER_TABLE = "user_table";
    private static final String DATABASE_NAME = "Ecotrack_database";
    public static final String ecoTrackLogTable ="ecoTrackLogTable";
    private static volatile EcoTrackDatabase INSTANCE;

    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecuter = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

     static EcoTrackDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EcoTrackDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EcoTrackDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "Database created! ");

        }
    };

    public abstract EcoTrackDAO ecoTrackDAO() ;
}

