package com.example.myapplication;

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
                // 在這裡處理點擊事件，例如啟動某個新的 Activity 或播放音樂
                // 例如可以添加一個跳轉到其他頁面的代碼
                // Intent intent = new Intent(Home.this, SomeOtherActivity.class);
                // startActivity(intent);
            }
        });
    }
}
