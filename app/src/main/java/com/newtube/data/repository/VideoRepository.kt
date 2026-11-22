package com.newtube.data.repository

import android.net.Uri
import androidx.paging.PagingData
import com.newtube.data.library.Owner
import com.newtube.data.video.OwnerTotals
import com.newtube.data.video.Reaction
import com.newtube.domain.model.Comment
import com.newtube.domain.model.Video
import com.newtube.domain.model.VideoStats
import kotlinx.coroutines.flow.Flow
import java.io.File

interface VideoRepository {

    fun longVideos(): Flow<PagingData<Video>>

    fun shorts(): Flow<PagingData<Video>>

    fun observe(id: String): Flow<Video>?

    fun related(excludeId: String): Flow<List<Video>>

    fun ownedBy(login: String): Flow<List<Video>>

    fun savedBy(login: String): Flow<List<Video>>

    fun stats(id: String, login: String): Flow<VideoStats>

    suspend fun react(id: String, reaction: Reaction, login: String)

    fun comments(videoId: String): Flow<List<Comment>>

    suspend fun addComment(id: String, owner: Owner, text: String)

    suspend fun recordingView(id: String)

    suspend fun upload(
        source: Uri,
        owner: Owner,
        title: String,
        description: String,
        isShort: Boolean
    )

    suspend fun publishExport(
        id: String,
        file: File,
        owner: Owner,
        title: String,
        description: String,
        isShort: Boolean
    )

    suspend fun updateVideo(
        id: String,
        title: String,
        description: String,
        coverPath: String
    )

    suspend fun frameChooses(
        target: Video
    ): List<String>


    suspend fun saveCustomCover(
        source: Uri,
        videoId: String
    )

    suspend fun renameAccount(
        newLogin: String,
        oldLogin: String,
        name: String
    )

    fun ownerTotals(
        login: String
    ) : Flow<OwnerTotals>




}