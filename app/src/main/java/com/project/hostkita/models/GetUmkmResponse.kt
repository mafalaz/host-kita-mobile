package com.project.hostkita.models

import com.google.gson.annotations.SerializedName

data class GetUmkmResponse(

	@field:SerializedName("nama_umkm")
	val namaUmkm: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
