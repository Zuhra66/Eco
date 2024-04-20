package com.example.eco.database;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

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
                String username = binding.newUsernameEditText.getText().toString().trim();
                String password = binding.newPasswordEditText.getText().toString().trim();
                String retypePassword = binding.newRetypePasswordEditText.getText().toString().trim();

                // Validate user input
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                repository.doesUserExist(username).observe(SignUpActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
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
                    }
                });
            }
        });
    }
}