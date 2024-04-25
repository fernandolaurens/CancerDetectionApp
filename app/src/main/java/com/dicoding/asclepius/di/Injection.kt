package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.local.HistoryDatabase
import com.dicoding.asclepius.repository.HistoryRepository

object Injection {
    fun getHistoryRepository(context: Context):HistoryRepository {
        val db = HistoryDatabase.getInstanceOfDatabaseAnalyzeHistory(context)
        val dao = db.historyDao()
        return HistoryRepository.getInstanceOfHistoryRepository(dao)
    }

}