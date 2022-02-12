package com.shivichu.passwordsaver.model

import com.google.gson.annotations.SerializedName

data class CommonResponseModel(
    @SerializedName("status") var status : Boolean?,
    @SerializedName("message") var message : String? = "",
)
