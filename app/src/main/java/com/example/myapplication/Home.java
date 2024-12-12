package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private String city = "taiwan";
    private String town;

    public String returnCity;
    public String returnTown;

    private LatLng qlatLng;

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
            // 呼叫 RequestQuestion 並傳遞回調
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