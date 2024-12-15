package com.example.myapplication;

import android.animation.ValueAnimator;
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

        // Find the TextView
        TextView sumTextView = findViewById(R.id.sumTextView);

        // Animate the TextView to count from zero to the sum
        animateSumTextView(sumTextView, sum);

        // Find the retry button and apply animation
        ImageButton playButton = findViewById(R.id.retry);
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        playButton.startAnimation(pulseAnimation);

        // Play background music
        mediaPlayer = MediaPlayer.create(this, R.raw.failure_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Adjust padding for system bars
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

    /**
     * Animate the TextView to count from 0 to the target value.
     *
     * @param textView The TextView to animate.
     * @param targetValue The final value to count up to.
     */
    private void animateSumTextView(final TextView textView, float targetValue) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, targetValue);
        animator.setDuration(5000); // Duration of the animation in milliseconds
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            textView.setText(String.format("%.0f", animatedValue)); // Update the TextView with the current value
        });
        animator.start();
    }

    /**
     * Navigate back to the home screen.
     */
    private void navigateToHome() {
        Intent intent = new Intent(this, Home.class); // Replace `Home` with your home activity class
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
