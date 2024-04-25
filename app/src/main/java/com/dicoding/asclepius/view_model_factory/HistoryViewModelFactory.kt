package com.dicoding.asclepius.view_model_factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.di.Injection
import com.dicoding.asclepius.repository.HistoryRepository
import com.dicoding.asclepius.viewmodel.HistoryViewModel

class HistoryViewModelFactory(private val historyRepository: HistoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(historyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HistoryViewModelFactory? = null

        fun getInstanceHistoryViewModelFactoryForAnalyze(context: Context): HistoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HistoryViewModelFactory(
                    Injection.getHistoryRepository(context)
                )
            }.also { instance = it }
    }
}