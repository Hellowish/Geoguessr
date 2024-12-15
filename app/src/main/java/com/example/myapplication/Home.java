package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class Home extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaPlayer clickSound; // 用于播放点击音效

    private String city = "taiwan";
    private String town;

    String[] qCities = new String[3];
    String[] qTowns = new String[3];
    double[] qLatitudes = new double[3];
    double[] qLongitudes = new double[3];

    private ProgressBar progressBar; // Declare ProgressBar
    private TextView percentageText;
    private Handler handler = new Handler();
    private int progressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_home); // Load the game_home layout

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE); // Set to white
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Dark icons
        }

        // Apply pulse animation to play button
        ImageButton playButton = findViewById(R.id.play_button);
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation2);
        playButton.startAnimation(pulseAnimation);

        ImageButton rank = findViewById(R.id.profile_button);
        Animation up = AnimationUtils.loadAnimation(this, R.anim.button_animation3);
        rank.startAnimation(up);

        ImageButton score = findViewById(R.id.record_button);
        Animation down = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        score.startAnimation(down);

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progressBar);
        percentageText = findViewById(R.id.percentageText);

        Drawable drawable = progressBar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(Color.parseColor("#afeeee"), PorterDuff.Mode.SRC_IN); // 設置顏色

        // Set the maximum value for the progress bar (if not set in XML)
        progressBar.setMax(100);

        // Initialize background music
        if (mediaPlayer == null) { // Initialize only if it's not already initialized
            mediaPlayer = MediaPlayer.create(this, R.raw.home); // Background music file
            mediaPlayer.setLooping(true); // Loop music indefinitely
            mediaPlayer.start(); // Start the music
        }

        // Initialize click sound
        clickSound = MediaPlayer.create(this, R.raw.click); // Make sure to put your click sound file in res/r

        // 获取进度条和按钮

        ProgressBar progressBar = findViewById(R.id.progressBar);

        playButton.setOnClickListener(v -> {
            // Play click sound
            playClickSound();

            // Apply button scale animation
            animateButtonClick(v);

            // Show ProgressBar (loading circle)
            progressBar.setVisibility(View.VISIBLE);
            percentageText.setVisibility(View.VISIBLE);

            // 確保進度條和百分比文字在其他視圖之上
            progressBar.bringToFront();
            percentageText.bringToFront();

            // Start a thread to simulate loading and update the ProgressBar
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressStatus < 100) {
                        progressStatus++;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progressStatus);
                                percentageText.setText(progressStatus + "%");
                            }
                        });

                        try {
                            Thread.sleep(50); // Simulate work being done
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Once loading is complete, make request
                    RequestQuestion(city, town, new RequestQuestionCallback() {
                        @Override
                        public void onRequestQuestionCompleted() {
                            progressBar.setVisibility(View.GONE);
                            percentageText.setVisibility(View.GONE);

                            // Start MainPlay Activity after completion
                            Intent intent = new Intent(Home.this, MainPlay.class);
                            intent.putExtra("qLatitudes", qLatitudes);
                            intent.putExtra("qLongitudes", qLongitudes);
                            intent.putExtra("city", qCities);
                            intent.putExtra("town", qTowns);

                            // Set max distance based on city
                            if ("taiwan".equals(city))
                                intent.putExtra("maxDistance", 200);
                            else
                                intent.putExtra("maxDistance", 20);

                            startActivity(intent);
                        }
                    });
                }
            }).start();  // Start the loading thread
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

            overridePendingTransition(R.anim.record_in, R.anim.record_out);
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

            overridePendingTransition(R.anim.record_in, R.anim.record_out);
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
                    case 0: // 台北市
                        city = "taipei";
                        townArrayId = R.array.towns_taipei;
                        break;
                    case 1: // 新北市
                        city = "newtaipei";
                        townArrayId = R.array.towns_newtaipei;
                        break;
                    case 2: // 桃園市
                        city = "taoyuan";
                        townArrayId = R.array.towns_taoyuan;
                        break;
                    case 3: // 台中市
                        city = "taichung";
                        townArrayId = R.array.towns_taichung;
                        break;
                    case 4: // 台南市
                        city = "tainan";
                        townArrayId = R.array.towns_tainan;
                        break;
                    case 5: // 高雄市
                        city = "kaohsiung";
                        townArrayId = R.array.towns_kaohsiung;
                        break;
                    case 6: // 基隆市
                        city = "keelung";
                        townArrayId = R.array.towns_keelung;
                        break;
                    case 7: // 新竹市
                        city = "hsinchu_city";
                        townArrayId = R.array.towns_hsinchu;
                        break;
                    case 8: // 嘉義市
                        city = "chiayi_county";
                        townArrayId = R.array.towns_chiayi;
                        break;
                    case 9: // 宜蘭縣
                        city = "yilan";
                        townArrayId = R.array.towns_yilan;
                        break;
                    case 10: // 新竹縣
                        city = "hsinchu_county";
                        townArrayId = R.array.towns_hsinchucounty;
                        break;
                    case 11: // 苗栗縣
                        city = "miaoli";
                        townArrayId = R.array.towns_miaoli;
                        break;
                    case 12: // 彰化縣
                        city = "chiayi_city";
                        townArrayId = R.array.towns_changhua;
                        break;
                    case 13: // 南投縣
                        city = "nantou";
                        townArrayId = R.array.towns_nantou;
                        break;
                    case 14: // 雲林縣
                        city = "yunlin";
                        townArrayId = R.array.towns_yunlin;
                        break;
                    case 15: // 嘉義縣
                        city = "chiayi_county";
                        townArrayId = R.array.towns_chiayicounty;
                        break;
                    case 16: // 屏東縣
                        city = "pingtung";
                        townArrayId = R.array.towns_pingtung;
                        break;
                    case 17: // 臺東縣
                        city = "taitung";
                        townArrayId = R.array.towns_taitung;
                        break;
                    case 18: // 花蓮縣
                        city = "hualien";
                        townArrayId = R.array.towns_hualien;
                        break;
                    case 19: // 澎湖縣
                        city = "penghu";
                        townArrayId = R.array.towns_penghu;
                        break;
                    case 20: // 金門縣
                        city = "kinmen";
                        townArrayId = R.array.towns_kinmen;
                        break;
                    case 21: // 連江縣
                        city = "lienchiang";
                        townArrayId = R.array.towns_lianjiang;
                        break;
                    case 22: // 台灣
                        city = "taiwan";
                        townArrayId = R.array.taiwan;
                        break;
                    default:
                        city = "taiwan";
                        townArrayId = R.array.taiwan; // 預設台灣
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

        spinnerTown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                // 獲取選擇的城鎮名稱
                town = parentView.getItemAtPosition(position).toString(); // 存儲城鎮名稱
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 預設處理
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

    public interface RequestQuestionCallback {
        void onRequestQuestionCompleted();  // 當處理完成後調用
    }

    public void RequestQuestion(String city, String town, RequestQuestionCallback callback) {
        ApiHelper.fetchCoordinates(this, city, town,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // 遍歷整個 JSONArray
                            if (response.length() == 0) {
                                Log.d("DEBUG", "No data found in the response.");
                                return;  // 如果資料為空，則不繼續處理
                            }

                            // 如果有資料，開始處理
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject data = response.getJSONObject(i);
                                // 解析每一筆資料
                                String returnCity = data.getString("city");
                                String returnTown = data.getString("town");
                                double latitude = data.getDouble("latitude");
                                double longitude = data.getDouble("longitude");

                                // 進行必要的處理
                                latitude = Math.round(latitude * 10000.0) / 10000.0;
                                longitude = Math.round(longitude * 10000.0) / 10000.0;

                                // 保存解析出來的資料
                                qCities[i] = returnCity;
                                qTowns[i] = returnTown;
                                qLatitudes[i] = latitude;
                                qLongitudes[i] = longitude;

                                // 輸出資料
                                Log.d("City Info", "City: " + returnCity + ", Town: " + returnTown);
                            }

                            // 延遲隱藏 ProgressBar，確保動畫完成後再隱藏
                            progressBar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    callback.onRequestQuestionCompleted(); // 呼叫完成的回調
                                }
                            }, 0); // 延遲500ms，根據需要調整延遲時間

                        } catch (Exception e) {
                            Toast.makeText(Home.this,
                                    "Parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.d("DEBUG", "Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.d("DEBUG", "Error: " + error.getMessage());
                        progressBar.setVisibility(View.GONE); // 如果請求失敗也隱藏進度條
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the background music when the activity is no longer in the foreground
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Pause music
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the background music if it was paused
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // Start the music again
        }
        // Reset progress when returning to Home
        progressStatus = 0;
        progressBar.setProgress(0);
        percentageText.setText("0%");
        progressBar.setVisibility(View.GONE);
        percentageText.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed to free resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        // Release the click sound MediaPlayer
        if (clickSound != null) {
            clickSound.release();
        }
    }
}