package com.example.delightcart.Activities.SignUpActivities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.delightcart.MainActivity
import com.example.delightcart.Models.User
import com.example.delightcart.R
import com.example.delightcart.Activities.SignInActivities.AdminSignInActivity
import com.example.delightcart.Activities.SignInActivities.SignInActivity
import com.example.delightcart.Util.RegisterValidation
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.RegisterViewModel
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class SignUpUserDetailsActivity : AppCompatActivity() {
    private lateinit var signUpBackButton: ImageView
    private lateinit var nextButton: CircularProgressButton
    private lateinit var loginButton: CircularProgressButton
    private lateinit var signupAsAdminButton: CircularProgressButton

    private lateinit var userFirstName: EditText
    private lateinit var userLastName: EditText
    private lateinit var userEmail: EditText
    private lateinit var userPhone: EditText
    private lateinit var userPassword: EditText

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_user_details)

        signUpBackButton = findViewById(R.id.signUpBackButton)
        nextButton = findViewById(R.id.signupNextButton)
        loginButton = findViewById(R.id.signupLoginButton)
        signupAsAdminButton = findViewById(R.id.signupAsAdminButton)
        
        userFirstName = findViewById(R.id.signUpUserFirstName)
        userLastName = findViewById(R.id.signUpUserLastName)
        userEmail = findViewById(R.id.signUpUserEmail)
        userPhone = findViewById(R.id.signUpUserPhone)
        userPassword = findViewById(R.id.signUpUserPassword)

        nextButton.setOnClickListener {
            val user = User(
                userFirstName.text.toString().trim(),
                userLastName.text.toString().trim(),
                userEmail.text.toString().trim(),
                userPhone.text.toString().trim()
            )
            val password = userPassword.text.toString()
            viewModel.createAccountWithEmailAndPassword(user, password)
        }


        observeRegister()
        observeValidation()

        signUpBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        loginButton.setOnClickListener {
            val intent = Intent(this@SignUpUserDetailsActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        signupAsAdminButton.setOnClickListener {
            val intent = Intent(this@SignUpUserDetailsActivity, AdminSignInActivity::class.java)
            startActivity(intent)
        }


    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
        // Convert drawable resource to Bitmap
        val drawable = context.resources.getDrawable(drawableId, null)
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // Use Canvas to draw the drawable onto the bitmap
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun observeRegister() {
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        nextButton.startAnimation()
                    }

                    is Resource.Success -> {
                        nextButton.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                        nextButton.doneLoadingAnimation(
                            resources.getColor(R.color.colorPrimary),
                            drawableToBitmap(
                                this@SignUpUserDetailsActivity,
                                R.drawable.baseline_check_circle_24
                            )
                        )
                        Toast.makeText(
                            this@SignUpUserDetailsActivity,
                            "Account Created Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            nextButton.setBackgroundColor(resources.getColor(R.color.white))
                            nextButton.revertAnimation()
                        }, 2000)
                    }

                    is Resource.Error -> {
                        nextButton.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                        nextButton.doneLoadingAnimation(
                            resources.getColor(R.color.colorPrimary),
                            drawableToBitmap(
                                this@SignUpUserDetailsActivity,
                                R.drawable.cross_circle_svgrepo_com
                            )
                        )
                        Toast.makeText(
                            this@SignUpUserDetailsActivity,
                            "An error occurred while creating account",
                            Toast.LENGTH_SHORT
                        ).show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            nextButton.setBackgroundColor(resources.getColor(R.color.white))
                            nextButton.revertAnimation()
                        }, 2000)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun observeValidation() {
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->

                if (validation.firstName is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        userFirstName.apply {
                            requestFocus()
                            error = validation.firstName.message
                        }
                    }
                }

                if (validation.lastName is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        userLastName.apply {
                            requestFocus()
                            error = validation.lastName.message
                        }
                    }
                }

                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        userEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if (validation.phone is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        userPhone.apply {
                            requestFocus()
                            error = validation.phone.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        userPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}