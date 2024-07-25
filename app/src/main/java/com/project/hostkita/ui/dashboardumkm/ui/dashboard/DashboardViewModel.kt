package com.project.hostkita.ui.dashboardumkm.ui.dashboard

import androidx.lifecycle.ViewModel
import com.project.hostkita.api.ApiConfig
import com.project.hostkita.di.UserRepository
import com.project.hostkita.models.OrderResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DashboardViewModel(private val repository: UserRepository) : ViewModel() {

    fun uploadImage(
        namaProduk: String,
        hargaProduk: String,
        beratProduk: String,
        panjangProduk: String,
        lebarProduk: String,
        tinggiProduk: String,
        jumlahProduk: String,
        fotoProduk: File,
        tanggalLive: String,
        deskripsi: String,
        biayaPacking: String,
        biayaHost: String,
        biayaPlatform: String,
        totalPayment: String,
        buktiTransfer: File,
        token: String,
        onImageUploadComplete: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val namaProdukReq = namaProduk.toRequestBody("text/plain".toMediaType())
        val hargaProdukReq = hargaProduk.toRequestBody("text/plain".toMediaType())
        val beratProdukReq = beratProduk.toRequestBody("text/plain".toMediaType())
        val panjangProdukReq = panjangProduk.toRequestBody("text/plain".toMediaType())
        val lebarProdukReq = lebarProduk.toRequestBody("text/plain".toMediaType())
        val tinggiProdukReq = tinggiProduk.toRequestBody("text/plain".toMediaType())
        val jumlahProdukReq = jumlahProduk.toRequestBody("text/plain".toMediaType())
        val tanggalLiveReq = tanggalLive.toRequestBody("text/plain".toMediaType())
        val deskripsiReq = deskripsi.toRequestBody("text/plain".toMediaType())
        val biayaPackingReq = biayaPacking.toRequestBody("text/plain".toMediaType())
        val biayaHostReq = biayaHost.toRequestBody("text/plain".toMediaType())
        val biayaPlatformReq = biayaPlatform.toRequestBody("text/plain".toMediaType())
        val totalPaymentReq = totalPayment.toRequestBody("text/plain".toMediaType())

        val fotoProdukRequestBody = fotoProduk.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val buktiTransferRequestBody = buktiTransfer.asRequestBody("image/jpeg".toMediaTypeOrNull())

        val fotoProdukPart = MultipartBody.Part.createFormData("fotoProduk", fotoProduk.name, fotoProdukRequestBody)
        val buktiTransferPart = MultipartBody.Part.createFormData("buktiTransfer", buktiTransfer.name, buktiTransferRequestBody)

        val client = ApiConfig.getApiService(token).userOrder(
            fotoProdukPart,
            buktiTransferPart,
            namaProdukReq,
            hargaProdukReq,
            beratProdukReq,
            panjangProdukReq,
            lebarProdukReq,
            tinggiProdukReq,
            jumlahProdukReq,
            tanggalLiveReq,
            deskripsiReq,
            biayaPackingReq,
            biayaHostReq,
            biayaPlatformReq,
            totalPaymentReq
        )

        client.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    response.body()?.message?.let { onImageUploadComplete(it) }
                } else {
                    val errorMessage = "Error: ${response.code()} ${response.message()}"
                    onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                onError("Network call failed: ${t.message}")
            }
        })
    }
}
