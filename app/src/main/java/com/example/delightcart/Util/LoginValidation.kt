package com.example.delightcart.Util

sealed class LoginValidation() {
    object Success : LoginValidation()
    data class Failed(val message: String) : LoginValidation()
}

data class LoginFieldState(
    val email: RegisterValidation,
    val password: RegisterValidation
)

sealed class AdminLoginValidation() {
    object Success : AdminLoginValidation()
    data class Failed(val message: String) : AdminLoginValidation()
}

data class AdminLoginFieldState(
    val firstName: RegisterValidation,
    val password: RegisterValidation
)