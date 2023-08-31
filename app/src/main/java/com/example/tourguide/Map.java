package com.example.tourguide;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.tourguide.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class Map extends FragmentActivity implements OnMapReadyCallback {
    public ActivityMapsBinding binding;
    private static final int LOCATION_PERMISSION_CODE = 101;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //nav bar
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.navigation_home:
                    startActivity(new Intent(Map.this, home.class));
                    break;
                case R.id.navigation_map:
                    startActivity(new Intent(Map.this, Map.class));
                    break;
                case R.id.navigation_about:
                    startActivity(new Intent(Map.this, about.class));
                    break;
                case R.id.navigation_profile:
                    startActivity(new Intent(Map.this, profile.class));
                    break;
            }
            // Uncheck the previously selected item
            bottomNavigationView.getMenu().findItem(itemId).setChecked(true);
            return true;
        });
        // Check if location permission is granted
        if(isLocationPermissionGranted()){
            // Display the map
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            mapFragment.getMapAsync(this);

            // Get the location manager
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Check if GPS is enabled
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Show an alert dialog to prompt the user to enable GPS
                new AlertDialog.Builder(this)
                        .setTitle("Location services disabled")
                        .setMessage("Please enable location services to use this feature.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Open the location settings screen to allow the user to enable GPS
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            } else {
            }
        } else {
            // Request location permission
            requestLocationPermission();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Enable the "My Location" button on the map
            googleMap.setMyLocationEnabled(true);

            // Get the user's current location
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            // Retrieve the current location's address
                            Geocoder geocoder = new Geocoder(Map.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            Address currentAddress = addresses.get(0);
                            String address = currentAddress.getThoroughfare() + ", " + currentAddress.getLocality();

                            // Retrieve the current weather from OpenWeatherMap API
                            OkHttpClient client = new OkHttpClient();
                            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&APPid=244a15ad27a5b6784b380b6eabfd8266";
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        assert response.body() != null;
                                        String responseStr = response.body().string();
                                        try {
                                            // weather information
                                            JSONObject json = new JSONObject(responseStr);
                                            JSONObject main = json.getJSONObject("main");
                                            double tempCelsius = main.getDouble("temp") - 273.15; // Convert temperature from Kelvin to Celsius
                                            DecimalFormat df = new DecimalFormat("#.#"); // Create a DecimalFormat object with one decimal western
                                            String tempFormatted = df.format(tempCelsius); // Format the temperature
                                            String weather = json.getJSONArray("weather").getJSONObject(0).getString("main");
                                            String iconCode = json.getJSONArray("weather").getJSONObject(0).getString("icon");


                                            String locationAndWeather = "Current Location: "+address+"\nWeather: "+weather+"\nTemperature: "+tempFormatted+"°C";

                                            // Update the  location and weather details
                                            runOnUiThread(() -> {
                                                // Display the location and weather details in a text view
                                                TextView locationAndWeatherTextView = findViewById(R.id.weather_text_view);
                                                locationAndWeatherTextView.setText(locationAndWeather);

                                                // Display the weather icon using Glide library
                                                ImageView weatherIconImageView = findViewById(R.id.weather_image_view);
                                                Glide.with(Map.this)
                                                        .load("https://openweathermap.org/img/w/"+iconCode+".png")
                                                        .into(weatherIconImageView);
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        throw new IOException("Unexpected HTTP response: " + response);
                                    }
                                }

                            });
                        }
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
        }
    }
    private boolean isLocationPermissionGranted(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }
    //request location permission
    private void requestLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Location Permission Required")
                    .setMessage("Please enable location permission to use this feature")
                    .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(Map.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_CODE))
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }
    }
}