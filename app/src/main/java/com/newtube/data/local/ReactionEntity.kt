package com.newtube.data.local

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "reactions",
    primaryKeys = ["videoId", "login"],
    indices = [Index("login")]
)
data class ReactionEntity(
    val videoId: String,
    val login: String,
    val liked: Boolean,
    val disliked: Boolean,
    val saved: Boolean,
)