package com.mai.fake.prank.call.audio.call.app.recorder.Activities.Splash

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.Language.Languages
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.MainActivity
import com.mai.fake.prank.call.audio.call.app.recorder.Common.SharedHelper
import com.mai.fake.prank.call.audio.call.app.recorder.R

class Splash : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 3000

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

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
