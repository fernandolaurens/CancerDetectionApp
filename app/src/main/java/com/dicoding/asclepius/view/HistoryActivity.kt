package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.view_model_factory.HistoryViewModelFactory
import com.dicoding.asclepius.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val historyViewModel : HistoryViewModel by viewModels {
        HistoryViewModelFactory.getInstanceHistoryViewModelFactoryForAnalyze(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)

        setContentView(binding.root)
        historyViewModel.getAllAnalyzeHistory().observe(this@HistoryActivity){
            with(binding){
                val layoutManager = LinearLayoutManager(this@HistoryActivity)
                rvListHistory.layoutManager = layoutManager
                val adapter = HistoryAdapter(it,this@HistoryActivity)
                rvListHistory.adapter = adapter
            }
        }
    }
}