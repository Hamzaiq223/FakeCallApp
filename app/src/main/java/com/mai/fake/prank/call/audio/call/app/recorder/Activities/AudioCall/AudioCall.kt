package com.mai.fake.prank.call.audio.call.app.recorder.Activities.AudioCall

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityAudioCallBinding
import java.io.IOException

class AudioCall : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioRefs: List<StorageReference>
    private var currentAudioIndex = 0
    private var timer: CountDownTimer? = null

    private lateinit var audioCallBinding: ActivityAudioCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioCallBinding = DataBindingUtil.setContentView(this, R.layout.activity_audio_call)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        mediaPlayer = MediaPlayer()

        // Fetch audio files from Firebase Storage
        fetchAudioFiles()
    }

    private fun fetchAudioFiles() {
        val folderName = "Ronaldo"
        val folderRef = storageRef.child(folderName)

        folderRef.listAll().addOnSuccessListener { listResult ->
            audioRefs = listResult.items.filter { it.name.endsWith(".mp3") || it.name.endsWith(".mpeg") } // Filter only audio files

            // Start playing the first audio
            if (audioRefs.isNotEmpty()) {
                playAudio(audioRefs[currentAudioIndex])
            }
        }.addOnFailureListener {
            // Handle error
        }
    }

    private fun playAudio(audioRef: StorageReference) {
        audioRef.downloadUrl.addOnSuccessListener { uri ->
            try {
                mediaPlayer.apply {
                    reset() // Reset media player before playing new audio

                    // Check if the URI is valid
                    if (uri != null) {
                        setDataSource(applicationContext, uri)
                        prepare()

                        // Set audio attributes after preparing the media player
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )

                        start()

                        // Set onCompletionListener to play the next audio when current audio finishes
                        setOnCompletionListener {
                            // Increment current audio index for the next audio
                            currentAudioIndex = (currentAudioIndex + 1) % audioRefs.size
                            if (currentAudioIndex < audioRefs.size) {
                                playAudio(audioRefs[currentAudioIndex])
                            }
                        }
                    } else {
                        // Handle null URI
                        // Log an error or show a message to the user
                    }
                }

                // Start timer
                startTimer(mediaPlayer.duration.toLong())

            } catch (e: IOException) {
                e.printStackTrace()
                // Handle IO exception
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                // Handle illegal state exception
            }
        }.addOnFailureListener {
            // Handle error
        }
    }



    override fun onResume() {
        super.onResume()
        fetchAudioFiles()
    }

    private fun startTimer(duration: Long) {
        timer = object : CountDownTimer(duration, 1000) {
            var elapsedTime = 0L

            override fun onTick(millisUntilFinished: Long) {
                // Update TextView with timer text
                elapsedTime += 1000
                val totalSeconds = elapsedTime / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                audioCallBinding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Do nothing when the timer finishes
            }
        }
        timer?.start()
    }

    override fun onStop() {
        super.onStop()
        // Stop and release media player when activity stops
        mediaPlayer.apply {
            if (isPlaying) {
                stop()
            }
            reset()
            release()
        }
        // Cancel timer to prevent memory leaks
        timer?.cancel()
    }
}
