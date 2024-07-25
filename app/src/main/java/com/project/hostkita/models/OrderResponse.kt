package com.project.hostkita.models

import com.google.gson.annotations.SerializedName

data class OrderResponse(

	@field:SerializedName("orderId")
	val orderId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
