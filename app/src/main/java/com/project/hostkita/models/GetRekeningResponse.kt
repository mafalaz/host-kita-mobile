package com.project.hostkita.models

import com.google.gson.annotations.SerializedName

data class GetRekeningResponse(

	@field:SerializedName("rekening")
	val rekening: List<RekeningItem>,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class RekeningItem(

	@field:SerializedName("namaBank")
	val namaBank: String? = null,

	@field:SerializedName("atasNama")
	val atasNama: String? = null,

	@field:SerializedName("rekeningid")
	val rekeningid: Int? = null,

	@field:SerializedName("noRekening")
	val noRekening: Int? = null
)
