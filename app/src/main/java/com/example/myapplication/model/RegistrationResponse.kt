package com.example.myapplication.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RegistrationResponse(
    val `data`: Any,
    val msg: String,
    val result: String
) {
    @Keep
    data class Data(
        val city_id: String,
        val created_by: String,
        val created_date: String,
        val device_id: String,
        val email: String,
        val gcm_id: String,
        val guid: String,
        val image: String,
        val is_active: String,
        val is_verified: String,
        val lastlogin: String,
        val latitude: String,
        val login_status: String,
        val longitude: String,
        val name: String,
        val os: String,
        val otp: String,
        val password: String,
        val phone_no: String,
        val rep_to: String,
        val support_contact: String,
        val today_date: String,
        val tracking_time: String,
        val uid: String,
        val updated_by: String,
        val updated_date: String,
        val utype_id: String,
        val version: String
    )
}
