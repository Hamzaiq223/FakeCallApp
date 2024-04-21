package com.mai.fake.prank.call.audio.call.app.recorder.Activities.AudioCall

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityAudioCallBinding

import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class AudioCall : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var isTimerRunning = false
    private var startTimeInMillis: Long = 0
    private var timer: CountDownTimer? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var binding: ActivityAudioCallBinding
    private var isAudioPlaying = false
    private var receivedString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_call)

        receivedString = intent.getStringExtra("characterName")

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        playAudioWithTimer(receivedString)
    }

    private fun playAudioWithTimer(receivedString: String?) {
        val folderName = "Ronaldo"
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        var lastPlayedAudioIndex = sharedPreferences.getInt("lastPlayedAudioIndex", -1)

        val folderRef = receivedString?.let { storageRef.child(it) }

        folderRef!!.listAll()
            .addOnSuccessListener { listResult ->
                val audioRefs = listResult.items as ArrayList<StorageReference>
                if (audioRefs.isNotEmpty() && !isAudioPlaying) {
                    val nextAudioIndex = (lastPlayedAudioIndex + 1) % audioRefs.size
                    val nextAudioRef = audioRefs[nextAudioIndex]

                    nextAudioRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            val audioUrl = uri.toString()
                            if (mediaPlayer == null) {
                                mediaPlayer = MediaPlayer()
                            }
                            try {
                                mediaPlayer!!.reset()
                                mediaPlayer!!.setDataSource(audioUrl)
                                mediaPlayer!!.prepare()
                                mediaPlayer!!.start()
                                startTimeInMillis = System.currentTimeMillis()
                                startTimer()
                                val editor = sharedPreferences.edit()
                                editor.putInt("lastPlayedAudioIndex", nextAudioIndex)
                                editor.apply()
                                isAudioPlaying = true
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        .addOnFailureListener {
                            // Handle error
                        }
                }
            }
            .addOnFailureListener {
                // Handle error
            }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMillis
                updateTimerDisplay(elapsedTimeInMillis)
            }

            override fun onFinish() {}
        }
        timer!!.start()
        isTimerRunning = true
    }

    private fun updateTimerDisplay(elapsedTimeInMillis: Long) {
        val minutes = (elapsedTimeInMillis / 1000 / 60).toInt()
        val seconds = (elapsedTimeInMillis / 1000 % 60).toInt()
        val timeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.tvTimer.text = timeString
    }

    override fun onResume() {
        super.onResume()
        playAudioWithTimer(receivedString)
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        if (timer != null) {
            timer!!.cancel()
            isTimerRunning = false
        }
        isAudioPlaying = false
    }
}
