package com.project.hostkita.ui.dashboardumkm.ui.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hostkita.api.ApiConfig
import com.project.hostkita.di.UserRepository
import com.project.hostkita.models.GetUmkmResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadGetUmkm(token: String) {
        val client = ApiConfig.getApiService(token).getUmkm()
        client.enqueue(object : Callback<GetUmkmResponse> {
            override fun onResponse(call: Call<GetUmkmResponse>, response: Response<GetUmkmResponse>) {
                if (response.isSuccessful) {
                    val totalResponse = response.body()
                    totalResponse?.let {
                        _name.postValue((it.namaUmkm ?: "N/A").toString())
                    } ?: run {
                        _error.postValue("Response body is null")
                    }
                } else {
                    _error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetUmkmResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure Call: ${t.message}")
                _error.postValue("Error : ${t.message}")
            }
        })
    }
}