package com.example.eco;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eco.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
        ActivityMainBinding binding;
    String Transportation;
    String Energy;
    String Dietary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.WelcomeTextView.setMovementMethod(new ScrollingMovementMethod());
        binding.Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                updateDisplay();
            }
        });
    }
    private void updateDisplay(){
        String currentInfo = binding.WelcomeTextView.getText().toString();

        String newDisplay = String.format(Locale.US,"Transportation : %s%n Energy : %s%nDietary : %s%n============%n",Transportation,Energy,Dietary);
        binding.WelcomeTextView.setText(newDisplay);
    }
    private void getInformationFromDisplay(){
        Transportation = binding.EnterChoiceTransportationInputEditText.getText().toString();
        Energy = binding.EnterEnergyChoiceInputEditText.getText().toString();
        Dietary = binding.EnterChoiceDietaryInputEditText.getText().toString();

    }
}