package com.example.tourguide;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class galleface extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;
    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Marker currentLocationMarker;
    private LatLng galleFaceLocation;
    private LatLng currentLocation;
    private Polyline routePolyline;
    private Handler handler = new Handler();
    private Runnable locationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleface);

        // Initialize the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Set Galle Face location coordinates
        galleFaceLocation = new LatLng(6.9324, 79.8427);

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        } else {
            // Permission already granted
            initLocationUpdates();
        }
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                requestLocationUpdate();
                handler.postDelayed(this, 30000); // Refresh location every 10 seconds
            }
        };

    }
    private void requestLocationUpdate() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        }
    }


    private void initLocationUpdates() {
        // Get location updates
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        // Start the location updates
        handler.post(locationRunnable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                initLocationUpdates();
            } else {
                // Location permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker for Galle Face
        googleMap.addMarker(new MarkerOptions().position(galleFaceLocation).title("Galle Face"));


    }

    @Override
    public void onLocationChanged(Location location) {
        // Update current location
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        // Clear previous markers and polylines
        if (currentLocationMarker != null)
            currentLocationMarker.remove();
        if (routePolyline != null)
            routePolyline.remove();

        // Add a marker for the current location
        currentLocationMarker = googleMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Current Location"));

        // Calculate distance between current location and Galle Face
        float[] distance = new float[1];
        Location.distanceBetween(
                currentLocation.latitude,
                currentLocation.longitude,
                galleFaceLocation.latitude,
                galleFaceLocation.longitude,
                distance
        );

        // Convert distance to kilometers
        double distanceInKilometers = distance[0] / 1000.0;

        // Display the distance in kilometers
        Toast.makeText(this, "Distance to Galle Face: " + String.format("%.2f", distanceInKilometers) + " km", Toast.LENGTH_SHORT).show();

        // Fetch and display the travel route
        getDirections(currentLocation, galleFaceLocation);
    }
    private void getDirections(LatLng origin, LatLng destination) {
        // Build the request URL for the Directions API
        String apiUrl = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&key=AIzaSyDbr2R01FrbqiHYASb0_QTgG-05zG4X34g"; // Replace with your own API key

        // Create an HTTP client and request object
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        // Make the API call asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle the failure case
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Parse the response JSON
                String jsonData = response.body().string();
                try {
                    JSONObject json = new JSONObject(jsonData);
                    JSONArray routes = json.getJSONArray("routes");
                    if (routes.length() > 0) {
                        JSONObject route = routes.getJSONObject(0);
                        JSONObject polyline = route.getJSONObject("overview_polyline");
                        String polylinePoints = polyline.getString("points");

                        // Decode the polyline points into LatLng coordinates
                        List<LatLng> decodedPoints = decodePolyline(polylinePoints);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Draw polyline for the route
                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .addAll(decodedPoints)
                                        .color(ContextCompat.getColor(galleface.this, R.color.colorPrimary));
                                routePolyline = googleMap.addPolyline(polylineOptions);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<LatLng> decodePolyline(String polyline) {
        List<LatLng> decodedPoints = new ArrayList<>();
        int index = 0, len = polyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            double latitude = lat * 1e-5;
            double longitude = lng * 1e-5;
            LatLng point = new LatLng(latitude, longitude);
            decodedPoints.add(point);
        }
        return decodedPoints;
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        // Check if location services are enabled
        if (!isLocationEnabled()) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        // Stop the location updates
        handler.removeCallbacks(locationRunnable);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
