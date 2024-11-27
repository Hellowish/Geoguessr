package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_home); // 加載布局文件

        // 初始化 ImageButton5（Play 按鈕）
        ImageButton playButton = findViewById(R.id.imageButton5);
        playButton.setOnClickListener(v -> {
            // 啟動 MainPlay Activity
            Intent intent = new Intent(Home.this, MainPlay.class);
            startActivity(intent);
        });

        // 初始化 ImageButton2（Ranking 按鈕）
        ImageButton rankingButton = findViewById(R.id.imageButton2);
        rankingButton.setOnClickListener(v -> {
            // 啟動 Ranking Activity
            Intent intent = new Intent(Home.this, Ranking.class);
            startActivity(intent);
        });
    }
}


