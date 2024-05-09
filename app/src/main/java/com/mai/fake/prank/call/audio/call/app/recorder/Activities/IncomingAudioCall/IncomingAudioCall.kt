package com.mai.fake.prank.call.audio.call.app.recorder.Activities.IncomingAudioCall

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.AudioCall.AudioCall
import com.mai.fake.prank.call.audio.call.app.recorder.Common.BlurBuilder
import com.mai.fake.prank.call.audio.call.app.recorder.Common.CharacterImageHelper
import com.mai.fake.prank.call.audio.call.app.recorder.Common.FlashlightController
import com.mai.fake.prank.call.audio.call.app.recorder.Common.SharedHelper
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityIncomingCallBinding


class IncomingAudioCall : AppCompatActivity() {

    private lateinit var binding: ActivityIncomingCallBinding
    private var receivedString: String? = null
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator
    private val handler = Handler()
    private var isVibrating = false
    var flash : Boolean = false
    private val buttonHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call)

        var volume : Boolean
        var vibration : Boolean


        volume = SharedHelper.getBoolean(this,"volume_off",false)
        vibration = SharedHelper.getBoolean(this,"vibration_off",false)
        flash = SharedHelper.getBoolean(this,"flash_off",false)

        receivedString = intent.getStringExtra("characterName")

        val context: Context = applicationContext // or any valid context
        val characterDrawable: Int? = CharacterImageHelper.getCharacterImageResourceId(context, receivedString!!)

        val originalBitmap: Bitmap =
            characterDrawable?.let { BitmapFactory.decodeResource(resources, it) }!!
        val blurredBitmap = BlurBuilder.blur(this, originalBitmap)

        binding.ivImage.setImageBitmap(blurredBitmap)
        binding.ivUser.setImageBitmap(originalBitmap)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

        binding.btnReturn.setOnClickListener{
            finish()
        }

        binding.ivAttendCall.setOnClickListener{
            val intent = Intent(this@IncomingAudioCall, AudioCall::class.java)
            intent.putExtra("characterName", receivedString)
            startActivity(intent)
            remove()
            showButton()
        }

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


        if(!vibration){
            // Start the vibration scheduler
            handler.post(vibrationRunnable)
        }


        binding.ivEndCall.setOnClickListener{
            finish()
        }
    }

    private fun showButton(){
        val delayMillis = 1500 // Delay in milliseconds (2 seconds in this example)
        buttonHandler.postDelayed({
            // Code block to be executed after the delay
            binding.layoutAttendCall.visibility = View.GONE
            binding.layoutEndCall.visibility = View.GONE
            binding.btnReturn.visibility = View.VISIBLE
            binding.textView3.setText("Call Ended")
        }, delayMillis.toLong())
    }

    private fun startVibration() {
        try {
            vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.apply {
                // Vibrate indefinitely until the ringtone ends
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrate(VibrationEffect.createWaveform(longArrayOf(0, 500), 0))
                }
            }

            if(!flash){
                val flashlightController = FlashlightController(this)
                // To turn on the flashlight
                flashlightController.turnOnFlash()
                // To turn off the flashlight
                flashlightController.turnOffFlash()

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