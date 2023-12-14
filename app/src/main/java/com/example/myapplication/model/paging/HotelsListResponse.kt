package com.example.myapplication.model.paging


import androidx.annotation.Keep

@Keep
data class HotelsListResponse(
    val `data`: List<Data>,
    val msg: String,
    val result: String
)