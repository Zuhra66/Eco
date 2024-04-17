package com.example.eco.viewHolders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eco.database.EcoTrackRepository;
import com.example.eco.database.entity.EcoTrackLog;

import java.util.List;

public class EcoTrackLogViewModel extends AndroidViewModel {
private EcoTrackRepository repository;
private final LiveData<List<EcoTrackLog>> allLogsById;
public EcoTrackLogViewModel(Application application){
    super((application));
    repository = EcoTrackRepository.getRepository(application);
    //allLogsById = repository.getAllLogsByUserIdLiveData(userId);
}

    public LiveData<List<EcoTrackLog>> getAllLogsById(int userId) {
        return repository.getAllLogsByUserIdLiveData(userId);
    }
    public void insert(EcoTrackLog log){
    repository.insertEcoTrackLog(log);
    }
}
