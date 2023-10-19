package com.example.myapplication.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RegistrationResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("statusCode")
    val statusCode: Int
) {
    @Keep
    data class Data(
        @SerializedName("email")
        val email: String
    )
}