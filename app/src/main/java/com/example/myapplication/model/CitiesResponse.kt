package com.example.myapplication.model


import androidx.annotation.Keep

@Keep
data class CitiesResponse(
    val `data`: List<Data>,
    val msg: String,
    val result: String
) {
    @Keep
    data class Data(
        val city_id: String,
        val city_image: String,
        val city_name: String,
        val hotels_count: String,
        val is_popular: String,
        val latitude: String,
        val longitude: String
    )
}