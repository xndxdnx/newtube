package com.newtube.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "videos",
    indices = [Index("ownerLogin"), Index("isShort")]
)
data class VideoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val ownerLogin: String,
    val ownerName: String,
    val filePath: String,
    val coverPath: String,
    val durationMs: Long,
    val width: Int,
    val height: Int,
    val isShort: Boolean,
    val views: Long,
    val createdAt: Long,
)