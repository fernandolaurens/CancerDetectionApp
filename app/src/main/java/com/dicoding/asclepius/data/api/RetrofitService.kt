package com.dicoding.asclepius.data.api

import com.dicoding.asclepius.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("top-headlines")
    fun getAllNews(
        @Query("q") query: String = "Cancer",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.APIKEY
    ): Call<NewsApiResponse>
}