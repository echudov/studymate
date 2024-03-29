package com.example.studymate;

import android.content.BroadcastReceiver;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MainScreenActivity extends AppCompatActivity {


    private BroadcastReceiver locationUpdateReceiver;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LatLng mostRecentLocation;

    private NavController navController;

    private boolean hasLocationPermission;

    public HashMap<Integer, SearchResultData> users;
    private String TAG = "MainScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Possibly use the action bar, but still unsure
        // DON'T FORGET TO CHANGE styles.xml to DarkActionBar
        // AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //          R.id.navigation_floor, R.id.navigation_map, R.id.navigation_notifications, R.id.navigation_settings)
        //          .build();
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // LOCATION DATA FOR USER
        // IMPLEMENT PRERERENCE TO KEEP IT EITHER HIDDEN FROM OTHERS /
        // NOT USED


        // hasLocationPermission change based on settings preferences, no idea how to do that rn
        // madhav if you could help that would be brain boosting
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });
        // location doesn't quite work
        // next feature to add:
        // if the user leaves/changes location to remove him from the database

        locationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    mostRecentLocation = new LatLng (location.getLatitude(), location.getLongitude());
                }
            }
        };
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
    }

    /**
     * start getting location updates
     * not fully functional
     */
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }
    /**
     * Switch to floor function
     * Called by child fragments to switch to the floor view
     * Extra information passed thru bundles
     * @param library library to switch to
     * @param floor floor to look at
     * @param fragment fragment switching from
     */
    public void switchToFloor(String library, int floor, String fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("library", library);
        bundle.putInt("floor", floor);
        switch (fragment) {
            case "map":
                navController.navigate(R.id.action_navigation_map_to_navigation_floor, bundle);
                break;
            case "search":
                navController.navigate(R.id.action_navigation_search_to_navigation_floor, bundle);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + fragment);
        }
    }
}
