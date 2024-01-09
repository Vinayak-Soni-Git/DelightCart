package com.example.delightcart.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.delightcart.Data.OnboardingViewPagerData
import com.example.delightcart.R

class ViewPagerAdapter(private val context: Context) : PagerAdapter() {
    override fun getCount(): Int {
        return OnboardingViewPagerData().headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slider_layout, container, false)
        val slideTitleImage: ImageView = view.findViewById(R.id.slideTitleImage)
        val slideHeading: TextView = view.findViewById(R.id.slideTextTitle)
        val slideDescription: TextView = view.findViewById(R.id.slideTextDescription)

        slideTitleImage.setImageResource(OnboardingViewPagerData().images[position])
        slideHeading.text = OnboardingViewPagerData().headings[position]
        slideDescription.text = OnboardingViewPagerData().descriptions[position]

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

}