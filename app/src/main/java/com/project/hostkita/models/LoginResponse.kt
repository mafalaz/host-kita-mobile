package com.project.hostkita.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("user")
    val loginResult: User? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class User(

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("isLogin")
    val isLogin: Boolean? = false
)