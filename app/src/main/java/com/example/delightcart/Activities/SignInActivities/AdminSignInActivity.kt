package com.example.delightcart.Activities.SignInActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.delightcart.Admin.AdminPortal
import com.example.delightcart.Home.Home
import com.example.delightcart.R
import com.example.delightcart.Util.RegisterValidation
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.AdminLoginViewModel
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AdminSignInActivity : AppCompatActivity() {
    private val viewModel by viewModels<AdminLoginViewModel>()
    private lateinit var nextButton: CircularProgressButton

    private lateinit var adminFirstName: EditText
    private lateinit var adminPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sign_in)

        adminFirstName = findViewById(R.id.signInAdminFirstName)
        adminPassword = findViewById(R.id.signInAdminPassword)
        nextButton = findViewById(R.id.signInAdminNextButton)

        nextButton.setOnClickListener {
            Toast.makeText(this@AdminSignInActivity, "clicked", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch {
                viewModel.adminLogin(adminFirstName.text.toString().trim(), adminPassword.text.toString())
            }
        }
        
        observeValidation()
        observeLogin()
    }

    private fun observeLogin() {
        lifecycleScope.launchWhenStarted {
            viewModel.adminLogin.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        nextButton.startAnimation()
                    }

                    is Resource.Success -> {
                        nextButton.revertAnimation()
                        Intent(this@AdminSignInActivity, AdminPortal::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(this@AdminSignInActivity, it.message, Toast.LENGTH_LONG).show()
                        nextButton.revertAnimation()
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
                        adminFirstName.apply {
                            requestFocus()
                            error = validation.firstName.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        adminPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
    
}