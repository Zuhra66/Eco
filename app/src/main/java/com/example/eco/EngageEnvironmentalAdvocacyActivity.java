package com.example.eco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;


import com.example.eco.database.EcoTrackRepository;
import com.example.eco.database.entity.User;

import com.example.eco.databinding.ActivityEngageEnvironmentalAdvocacyBinding;
import com.example.eco.databinding.ActivityEnviromentalNewsBinding;
import com.example.eco.viewHolders.EcoTrackAdapter;
import com.example.eco.viewHolders.EcoTrackLogViewModel;

public class EngageEnvironmentalAdvocacyActivity extends AppCompatActivity {
    private static final String ENGAGE_ENVIRONMENTAL_USER_ID = "com.example.eco.ENGAGE_ENVIRONMENTAL_USER_ID";
    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.eco.SAVED_INSTANCE_STATE_USERID_KEY";
    private ActivityEngageEnvironmentalAdvocacyBinding binding;
    private EcoTrackRepository repository;
    private EcoTrackLogViewModel ecoTrackLogViewModel;
    private EcoTrackAdapter adapter;
    private static final int LOGGED_OUT = -1;

    private int loggedInUserId = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEngageEnvironmentalAdvocacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = EcoTrackRepository.getRepository(getApplication());

        TextView engageEnvironmentalAdvocacyTextView = findViewById(R.id.engage_environmental_advocacy_TextView);
        String content = " How to Get Involved with Environmental Advocacy\n" +
                "We’re always told that every little bit of concern about the environment makes a difference: " +
                "drinking from reusable water bottles, " +
                "forgoing straws, taking shorter showers." +
                " Those are helpful, for sure, but they are incremental and limited by the number of people who embrace such mindful habits.";
        engageEnvironmentalAdvocacyTextView.setText(content);

        ecoTrackLogViewModel = new ViewModelProvider(this).get(EcoTrackLogViewModel.class);

        getUser(savedInstanceState);
    }
    private void getUser(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        if (loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if (loggedInUserId == LOGGED_OUT) {
            loggedInUserId = getIntent().getIntExtra(ENGAGE_ENVIRONMENTAL_USER_ID , LOGGED_OUT);
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(EngageEnvironmentalAdvocacyActivity.this);
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
        getIntent().putExtra(ENGAGE_ENVIRONMENTAL_USER_ID, loggedInUserId);
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

    public static Intent engageEnvironmentalAdvocacyActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, EngageEnvironmentalAdvocacyActivity.class);
        intent.putExtra(ENGAGE_ENVIRONMENTAL_USER_ID, userId);
        return intent;
    }

}