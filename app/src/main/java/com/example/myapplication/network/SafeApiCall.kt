package com.example.myapplication.network


import com.example.myapplication.model.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type


val gson = Gson()
val type: Type = object : TypeToken<ErrorResponse>() {}.type!!
private var errorResponse: ErrorResponse? = null

fun <T> result(
    call: suspend () -> retrofit2.Response<T>
): Flow<ApiState<T?>> = flow {
    emit(ApiState.Loading)
    try {
        val c = call()
        c.let {
            if (c.isSuccessful) {
                emit(ApiState.Success(it.body()))
            } else {
                c.errorBody()?.let { error ->
                    error.close()
                    errorResponse = gson.fromJson(error.charStream(), type)
                    if (errorResponse?.result.equals("failure")) {
                        emit(ApiState.Failure(errorResponse?.msg.toString()))
                    } else {
                        emit(ApiState.Failure(errorResponse?.msg.toString()))
                    }
                }
            }
        }

    } catch (t: Throwable) {
        t.printStackTrace()

        if (t is NoConnectivityException) {
            // show No Connectivity message to user or do whatever you want.
            emit(ApiState.Failure(t.message))
        }
        emit(ApiState.Failure(t.message.toString()))
    }
}