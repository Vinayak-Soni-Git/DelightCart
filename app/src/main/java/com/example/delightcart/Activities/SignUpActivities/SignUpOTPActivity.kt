package com.example.delightcart.Activities.SignUpActivities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.example.delightcart.R

class SignUpOTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otplogin)
        
//        val pinView:PinView = findViewById(R.id.pinView)
//        pinView.requestFocus()
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(pinView, InputMethodManager.SHOW_IMPLICIT)
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE )

        val otpBox1:EditText = findViewById(R.id.otpBox1)
        val otpBox2:EditText = findViewById(R.id.otpBox2)
        otpBox1.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                otpBox2.requestFocus()
            }
            true
        }
        val signUpBackButton: ImageView = findViewById(R.id.signUpBackButton)
        signUpBackButton.setOnClickListener{
            val intent = Intent(this, SignUpPhoneDetailsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        
        
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}