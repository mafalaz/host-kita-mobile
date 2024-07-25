package com.project.hostkita.models

import com.google.gson.annotations.SerializedName

data class UpdateRekeningResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
