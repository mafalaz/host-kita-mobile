package com.project.hostkita.models

import com.google.gson.annotations.SerializedName

data class DetailOrderResponse(

	@field:SerializedName("orderDetail")
	val orderDetail: OrderDetail? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class OrderDetail(

	@field:SerializedName("namaProduk")
	val namaProduk: String? = null,

	@field:SerializedName("tinggiProduk")
	val tinggiProduk: Int? = null,

	@field:SerializedName("orderId")
	val orderId: Int? = null,

	@field:SerializedName("panjangProduk")
	val panjangProduk: Int? = null,

	@field:SerializedName("fotoProduk")
	val fotoProduk: String? = null,

	@field:SerializedName("hargaProduk")
	val hargaProduk: Int? = null,

	@field:SerializedName("lebarProduk")
	val lebarProduk: Int? = null,

	@field:SerializedName("statusLive")
	val statusLive: String? = null,

	@field:SerializedName("tanggalLive")
	val tanggalLive: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("biayaPacking")
	val biayaPacking: String? = null,

	@field:SerializedName("biayaHost")
	val biayaHost: String? = null,

	@field:SerializedName("biayaPlatform")
	val biayaPlatform: String? = null,

	@field:SerializedName("totalPayment")
	val totalPayment: String? = null,

	@field:SerializedName("beratProduk")
	val beratProduk: Int? = null,

	@field:SerializedName("jumlahProduk")
	val jumlahProduk: Int? = null
)
