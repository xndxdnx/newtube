package com.newtube.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    indices = [Index("videoId")]
)
data class CommentEntity(
    @PrimaryKey
    val id: String,
    val videoId: String,
    val authorLogin: String,
    val authorName: String,
    val text: String,
    val createdAt: Long,
)