package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FailureActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_failure);
        // Retrieve the passed sum value
        float sum = getIntent().getFloatExtra("sum", 0.0f);

        // Display or use the sum value as needed
        TextView sumTextView = findViewById(R.id.sumTextView); // Assume you have a TextView with this ID
        sumTextView.setText(String.format("%.0f",sum));

        ImageButton playButton = findViewById(R.id.retry);
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        playButton.startAnimation(pulseAnimation);

        // Play background music
        mediaPlayer = MediaPlayer.create(this, R.raw.failure_music); // Replace "failure_music" with your audio file name in res/raw
        mediaPlayer.setLooping(true); // Loop the music
        mediaPlayer.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the back-to-home button
        findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, Home.class); // Replace `HomeActivity` with your home page activity class
        startActivity(intent);
        finish(); // Close the current activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
