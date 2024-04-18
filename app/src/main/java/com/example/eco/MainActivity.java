package com.example.eco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eco.database.CarbonCalculator;
import com.example.eco.database.EcoTrackRepository;
import com.example.eco.database.entity.EcoTrackLog;
import com.example.eco.database.entity.User;
import com.example.eco.databinding.ActivityMainBinding;
import com.example.eco.viewHolders.EcoTrackAdapter;
import com.example.eco.viewHolders.EcoTrackLogViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "malfunction";
    private static final String MAIN_ACTIVITY_USER_ID = "com.example.eco.MAIN_ACTIVITY_USER_ID";
    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.eco.SAVED_INSTANCE_STATE_USERID_KEY";
    private ActivityMainBinding binding;
    private EcoTrackRepository repository;
    private EcoTrackLogViewModel ecoTrackLogViewModel;
    private EcoTrackAdapter adapter;
    private static final int LOGGED_OUT = -1;
    int id;
    String Transportation = "";
    String Energy = "";
    String Dietary = "";
    String totalEmissions = "";
    private int loggedInUserId = -1;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ecoTrackLogViewModel = new ViewModelProvider(this).get(EcoTrackLogViewModel.class);

        RecyclerView recyclerView = binding.welcomeDisplayRecyclerView;
        adapter = new EcoTrackAdapter(new EcoTrackAdapter.EcoTrackLogDiff(), isAdmin());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = EcoTrackRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        ecoTrackLogViewModel.getAllLogsById(loggedInUserId).observe(this, ecoTrackLogs -> {
            adapter.submitList(ecoTrackLogs);
        });

        if (loggedInUserId == -1) {
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        updateSharedPreferences();
        binding.Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                insertEcoTrackLogRecord();

            }
        });
    }

    private boolean isAdmin() {
        // Implement logic to determine if the user is an admin
        // For example, you can check the user role or permissions
        return false;
    }

    private void loginUser(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        if (loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if (loggedInUserId == LOGGED_OUT) {
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if (loggedInUserId == LOGGED_OUT) {
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this, user -> {
            this.user = user;
            if (this.user != null) {
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        updateSharedPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        if (user == null) {
            return false;
        }
        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                showLogoutDialog();
                return false;
            }
        });
        return true;
    }

    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();
        alertBuilder.setMessage("Logout");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertBuilder.create().show();
    }

    private void logout() {
        loggedInUserId = LOGGED_OUT;
        updateSharedPreferences();
        getIntent().putExtra(MAIN_ACTIVITY_USER_ID, loggedInUserId);
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

    public static Intent mainActivityIntentFactory(Context context, int UserId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, UserId);
        return intent;
    }

    private void insertEcoTrackLogRecord() {
        if (Transportation.isEmpty()) {
            return;
        }

        // Calculate total emissions based on user choices
        CarbonCalculator calculator = new CarbonCalculator();
        calculator.calculateTransportationEmissions(Transportation);
        calculator.calculateEnergyEmissions(Energy);
        calculator.calculateDietaryEmissions(Dietary);
        double totalEmissions = calculator.getTotalEmissions();

        // Create EcoTrackLog object with calculated total emissions
        EcoTrackLog log = new EcoTrackLog(Transportation, Energy, Dietary, String.valueOf(totalEmissions), loggedInUserId);

        // Insert EcoTrackLog record into the database
        repository.insertEcoTrackLog(log);
    }

    private void getInformationFromDisplay() {
        Transportation = binding.EnterChoiceTransportationInputEditText.getText().toString();
        Energy = binding.EnterEnergyChoiceInputEditText.getText().toString();
        Dietary = binding.EnterChoiceDietaryInputEditText.getText().toString();

        // Creates an instance of CarbonCalculator
        CarbonCalculator calculator = new CarbonCalculator();

        // Calculate emissions based on user choices
        calculator.calculateTransportationEmissions(Transportation);
        calculator.calculateEnergyEmissions(Energy);
        calculator.calculateDietaryEmissions(Dietary);

        // Get the total emissions from CarbonCalculator
        totalEmissions = String.valueOf(calculator.getTotalEmissions());

    }
    private void deleteItem(int position) {
        adapter.deleteItem(position);
    }
}