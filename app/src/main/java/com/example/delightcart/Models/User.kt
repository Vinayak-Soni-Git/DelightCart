package com.example.delightcart.Models

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val imagePath: String = ""
) {
    constructor() : this("", "", "", "", "")
}
