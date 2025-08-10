package com.example.blackstone.network

import com.example.blackstone.model.LoginReq
import com.example.blackstone.model.SignupReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/signup")
    suspend fun signup(@Body req: SignupReq): Response<String>

    @POST("api/login")
    suspend fun login(@Body req: LoginReq): Response<String>
}