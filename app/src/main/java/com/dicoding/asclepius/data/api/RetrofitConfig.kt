package com.dicoding.asclepius.data.api

import com.dicoding.asclepius.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitConfig {
    companion object {
        fun connectTORetrofitService(): RetrofitService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
            val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit =
                Retrofit.Builder().baseUrl(BuildConfig.BASEURL).addConverterFactory(
                    GsonConverterFactory.create()
                )
                    .client(clientBuilder)
                    .build()

            return retrofit.create<RetrofitService>()
        }
    }
}