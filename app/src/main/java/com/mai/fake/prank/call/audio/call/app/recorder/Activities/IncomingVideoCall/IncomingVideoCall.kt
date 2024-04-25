package com.mai.fake.prank.call.audio.call.app.recorder.Activities.IncomingVideoCall


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mai.fake.prank.call.audio.call.app.recorder.Common.BlurBuilder
import com.mai.fake.prank.call.audio.call.app.recorder.Common.CharacterImageHelper
import com.mai.fake.prank.call.audio.call.app.recorder.R



class IncomingVideoCall : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var ivUser: ImageView
    private lateinit var ivBlurBackground: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator
    private val handler = Handler()
    private var isVibrating = false
    private var receivedString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_video_call)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

        ivUser = findViewById(R.id.ivUser)
        ivBlurBackground = findViewById(R.id.ivImage)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        receivedString = intent.getStringExtra("characterName")

        val context: Context = applicationContext // or any valid context
        val characterDrawable: Int? = CharacterImageHelper.getCharacterImageResourceId(context, receivedString!!)

        val originalBitmap: Bitmap =
            characterDrawable?.let { BitmapFactory.decodeResource(resources, it) }!!
        val blurredBitmap = BlurBuilder.blur(this, originalBitmap)

        ivBlurBackground.setImageBitmap(blurredBitmap)
        ivUser.setImageBitmap(originalBitmap)

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
        stopVibration()
        // Release the MediaPlayer and stop the vibration when the activity is destroyed
        mediaPlayer?.let {
            it.release()
        }

    }
}
