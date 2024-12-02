package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

import org.json.JSONObject;

public class MainPlay extends AppCompatActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private LatLng mapCoordinate;
    private LatLng strretViewCoordinate;
    private GoogleMap mapIns;
    private StreetViewPanorama streetViewIns;
    private SupportMapFragment mapFragment;
    private SupportStreetViewPanoramaFragment streetViewFragment;

    private TextView timerText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 300000; // 30 seconds in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_play);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapCoordinate = new LatLng(23.975667, 120.973861);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        streetViewFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetview_fragment);
        if (streetViewFragment != null)
            streetViewFragment.getStreetViewPanoramaAsync(this);

        // Initialize timer TextView
        timerText = findViewById(R.id.timer_text);

        // Start the countdown timer
        startTimer();

        // Set up the hint button
        findViewById(R.id.hint).setOnClickListener(view -> showHintPopup());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                switchToFailurePage();
            }
        }.start();
    }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        timerText.setText(String.valueOf(seconds));
    }

    private void switchToFailurePage() {
        Intent intent = new Intent(MainPlay.this, FailureActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        mapIns = map;
        mapIns.addMarker(new MarkerOptions().position(mapCoordinate).title("Location"));
        mapIns.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCoordinate, 8f));
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewIns = streetViewPanorama;

        streetViewPanorama.setPanningGesturesEnabled(true);
        streetViewPanorama.setUserNavigationEnabled(true);
        streetViewPanorama.setZoomGesturesEnabled(true);
        streetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder()
                        .orientation(new StreetViewPanoramaOrientation(20, 20))
                        .zoom(streetViewPanorama.getPanoramaCamera().zoom)
                        .build(), 2000
        );

        RequestQuetion(1);
    }

    public void setStreetViewPosition(LatLng latLng) {
        streetViewIns.setPosition(latLng);
    }

    public void RequestQuetion(int id) {
        ApiHelper.fetchCoordinates(this, id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse latitude and longitude from the response
                            double latitude = response.getDouble("latitude");
                            double longitude = response.getDouble("longitude");

                            latitude = Math.round(latitude * 1000.0) / 1000.0;
                            longitude = Math.round(longitude * 1000.0) / 1000.0;

                            // Create LatLng object
                            strretViewCoordinate = new LatLng(latitude, longitude);
                            setStreetViewPosition(strretViewCoordinate);
                        } catch (Exception e) {
                            Toast.makeText(MainPlay.this,
                                    "Parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.d("DEBUG", "Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainPlay.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.d("DEBUG", "Error: " + error.getMessage());
                    }
                }
        );
    }

    private void showHintPopup() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Warning, will deduct points.")
                .setPositiveButton("Yes", (dialog, which) -> showHintDetailsPopup())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showHintDetailsPopup() {
        new AlertDialog.Builder(this)
                .setTitle("Hint")
                .setMessage("Here is your hint: Look closer at the location!")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
