package com.mai.fake.prank.call.audio.call.app.recorder.Activities.IncomingVideoCall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mai.fake.prank.call.audio.call.app.recorder.R

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build

import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.ImageView

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mai.fake.prank.call.audio.call.app.recorder.Common.BlurBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class IncomingVideoCall : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var imageView: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator
    private val handler = Handler()
    private var isVibrating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_video_call)

        imageView = findViewById(R.id.ivImage)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        displayImageFromFolder("Ronaldo")

        // Initialize the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.iphone_ringtone)

        // Start playing the ringtone
        mediaPlayer?.let {
            it.start()
            it.setOnCompletionListener { mp ->
                // Stop the vibration when the ringtone ends
                stopVibration()
                handler.removeCallbacks(vibrationRunnable)
            }
        }

        // Start the vibration scheduler
        handler.post(vibrationRunnable)
    }

    private fun displayImageFromFolder(folderName: String) {
        val folderRef = storageRef.child(folderName)

        folderRef.listAll()
            .addOnSuccessListener { listResult ->
                // Get the list of items (images) in the folder
                val imageRefs = listResult.items

                // Check if there are images in the folder
                if (imageRefs.isNotEmpty()) {
                    // Get the reference of the first image in the list
                    val firstImageRef = imageRefs[0] // You can change this logic to select any other image

                    firstImageRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()

                            // Load the image from URL using Picasso (or any other library you prefer)
                            Picasso.get().load(imageUrl).into(object : Target {
                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                    // Apply blur effect to the bitmap
                                    val blurredBitmap = BlurBuilder.blur(this@IncomingVideoCall, bitmap!!)
                                    imageView.setImageBitmap(blurredBitmap)
                                }

                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                                    // Handle failure
                                }

                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                    // Handle loading
                                }
                            })
                        }
                        .addOnFailureListener { e ->
                            // Handle error
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    // Method to start vibrating the device
    private fun startVibration() {
        try {
            vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.apply {
                // Vibrate indefinitely until the ringtone ends
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrate(VibrationEffect.createWaveform(longArrayOf(0, 500), 0))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Method to stop vibrating the device
    private fun stopVibration() {
        try {
            vibrator.cancel() // Cancel the vibration
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val vibrationRunnable: Runnable = object : Runnable {
        override fun run() {
            if (isVibrating) {
                stopVibration()
            } else {
                startVibration()
            }
            // Toggle the vibration state
            isVibrating = !isVibrating
            // Schedule the next vibration after 1 second
            handler.postDelayed(this, 1000)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer and stop the vibration when the activity is destroyed
        mediaPlayer?.let {
            it.release()
        }
        stopVibration()
    }
}
