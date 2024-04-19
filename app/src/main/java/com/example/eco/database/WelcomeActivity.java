package com.example.eco.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
                SharedPreferences sharedPreferences = getSharedPreferences("EcoTrackPrefs", Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("userId", -1); // Default to -1 if not found
                if (userId != -1) {
                    startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(), userId));
                } else {
                    // Handle the case where user ID is not found
                    Toast.makeText(WelcomeActivity.this, "User ID not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to create an intent for starting WelcomeActivity
    public static Intent welcomeIntentFactory(Context context) {
        return new Intent(context, WelcomeActivity.class);
    }
}