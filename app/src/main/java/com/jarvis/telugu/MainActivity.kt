
package com.jarvis.telugu

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private lateinit var status: TextView
    private lateinit var tts: TextToSpeech

    private val micPermission =
        android.Manifest.permission.RECORD_AUDIO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(32, 32, 32, 32)
        layout.setBackgroundColor(Color.rgb(10, 20, 40))

        val title = TextView(this)
        title.text = "JARVIS TELUGU"
        title.textSize = 30f
        title.setTextColor(Color.CYAN)
        title.gravity = Gravity.CENTER

        status = TextView(this)
        status.text = "Hello! I am Jarvis."
        status.textSize = 20f
        status.setTextColor(Color.WHITE)
        status.gravity = Gravity.CENTER
        status.setPadding(10, 40, 10, 40)

        val button = Button(this)
        button.text = "TALK TO JARVIS"

        button.setOnClickListener {
            startListening()
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(button)

        setContentView(layout)
    }

    private fun startListening() {
        if (checkSelfPermission(micPermission) !=
            PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(micPermission), 101)
            return
        }

        try {
            val intent = Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
            )

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "te-IN"
            )

            intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Speak to Jarvis"
            )

            startActivityForResult(intent, 102)

        } catch (e: Exception) {
            status.text = "Speech recognition unavailable"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )

        if (requestCode == 101 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startListening()
        } else {
            status.text = "Microphone permission required"
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 102 && resultCode == RESULT_OK) {
            val results = data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )

            val spokenText = results?.firstOrNull() ?: ""

            status.text = "You: $spokenText"

            if (spokenText.isNotBlank()) {
                val reply =
                    "Hello! I heard you. You said $spokenText"

                status.text =
                    "You: $spokenText\n\nJarvis: $reply"

                speak(reply)
            }
        }
    }

    private fun speak(message: String) {
        tts.speak(
            message,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "jarvis_reply"
        )
    }

    override fun onInit(statusCode: Int) {
        if (statusCode == TextToSpeech.SUCCESS) {
            tts.language = Locale("en", "IN")
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
