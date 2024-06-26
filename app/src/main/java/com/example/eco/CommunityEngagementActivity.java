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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eco.database.EcoTrackRepository;
import com.example.eco.database.entity.User;
import com.example.eco.databinding.ActivityCommunityEngagementBinding;
import com.example.eco.viewHolders.EcoTrackAdapter;
import com.example.eco.viewHolders.EcoTrackLogViewModel;

public class CommunityEngagementActivity extends AppCompatActivity {
    private static final String COMMUNITY_ACTIVITY_USER_ID = "com.example.eco.COMMUNITY_ACTIVITY_USER_ID";
    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.eco.SAVED_INSTANCE_STATE_USERID_KEY";
    private ActivityCommunityEngagementBinding binding;
    private EcoTrackRepository repository;
    private EcoTrackLogViewModel ecoTrackLogViewModel;
    private EcoTrackAdapter adapter;
    private static final int LOGGED_OUT = -1;

    private int loggedInUserId = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityEngagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = EcoTrackRepository.getRepository(getApplication());

        TextView communityEngagementTextView = findViewById(R.id.community_engagement_TextView);

        String content = "Simple Tips to Reduce Your Carbon Footprint\n" +
                "Switch it Off\n" +
                "Turn off the lights when natural light is sufficient and when you leave the room. It’s that simple!" +
                "Climate Control\n" +
                "Keep your temperature system on a moderate setting while you’re in the room." +
                "Wasteful Windows\n" +
                "Use your windows wisely! If your climate control system is on, shut them…if you need a little fresh air, turn off the heat or AC." +
                "Minimize Plug Load\n" +
                "Cut down the number of appliances you are running and you will save big on energy. For example, share your minifridge with roomates and minimize the number of printers in your office." +
                "Phantom Power\n" +
                "Did you know that many electronics continue using energy even when powered down? This is true of any charger, television, printer, etc. Use a power strip to easily unplug these electronics when not in use." +
                "Give it a Rest\n" +
                "Power your computer down when you’re away. A computer turned off uses at least 65% less energy than a computer left on or idle on a screen saver." +
                "Take the Stairs\n" +
                "Use the stairs as often as possible. Elevators consume electricity. You, on the other hand, do not." +
                "Loaded Laundry\n" +
                "Only do full loads of laundry and use the bright colors cycle whenever possible." +
                "Shorter Showers\n" +
                "Try to take shorter showers. The less hot water you use, the less energy is needed to heat the water.";
        communityEngagementTextView.setText(content);
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
            loggedInUserId = getIntent().getIntExtra(COMMUNITY_ACTIVITY_USER_ID , LOGGED_OUT);
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CommunityEngagementActivity.this);
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
        getIntent().putExtra(COMMUNITY_ACTIVITY_USER_ID, loggedInUserId);
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

    public static Intent communityEngagementActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, CommunityEngagementActivity.class);
        intent.putExtra(COMMUNITY_ACTIVITY_USER_ID, userId);
        return intent;
    }

}