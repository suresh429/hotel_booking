package com.example.myapplication.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VersionUpdateResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String,
) {
    @Keep
    data class Data(
        @SerializedName("device")
        val device: String,
        @SerializedName("_id")
        val id: String,
        @SerializedName("mandatory")
        val mandatory: Boolean,
        @SerializedName("releaseNotes")
        val releaseNotes: String,
        @SerializedName("version")
        val version: String,
    )
}
