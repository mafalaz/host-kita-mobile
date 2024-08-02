package com.project.hostkita.models

import com.google.gson.annotations.SerializedName

data class PenjualanResponse(

	@field:SerializedName("listPenjualan")
	val listPenjualan: List<ListPenjualanItem>,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ListPenjualanItem(

	@field:SerializedName("sisaProduk")
	val sisaProduk: Int? = null,

	@field:SerializedName("namaProduk")
	val namaProduk: String? = null,

	@field:SerializedName("orderId")
	val orderId: Int? = null,

	@field:SerializedName("totalCheckout")
	val totalCheckout: Int? = null,

	@field:SerializedName("tanggalUpdatePenjualan")
	val tanggalUpdatePenjualan: String? = null,

	@field:SerializedName("hargaProduk")
	val hargaProduk: Int? = null,

	@field:SerializedName("nama_umkm")
	val namaUmkm: String? = null,

	@field:SerializedName("totalPendapatan")
	val totalPendapatan: Int? = null,

	@field:SerializedName("penjualanId")
	val penjualanId: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("jumlahProduk")
	val jumlahProduk: Int? = null
)
