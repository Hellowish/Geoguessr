package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

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

        // Initialize ImageButton2 (User page button)
        ImageButton userPageButton = findViewById(R.id.imageButton);
        userPageButton.setOnClickListener(v -> {
            // Start User Activity
            Intent intent = new Intent(Home.this, User.class);
            startActivity(intent);
        });

        Spinner spinnerCity = findViewById(R.id.spinner_city);
        Spinner spinnerTown = findViewById(R.id.spinner_town);

// 建立縣市的 Adapter
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);

        spinnerCity.setSelection(22); //預設全台灣

// 縣市選擇監聽器
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                int townArrayId;
                switch (position) {
                    case 0: // 台北市
                        townArrayId = R.array.towns_taipei;
                        break;
                    case 1: // 新北市
                        townArrayId = R.array.towns_newtaipei;
                        break;
                    case 2: // 桃園市
                        townArrayId = R.array.towns_taoyuan;
                        break;
                    case 3: // 台中市
                        townArrayId = R.array.towns_taichung;
                        break;
                    case 4: // 台南市
                        townArrayId = R.array.towns_tainan;
                        break;
                    case 5: // 高雄市
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
                        townArrayId = R.array.taiwan; // 預設台灣
                        break;
                }

                // 更新鄉鎮的 Adapter
                ArrayAdapter<CharSequence> townAdapter = ArrayAdapter.createFromResource(Home.this,
                        townArrayId, android.R.layout.simple_spinner_item);
                townAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTown.setAdapter(townAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 預設處理
            }
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