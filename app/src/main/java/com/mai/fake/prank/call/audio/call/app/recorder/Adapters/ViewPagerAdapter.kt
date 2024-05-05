package com.mai.fake.prank.call.audio.call.app.recorder.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Model.WelcomeItem
import com.mai.fake.prank.call.audio.call.app.recorder.R

class ViewPagerAdapter(private val context: Context, private val items: List<WelcomeItem>) : PagerAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.pager_item, container, false)

        val ivBackground = view.findViewById<ImageView>(R.id.ivBackground)
        val ivMobile = view.findViewById<ImageView>(R.id.ivMainImage)
        val ivText = view.findViewById<ImageView>(R.id.ivText)

        ivMobile.setImageResource(items[position].imageResId)
        ivBackground.setImageResource(items[position].backgroundPicture)
        ivText.setImageResource(items[position].textImage)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
