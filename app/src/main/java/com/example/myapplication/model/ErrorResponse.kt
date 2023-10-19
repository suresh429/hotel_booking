package com.example.myapplication.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorResponse(
    val msg: String,
    val result: String,
    val `data`: List<Any?>,
)