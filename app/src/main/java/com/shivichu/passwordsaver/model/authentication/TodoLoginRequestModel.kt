package com.shivichu.passwordsaver.model.authentication

import com.google.gson.annotations.SerializedName

data class TodoLoginRequestModel(
    @SerializedName("email") var email :  String? = "",
    @SerializedName("password") var password : String? = ""
)