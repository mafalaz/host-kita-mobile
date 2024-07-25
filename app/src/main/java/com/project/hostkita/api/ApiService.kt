package com.project.hostkita.api

import com.project.hostkita.models.DetailOrderResponse
import com.project.hostkita.models.GetOrderUserResponse
import com.project.hostkita.models.GetRekeningResponse
import com.project.hostkita.models.GetUmkmResponse
import com.project.hostkita.models.LoginResponse
import com.project.hostkita.models.OrderResponse
import com.project.hostkita.models.RegisterResponse
import com.project.hostkita.models.RekeningResponse
import com.project.hostkita.models.UpdateRekeningResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

data class RegisterRequest(
    val nama_umkm: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class RekeningRequest(
    val namaBank: String,
    val noRekening: Int,
    val atasNama: String
)

interface ApiService {
    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("auth/getUmkm")
    fun getUmkm(): Call<GetUmkmResponse>

    @Multipart
    @POST("user/addUserOrder")
    fun userOrder(
        @Part file: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part("namaProduk") namaProduk: RequestBody,
        @Part("hargaProduk") hargaProduk: RequestBody,
        @Part("beratProduk") beratProduk: RequestBody,
        @Part("panjangProduk") panjangProduk: RequestBody,
        @Part("lebarProduk") lebarProduk: RequestBody,
        @Part("tinggiProduk") tinggiProduk: RequestBody,
        @Part("jumlahProduk") jumlahProduk: RequestBody,
        @Part("tanggalLive") tanggalLive: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("biayaPacking") biayaPacking: RequestBody,
        @Part("biayaHost") biayaHost: RequestBody,
        @Part("biayaPlatform") biayaPlatform: RequestBody,
        @Part("totalPayment") totalPayment: RequestBody
    ): Call<OrderResponse>

    @POST("user/rekening")
    fun rekening(@Body request: RekeningRequest): Call<RekeningResponse>

    @GET("user/getRekening")
    fun userGetRekening(): Call<GetRekeningResponse>

    @PUT("user/updateRekening/{rekeningId}")
    fun updateRekening(
        @Path("rekeningId") rekeningId: Int,
        @Body rekeningRequest: RekeningRequest
    ): Call<UpdateRekeningResponse>

    @GET("user/orderUser")
    fun getOrder(): Call<GetOrderUserResponse>

    @GET("user/orderUser/{orderId}")
    fun detailOrder(
        @Path("orderId") id: String,
    ): Call<DetailOrderResponse>
}