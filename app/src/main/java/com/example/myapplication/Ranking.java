package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.animation.ObjectAnimator;
import android.util.Log;  // 导入 Log 类，用于调试
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageButton;
import android.view.View;

public class Ranking extends AppCompatActivity {

    private MediaPlayer clickSound; // 用于播放点击音效

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // 初始化点击音效
        clickSound = MediaPlayer.create(this, R.raw.click); // 确保将点击声音文件放在 res/raw
        if (clickSound == null) {
            Log.e("Ranking", "Failed to initialize click sound"); // 如果音效初始化失败，输出错误日志
        }

        // 初始化 return_button
        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> {
            // 播放点击音效
            playClickSound();

            // 启动 MainPlay Activity
            Intent intent = new Intent(Ranking.this, Home.class);
            startActivity(intent);

            // 应用按钮的缩放动画效果
            animateButtonClick(v);
        });

        // 设置 WindowInsets Listener 以处理边缘间距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 播放点击声音
    private void playClickSound() {
        if (clickSound != null) {
            //Log.d("Ranking", "Playing click sound");
            clickSound.start(); // 播放声音
        //} else {
            //Log.e("Ranking", "clickSound is null");  // 如果 MediaPlayer 没有正确初始化，输出错误日志
        }
    }

    // 按钮动画效果
    private void animateButtonClick(View view) {
        // 放大按钮（X 和 Y 轴）
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f); // X 轴缩放
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f); // Y 轴缩放
        scaleUpX.setDuration(100); // 放大时的持续时间
        scaleUpY.setDuration(100); // 放大时的持续时间
        scaleUpX.start();
        scaleUpY.start();

        // 放大后再恢复到原始大小
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f); // X 轴恢复
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1f); // Y 轴恢复
        scaleDownX.setDuration(100); // 恢复时的持续时间
        scaleDownY.setDuration(100); // 恢复时的持续时间
        scaleDownX.start();
        scaleDownY.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放资源
        if (clickSound != null) {
            clickSound.release();
            //Log.d("Ranking", "clickSound released");
        }
    }
}
