package com.newtube.domain.model

data class Video(
    val id: String,
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


data class VideoStats(
    val likes: Int = 0,
    val dislikes: Int = 0,
    val comments: Int = 0,
    val liked: Boolean = false,
    val disliked: Boolean = false,
    val saved: Boolean = false,
)

data class Comment(
    val id: String,
    val videoId: String,
    val authorLogin: String,
    val authorName: String,
    val text: String,
    val createdAt: Long,
)