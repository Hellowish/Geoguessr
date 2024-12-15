package com.example.myapplication;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.view.View;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ranking extends AppCompatActivity {

    private MediaPlayer clickSound; // 用於播放點擊音效
    private RecyclerView recyclerView; // 用來顯示遊戲歷史紀錄的 RecyclerView

    private MediaPlayer backgroundMusic; // For background music

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // Initialize and play background music
        backgroundMusic = MediaPlayer.create(this, R.raw.rank);
        backgroundMusic.setLooping(true); // Loop the music
        backgroundMusic.start();

        // 添加淡入效果
        View view = findViewById(android.R.id.content);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(500);
        fadeIn.start();

        // 初始化點擊音效
        clickSound = MediaPlayer.create(this, R.raw.click); // 確保將點擊音效檔案放在 res/raw
        if (clickSound == null) {
            Log.e("Ranking", "Failed to initialize click sound"); // 如果音效初始化失敗，輸出錯誤日誌
        }

        // 初始化 return_button
        ImageButton returnButton = findViewById(R.id.return_button);

        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        returnButton.startAnimation(pulseAnimation);

        returnButton.setOnClickListener(v -> {
            // 播放點擊音效
            playClickSound();

            // 啟動 MainPlay Activity
            Intent intent = new Intent(Ranking.this, Home.class);
            startActivity(intent);

            // 應用按鈕的縮放動畫效果
            animateButtonClick(v);

            overridePendingTransition(R.anim.record_out, R.anim.record_in);
        });

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.game_history_recycler_view);

        // 設置 RecyclerView 的 LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 設置初始資料
        getInitialGameHistory();

        // 設置 WindowInsets Listener 以處理邊緣間距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 播放點擊聲音
    private void playClickSound() {
        if (clickSound != null) {
            Log.d("Ranking", "Playing click sound");
            clickSound.start(); // 播放音效
        } else {
            Log.e("Ranking", "clickSound is null");  // 如果 MediaPlayer 沒有正確初始化，輸出錯誤日誌
        }
    }

    // 按鈕動畫效果
    private void animateButtonClick(View view) {
        // 放大按鈕（X 和 Y 軸）
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f); // X 軸縮放
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f); // Y 軸縮放
        scaleUpX.setDuration(100); // 放大時的持續時間
        scaleUpY.setDuration(100); // 放大時的持續時間
            scaleUpX.start();
        scaleUpY.start();

        // 放大後再恢復到原始大小
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f); // X 軸恢復
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1f); // Y 軸恢復
        scaleDownX.setDuration(100); // 恢復時的持續時間
        scaleDownY.setDuration(100); // 恢復時的持續時間
        scaleDownX.start();
        scaleDownY.start();
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
        if (clickSound != null) {
            clickSound.release();
            Log.d("Ranking", "clickSound released");
        }
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
            Log.d("Ranking", "backgroundMusic released");
        }
    }

    // 返回資料
    private void getInitialGameHistory() {
        new Thread(() -> {
            List<GameHistory> gameHistoryList = new ArrayList<>();
            try {
                ApiHelper.readData(
                        this,
                        response -> {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    int id = data.getInt("id");
                                    int score = data.getInt("score");
                                    String city = data.getString("city");
                                    String town = data.getString("town");
                                    String created_at = data.getString("created_at");

                                    Log.d("READ_DATA", "id: " + id + ", 分數: " + score + ", 城市: " + city);
                                    gameHistoryList.add(new GameHistory(
                                            id,  // ID
                                            created_at,  // 時間
                                            String.valueOf(score),  // 分數
                                            city,  // 城市
                                            town   // 區域
                                    ));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            // 使用 Handler 在 UI 執行緒中更新 UI
                            new Handler(Looper.getMainLooper()).post(() -> {
                                GameHistoryAdapter adapter = new GameHistoryAdapter(gameHistoryList);
                                recyclerView.setAdapter(adapter);
                            });

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


    // GameHistory 內部類別
    public static class GameHistory {
        private final String id;
        private final String time;
        private final String score;
        private final String city;
        private final String area;

        public GameHistory(int id, String time, String score, String city, String area) {
            this.id = String.valueOf(id);
            this.time = time;
            this.score = score;
            this.city = city;
            this.area = area;
        }

        public String getId() {
            return id;
        }

        public String getTime() {
            return time;
        }

        public String getScore() {
            return score;
        }

        public String getCity() {
            return city;
        }

        public String getArea() {
            return area;
        }
    }

    // GameHistoryAdapter 內部類別
    public static class GameHistoryAdapter extends RecyclerView.Adapter<GameHistoryAdapter.ViewHolder> {

        private final List<GameHistory> gameHistoryList;

        public GameHistoryAdapter(List<GameHistory> gameHistoryList) {
            this.gameHistoryList = gameHistoryList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            GameHistory gameHistory = gameHistoryList.get(position);
            holder.idTextView.setText(gameHistory.getId());
            holder.timeTextView.setText(gameHistory.getTime());
            holder.scoreTextView.setText(gameHistory.getScore());
            holder.cityTextView.setText(gameHistory.getCity());
            holder.areaTextView.setText(gameHistory.getArea());
        }

        @Override
        public int getItemCount() {
            return gameHistoryList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView idTextView, timeTextView, scoreTextView, cityTextView, areaTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                idTextView = itemView.findViewById(R.id.id_text);
                timeTextView = itemView.findViewById(R.id.time_text);
                scoreTextView = itemView.findViewById(R.id.score_text);
                cityTextView = itemView.findViewById(R.id.city_text);
                areaTextView = itemView.findViewById(R.id.area_text);
            }
        }
    }
}
