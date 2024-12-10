package com.example.myapplication

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize MediaPlayer with a music file
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        // Start background animation
        val backgroundLayout = findViewById<ConstraintLayout>(R.id.main)
        val background = backgroundLayout.background
        if (background is AnimationDrawable) {
            background.setEnterFadeDuration(500)
            background.setExitFadeDuration(500)
            background.start()
        }

        // Start button animation
        val button = findViewById<ImageButton>(R.id.button)
        val animation = AnimationUtils.loadAnimation(this, R.anim.button_animation)
        button.startAnimation(animation)

        // Button click listener to navigate to Home activity
        button.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(backgroundLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
