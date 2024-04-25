package com.dicoding.asclepius.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.HistoryDao
import com.dicoding.asclepius.data.local.HistoryEntity


class HistoryRepository private constructor(private val historyDao: HistoryDao) {
    fun getAllHistoryForEntity(): LiveData<List<HistoryEntity>> {
        return historyDao.getAllHistoryForEntity()
    }
    suspend fun insertIntoHistory(historyEntity: HistoryEntity) {
        return historyDao.insertHistoryForAnalyze(historyEntity)
    }

    companion object {
        @Volatile
        private var instance: HistoryRepository? = null

        fun getInstanceOfHistoryRepository(
            historyDaoForAnalyze: HistoryDao
        ): HistoryRepository = instance ?: synchronized(this) {
            instance ?: HistoryRepository(historyDaoForAnalyze).also { instance = it }
        }
    }
}
