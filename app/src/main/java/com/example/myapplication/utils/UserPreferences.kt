package com.example.myapplication.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = R.string.app_name.toString())

class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val appContext = context.applicationContext


    val accessToken: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }

    val userId: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[USERID]
        }

    val name: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[NAME]
        }

    val firstName: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[FIRSTNAME]
        }

    val lastname: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[LASTNAME]
        }

    val dob: Flow<Long?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[DOB]
        }

    val gender: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[GENDER]
        }

    val referralCode: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[REFERRAL_CODE]
        }

    val profilePic: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[PROFILE_PIC]
        }

    val email: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[EMAIL]
        }


    val phone: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[PHONE]
        }


    val countryCode: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[COUNTRY_CODE]
        }

    val country: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[COUNTRY]
        }

    val line1: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[LINE1]
        }

    val line2: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[LINE2]
        }

    val state: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[STATE]
        }

    val city: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CITY]
        }

    val zipCode: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ZIPCODE]
        }

    val emailVerified: Flow<Boolean?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[EMAIL_VERIFIED] ?: false
        }

    val summery: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[SUMMERY]
        }

    val role: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ROLE]
        }

    val countryId: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[COUNTRY_ID]
        }

    val stateId: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[STATE_ID]
        }

    val cityId: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CITY_ID]
        }

    val ipAddress: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[IPADDRESS]
        }

    val inrPrice: Flow<Double?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[INRPRICE]
        }

    val isTerms: Flow<Boolean?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[IS_TERMS] ?: false
        }

    val latitude: Flow<Double?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[LATITUDE]
        }

    val longitude: Flow<Double?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[LONGITUDE]
        }

    val phoneVerified: Flow<Boolean?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[PHONE_VERIFIED] ?: false
        }


    suspend fun saveAccessTokens(accessToken: String) {
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun saveCountryIds(countyId: String) {
        appContext.dataStore.edit { preferences ->
            preferences[COUNTRY_ID] = countyId

        }
    }

    suspend fun saveStateIds(stateId : String) {
        appContext.dataStore.edit { preferences ->
            preferences[STATE_ID] = stateId
        }
    }

    suspend fun saveCityIds(cityId:String) {
        appContext.dataStore.edit { preferences ->
            preferences[CITY_ID] = cityId
        }
    }

    suspend fun savePhoneStatus(phoneStatus: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[PHONE_VERIFIED] = phoneStatus
        }
    }


    suspend fun saveUserData(
        userId: String,
        name: String,
        email : String,
        phone : String,
    ) {
        appContext.dataStore.edit { preferences ->
            preferences[USERID] = userId
            preferences[NAME] = name
            preferences[EMAIL] = email
            preferences[PHONE] = phone

        }
    }

    suspend fun clear() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val USERID = stringPreferencesKey("userId")
        private val NAME = stringPreferencesKey("name")
        private val FIRSTNAME = stringPreferencesKey("firstName")
        private val LASTNAME = stringPreferencesKey("lastname")
        private val DOB = longPreferencesKey("dob")
        private val GENDER = stringPreferencesKey("gender")
        private val REFERRAL_CODE = stringPreferencesKey("referral_code")
        private val PROFILE_PIC = stringPreferencesKey("profilePic")
        private val EMAIL = stringPreferencesKey("email")
        private val PHONE = stringPreferencesKey("phone")
        private val COUNTRY_CODE = stringPreferencesKey("countryCode")
        private val COUNTRY = stringPreferencesKey("country")
        private val LINE1 = stringPreferencesKey("line1")
        private val LINE2 = stringPreferencesKey("line2")
        private val STATE = stringPreferencesKey("state")
        private val CITY = stringPreferencesKey("city")
        private val ZIPCODE = stringPreferencesKey("zip_code")
        private val EMAIL_VERIFIED = booleanPreferencesKey("email_verified")
        private val PHONE_VERIFIED = booleanPreferencesKey("phone_verified")
        private val SUMMERY = stringPreferencesKey("summery")
        private val ROLE = stringPreferencesKey("role")
        private val COUNTRY_ID = stringPreferencesKey("countryId")
        private val STATE_ID = stringPreferencesKey("stateId")
        private val CITY_ID = stringPreferencesKey("cityId")
        private val INRPRICE = doublePreferencesKey("inrPrice")
        private val IPADDRESS = stringPreferencesKey("ipAddress")
        private val IS_TERMS = booleanPreferencesKey("isTerms")
        private val LATITUDE = doublePreferencesKey("latitude")
        private val LONGITUDE = doublePreferencesKey("longitude")

        private val LOGIN_STATUS = booleanPreferencesKey("login_status")

    }


}