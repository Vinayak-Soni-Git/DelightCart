package com.example.delightcart.Activities.SignUpActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.delightcart.R

class SignUpPhoneDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_phone_details)
        val signUpBackButton:ImageView = findViewById(R.id.signUpBackButton)
        signUpBackButton.setOnClickListener{
            val intent = Intent(this, SignUpUserAdditionalDetailsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    fun callNextSignUpScreen(view: View) {
        val intent = Intent(this, SignUpOTPActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}