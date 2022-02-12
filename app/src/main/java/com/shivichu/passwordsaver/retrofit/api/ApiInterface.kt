package com.shivichu.passwordsaver.retrofit.api

import com.shivichu.passwordsaver.model.authentication.TodoLoginRequestModel
import com.shivichu.passwordsaver.model.authentication.TodoLoginResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiInterface {

    @POST("api/login")
    suspend fun loginUser2(@Body data : TodoLoginRequestModel) : Response<TodoLoginResponseModel>

}