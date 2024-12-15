package com.example.myapplication;

import android.content.Intent;
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



public class User extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        TextView time = findViewById(R.id.tp);
        TextView scoreText = findViewById(R.id.hs);
        time.setText(String.format("0"));
        scoreText.setText(String.format("0"));
        // Set window insets (optional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up ImageButton click listener
        ImageButton imageButton = findViewById(R.id.imageButton);
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        imageButton.startAnimation(pulseAnimation);

        imageButton.setOnClickListener(v -> {
            // Navigate to MainActivity (Home)
            Intent intent = new Intent(User.this, Home.class);
            startActivity(intent);
        });
    }
}
