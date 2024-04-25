package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.api.ArticlesItem
import com.dicoding.asclepius.data.api.NewsApiResponse
import com.dicoding.asclepius.data.api.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel:ViewModel() {
    private var _newsList = MutableLiveData<List<ArticlesItem>?>()
    val newsList: LiveData<List<ArticlesItem>?> = _newsList

    private var _loadingNewsData = MutableLiveData<Boolean>()
    val loadingNewsData: LiveData<Boolean> = _loadingNewsData

    private var _resultError = MutableLiveData<String>()
    val resultError: LiveData<String> = _resultError

    init {
        getReponseByNews()
    }

    private fun getReponseByNews() {
        _loadingNewsData.value = true
        RetrofitConfig.connectTORetrofitService().getAllNews().enqueue(
            object : Callback<NewsApiResponse> {
                override fun onResponse(
                    call: Call<NewsApiResponse>,
                    response: Response<NewsApiResponse>
                ) {
                    _loadingNewsData.value = false
                    if (response.isSuccessful) {
                        _newsList.value = response.body()?.articles
                    } else {
                        _resultError.value = "Something went wrong while fetching data"
                    }

                }

                override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                    _loadingNewsData.value = false
                    Log.e("NewsViewModel", t.message.toString())
                    _resultError.value = "Something went wrong while fetching data"
                }

            }
        )
    }
}