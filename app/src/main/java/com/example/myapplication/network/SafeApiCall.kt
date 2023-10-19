package com.example.myapplication.network

import android.util.Log
import com.example.myapplication.model.ErrorResponse
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.touchalife.talhospitals.data.network.NoConnectivityException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type


private const val TAG = "SafeApiCall"
val gson = Gson()
val type: Type = object : TypeToken<ErrorResponse>() {}.type!!
private var errorResponse: ErrorResponse? = null

fun <T> result(
    call: suspend () -> retrofit2.Response<T>
): Flow<ApiState<T?>> = flow {
    emit(ApiState.Loading)
    val c = call()
    try {
        c.let {
            if (c.isSuccessful) {
                emit(ApiState.Success(it.body()))
            } else {
                c.errorBody()?.let { error ->
                    errorResponse = gson.fromJson(error.string(), type)
                    if (errorResponse?.result.equals("failure")) {
                        emit(ApiState.Failure(errorResponse?.msg.toString()))
                    } else {
                        emit(ApiState.Failure(errorResponse?.msg.toString()))
                    }
                    error.close()
                }
            }
        }

    } catch (t: Throwable) {
        t.printStackTrace()

        if (t is NoConnectivityException) {
            // show No Connectivity message to user or do whatever you want.
            emit(ApiState.Failure(t.message))
        } else {
            emit(ApiState.Failure(t.message.toString()))
        }

    }
}