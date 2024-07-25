package com.project.hostkita.rekening

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hostkita.api.ApiConfig
import com.project.hostkita.api.RekeningRequest
import com.project.hostkita.di.UserRepository
import com.project.hostkita.models.GetRekeningResponse
import com.project.hostkita.models.RekeningResponse
import com.project.hostkita.models.UpdateRekeningResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RekeningViewModel(private val repository: UserRepository): ViewModel(){
    fun addRekeningUser(token: String, namaBank: String, noRekening: Int, atasNama: String) {
        val resultLiveData = MutableLiveData<Result<String>>()
        val apiService = ApiConfig.getApiService(token)


        val call = apiService.rekening(RekeningRequest(namaBank, noRekening, atasNama ))

        call.enqueue(object : Callback<RekeningResponse> {
            override fun onResponse(call: Call<RekeningResponse>, response: Response<RekeningResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        loginResponse.message
                    } else {
                        resultLiveData.postValue(Result.Error("Response body is null"))
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    resultLiveData.postValue(Result.Error(errorMessage ?: "Unknown error"))
                }
            }

            override fun onFailure(call: Call<RekeningResponse>, t: Throwable) {
                resultLiveData.postValue(Result.Error(t.message ?: "Network error"))
            }
        })
    }

    private val _namaBank = MutableLiveData<String>()
    val namaBank: LiveData<String> get() = _namaBank

    private val _noRekening = MutableLiveData<String>()
    val noRekening: LiveData<String> get() = _noRekening

    private val _atasNama = MutableLiveData<String>()
    val atasNama: LiveData<String> get() = _atasNama

    private val _rekeningId = MutableLiveData<String>()
    val rekeningId: LiveData<String> get() = _rekeningId

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadRekening(token: String) {
        val client = ApiConfig.getApiService(token).userGetRekening()
        client.enqueue(object : Callback<GetRekeningResponse> {
            override fun onResponse(call: Call<GetRekeningResponse>, response: Response<GetRekeningResponse>) {
                if (response.isSuccessful) {
                    val totalResponse = response.body()
                    totalResponse?.let {
                        val rekeningItem = it.rekening.firstOrNull()
                        if (rekeningItem != null) {
                            _namaBank.postValue(rekeningItem.namaBank ?: "N/A")
                            _noRekening.postValue(rekeningItem.noRekening?.toString() ?: "N/A")
                            _atasNama.postValue(rekeningItem.atasNama ?: "N/A")
                            _rekeningId.postValue(rekeningItem.rekeningid?.toString() ?: "N/A")
                        } else {
                            _error.postValue("No Rekening found")
                        }
                    } ?: run {
                        _error.postValue("Response body is null")
                    }
                } else {
                    _error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetRekeningResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure Call: ${t.message}")
                _error.postValue("Error : ${t.message}")
            }
        })
    }

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    fun updateRekening(token: String, rekeningId: Int, namaBank: String, noRekening: Int, atasNama: String) {
        val client = ApiConfig.getApiService(token).updateRekening(
            rekeningId,
            RekeningRequest(
                namaBank = namaBank,
                noRekening = noRekening,
                atasNama = atasNama
            )
        )

        client.enqueue(object : Callback<UpdateRekeningResponse> {
            override fun onResponse(call: Call<UpdateRekeningResponse>, response: Response<UpdateRekeningResponse>) {
                if (response.isSuccessful) {
                    val totalResponse = response.body()
                    totalResponse?.let {
                        _success.postValue(true)
                    } ?: run {
                        _error.postValue("Response body is null")
                    }
                } else {
                    _error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdateRekeningResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure Call: ${t.message}")
                _error.postValue("Error : ${t.message}")
            }
        })
    }
}