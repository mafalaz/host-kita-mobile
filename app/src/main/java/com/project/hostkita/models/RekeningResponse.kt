package com.project.hostkita.models

import com.google.gson.annotations.SerializedName

data class RekeningResponse(

	@field:SerializedName("rekeningId")
	val rekeningId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
