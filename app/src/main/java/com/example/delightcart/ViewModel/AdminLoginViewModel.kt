package com.example.delightcart.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delightcart.Util.AdminLoginFieldState
import com.example.delightcart.Util.RegisterValidation
import com.example.delightcart.Util.Resource
import com.example.delightcart.Util.validateFirstName
import com.example.delightcart.Util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AdminLoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _adminLogin = MutableSharedFlow<Resource<String>>()
    val adminLogin = _adminLogin.asSharedFlow()

    private val _validation = Channel<AdminLoginFieldState>()
    val validation = _validation.receiveAsFlow()


    fun adminLogin(firstName: String, password: String) {
        if (checkValidation(firstName, password)) {
            Log.d("adminLogin", "we are in")
            viewModelScope.launch { _adminLogin.emit(Resource.Loading()) }
            firestore.collection("admin").whereEqualTo("firstName", firstName)
                .whereEqualTo("password", password).get()
                .addOnCompleteListener {
                    if (!it.result.isEmpty) {
                        viewModelScope.launch {
                            _adminLogin.emit(Resource.Success(it.toString()))
                        }
                    } else {
                        viewModelScope.launch {
                            _adminLogin.emit(Resource.Error(it.toString()))
                        }
                    }
                }
        } else {
            Log.d("adminLogin", "we are not in")
            val loginFieldState = AdminLoginFieldState(
                validateFirstName(firstName), validatePassword(password)
            )
            runBlocking {
                _validation.send(loginFieldState)
            }
        }
    }

    private fun checkValidation(firstName: String, password: String): Boolean {
        val firstNameValidation = validateFirstName(firstName)
        val passwordValidation = validatePassword(password)

        return (firstNameValidation is RegisterValidation.Success
                && passwordValidation is RegisterValidation.Success)
    }
}