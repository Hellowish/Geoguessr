package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

public class MainPlay extends AppCompatActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private LatLng mapCoordinate;
    private GoogleMap mapIns;
    private StreetViewPanorama streetViewIns;
    private SupportMapFragment mapFragment;
    private SupportStreetViewPanoramaFragment streetViewFragment;

    private TextView timerText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000; // 30 seconds in milliseconds

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

        LatLng FUJEN = new LatLng(25.0326369, 121.4342057);
        setStreetViewPosition(FUJEN);
    }

    public void setStreetViewPosition(LatLng latLng) {
        streetViewIns.setPosition(latLng);
    }
}
