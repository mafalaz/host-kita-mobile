package com.project.hostkita.ui.register

import androidx.lifecycle.ViewModel
import com.project.hostkita.api.ApiConfig
import com.project.hostkita.api.RegisterRequest
import com.project.hostkita.di.UserRepository
import com.project.hostkita.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val repository: UserRepository): ViewModel(){
    private val apiService = ApiConfig.getApiService("")
    fun register(name: String, email: String, password: String, callback: (Boolean, String?) -> Unit) {
        val call = apiService.register(RegisterRequest(name, email, password))

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    callback(true, null)
                } else {
                    val errorMessage = response.errorBody()?.string()
                    callback(false, errorMessage)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                callback(false, t.message)
            }
        })
    }
}