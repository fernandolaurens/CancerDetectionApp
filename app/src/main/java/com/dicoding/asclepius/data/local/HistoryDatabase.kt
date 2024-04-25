package com.dicoding.asclepius.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false )
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun historyDao() : HistoryDao


    //SINGLE TON
    companion object {
        @Volatile
            private var instance: HistoryDatabase? = null
            fun getInstanceOfDatabaseAnalyzeHistory(context: Context): HistoryDatabase =
                instance ?: synchronized(this) {
                    instance ?: Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        "HistoryAnalyze.db"
                    ).build()
                }
    }

}