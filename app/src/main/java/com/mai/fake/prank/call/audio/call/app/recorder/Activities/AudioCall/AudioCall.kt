package com.mai.fake.prank.call.audio.call.app.recorder.Activities.AudioCall

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mai.fake.prank.call.audio.call.app.recorder.R

class AudioCall : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var audioUrl: String
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_call)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        mediaPlayer = MediaPlayer()

        playAudio()
    }

    private fun playAudio() {
        val folderName = "Ronaldo"

        val folderRef = storageRef.child(folderName)

        folderRef.listAll().addOnSuccessListener { listResult ->
            val audioRefs = listResult.items.filter { it.name.endsWith(".mp3") || it.name.endsWith(".mpeg") } // Filter only audio files

            if (audioRefs.isNotEmpty()) {
                val audioRef = audioRefs[0] // Get the first audio file

                audioRef.downloadUrl.addOnSuccessListener { uri ->
                    audioUrl = uri.toString()

                    try {
                        mediaPlayer.apply {
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build()
                            )
                            setDataSource(applicationContext, Uri.parse(audioUrl))
                            prepare()
                            start()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }.addOnFailureListener {
                    // Handle error
                }
            }
        }.addOnFailureListener {
            // Handle error
        }
    }

    override fun onStop() {
        super.onStop()
        // Stop and release media player when activity stops
        mediaPlayer.apply {
            stop()
            release()
        }
    }
}