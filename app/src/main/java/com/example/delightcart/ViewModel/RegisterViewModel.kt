package com.example.delightcart.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.delightcart.Models.User
import com.example.delightcart.Util.Constants.USERS_COLLECTION
import com.example.delightcart.Util.RegisterFieldState
import com.example.delightcart.Util.RegisterValidation
import com.example.delightcart.Util.Resource
import com.example.delightcart.Util.validateEmail
import com.example.delightcart.Util.validateFirstName
import com.example.delightcart.Util.validateLastName
import com.example.delightcart.Util.validatePassword
import com.example.delightcart.Util.validatePhone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore

) : ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        val firstName = ""
        val lastName = ""
        val phone = ""
        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid, user)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val registerFieldState = RegisterFieldState(
                validateFirstName(firstName),
                validateLastName(lastName),
                validateEmail(user.email),
                validatePhone(phone),
                validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldState)
            }
        }

    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)

        return (emailValidation is RegisterValidation.Success
                && passwordValidation is RegisterValidation.Success)
    }

    fun saveUserInfo(userUid: String, user: User) {
        db.collection(USERS_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
                Log.d("saveuser", it.toString())
                Log.d("saveuser", user.toString())
                Log.d("saveuser", "user saved")
            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
                Log.d("saveuser", it.message.toString())
            }
    }
}