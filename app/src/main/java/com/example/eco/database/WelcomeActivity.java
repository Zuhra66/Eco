package com.example.eco.database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eco.MainActivity;
import com.example.eco.R;
import com.example.eco.databinding.ActivityMainBinding;
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
                // When the button is clicked, start MainActivity
                startMainActivity();
            }
        });
    }

    // Method to start MainActivity
    private void startMainActivity() {
        // Create an intent to start MainActivity
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        // Start the activity
        startActivity(intent);
        // Finish the WelcomeActivity to prevent it from staying in the back stack
        finish();
    }

    // Method to create an intent for starting WelcomeActivity
    public static Intent welcomeIntentFactory(Context context) {
        return new Intent(context, WelcomeActivity.class);
    }

    public void onCalculateCarbonButtonClick(View view) {
        // Start MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}