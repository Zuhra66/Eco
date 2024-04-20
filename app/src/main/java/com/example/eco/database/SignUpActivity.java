package com.example.eco.database;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eco.LoginActivity;
import com.example.eco.database.entity.User;
import com.example.eco.databinding.ActivitySignUpBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private EcoTrackRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = EcoTrackRepository.getRepository(getApplication());

        binding.signUpButton.setOnClickListener(v -> {
            // Get user input from EditText fields
            String username = binding.newUsernameEditText.getText().toString().trim();
            String password = binding.newPasswordEditText.getText().toString().trim();
            String retypePassword = binding.newRetypePasswordEditText.getText().toString().trim();

            // Validate user input
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            //check for whitespace in username and password
            Pattern pattern = Pattern.compile("\\s");
            Matcher usernameMatcher = pattern.matcher(username);
            Matcher passwordMatcher = pattern.matcher(password);
            if (usernameMatcher.find() || passwordMatcher.find()){
                Toast.makeText(this, "You cannot have any whitespace in username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            repository.doesUserExist(username).observe(SignUpActivity.this, aBoolean -> {
                if (aBoolean) {
                    // User already exists, show error message
                    Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.length() >= 8 && password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                        if (password.equals(retypePassword)) {
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
                        } else {
                            Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (password.length() < 8) {
                            Toast.makeText(SignUpActivity.this, "Password is not at least 8 characters long.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Missing special character(i.e. !, @, #, etc.)", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        });
    }
}