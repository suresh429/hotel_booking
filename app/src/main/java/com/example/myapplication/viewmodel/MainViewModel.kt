package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.MainRepository
import com.example.myapplication.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    suspend fun saveUserData(
        userId: String,
        name: String,
        email: String,
        phone: String,
    ) {
        repository.saveUserData(
            userId,
            name,
            email,
            phone,
        )
    }


    // login
    fun getLogin(username: String, password: String,deviceId:String,appVersion:String,os:String) = repository.getLogin(username,password,deviceId,appVersion, os)


    // SignUp
    fun getSignUp(name: String, mobileNo:String,emailId:String,password: String,deviceId:String,appVersion:String,os:String) = repository.getSignUp(name,mobileNo,emailId,password,deviceId,appVersion, os)


    // resendOtp
    fun resetPassword(email: String?) = repository.getResetPassword(email)

    // verify OTP
    fun verifyOtp(
        email: String?,
        otp: String?,
        type: String?,
        phone: String?,
        countryCode: String?,
    ) = repository.getOtpVerify(email, otp, type, phone, countryCode)

    // resendOtp
    fun resendOtp(email: String?, type: String?, phone: String?) = repository.getResendOtp(email, type, phone)

    // changePassword
    fun changePassword(email: String?, password: String?, otp: String?) = repository.getChangePassword(email, password, otp)

}