package com.example.myapplication.model


import androidx.annotation.Keep

@Keep
data class Data(
    val amenities: String,
    val area: String,
    val base_room_price: String,
    val city: String,
    val corporate_id: String,
    val coupon_code: String,
    val coupon_id: String,
    val created_at: String,
    val discount: String,
    val discount_type: String,
    val email: String,
    val hotel_about: Any?,
    val hotel_address: String,
    val hotel_grade: String,
    val hotel_name: String,
    val hotel_phone_no: String,
    val hotel_pic: String,
    val hotel_star_id: String,
    val hotel_star_name: String,
    val id: String,
    val is_active: String,
    val is_popular: String,
    val latitude: String,
    val location: String,
    val longitude: String,
    val rating: String,
    val rating_users_count: String,
    val star_count: String,
    val state: Any?,
    val top_hotel: String,
    val total_rooms: String,
    val updated_at: String,
    val user_discount: Any?,
    val user_room_price: String,
    val vendor_name: String
)