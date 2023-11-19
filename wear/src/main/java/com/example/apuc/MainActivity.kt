package com.example.apuc

import android.icu.util.TimeUnit
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.google.android.gms.wearable.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks


private const val VOICE_TRANSCRIPTION_CAPABILITY_NAME = "voice_transcription"
const val MESSAGE_PATH = "/apuc_message"

class MainActivity : WearableActivity(), GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {

    //private lateinit var gestureDetector: GestureDetector
    //private lateinit var ambientController: AmbientController
    private lateinit var client: GoogleApiClient
    private lateinit var gestureText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAmbientEnabled()
        Log.v("test","Ambient Enabled.")

        Thread{
            setupVoiceTranscription()
        }.start()

        gestureText = TextView(this)

        client = GoogleApiClient.Builder(this).addApi(Wearable.API).build()
        client.connect()
        Log.v("test","Client Connected.")

        val button = findViewById<View>(R.id.button)
        button.setOnClickListener {
            Log.v("test","Click Detected.")
            val dataMap = DataMap()
            val hi = ByteArray(0)
            val request = PutDataMapRequest.create("/apuc_message").apply {
                dataMap.putString("message", "message from the Watch")
                setUrgent()
                Log.v("test", "request Applied.")
            }.asPutDataRequest()
            val dataApi = Wearable.DataApi
            dataApi.putDataItem(client, request).setResultCallback { result ->
                if (!result.status.isSuccess) {
                    Log.v("test","message send failed")
                }else{
                    Log.v("test","message send successfully")
                }
            }
            requestTranscription(hi)
        }

        /*
        val detector = GestureDetectorCompat(this, object: GestureDetector.SimpleOnGestureListener()
        {
            override fun onDoubleTap(e: MotionEvent): Boolean
            {
                sendDataToPhone()
                return true
            }
        })

        detector.setOnDoubleTapListener(this)
        */
    }

   /* private fun sendDataToPhone()
    {
        val dataMap = PutDataMapRequest().apply {
            putString("message", "from Watch")
        }.asPutDataRequest()

        val dataItem = PutDataMapRequest.create("/data").setUrgent().asPutDataRequest().setData(dataMap.toByteArray())

        if (client.isConnected) {
            Wearable.getDataClient(this).putDataItem(dataItem)
        }
    }*/

    private fun setupVoiceTranscription() {
        val capabilityInfo: CapabilityInfo = Tasks.await(
            Wearable.getCapabilityClient(this)
                .getCapability(
                    VOICE_TRANSCRIPTION_CAPABILITY_NAME,
                    CapabilityClient.FILTER_REACHABLE
                )
        )
        // capabilityInfo has the reachable nodes with the transcription capability
        updateTranscriptionCapability(capabilityInfo)
        Log.v("reqT", "setup")
    }
    private var transcriptionNodeId: String? = null

    private fun updateTranscriptionCapability(capabilityInfo: CapabilityInfo) {
        transcriptionNodeId = pickBestNodeId(capabilityInfo.nodes)
        Log.v("reqT", "update")
    }

    private fun pickBestNodeId(nodes: Set<Node>): String? {
        // Find a nearby node or pick one arbitrarily
        Log.v("reqT", "pickBestNodeId")
        return nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id
    }

    private fun requestTranscription(voiceData: ByteArray) {
        transcriptionNodeId?.also { nodeId ->
            val sendTask: Task<*> = Wearable.getMessageClient(this).sendMessage(
                nodeId,
                MESSAGE_PATH,
                voiceData
            ).apply {
                addOnSuccessListener { Log.v("reqT", "success") }
                addOnFailureListener { Log.v("reqT", "failed") }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        client.disconnect()
    }

    override fun onDown(e: MotionEvent): Boolean {
        gestureText.setText("onDown")
        TODO("Not yet implemented")
    }

    override fun onShowPress(e: MotionEvent) {
        gestureText.setText("onShowPress")
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        gestureText.setText("onSingleTapUp")
        TODO("Not yet implemented")
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        gestureText.setText("onScroll")
        TODO("Not yet implemented")
    }

    override fun onLongPress(e: MotionEvent) {
        gestureText.setText("onLongPress")
        TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        gestureText.setText("onFling")
        TODO("Not yet implemented")
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        gestureText.setText("onSingleTapConfirmed")
        TODO("Not yet implemented")
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        gestureText.setText("onDoubleTap")
        TODO("Not yet implemented")
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        gestureText.setText("onDoubleTapEvent")
        TODO("Not yet implemented")
    }
}
