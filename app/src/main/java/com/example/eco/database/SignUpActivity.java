package com.example.eco.database;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eco.LoginActivity;
import com.example.eco.database.entity.User;
import com.example.eco.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private EcoTrackRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = EcoTrackRepository.getRepository(getApplication());

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditText fields
                String username = binding.usernameEditText.getText().toString().trim();
                String password = binding.passwordEditText.getText().toString().trim();

                // Validate user input
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the username already exists in the database
                if (repository.doesUserExist(username)) {
                    Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    // Create a new User object
                    User newUser = new User(username, password);

                    // Insert the new user into the database
                    repository.insertUser(newUser);

                    // Display a success message
                    Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                    // Navigate back to the LoginActivity
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Finish the SignUpActivity to prevent going back to it when pressing back button
                }
            }
        });
    }
}