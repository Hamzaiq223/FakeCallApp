package com.mai.fake.prank.call.audio.call.app.recorder.Activities.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.Language.Languages
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.MainActivity
import com.mai.fake.prank.call.audio.call.app.recorder.Common.SharedHelper
import com.mai.fake.prank.call.audio.call.app.recorder.R

class Splash : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val language = SharedHelper.getString(this, "language", "")

        Handler(Looper.getMainLooper()).postDelayed({
            if (language != null && language != "") {
                Log.d("Language", language)
                val languages = Languages()
                languages.changeLanguage(this ,language)

            } else {
                startActivity(Intent(this@Splash, MainActivity::class.java))
                finish() // Finish the splash activity so the user can't go back to it
            }
        }, SPLASH_DELAY)
    }
}
