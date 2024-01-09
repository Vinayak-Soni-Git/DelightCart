package com.example.delightcart.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.delightcart.Activities.ProductsAdder.ProductAdder
import com.example.delightcart.Activities.SignInActivities.AdminSignInActivity
import com.example.delightcart.R

class AdminPortal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_portal)

        val productAdderIntent = Intent(this@AdminPortal, ProductAdder::class.java)
        val openProductAdderButton: Button = findViewById(R.id.openProductAdderButton)
        openProductAdderButton.setOnClickListener {
            startActivity(productAdderIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this@AdminPortal, AdminSignInActivity::class.java)
        startActivity(intent)
    }
}