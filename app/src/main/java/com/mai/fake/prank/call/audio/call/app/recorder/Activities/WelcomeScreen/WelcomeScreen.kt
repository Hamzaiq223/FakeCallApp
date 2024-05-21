package com.mai.fake.prank.call.audio.call.app.recorder.Activities.WelcomeScreen

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.AudioCall.AudioCall
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.Splash.Splash
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.ViewPagerAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Model.WelcomeItem
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator


class WelcomeScreen : AppCompatActivity() {

    private lateinit var viewPager : ViewPager
    private lateinit var warmDotIndicator : WormDotsIndicator
    private lateinit var tvNext : TextView
    private val items = listOf(
        WelcomeItem(R.drawable.text_one, R.drawable.icon_mobile_one,R.drawable.first_screen_bg),
        WelcomeItem(R.drawable.text_two,R.drawable.icon_mobile_two, R.drawable.second_screen_bg)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        viewPager = findViewById(R.id.view_pager)
        warmDotIndicator = findViewById(R.id.worm_dots_indicator)
        tvNext = findViewById(R.id.tvNext)

        var count = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

        val adapter = ViewPagerAdapter(this, items)
        viewPager.adapter = adapter
        warmDotIndicator.attachTo(viewPager)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == items.size - 1) {
                    tvNext.setText(getString(R.string.next))
                } else {
                    tvNext.setText(getString(R.string.start))
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


        tvNext.setOnClickListener{
             if(count == 0) {
                 tvNext.setText(getString(R.string.next))
                 viewPager.setCurrentItem(1)
                 count++
             }else{
                 val intent = Intent(this@WelcomeScreen, Splash::class.java)
                 intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                 startActivity(intent)
                 finish() // Finish the WelcomeScreen activity
             }
        }

    }
}