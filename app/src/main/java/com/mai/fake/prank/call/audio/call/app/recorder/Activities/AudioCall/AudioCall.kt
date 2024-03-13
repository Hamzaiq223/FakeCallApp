package com.mai.fake.prank.call.audio.call.app.recorder.Activities.AudioCall

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.mai.fake.prank.call.audio.call.app.recorder.R

class AudioCall : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler: Handler
    private lateinit var audioFiles: MutableList<String>
    private var currentAudioIndex: Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    private var isPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_call)

        handler = Handler()

        audioFiles = mutableListOf()

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Start playing audio
        playAudio()
    }

    override fun onResume() {
        super.onResume()
        if (isPaused) {
            isPaused = false
            playAudio()
        }
    }

    private fun playAudio() {
        mediaPlayer = MediaPlayer()
        val folderName = "Ronaldo"
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child(folderName)

        val lastPlayedAudioIndex = sharedPreferences.getInt("lastPlayedAudioIndex", -1)

        storageRef.listAll().addOnSuccessListener { listResult ->
            audioFiles.addAll(listResult.items.map { it.name })

            if (audioFiles.isNotEmpty()) {
                currentAudioIndex = if (lastPlayedAudioIndex != -1 && lastPlayedAudioIndex < audioFiles.size) {
                    lastPlayedAudioIndex
                } else {
                    0
                }

                val nextAudioRef = storageRef.child(audioFiles[currentAudioIndex])

                nextAudioRef.downloadUrl.addOnSuccessListener { uri ->
                    mediaPlayer.apply {
                        if (isPlaying) {
                            stop()
                            reset()
                        }
                        setDataSource(this@AudioCall, Uri.parse(uri.toString()))
                        prepare()
                        start()
                    }

                    // Start timer
                    handler.postDelayed(updateTimer, 1000)

                    // Set on completion listener
                    mediaPlayer.setOnCompletionListener {
                        // Move to the next audio file
                        currentAudioIndex = (currentAudioIndex + 1) % audioFiles.size
                        val editor = sharedPreferences.edit()
                        editor.putInt("lastPlayedAudioIndex", currentAudioIndex)
                        editor.apply()
                        playAudio()
                    }
                }.addOnFailureListener {
                    // Handle error
                }
            }
        }.addOnFailureListener {
            // Handle error
        }
    }


    private val updateTimer = object : Runnable {
        override fun run() {
            // Update your timer display here
            handler.postDelayed(this, 1000)
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimer)
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPaused = true
        }
    }
}
