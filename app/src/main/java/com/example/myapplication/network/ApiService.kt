package com.example.myapplication.network

import com.example.myapplication.di.AppModule.USER_APP_KEY
import com.example.myapplication.model.CitiesResponse
import com.example.myapplication.model.LoginResponse
import com.example.myapplication.model.RecommendedhotelsResponse
import com.example.myapplication.model.RegistrationResponse
import com.example.myapplication.model.VerifyOtpResponse
import com.example.myapplication.model.paging.HotelsListResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {


    @FormUrlEncoded
    @POST("signIn")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("device_id") deviceId: String,
        @Field("app_version") appVersion: String,
        @Field("os") os: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("signUp")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("mobile_no") mobileNo: String,
        @Field("email_id") emailId: String,
        @Field("password") password: String,
        @Field("device_id") deviceId: String,
        @Field("app_version") appVersion: String,
        @Field("os") os: String
    ): Response<RegistrationResponse>


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

    @FormUrlEncoded
    @POST("getCities")
    suspend fun getCities(
        @Field("user_app_key") userAppKey: String?,
        @Field("is_launched") isLaunched: String?,
        @Field("is_popular") isPopular: String?,
    ): Response<CitiesResponse>


    @FormUrlEncoded
    @POST("get_recommended_hotels")
    suspend fun getRecommendedHotels(
        @Field("user_app_key") userAppKey: String?,
        @Field("city_id") cityId: String?,
    ): Response<RecommendedhotelsResponse>

    @FormUrlEncoded
    @POST("get_hotels_by_city_or_location")
    suspend fun getHotels(
        @Field("user_app_key") userAppKey: String?,
        @Field("range") range: Int?,
        @Field("city_id") cityId: String?,
        @Field("check_in_date") check_in_date: String?,
        @Field("check_out_date") check_out_date: String?,
        @Field("adult_count") adult_count: String?,
        @Field("child_count") child_count: String?,
        @Field("pay_at_hotel") pay_at_hotel: String?,
    ): HotelsListResponse

}