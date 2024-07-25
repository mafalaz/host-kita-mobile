package com.project.hostkita.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hostkita.api.ApiConfig
import com.project.hostkita.di.UserRepository
import com.project.hostkita.models.DetailOrderResponse
import com.project.hostkita.models.OrderDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailOrderViewModel(repository: UserRepository) : ViewModel() {
    private val _orderDetail = MutableLiveData<OrderDetail?>()
    val orderDetail: LiveData<OrderDetail?> = _orderDetail
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadOrderDetail(token: String, orderId: String) {
        val call = ApiConfig.getApiService(token).detailOrder(orderId)
        call.enqueue(object : Callback<DetailOrderResponse> {
            override fun onResponse(call: Call<DetailOrderResponse>, response: Response<DetailOrderResponse>) {
                if (response.isSuccessful) {
                    val recycling = response.body()?.orderDetail
                    _orderDetail.value = recycling
                } else {
                    val errorMessage = "Error: ${response.code()} ${response.message()}"
                    _error.value = errorMessage
                }
            }

            override fun onFailure(call: Call<DetailOrderResponse>, t: Throwable) {
                _error.value = "Failure: ${t.message}"
            }
        })
    }
}