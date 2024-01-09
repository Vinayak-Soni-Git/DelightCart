package com.example.delightcart.Activities.SignInActivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.delightcart.Dialog.setupBottomSheetDialog
import com.example.delightcart.Home.Home
import com.example.delightcart.Models.User
import com.example.delightcart.R
import com.example.delightcart.Activities.SignUpActivities.SignUpUserDetailsActivity
import com.example.delightcart.Util.RegisterValidation
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.LoginViewModel
import com.example.delightcart.ViewModel.RegisterViewModel
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var googleSignInButton: SignInButton
    private val registerViewModel by viewModels<RegisterViewModel>()

    private val RC_SIGN_IN: Int = 1009
    private val TAG = "firebaseAuth"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_in)

        val rootView: View = findViewById(android.R.id.content)
        val createNewUserButton: CircularProgressButton = findViewById(R.id.signInNewUserButton)
        val forgetPasswordButton: Button = findViewById(R.id.singInForgetPasswordButton)
        val signInButton: CircularProgressButton = findViewById(R.id.signInSingInButton)
        googleSignInButton = findViewById(R.id.googleSingInButton)

        emailEditText = findViewById(R.id.singInEmail)
        passwordEditText = findViewById(R.id.singInPassword)
        
        
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            viewModel.login(email, password)

        }
        forgetPasswordButton.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.resetPassword(email)
            }
        }
        createNewUserButton.setOnClickListener {
            val intent = Intent(this, SignUpUserDetailsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        
        observeLogin(signInButton)
        observeResetPassword(rootView)
        observeValidation()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
        Log.d("firebaseAuth", auth.toString())


        googleSignInButton.setOnClickListener{
            signIn()
        }

        
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main) {
                if (viewModel.shouldSaveUserData(firebaseUser?.email!!)){
                    val user = User(
                        firebaseUser.displayName!!,
                        "",
                        firebaseUser.email!!,
                        "+91",
                        firebaseUser.photoUrl.toString() )
                    registerViewModel.saveUserInfo(firebaseUser.uid, user)
                    val intent = Intent(this@SignInActivity, Home::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@SignInActivity, Home::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun observeValidation() {
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        emailEditText.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        passwordEditText.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }

    private fun observeResetPassword(rootView: View) {
        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        Snackbar.make(
                            rootView,
                            "Reset link was sent to your email",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Error -> {
                        Snackbar.make(rootView, "Error ${it.message}", Snackbar.LENGTH_LONG).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun observeLogin(signInButton: CircularProgressButton) {
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        signInButton.startAnimation()
                    }

                    is Resource.Success -> {
                        signInButton.revertAnimation()
                        Intent(this@SignInActivity, Home::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(this@SignInActivity, it.message, Toast.LENGTH_LONG).show()
                        signInButton.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}