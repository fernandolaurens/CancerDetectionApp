package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.HistoryEntity
import com.dicoding.asclepius.repository.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: HistoryRepository): ViewModel() {
    fun getAllAnalyzeHistory() = historyRepository.getAllHistoryForEntity()

    fun insertAnalyzeHistory(historyEntity: HistoryEntity){
        viewModelScope.launch {
            historyRepository.insertIntoHistory(historyEntity)
        }
    }
}