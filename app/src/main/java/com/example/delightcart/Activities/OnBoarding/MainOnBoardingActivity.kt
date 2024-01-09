package com.example.delightcart.Activities.OnBoarding

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.delightcart.R
import com.example.delightcart.Activities.SignUpActivities.SignUpUserDetailsActivity
import com.example.delightcart.Adapters.ViewPagerAdapter

class MainOnBoardingActivity : AppCompatActivity() {
    private lateinit var slideViewPager:ViewPager
    private lateinit var dotLayout:LinearLayout
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var dots:Array<TextView?>
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_on_boarding)
        
        slideViewPager = findViewById(R.id.onboardingSlideViewPager)
        dotLayout = findViewById(R.id.onboardingIndicatorLayout)
        
        val(backButton, nextButton, skipButton) = listOf<Button>(
            findViewById(R.id.onboardingBackButton),
            findViewById(R.id.onboardingNextButton),
            findViewById(R.id.onboardingSkipButton)
        )
        
        viewPagerAdapter = ViewPagerAdapter(this)
        slideViewPager.adapter = viewPagerAdapter

        val viewListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Implement onPageScrolled if needed
            }

            override fun onPageSelected(position: Int) {
                setUpIndicator(position)

                if (position > 0) {
                    backButton.visibility = View.VISIBLE
                } else {
                    backButton.visibility = View.INVISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                
            }
        }
        
        backButton.setOnClickListener { 
            if (getItem(0) > 0){
                slideViewPager.setCurrentItem(getItem(-1), true)
            }
        }
        nextButton.setOnClickListener { 
            if (getItem(0) < 3){
                slideViewPager.setCurrentItem(getItem(1), true)
            } else {
                val intent = Intent(this, SignUpUserDetailsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()
            }
        }
        setUpIndicator(0)
        slideViewPager.addOnPageChangeListener(viewListener)
        
        skipButton.setOnClickListener {
            val intent = Intent(this, SignUpUserDetailsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
    }
    private fun getItem(i:Int):Int{
        return slideViewPager.currentItem + i
    }
    private fun setUpIndicator(position:Int){
        dots = arrayOfNulls(4)
        dotLayout.removeAllViews()
        
        for (i in dots.indices){
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226")
            dots[i]?.textSize = 35F
            dots[i]?.setTextColor(resources.getColor(com.google.android.material.R.color.material_grey_300, applicationContext.theme))
            dotLayout.addView(dots[i])
        }
        dots[position]?.setTextColor(resources.getColor(R.color.colorPrimary, applicationContext.theme))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}