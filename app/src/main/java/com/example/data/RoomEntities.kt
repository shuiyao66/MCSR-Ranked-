package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val visitedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "web_tabs")
data class WebTabEntity(
    @PrimaryKey val tabId: String,
    val title: String,
    val url: String,
    val isActive: Boolean = false,
    val orderIndex: Int = 0
)

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val url: String,
    val filePath: String,
    val fileSize: Long,
    val status: String, // COMPLETED, DOWNLOADING, FAILED
    val timestamp: Long = System.currentTimeMillis()
)
