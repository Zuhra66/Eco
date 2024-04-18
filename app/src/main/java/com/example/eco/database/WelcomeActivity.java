package com.example.eco.database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eco.MainActivity;

import com.example.eco.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find the "Calculate Carbon Footprint" button using binding
        binding.calculateCarbonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(),2));

            }
        });
    }


    // Method to create an intent for starting WelcomeActivity
    public static Intent welcomeIntentFactory(Context context) {
        return new Intent(context, WelcomeActivity.class);
    }

}