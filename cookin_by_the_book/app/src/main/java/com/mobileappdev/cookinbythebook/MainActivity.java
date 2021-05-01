package com.mobileappdev.cookinbythebook;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_add_recipe, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if(getActionBar() != null){
            getActionBar().hide();
        }

        View background = this.findViewById(R.id.imageView);
        sendViewToBack(background);

        Log.d(TAG, "onCreate: Success");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Success");
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) {
            Log.d(TAG, "no user");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    // This makes sure that our background art is always behind everything else
    // From https://stackoverflow.com/a/19872801
    public void sendViewToBack(View child) {

        final ViewGroup mParent = (ViewGroup)child.getParent();
        if (null != mParent) {
            mParent.removeView(child);
            mParent.addView(child, 0);
        }
    }
}