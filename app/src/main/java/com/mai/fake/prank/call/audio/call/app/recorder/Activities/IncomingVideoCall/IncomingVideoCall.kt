package com.mai.fake.prank.call.audio.call.app.recorder.Activities.IncomingVideoCall


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.VideoCall.VideoCalling
import com.mai.fake.prank.call.audio.call.app.recorder.Common.BlurBuilder
import com.mai.fake.prank.call.audio.call.app.recorder.Common.CharacterImageHelper
import com.mai.fake.prank.call.audio.call.app.recorder.Common.FlashlightController
import com.mai.fake.prank.call.audio.call.app.recorder.Common.SharedHelper
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.ncorti.slidetoact.SlideToActView


class IncomingVideoCall : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var ivUser: ImageView
    private lateinit var ivBlurBackground: ImageView
    private lateinit var tvName: TextView
    private lateinit var btnSlide: SlideToActView
    private lateinit var layoutMessage: LinearLayout
    private lateinit var btnReturn: TextView
    private lateinit var layoutReminder: LinearLayout
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator
    private val handler = Handler()
    private var isVibrating = false
    var flash: Boolean = false
    private val buttonHandler = Handler()
    private var receivedString: String? = null
    private lateinit var tvCallStatus : TextView
    private lateinit var flashlightController: FlashlightController
    private val flashHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_video_call)

        var volume: Boolean
        var vibration: Boolean

        volume = SharedHelper.getBoolean(this, "volume_off", false)
        vibration = SharedHelper.getBoolean(this, "vibration_off", false)
        flash = SharedHelper.getBoolean(this, "flash_off", false)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }


        tvName = findViewById(R.id.tvName)
        ivUser = findViewById(R.id.ivUser)
        ivBlurBackground = findViewById(R.id.ivImage)
        layoutMessage = findViewById(R.id.layoutMessage)
        layoutReminder = findViewById(R.id.layoutReminder)
        btnSlide = findViewById(R.id.btnSlide)
        btnReturn = findViewById(R.id.btnReturn)
        tvCallStatus = findViewById(R.id.textView3)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        receivedString = intent.getStringExtra("characterName")

        tvName.text = receivedString

        val context: Context = applicationContext // or any valid context
        val characterDrawable: Int? = CharacterImageHelper.getCharacterImageResourceId(context, receivedString!!)

        val originalBitmap: Bitmap =
            characterDrawable?.let { BitmapFactory.decodeResource(resources, it) }!!
        val blurredBitmap = BlurBuilder.blur(this, originalBitmap)

        btnSlide.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                val intent = Intent(this@IncomingVideoCall, VideoCalling::class.java)
                intent.putExtra("characterName", receivedString)
                startActivity(intent)
                remove()
                showButton()
            }
        }


        btnReturn.setOnClickListener{
            finish()
        }

        ivBlurBackground.setImageBitmap(blurredBitmap)
        ivUser.setImageBitmap(originalBitmap)

        // Initialize the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.iphone_ringtone)

        if(!volume){
            // Start playing the ringtone
            mediaPlayer?.let {
                it.start()
                it.setOnCompletionListener { mp ->
                    // Stop the vibration when the ringtone ends
                    stopVibration()
                    handler.removeCallbacks(vibrationRunnable)

                }
            }
        }

        if (!vibration) {
            // Start the vibration scheduler
            handler.post(vibrationRunnable)
        }

        if (!flash) {
            flashlightController = FlashlightController(this)
            // Start the flashlight scheduler
            flashHandler.post(flashlightRunnable)
        }

    }

    private val flashlightRunnable: Runnable = object : Runnable {
        override fun run() {
            if(!flash){
                flashlightController.turnOnFlash()
                // To turn off the flashlight
                flashlightController.turnOffFlash()
            }
            // Schedule the next flashlight toggle after 1 second
            flashHandler.postDelayed(this, 1000)
        }
    }

    private fun showButton(){
        val delayMillis = 1500 // Delay in milliseconds (2 seconds in this example)
        buttonHandler.postDelayed({
            // Code block to be executed after the delay
            btnSlide.visibility = View.GONE
            layoutMessage.visibility = View.GONE
            layoutReminder.visibility = View.GONE
            btnReturn.visibility = View.VISIBLE
            tvCallStatus.setText("Call Ended")
        }, delayMillis.toLong())
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
                // To turn on the flashlight
            } else {
                startVibration()
                // To turn off the flashlight

            }
            // Toggle the vibration state
            isVibrating = !isVibrating
            // Schedule the next vibration after 1 second
            handler.postDelayed(this, 1000)
        }
    }

    private fun remove(){
        handler.removeCallbacksAndMessages(null) // Cancel all scheduled callbacks
        stopVibration()
        flashHandler.removeCallbacksAndMessages(null) // Cancel all flashlight scheduled callbacks

        // Release the MediaPlayer and stop the vibration when the activity is destroyed
        mediaPlayer?.let {
            it.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        remove()
        buttonHandler.removeCallbacksAndMessages(null)
    }
}
