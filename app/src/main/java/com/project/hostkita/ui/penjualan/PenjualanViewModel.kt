package com.project.hostkita.ui.penjualan

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hostkita.api.ApiConfig
import com.project.hostkita.di.UserRepository
import com.project.hostkita.models.GetOrderUserResponse
import com.project.hostkita.models.ListPenjualanItem
import com.project.hostkita.models.OrderUserItem
import com.project.hostkita.models.PenjualanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PenjualanViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    val error = MutableLiveData<String>()

    val orderList = MutableLiveData<List<ListPenjualanItem>>()

    fun loadPenjualanUser(token: String) {
        val client = ApiConfig.getApiService(token).getPenjualan()
        client.enqueue(object : Callback<PenjualanResponse> {
            override fun onResponse(call: Call<PenjualanResponse>, response: Response<PenjualanResponse>) {
                if (response.isSuccessful) {
                    orderList.postValue(response.body()?.listPenjualan)
                } else {
                    error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PenjualanResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure Call: ${t.message}")
                error.postValue("Error : ${t.message}")
            }
        })
    }
}