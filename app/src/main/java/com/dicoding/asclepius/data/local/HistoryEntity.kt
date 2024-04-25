package com.dicoding.asclepius.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "label")
    val label: String,

    @ColumnInfo(name = "confidence_score")
    val confidenceScore: String,

    @ColumnInfo(name = "image")
    val image: String,
    )