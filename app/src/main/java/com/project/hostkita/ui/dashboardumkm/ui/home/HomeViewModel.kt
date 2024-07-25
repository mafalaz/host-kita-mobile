package com.project.hostkita.ui.dashboardumkm.ui.home

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hostkita.api.ApiConfig
import com.project.hostkita.di.UserRepository
import com.project.hostkita.models.GetOrderUserResponse
import com.project.hostkita.models.GetUmkmResponse
import com.project.hostkita.models.OrderUserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    val error = MutableLiveData<String>()

    val orderList = MutableLiveData<List<OrderUserItem>>()

    fun loadOrderUser(token: String) {
        val client = ApiConfig.getApiService(token).getOrder()
        client.enqueue(object : Callback<GetOrderUserResponse> {
            override fun onResponse(call: Call<GetOrderUserResponse>, response: Response<GetOrderUserResponse>) {
                if (response.isSuccessful) {
                    orderList.postValue(response.body()?.orderUser)
                } else {
                    error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetOrderUserResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure Call: ${t.message}")
                error.postValue("Error : ${t.message}")
            }
        })
    }
}