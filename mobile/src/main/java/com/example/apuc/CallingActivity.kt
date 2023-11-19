package com.example.apuc

import android.content.Intent
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class CallingActivity: AppCompatActivity() {
    lateinit var music: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calling)

        music = MediaPlayer.create(this, R.raw.apuc)
        music.start()
        var button: Button = findViewById(R.id.button3)
        button.setOnClickListener {
            music.stop()
            finishAffinity() }
    }
}
