package com.example.myapplication;
import static android.app.PendingIntent.getActivity;

import android.graphics.Color;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import okhttp3.Callback;
import okhttp3.Call;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONObject;

public class MainPlay extends AppCompatActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    public static float sum=0;
    private static LatLng answerCord;
    private static LatLng streetViewCoordinate;

    public static double maxDistance;

    public static String[] city = new String[3];
    public static String[] town = new String[3];

    double[] qLatitudes = new double[3];
    double[] qLongitudes = new double[3];

    int currentQuestion;

    private Marker currentMarker;
    private Marker answerMarker;
    private Polyline polyline;
    private GoogleMap mapIns;

    private static StreetViewPanorama streetViewIns;
    private SupportMapFragment mapFragment;
    private ChatGPTClient client;

    private TextView timerText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000; // 30 seconds in milliseconds

    private ImageButton expandButton;
    private boolean isMapFragmentVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_play);

        client = new ChatGPTClient(this);
        currentQuestion = 0;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve qlatLng from Intent
        maxDistance = getIntent().getDoubleExtra("maxDistance", 200);
        city = new String[]{getIntent().getStringExtra("city")};
        town = new String[]{getIntent().getStringExtra("town")};
        qLatitudes = getIntent().getDoubleArrayExtra("qLatitudes");
        qLongitudes = getIntent().getDoubleArrayExtra("qLongitudes");
        // Log.d("qLatitudesInfo", "City: " + qLatitudes[0]);

        streetViewCoordinate = new LatLng(qLatitudes[currentQuestion], qLongitudes[currentQuestion]);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        SupportStreetViewPanoramaFragment streetViewFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetview_fragment);
        if (streetViewFragment != null)
            streetViewFragment.getStreetViewPanoramaAsync(this);

        // Initialize timer TextView
        timerText = findViewById(R.id.timer_text);

        // Initialize expand button
        expandButton = findViewById(R.id.expand);
        expandButton.setOnClickListener(v -> toggleFragmentVisibility());

        // Start the countdown timer
        startTimer();

        // Set up the hint button
        findViewById(R.id.hint).setOnClickListener(view -> showHintPopup());
        getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
        // Set up the answer button
        findViewById(R.id.Answer).setOnClickListener(v -> showScoreFragment());
    }

    private void showScoreFragment() {
        // Find buttons and timer and hide them
        ImageButton answerButton = findViewById(R.id.Answer);
        ImageButton hintButton = findViewById(R.id.hint);
        ImageButton expandButton = findViewById(R.id.expand);
        TextView timerText = findViewById(R.id.timer_text);
        ImageView box= findViewById(R.id.box);

        if (answerButton != null) {
            answerButton.setVisibility(View.GONE);
        }
        if (hintButton != null) {
            hintButton.setVisibility(View.GONE);
        }
        if (expandButton != null) {
            expandButton.setVisibility(View.GONE);
        }
        if (timerText != null) {
            timerText.setVisibility(View.GONE);
        }
        if (box != null) {
            box.setVisibility(View.GONE);
        }

        // Create a new instance of the ScoreFragment and pass necessary arguments
        ScoreFragment scoreFragment = ScoreFragment.newInstance(calculateScore());

        // Begin the fragment transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Apply the animation
        transaction.setCustomAnimations(
                R.anim.fragment_slide_in,  // Fragment enter animation
                R.anim.fragment_slide_out // Fragment exit animation
        );

        // 在點擊位置新增 Marker
        BitmapDescriptor blueMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        answerMarker = mapIns.addMarker(new MarkerOptions()
                .position(streetViewCoordinate) // Marker 的位置
                .title("") // Marker 的標題
                .snippet("")
                .icon(blueMarker));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currentMarker.getPosition());
        builder.include(answerMarker.getPosition());
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mapIns.moveCamera(cameraUpdate);

        // 绘制线
        drawLineBetweenMarkers();

        // Replace the container with the score fragment
        transaction.replace(R.id.fragment_container, scoreFragment);
        transaction.addToBackStack(null); // Optional: Add to back stack
        transaction.commit();
    }

    private void toggleFragmentVisibility() {
        if (mapFragment != null) {
            if (isMapFragmentVisible) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mapFragment)
                        .commit();
                expandButton.setImageResource(R.drawable.map2); // Update icon
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mapFragment)
                        .commit();
                expandButton.setImageResource(R.drawable.map2); // Update icon
            }
            isMapFragmentVisible = !isMapFragmentVisible;
        }
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                resetQuestion();
            }
        }.start();
    }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        timerText.setText(String.valueOf(seconds));
    }

    private void switchToFailurePage() {
        Intent intent = new Intent(MainPlay.this, FailureActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }
    private void navigateToScoreActivity() {
        double score = calculateScore(); // Calculate the score
        Intent intent = new Intent(MainPlay.this, Score.class);
        intent.putExtra("score", score); // Pass score to ScoreActivity
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        mapIns = map;
        mapIns.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapIns.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.975667, 120.973861), 8f));
        mapIns.getUiSettings().setRotateGesturesEnabled(false);

        mapIns.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                answerCord = latLng;
                // 在點擊位置新增 Marker
                currentMarker = mapIns.addMarker(new MarkerOptions()
                        .position(latLng) // Marker 的位置
                        .title("位置") // Marker 的標題
                        .snippet("經緯度: " + latLng.latitude + ", " + latLng.longitude)); // 顯示詳細信息
            }
        });
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewIns = streetViewPanorama;

        streetViewPanorama.setPanningGesturesEnabled(true);
        streetViewPanorama.setUserNavigationEnabled(true);
        streetViewPanorama.setZoomGesturesEnabled(true);
        streetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder()
                        .orientation(new StreetViewPanoramaOrientation(20, 20))
                        .zoom(streetViewPanorama.getPanoramaCamera().zoom)
                        .build(), 2000
        );

        streetViewIns.setPosition(streetViewCoordinate);
    }

    String quetionResult = null;
    private void showHintPopup() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Warning, will deduct points.")
                .setPositiveButton("Yes", (dialog, which) -> showHintDetailsPopup())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showHintDetailsPopup() {
        // 输入问题
        String question = "請根據" + city + town +
                "，提供其文化、地理或歷史相關的線索，但不要直接說出縣市或行政區的名稱，讓人通過線索猜出是什麼地方，不要太多字。";

        // 调用 ChatGPT 接口
        client.sendMessage(question, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    // 解析返回结果
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    // 获取消息内容
                    String message = jsonResponse
                            .getAsJsonArray("choices")
                            .get(0).getAsJsonObject()
                            .get("message").getAsJsonObject()
                            .get("content").getAsString();

                    // 确保在主线程中更新 UI
                    runOnUiThread(() -> {
                        // 确保提示内容不为空
                        if (message != null && !message.isEmpty()) {
                            showHint(message);
                        } else {
                            showError("No hint available.");
                        }
                    });
                } else {
                    // 失败时处理
                    String errorMessage = "Request failed with status: " + response.code();
                    runOnUiThread(() -> showError(errorMessage));
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                String errorMessage = "Request failed: " + e.getMessage();
                runOnUiThread(() -> showError(errorMessage));
            }
        });
    }

    private void showHint(String message) {
        new AlertDialog.Builder(MainPlay.this)
                .setTitle("Hint")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showError(String errorMessage) {
        Toast.makeText(MainPlay.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void drawLineBetweenMarkers() {
        if (answerMarker != null && currentMarker != null) {
            LatLng point1 = answerMarker.getPosition();
            LatLng point2 = currentMarker.getPosition();

            PatternItem dash = new Dash(30);  // 30 pixels for the dash length
            PatternItem gap = new Gap(20);    // 20 pixels for the gap between dashes
            java.util.List<PatternItem> pattern = Arrays.asList(dash, gap);

            // Create PolylineOptions with the dashed pattern
            PolylineOptions options = new PolylineOptions()
                    .add(point1, point2)
                    .width(10)
                    .color(Color.BLACK)
                    .pattern(pattern); // 你可以改变线的宽度和颜色

            // 添加 Polyline 到地图
            polyline = mapIns.addPolyline(options);
        }
    }

    public void resetQuestion() {
        // Inside your logic where currentQuestion is checked
        if (currentQuestion >= 2) {
            // Move to FailureActivity
            Intent intent = new Intent(MainPlay.this, FailureActivity.class);
            intent.putExtra("sum", sum); // Pass the sum value to FailureActivity
            startActivity(intent);
            finish(); // Optional: Close the current activity
        }


        // Find buttons and timer and hide them
        ImageButton answerButton = findViewById(R.id.Answer);
        ImageButton hintButton = findViewById(R.id.hint);
        ImageButton expandButton = findViewById(R.id.expand);
        TextView timerText = findViewById(R.id.timer_text);
        ImageView box= findViewById(R.id.box);

        if (answerButton != null) {
            answerButton.setVisibility(View.VISIBLE);
        }
        if (hintButton != null) {
            hintButton.setVisibility(View.VISIBLE);
        }
        if (expandButton != null) {
            expandButton.setVisibility(View.VISIBLE);
        }
        if (timerText != null) {
            timerText.setVisibility(View.VISIBLE);
        }
        if (box != null) {
            box.setVisibility(View.VISIBLE);
        }

        if (polyline != null) {
            polyline.remove();
        }

        if (answerMarker != null) {
            answerMarker.remove();
        }

        if(currentQuestion < 3)
            currentQuestion++;

        streetViewCoordinate = new LatLng(qLatitudes[currentQuestion], qLongitudes[currentQuestion]);
        streetViewIns.setPosition(streetViewCoordinate);
        timeLeftInMillis = 30000;
        startTimer();
    }

    // 計算兩個經緯度點之間的距離（單位：公里）
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        // 轉換為弧度
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // 哈弗辛公式
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 地球半徑（公里）
        double R = 6371.0;
        double distance = R * c;
        return distance;
    }

    public static double calculateScore() {
        // 計算距離
        double distance = haversine(streetViewCoordinate.latitude, streetViewCoordinate.longitude,
                answerCord.latitude, answerCord.longitude);
        // 計算分數
        sum += Math.max(0, 100 - (distance / maxDistance) * 100);
        return Math.max(0, 100 - (distance / maxDistance) * 100);
    }
}