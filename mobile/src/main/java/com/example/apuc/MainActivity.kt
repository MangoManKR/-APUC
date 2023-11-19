package com.example.apuc

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.support.wearable.activity.WearableActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.MessageApi
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.ExecutionException

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener {

    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var listenGestureList: MutableMap<String, Int> // map을 사용해야할 것 같음.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button:Button = findViewById(R.id.button)
        button.setOnClickListener{
            startActivity(Intent(this, CallActivity::class.java))
            finish()
        }

        val filter = IntentFilter()
        filter.addAction("com.google.android.gms.wearable.MESSAGE_RECEIVED")
        filter.addAction("com.google.android.gms.wearable.DATA_CHANGED")

        // Create a GoogleApiClient to connect to Google Play Services
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(Wearable.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        Log.v("test","client build.")

        var intent = Intent(this, MyWearableListenerService()::class.java)
        startService(intent)

    }

    override fun onStart() {
        super.onStart()
        // Connect to Google Play Services
        googleApiClient.connect()
        Log.v("test","client connected.")
    }

    override fun onStop() {
        super.onStop()
        // Disconnect from Google Play Services
        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
            Log.v("test","client disconnected")
        }
    }

    override fun onConnected(p0: Bundle?) {
        // Listen for incoming messages from the wearable device
        Wearable.MessageApi.addListener(googleApiClient, messageListener)
        Log.v("test","addListener")
    }

    override fun onConnectionSuspended(p0: Int) {
        // Handle connection suspension
        Log.v("test","connection Suspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        // Handle connection failure
        Log.v("test","connection failed")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("oNI","onNewIntent()")
        finish()
    }

    // Listener for incoming messages from the wearable device
    private val messageListener =
        MessageApi.MessageListener { messageEvent ->
            if (messageEvent.path == "/apuc") {
                val data = messageEvent.data
                Log.v("test","message accepted!")
            }
        }

    override fun onMessageReceived(p0: MessageEvent) {
        Log.v("onMessageReceived","message received")
    }
}

class MyWearableListenerService : WearableListenerService() {
    override fun onCreate() {
        Log.v("mWLS", "service start!")
        super.onCreate()
    }
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/apuc_message") {
            Log.v("test", "message received by listener")
            val message = String(messageEvent.data)
            Log.d("MyWearableListener", "Received message: $message")
            val showIntent = Intent(this, CallActivity::class.java)
            showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(showIntent)
        } else {
            super.onMessageReceived(messageEvent)
        }
    }
}

