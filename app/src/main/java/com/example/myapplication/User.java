package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class User extends AppCompatActivity {


    private MediaPlayer backgroundMusic; // For background music

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        getInitialGameHistory();

        // Initialize and play background music
        backgroundMusic = MediaPlayer.create(this, R.raw.mu);
        backgroundMusic.setLooping(true); // Loop the music
        backgroundMusic.start();

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

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause(); // Pause music when activity is not visible
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start(); // Resume music when activity is back in the foreground
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 釋放資源

        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
            Log.d("Ranking", "backgroundMusic released");
        }
    }
    private void getInitialGameHistory() {
        new Thread(() -> {
            try {
                ApiHelper.readData(
                        this,
                        response -> {
                            int highScore = 0;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    int score = data.getInt("score");

                                    if(highScore < score)
                                        highScore = score;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            // 使用 Handler 在 UI 執行緒中更新 UI
                            TextView time = findViewById(R.id.tp);
                            TextView scoreText = findViewById(R.id.hs);

                            time.setText(String.format(String.valueOf(response.length())));
                            scoreText.setText(String.format(String.valueOf(highScore)));

                        },
                        error -> {
                            // 錯誤處理
                            new Handler(Looper.getMainLooper()).post(() -> {
                                Toast.makeText(this, "讀取失敗: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
