package com.example.myapplication.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class VerifyOtpResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String?,
    @SerializedName("statusCode")
    val statusCode: Int?
) {
    @Keep
    data class Data(
        @SerializedName("account_status")
        val accountStatus: Int?,
        @SerializedName("account_verified")
        val accountVerified: Boolean?,
        @SerializedName("address")
        val address: Address,
        @SerializedName("display_name")
        val displayName: String?,
        @SerializedName("dob")
        val dob: Long,
        @SerializedName("email")
        val email: String,
        @SerializedName("email_verified")
        val emailVerified: Boolean,
        @SerializedName("facebookProfileUrl")
        val facebookProfileUrl: String?,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("instagramProfileUrl")
        val instagramProfileUrl: String?,
        @SerializedName("isTourCompleted")
        val isTourCompleted: Boolean?,
        @SerializedName("linkedInProfileUrl")
        val linkedInProfileUrl: String?,
        @SerializedName("login_provider")
        val loginProvider: String?,
        @SerializedName("name")
        val name: Name,
        @SerializedName("password_verified")
        val passwordVerified: Boolean?,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("phone_verified")
        val phoneVerified: Boolean,
        @SerializedName("profile_image_url")
        val profileImageUrl: String,
        @SerializedName("roles")
        val roles: List<String?>,
        @SerializedName("social_verification")
        val socialVerification: SocialVerification,
        @SerializedName("summary")
        val summary: String,
        @SerializedName("token_detail")
        val tokenDetail: TokenDetail,
        @SerializedName("totalEmailsPerMonth")
        val totalEmailsPerMonth: Int?,
        @SerializedName("twitterProfileUrl")
        val twitterProfileUrl: String?,
        @SerializedName("unique_id")
        val uniqueId: String,
        @SerializedName("username")
        val username: String,
        @SerializedName("referral_code")
        val referral_code: String,
        @SerializedName("acceptedTermsAndConditions")
        val acceptedTermsAndConditions: Boolean,
        @SerializedName("ipAddress")
        val ipAddress: String,
        @SerializedName("location")
        val location: Location,
    ) {
        @Keep
        data class Location(
            @SerializedName("type")
            val type: String,
            @SerializedName("coordinates")
            val coordinates: List<Double>,
        )
        @Keep
        data class Address(
            @SerializedName("city")
            val city: String,
            @SerializedName("country")
            val country: String,
            @SerializedName("line1")
            val line1: String,
            @SerializedName("line2")
            val line2: String,
            @SerializedName("state")
            val state: String,
            @SerializedName("zipcode")
            val zipcode: String
        )

        @Keep
        data class Name(
            @SerializedName("first_name")
            val firstName: String,
            @SerializedName("last_name")
            val lastName: String,
            @SerializedName("middle_name")
            val middleName: String?
        )

        @Keep
        data class SocialVerification(
            @SerializedName("apple_verified")
            val appleVerified: Boolean?,
            @SerializedName("facebook_verified")
            val facebookVerified: Boolean?,
            @SerializedName("google_verified")
            val googleVerified: Boolean?,
            @SerializedName("linkedin_verified")
            val linkedinVerified: Boolean?,
            @SerializedName("twitter_verified")
            val twitterVerified: Boolean?
        )

        @Keep
        data class TokenDetail(
            @SerializedName("token")
            val token: String?,
            @SerializedName("type")
            val type: String?
        )
    }
}