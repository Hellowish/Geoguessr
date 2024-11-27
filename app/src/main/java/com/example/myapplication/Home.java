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
        setContentView(R.layout.game_home);  // 加載布局文件

        // 初始化 ImageButton
        ImageButton playButton = findViewById(R.id.imageButton5);

        // 設置 ImageButton 的點擊事件處理
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 點擊後啟動 MainPlay Activity
                Intent intent = new Intent(Home.this, MainPlay.class);
                startActivity(intent);  // 啟動 MainPlay Activity
            }
        });
    }
}

