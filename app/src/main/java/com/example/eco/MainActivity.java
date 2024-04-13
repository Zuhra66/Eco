package com.example.eco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eco.database.EcoTrackRepository;
import com.example.eco.database.entity.EcoTrackLog;
import com.example.eco.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String TAG ="malfunction" ;
    private ActivityMainBinding binding;
    private EcoTrackRepository repository;

    int id ;
    String Transportation = "";
    String Energy = "";
    String Dietary = "";
    int loggedInUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginUser();
        if(loggedInUserId == -1){
            Intent intent = LoginActivity.loginIntentFactor(getApplicationContext());
            startActivity(intent);
        }
        repository = EcoTrackRepository.getRepository(getApplication());
        binding.WelcomeTextView.setMovementMethod(new ScrollingMovementMethod());
        updateDisplay();
        binding.Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                insertEcoTrackLogRecord();
                updateDisplay();
            }
        });
        binding.EnterChoiceTransportationInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDisplay();
            }
        });
    }

    private void loginUser() {

    }

    private void insertEcoTrackLogRecord(){
        if(Transportation.isEmpty()){
            return;
        }
        EcoTrackLog log = new EcoTrackLog(Transportation,Energy,Dietary,loggedInUserId);
        repository.insertEcoTrackLog(log);

    }
    private void updateDisplay(){
        ArrayList<EcoTrackLog> allLogs = repository.getAllLogs();
        if(allLogs.isEmpty()){
            binding.Calculate.setText(R.string.nothing_to_show);
        }
        StringBuilder sb = new StringBuilder();
        for(EcoTrackLog log: allLogs){
            sb.append(log);
        }

        binding.WelcomeTextView.setText(sb.toString());


    }
    private void getInformationFromDisplay(){
        Transportation = binding.EnterChoiceTransportationInputEditText.getText().toString();
        Energy = binding.EnterEnergyChoiceInputEditText.getText().toString();
        Dietary = binding.EnterChoiceDietaryInputEditText.getText().toString();

    }
}