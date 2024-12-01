package com.example.myapplication;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        EditText usernameEditText = findViewById(R.id.et_username);
        EditText passwordEditText = findViewById(R.id.et_password);
        Button loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // 簡單驗證（實際開發中應結合伺服器驗證）
                if ("".equals(username) && "".equals(password)) {
                    // 登入成功，跳到主頁
                    Intent intent = new Intent(MainActivity2.this, Home.class);
                    startActivity(intent);
                    finish(); // 關閉當前Activity
                } else {
                    // 登入失敗
                    Toast.makeText(MainActivity2.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

