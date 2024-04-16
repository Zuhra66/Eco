package com.example.eco.database;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.eco.MainActivity;
import com.example.eco.database.entity.EcoTrackDAO;
import com.example.eco.database.entity.EcoTrackLog;
import com.example.eco.database.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class EcoTrackRepository {
    private final EcoTrackDAO ecoTrackDAO;
    private final UserDAO userDAO;
    private static EcoTrackRepository repository;

    private EcoTrackRepository(Application application) {
        EcoTrackDatabase db = EcoTrackDatabase.getDatabase(application);
        this.ecoTrackDAO = db.ecoTrackDAO();
        this.userDAO = db.userDAO();
    }

    public static EcoTrackRepository getRepository(Application application){
        if(repository != null){
            return repository;
        }
        Future<EcoTrackRepository> future = EcoTrackDatabase.databaseWriteExecuter.submit(
                new Callable<EcoTrackRepository>() {
                    @Override
                    public EcoTrackRepository call() throws Exception {
                        return new EcoTrackRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.TAG,"Problem getting EcoTrackRepository thread error.");
        }
        return null;
    }

    public ArrayList<EcoTrackLog> getAllLogs() {
        Future<ArrayList<EcoTrackLog>> future = EcoTrackDatabase.databaseWriteExecuter.submit(
                new Callable<ArrayList<EcoTrackLog>>() {
                    @Override
                    public ArrayList<EcoTrackLog> call() throws Exception {
                        return (ArrayList<EcoTrackLog>) ecoTrackDAO.getAllRecords();
                    }
                });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG,"Problem when getting all EcoTrackLog in the Repository");
        }
        return null;
    }

    public void insertEcoTrackLog(EcoTrackLog ecoTrackLog) {
        EcoTrackDatabase.databaseWriteExecuter.execute(() -> {
            ecoTrackDAO.insert(ecoTrackLog);
        });
    }

    public void insertUser(User... users) {
        EcoTrackDatabase.databaseWriteExecuter.execute(() -> {
            try {
                for (User currentUser : users) {
                    userDAO.insert(currentUser);
                    Log.d(MainActivity.TAG, "User inserted successfully: " + currentUser.getUsername());
                }
            } catch (Exception e) {
                Log.e(MainActivity.TAG, "Error inserting user: " + e.getMessage());
            }
        });
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public LiveData<List<EcoTrackLog>> getAllLogsByUserIdLiveData(int loggedInUserId) {
        return ecoTrackDAO.getRecordsByUserIdLiveData(loggedInUserId);
    }

    @Deprecated
    public ArrayList<EcoTrackLog> getAllLogsByUserId(int loggedInUserId) {
        Future<ArrayList<EcoTrackLog>> future = EcoTrackDatabase.databaseWriteExecuter.submit(
                new Callable<ArrayList<EcoTrackLog>>() {
                    @Override
                    public ArrayList<EcoTrackLog> call() throws Exception {
                        return (ArrayList<EcoTrackLog>) ecoTrackDAO.getRecordsByUserId(loggedInUserId);
                    }

                });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG,"Problem when getting all EcoTrackLog in the Repository");
        }
        return null;
    }

    public boolean doesUserExist(String username) {
        LiveData<User> userLiveData = getUserByUserName(username);
        return userLiveData.getValue() != null;
    }
}

