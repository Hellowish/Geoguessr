package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize MediaPlayer with a music file
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music) // Place music in res/raw folder

        mediaPlayer.isLooping = true // Loop the music
        mediaPlayer.start() // Start playing music

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up button click listener to open MainActivity2
        findViewById<ImageButton>(R.id.button).setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause() // Pause music when activity goes to background
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start() // Resume music when activity comes to foreground
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Release resources when activity is destroyed
    }
}
