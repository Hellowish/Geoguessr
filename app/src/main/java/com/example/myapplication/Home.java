package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    public static MediaPlayer mediaPlayer;
    private MediaPlayer clickSound; // 用于播放点击音效

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_home); // Load the game_home layout

        // Initialize background music
        if (mediaPlayer == null) { // Initialize only if it's not already initialized
            mediaPlayer = MediaPlayer.create(this, R.raw.home); // Background music file
            mediaPlayer.setLooping(true); // Loop music indefinitely
            mediaPlayer.start(); // Start the music
        }


        // Initialize click sound
        clickSound = MediaPlayer.create(this, R.raw.click); // Make sure to put your click sound file in res/raw

        // Initialize Play button
        ImageButton playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(v -> {
            // Play click sound
            playClickSound();

            // Start MainPlay Activity
            Intent intent = new Intent(Home.this, MainPlay.class);
            startActivity(intent);

            // Apply button scale animation
            animateButtonClick(v);
        });

        // Initialize record button
        ImageButton recordButton = findViewById(R.id.record_button);
        recordButton.setOnClickListener(v -> {
            // Play click sound
            playClickSound();

            // Start Ranking Activity
            Intent intent = new Intent(Home.this, Ranking.class);
            startActivity(intent);

            // Apply button scale animation
            animateButtonClick(v);
        });

        // Initialize profile_button
        ImageButton userPageButton = findViewById(R.id.profile_button);
        userPageButton.setOnClickListener(v -> {
            // Play click sound
            playClickSound();

            // Start User Activity
            Intent intent = new Intent(Home.this, User.class);
            startActivity(intent);

            // Apply button scale animation
            animateButtonClick(v);
        });

        // Spinner setup for cities and towns (same as before)
        Spinner spinnerCity = findViewById(R.id.spinner_city);
        Spinner spinnerTown = findViewById(R.id.spinner_town);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);

        spinnerCity.setSelection(22); // Default to Taiwan

        // City selection listener (same as before)
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                int townArrayId;
                switch (position) {
                    case 0: // Taipei City
                        townArrayId = R.array.towns_taipei;
                        break;
                    case 1: // New Taipei City
                        townArrayId = R.array.towns_newtaipei;
                        break;
                    case 2: // Taoyuan City
                        townArrayId = R.array.towns_taoyuan;
                        break;
                    case 3: // Taichung City
                        townArrayId = R.array.towns_taichung;
                        break;
                    case 4: // Tainan City
                        townArrayId = R.array.towns_tainan;
                        break;
                    case 5: // Kaohsiung City
                        townArrayId = R.array.towns_kaohsiung;
                        break;
                    case 6: // 基隆市
                        townArrayId = R.array.towns_keelung;
                        break;
                    case 7: // 新竹市
                        townArrayId = R.array.towns_hsinchu;
                        break;
                    case 8: // 嘉義市
                        townArrayId = R.array.towns_chiayi;
                        break;
                    case 9: // 宜蘭縣
                        townArrayId = R.array.towns_yilan;
                        break;
                    case 10: // 新竹縣
                        townArrayId = R.array.towns_hsinchucounty;
                        break;
                    case 11: // 苗栗縣
                        townArrayId = R.array.towns_miaoli;
                        break;
                    case 12: // 彰化縣
                        townArrayId = R.array.towns_changhua;
                        break;
                    case 13: // 南投縣
                        townArrayId = R.array.towns_nantou;
                        break;
                    case 14: // 雲林縣
                        townArrayId = R.array.towns_yunlin;
                        break;
                    case 15: // 嘉義縣
                        townArrayId = R.array.towns_chiayicounty;
                        break;
                    case 16: // 屏東縣
                        townArrayId = R.array.towns_pingtung;
                        break;
                    case 17: // 臺東縣
                        townArrayId = R.array.towns_taitung;
                        break;
                    case 18: // 花蓮縣
                        townArrayId = R.array.towns_hualien;
                        break;
                    case 19: // 澎湖縣
                        townArrayId = R.array.towns_penghu;
                        break;
                    case 20: // 金門縣
                        townArrayId = R.array.towns_kinmen;
                        break;
                    case 21: // 連江縣
                        townArrayId = R.array.towns_lianjiang;
                        break;
                    case 22: // 台灣
                        townArrayId = R.array.taiwan;
                        break;
                    default:
                        townArrayId = R.array.taiwan; // Default to Taiwan
                        break;
                }

                ArrayAdapter<CharSequence> townAdapter = ArrayAdapter.createFromResource(Home.this,
                        townArrayId, android.R.layout.simple_spinner_item);
                townAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTown.setAdapter(townAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Default behavior
            }
        });
    }

    // Play click sound
    private void playClickSound() {
        if (clickSound != null) {
            clickSound.start(); // Play the sound
        }
    }

    // Button scale animation
    private void animateButtonClick(View view) {
        // Scale the button (grow and shrink effect)
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f); // X-axis scale
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f); // Y-axis scale
        scaleUpX.setDuration(100); // Scale-up duration
        scaleUpY.setDuration(100); // Scale-up duration
        scaleUpX.start();
        scaleUpY.start();

        // After scaling up, scale down to original size
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f); // X-axis scale back
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1f); // Y-axis scale back
        scaleDownX.setDuration(100); // Scale-down duration
        scaleDownY.setDuration(100); // Scale-down duration
        scaleDownX.start();
        scaleDownY.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the background music when the activity is no longer in the foreground
        //if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            //mediaPlayer.pause(); // Pause music
        //}
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the background music if it was paused
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // Start the music again
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed to free resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null; // Make sure it's null to avoid memory leaks
        }

        // Release the click sound MediaPlayer
        if (clickSound != null) {
            clickSound.release();
        }
    }
}
