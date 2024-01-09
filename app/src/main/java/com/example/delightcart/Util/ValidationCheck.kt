package com.example.delightcart.Util

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty()) {
        return RegisterValidation.Failed("Email cannot be empty")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong Email Format")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Password cannot be empty")

    if (password.length < 6) {
        return RegisterValidation.Failed("Password should contains 6 char")
    }
    return RegisterValidation.Success
}

fun validateFirstName(firstName: String): RegisterValidation {
    if (firstName.isEmpty())
        return RegisterValidation.Failed("First Name cannot be empty")

    return RegisterValidation.Success
}

fun validateLastName(lastName: String): RegisterValidation {
    if (lastName.isEmpty())
        return RegisterValidation.Failed("Last Name cannot be empty")

    return RegisterValidation.Success
}

fun validatePhone(phone: String): RegisterValidation {
    if (phone.isEmpty())
        return RegisterValidation.Failed("Phone cannot be empty")

    return RegisterValidation.Success
}