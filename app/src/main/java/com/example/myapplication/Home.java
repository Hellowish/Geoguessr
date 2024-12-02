package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_home); // Load the game_home layout

        // Initialize MediaPlayer with the music file in the raw folder
        mediaPlayer = MediaPlayer.create(this, R.raw.home); // Make sure to put your music file in res/raw
        mediaPlayer.setLooping(true); // Loop the music indefinitely
        mediaPlayer.start(); // Start playing the music

        // Initialize ImageButton5 (Play button)
        ImageButton playButton = findViewById(R.id.imageButton5);
        playButton.setOnClickListener(v -> {
            // Start MainPlay Activity
            Intent intent = new Intent(Home.this, MainPlay.class);
            startActivity(intent);
        });

        // Initialize ImageButton2 (Ranking button)
        ImageButton rankingButton = findViewById(R.id.imageButton2);
        rankingButton.setOnClickListener(v -> {
            // Start Ranking Activity
            Intent intent = new Intent(Home.this, Ranking.class);
            startActivity(intent);
        });

        // Initialize ImageButton2 (Ranking button)
        ImageButton UserPage = findViewById(R.id.imageButton);
        UserPage.setOnClickListener(v -> {
            // Start Ranking Activity
            Intent intent = new Intent(Home.this, User.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the music when the activity is no longer in the foreground
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Pause music
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the music if it was paused
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // Start the music again when the activity resumes
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed to free resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}



