package com.project.hostkita.ui.detailpenjualan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hostkita.api.ApiConfig
import com.project.hostkita.di.UserRepository
import com.project.hostkita.models.DetailOrderResponse
import com.project.hostkita.models.DetailPenjualanResponse
import com.project.hostkita.models.OrderDetail
import com.project.hostkita.models.PenjualanDetailItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPenjualanViewModel(repository: UserRepository) : ViewModel() {
    private val _orderDetails = MutableLiveData<List<PenjualanDetailItem>?>()
    val penjualanDetail: LiveData<List<PenjualanDetailItem>?> = _orderDetails
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadPenjualanDetail(token: String, orderId: String) {
        val call = ApiConfig.getApiService(token).detailPenjualan(orderId)
        call.enqueue(object : Callback<DetailPenjualanResponse> {
            override fun onResponse(call: Call<DetailPenjualanResponse>, response: Response<DetailPenjualanResponse>) {
                if (response.isSuccessful) {
                    val penjualanList = response.body()?.penjualanDetail
                    _orderDetails.value = penjualanList
                } else {
                    val errorMessage = "Error: ${response.code()} ${response.message()}"
                    _error.value = errorMessage
                }
            }

            override fun onFailure(call: Call<DetailPenjualanResponse>, t: Throwable) {
                _error.value = "Failure: ${t.message}"
            }
        })
    }
}

