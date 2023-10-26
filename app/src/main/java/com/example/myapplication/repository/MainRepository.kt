package com.example.myapplication.repository

import com.example.myapplication.network.ApiService
import com.example.myapplication.network.result
import com.example.myapplication.utils.UserPreferences
import com.example.myapplication.utils.sourceOfCreation
import com.google.gson.JsonObject
import retrofit2.http.Field
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: ApiService,
                                         private val preferences: UserPreferences) {

    suspend fun saveUserData(
        userId: String,
        name: String,
        email: String,
        phone: String,
    ) {
        preferences.saveUserData(
            userId,
            name,
            email,
            phone,
        )
    }



    // login
    fun getLogin(username: String, password: String,deviceId:String,appVersion:String,os:String) = result {
        api.login(username,password,deviceId,appVersion, os)
    }


    // signup
    fun getSignUp(name: String, mobileNo:String,emailId:String,password: String,deviceId:String,appVersion:String,os:String) = result {
        api.signUp(name,mobileNo,emailId,password,deviceId,appVersion, os)
    }


    // reset password
    fun getResetPassword(email: String?) = result {
        // json raw Data
        val jsonObject = JsonObject()
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("appSource", sourceOfCreation)
        api.resendPassword(jsonObject)
    }


    // Otp Verify
    fun getOtpVerify(
        email: String?,
        otp: String?,
        type: String?,
        phone: String?,
        countryCode: String?
    ) = result {
        val jsonObject = JsonObject()
        if (type.equals("change number")) {
            jsonObject.addProperty("phone", "+$countryCode$phone")
            jsonObject.addProperty("email", email)
        } else {
            jsonObject.addProperty("email", email)
        }
        jsonObject.addProperty("otp", otp)
        jsonObject.addProperty("type", type)

        api.verifyOtp(jsonObject)
    }


    // resendOtp
    fun getResendOtp(email: String?, type: String?, phone: String?)= result {
        // json raw Data
        val jsonObject = JsonObject()
        jsonObject.addProperty("type", type)
        jsonObject.addProperty("email", email)
        if (type.equals("change number")) {
            jsonObject.addProperty("phone", phone)
        }
        api.resendOtp(jsonObject)
    }

    // change password
    fun getChangePassword(email: String?, password: String?, otp: String?) = result {
        // json raw Data
        val jsonObject = JsonObject()
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("otp", otp)
        api.changePassword(jsonObject)
    }

}