package com.example.delightcart

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.delightcart.Activities.OnBoarding.MainOnBoardingActivity
import com.example.delightcart.Activities.SignInActivities.SignInActivity
import com.example.delightcart.Home.Home
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var howManyTimesHaveBeenRun: Int = 0
    private val NUMBER_OF_TIMES_RUN_KEY: String = "NUMBER_OF_TIMES_RUN_KEY"
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val currentUser = firebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkUser()


        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#034A6A"))
        actionBar?.setBackgroundDrawable(colorDrawable)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu_bar, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun checkUser() {
        if (currentUser != null) {
            val intent = Intent(this@MainActivity, Home::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        val runDefaultValue = 0
        howManyTimesHaveBeenRun = sharedPreferences.getInt(NUMBER_OF_TIMES_RUN_KEY, runDefaultValue)

        if (howManyTimesHaveBeenRun == 0) {
            val intent = Intent(this, MainOnBoardingActivity::class.java)
            startActivity(intent)
        }
        howManyTimesHaveBeenRun += 1

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(NUMBER_OF_TIMES_RUN_KEY, howManyTimesHaveBeenRun)
        editor.apply()
    }
}