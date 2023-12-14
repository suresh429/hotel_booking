package com.example.myapplication.model


import androidx.annotation.Keep

@Keep
data class RecommendedhotelsResponse(
    val `data`: List<Data>,
    val msg: String,
    val result: String
)