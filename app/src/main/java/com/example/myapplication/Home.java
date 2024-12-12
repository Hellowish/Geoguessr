package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.Objects;

public class Home extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaPlayer clickSound; // 用于播放点击音效

    private String city = "taiwan";
    private String town;

    public String returnCity;
    public String returnTown;

    private LatLng qlatLng;


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

            // Apply button scale animation
            animateButtonClick(v);

            RequestQuestion(city, town, new RequestQuestionCallback() {
                @Override
                public void onRequestQuestionCompleted() {
                    // 當 RequestQuestion 執行完成後再啟動 MainPlay Activity
                    // Log.d("DEBUG", "qlatLng: " + qlatLng);
                    Intent intent = new Intent(Home.this, MainPlay.class);
                    intent.putExtra("qlatLng", qlatLng);  // Pass data via Intent

                    if(Objects.equals(city, "taiwan"))
                        intent.putExtra("maxDistance", 200);
                    else
                        intent.putExtra("maxDistance", 20);

                    intent.putExtra("city", returnCity);
                    intent.putExtra("town", returnTown);

                    startActivity(intent);
                }
            });
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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // 解析城市、城鎮、緯度和經度
                            returnCity = response.getString("city");
                            returnTown = response.getString("town");
                            double latitude = response.getDouble("latitude");
                            double longitude = response.getDouble("longitude");

                            // 進行必要的處理，比如四捨五入
                            latitude = Math.round(latitude * 1000.0) / 1000.0;
                            longitude = Math.round(longitude * 1000.0) / 1000.0;

                            qlatLng = new LatLng(latitude, longitude);

                            // 可以將城市和城鎮資訊傳遞給其他部分的代碼
                            Log.d("City Info", "City: " + returnCity + ", Town: " + returnTown);

                            callback.onRequestQuestionCompleted();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed to free resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            //mediaPlayer = null; // Make sure it's null to avoid memory leaks
        }

        // Release the click sound MediaPlayer
        if (clickSound != null) {
            clickSound.release();
        }
    }
}