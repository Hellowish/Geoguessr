package com.example.myapplication;

import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ScoreFragment extends Fragment {

    private TextView scoreTextView;

    public ScoreFragment() {
        // Required empty public constructor
    }

    private ImageButton resetButton; // Declare the reset button

    public static ScoreFragment newInstance(double score) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putDouble("score", score);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the score from arguments
        double score = getArguments() != null ? getArguments().getDouble("score", 0) : 0;

        // Get the TextView for displaying the score
        scoreTextView = view.findViewById(R.id.score_text);

        // Animate the score
        animateScore(score);

        // Get the reset button and set click listener
        resetButton = view.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(v -> {
            // Check if the host activity is MainPlay and call resetQuestion
            hideScoreFragment();
            if (getActivity() instanceof MainPlay) {
                MainPlay mainActivity = (MainPlay) getActivity();
                mainActivity.resetQuestion(); // Call resetQuestion() method in MainPlay
            }
        });
    }

    private void hideScoreFragment() {
        // Get the parent activity (MainPlay) to handle fragment transactions
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            // Remove the ScoreFragment
            transaction.remove(this);
            transaction.commit();
        }
    }

    private void animateScore(final double finalScore) {
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
                    scoreTextView.setText(String.format("%.0f", 100 * currentScore[0]));

                    // Repeat this update after the defined interval
                    new Handler().postDelayed(this, updateInterval);
                }
            }
        }, updateInterval);
    }
}