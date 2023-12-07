package com.example.myapplication.di

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.myapplication.BuildConfig
import com.example.myapplication.network.ApiService
import com.example.myapplication.network.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    const val  USER_APP_KEY: String = "YWGYW213FDY273TUWWGSASHAS66GV7DFWQML"

    @Provides
    fun providesUrl() = BuildConfig.BASE_URL


    @Provides
    @Singleton
    fun providesApiService(url: String, @ApplicationContext context: Context): ApiService =
        Retrofit.Builder()
            .baseUrl(url)
            .client(getRetrofitClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    private fun getRetrofitClient(
        context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            .addInterceptor { chain ->
                val url = chain.request().url
                    .newBuilder()
                    .addQueryParameter("user_app_key", USER_APP_KEY)
                    .build()

                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .url(url)
                    .build()

                try {
                    return@addInterceptor chain.proceed(request)
                } catch (e: IOException) {
                    Log.e("TAG", "Network request failed: ${e.message}")
                    // Handle the exception gracefully, such as showing an error message to the user
                    throw e // Rethrow the exception if needed
                }
            }
            .also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }
            .build()
    }


}

