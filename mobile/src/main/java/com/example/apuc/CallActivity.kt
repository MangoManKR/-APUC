package com.example.apuc

import android.content.Intent
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CallActivity: AppCompatActivity() {
    lateinit var music: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call)

        val soundPool = SoundPool.Builder().build()
        val soundId = soundPool.load(this, R.raw.galaxybells_gcs0c452,1)
        soundPool.setOnLoadCompleteListener { soundPool, soundId, status ->
            soundPool.play(soundId, 1.0f, 1.0f, 0, -1, 1.0f)
        }

        var button1: Button = findViewById(R.id.button2)
        var button2: Button = findViewById(R.id.button4)
        button1.setOnClickListener{
            soundPool.stop(soundId)
            startActivity(Intent(this, CallingActivity::class.java))
            finish()
        }
        button2.setOnClickListener{
            soundPool.stop(soundId)
            finishAffinity()
        }
    }
}