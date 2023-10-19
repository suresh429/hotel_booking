package com.example.myapplication.network

import com.example.myapplication.di.AppModule.USER_APP_KEY
import com.example.myapplication.model.LoginResponse
import com.example.myapplication.model.RegistrationResponse
import com.example.myapplication.model.VerifyOtpResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {


    @FormUrlEncoded
    @POST("api_user/signIn")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("device_id") deviceId: String,
        @Field("app_version") appVersion: String,
        @Field("os") os: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("api_user/signUp")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("mobile_no") mobileNo: String,
        @Field("email_id") emailId: String,
        @Field("password") password: String,
        @Field("device_id") deviceId: String,
        @Field("app_version") appVersion: String,
        @Field("os") os: String
    ): Response<LoginResponse>


    @POST("forgotpassword")
    suspend fun resendPassword(
        @Body jsonObject: JsonObject,
    ): Response<RegistrationResponse>

    @POST("verifyotp")
    suspend fun verifyOtp(
        @Body jsonObject: JsonObject,
    ): Response<VerifyOtpResponse>

    @POST("resendotp")
    suspend fun resendOtp(
        @Body jsonObject: JsonObject,
    ): Response<RegistrationResponse>

    @POST("changepassword")
    suspend fun changePassword(
        @Body jsonObject: JsonObject,
    ): Response<VerifyOtpResponse>


}