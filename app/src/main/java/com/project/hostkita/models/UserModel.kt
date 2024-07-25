package com.project.hostkita.models

data class UserModel(
    val email: String,
    var token: String,
    val isLogin: Boolean = false
)
