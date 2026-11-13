package com.newtube.data.mapper

import com.newtube.data.local.CommentEntity
import com.newtube.data.local.VideoEntity
import com.newtube.domain.model.Comment
import com.newtube.domain.model.Video

fun VideoEntity.toDomain(): Video {
    return Video(
        id = id,
        title = title,
        description = description,
        ownerLogin = ownerLogin,
        ownerName = ownerName,
        filePath = filePath,
        coverPath = coverPath,
        durationMs = durationMs,
        width = width,
        height = height,
        isShort = isShort,
        views = views,
        createdAt = createdAt,
    )
}

fun CommentEntity.toDomain(): Comment {
    return Comment(
        id = id,
        videoId = videoId,
        authorLogin = authorLogin,
        authorName = authorName,
        text = text,
        createdAt = createdAt,
        )
}