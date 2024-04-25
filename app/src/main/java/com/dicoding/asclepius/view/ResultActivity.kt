package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.data.api.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.viewmodel.NewsViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val newsViewModel by viewModels<NewsViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)

        setContentView(binding.root)
        with(binding){
            resultImage.setImageURI(intent.getStringExtra("IMAGE")?.toUri())
            resultText.text = "Category : ${intent.getStringExtra("LABEL")} \n" +
                    "Confidence Score : ${intent.getStringExtra("SCORE")}"
        }
        setupRV()

        with(newsViewModel){
            newsList.observe(this@ResultActivity){
                it?.let {
                    insertListNewsData(it)
                }
            }
            loadingNewsData.observe(this@ResultActivity){
                loaderIndicator(it)
            }
            resultError.observe(this@ResultActivity) {
                showToast(it)
            }
        }

    }
    private fun loaderIndicator(isLoading: Boolean) {
        binding.loaderListNews.visibility = if (isLoading)View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun setupRV() {
        binding.rvListNews.apply {
            val layoutManager = LinearLayoutManager(this@ResultActivity)
            this.layoutManager = layoutManager
            val itemDecor = DividerItemDecoration(this@ResultActivity, layoutManager.orientation)
            addItemDecoration(itemDecor)
        }
    }
    private fun insertListNewsData(data: List<ArticlesItem> ) {
        val adapter = NewsAdapter()
        adapter.submitList(data)
        binding.rvListNews.adapter = adapter
    }

}