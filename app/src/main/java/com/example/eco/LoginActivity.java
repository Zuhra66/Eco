package com.example.eco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.eco.database.EcoTrackRepository;
import com.example.eco.database.SignUpActivity;
import com.example.eco.database.WelcomeActivity;
import com.example.eco.database.entity.User;
import com.example.eco.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private EcoTrackRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = EcoTrackRepository.getRepository(getApplication());

        // Set click listener for login button
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });

        // Set click listener for sign-up button
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign-up activity
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void verifyUser() {
        String username = binding.usernameLoginEditText.getText().toString();
        if (username.isEmpty()) {
            toastMaker("Username should not be blank");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                String password = binding.passwordLoginEditText.getText().toString();
                if (password.equals(user.getPassword())) {
                    storeUserId(user.getId()); // Store the user ID
                    startActivity(WelcomeActivity.welcomeIntentFactory(getApplicationContext()));
                }
                else {
                    toastMaker("Invalid password");
                    binding.passwordLoginEditText.setSelection(0);
                }
            } else {
                toastMaker(String.format("%s is not a valid username", username));
                binding.usernameLoginEditText.setSelection(0);
            }
        });
    }
    private void storeUserId(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("EcoTrackPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }
    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}