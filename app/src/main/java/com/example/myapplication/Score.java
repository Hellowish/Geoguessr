package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Score extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Retrieve the score passed from the previous activity
        double score = getIntent().getDoubleExtra("score", 0);

        // Get the TextView for displaying the score
        TextView scoreText = findViewById(R.id.score_text);

        // Animate the score
        animateScore(score, scoreText);
    }

    private void animateScore(final double finalScore, final TextView scoreTextView) {
        // Duration for the animation (in milliseconds)
        int animationDuration = 2000; // 2 seconds

        // The interval between updates (in milliseconds)
        int updateInterval = 50;

        // The number of updates (based on the animation duration)
        int numberOfUpdates = animationDuration / updateInterval;

        // The increment for each update (based on the final score and number of updates)
        final double increment = finalScore / numberOfUpdates;

        // Mutable variable to hold the current score during animation
        final double[] currentScore = {0};

        // Use a Handler to update the score every updateInterval milliseconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentScore[0] < finalScore) {
                    // Increment the current score
                    currentScore[0] += increment;

                    // Ensure the score doesn't exceed the final score
                    if (currentScore[0] > finalScore) {
                        currentScore[0] = finalScore;
                    }

                    // Update the TextView with the current score (formatted to 2 decimal places)
                    scoreTextView.setText(String.format("Your Score: %.2f", currentScore[0]));

                    // Repeat this update after the defined interval
                    new Handler().postDelayed(this, updateInterval);
                }
            }
        }, updateInterval);
    }
}
