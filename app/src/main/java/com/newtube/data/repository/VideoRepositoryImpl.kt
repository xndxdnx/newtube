package com.newtube.data.repository

import android.net.Uri
import androidx.core.net.toFile
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.newtube.data.library.Owner
import com.newtube.data.library.VideoLibrary
import com.newtube.data.local.NewTubeDao
import com.newtube.data.local.entity.CommentEntity
import com.newtube.data.local.entity.ReactionEntity
import com.newtube.data.local.entity.VideoEntity
import com.newtube.data.mapper.toDomain
import com.newtube.data.video.OwnerTotals
import com.newtube.data.video.Reaction
import com.newtube.domain.model.Comment
import com.newtube.domain.model.Video
import com.newtube.domain.model.VideoStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import java.util.UUID
import javax.inject.Inject


class VideoRepositoryImpl @Inject constructor(
    private val dao: NewTubeDao,
    private val videoLibrary: VideoLibrary
) : VideoRepository {

    private fun pager(source: () -> PagingSource<Int, VideoEntity>) =
        Pager(
            config = PagingConfig(
                pageSize = 12,
                initialLoadSize = 24,
                prefetchDistance = 4,
                enablePlaceholders = false
            ),
            pagingSourceFactory = source,
        ).flow.map { data -> data.map { it.toDomain() } }


    override fun longVideos(): Flow<PagingData<Video>> = pager {
        dao.longVideoPaged()
    }

    override fun shorts(): Flow<PagingData<Video>> = pager {
        dao.shortVideoPaged()
    }

    override fun observe(id: String): Flow<Video>? {
        return dao.observeById(id = id)?.map { entity -> entity.toDomain() }
    }

    override fun related(excludeId: String): Flow<List<Video>> {
        return dao.observeRelated(excludeId = excludeId, limit = 20).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override fun ownedBy(login: String): Flow<List<Video>> {
        return dao.observeByOwner(login = login).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override fun savedBy(login: String): Flow<List<Video>> {
        return dao.observeSaved(login = login).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override fun stats(
        id: String,
        login: String
    ): Flow<VideoStats> = combine(
        dao.observeReaction(videoId = id, login = login),
        dao.observeCommentsCount(id = id),
        dao.observeLikeCount(videoId = id),
        dao.observeDislikeCount(videoId = id)
    ) { reactions, comments, likes, dislikes ->

        VideoStats(
            disliked = reactions?.liked == true,
            liked = reactions?.liked == true,
            saved = reactions?.saved == true,
            likes = likes,
            dislikes = dislikes,
            comments = comments
        )
    }

    override suspend fun react(
        id: String,
        reaction: Reaction,
        login: String
    ) {

        val current = dao.observeReaction(videoId = id, login = login).first() ?: ReactionEntity(
            videoId = id,
            login = login,
            liked = false,
            disliked = false,
            saved = false
        )

        val updated = when (reaction) {
            Reaction.LIKE -> current.copy(
                liked = !current.liked,
                disliked = false
            )

            Reaction.DISLIKE -> current.copy(
                liked = false,
                disliked = !current.disliked
            )

            Reaction.SAVE -> current.copy(
                saved = !current.saved
            )
        }
        dao.upsertReaction(reaction = updated)
    }

    override fun comments(videoId: String): Flow<List<Comment>> {
        return dao.observeComments(id = videoId).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun addComment(
        id: String,
        owner: Owner,
        text: String
    ) {

        val clean = text.trim()
        if (clean.isBlank()) {
            return
        }

        dao.insertComment(
            comment = CommentEntity(
                id = UUID.randomUUID().toString(),
                videoId = id,
                authorLogin = owner.login,
                authorName = owner.name,
                text = clean,
                createdAt = System.currentTimeMillis()
            )
        )

    }

    override suspend fun recordingView(id: String) {
        dao.incrementViewers(id = id)
    }

    override suspend fun upload(
        source: Uri,
        owner: Owner,
        title: String,
        description: String,
        isShort: Boolean
    ) {

        val id = UUID.randomUUID().toString()

        val file = videoLibrary.copyIntoLibrary(source = source, id = id)

        videoLibrary.register(
            owner = owner,
            file = file,
            id = id,
            title = title,
            description = description,
            isShortOverride = isShort
        )


    }

    override suspend fun publishExport(
        id: String,
        file: File,
        owner: Owner,
        title: String,
        description: String,
        isShort: Boolean
    ) {

        videoLibrary.register(
            owner = owner,
            file = file,
            id = id,
            title = title,
            description = description,
            isShortOverride = isShort
        )

    }

    override suspend fun updateVideo(
        id: String,
        title: String,
        description: String,
        coverPath: String
    ) {
        dao.updateMeta(
            id = id,
            title = title.ifBlank { "без названия" },
            description = description,
            coverPath = coverPath
        )
    }

    override suspend fun frameChooses(target: Video): List<String> {
        return videoLibrary.extractFrames(
            videoPath = target.filePath,
            id = target.id,
            duration = target.durationMs,
            )
    }

    override suspend fun saveCustomCover(source: Uri, videoId: String) {
        videoLibrary.copyImage(
            source = source,
            target = videoLibrary.coverFile(id = videoId)
        )
    }

    override suspend fun renameAccount(
        newLogin: String,
        oldLogin: String,
        name: String
    ) {
        dao.renameAccount(
            newLogin = newLogin,
            oldLogin = oldLogin,
            name = name
        )

    }

    override fun ownerTotals(login: String): Flow<OwnerTotals> = combine(
        dao.observeByOwner(login = login),
        dao.observeLikesFor(login = login),
        dao.observeCommentsFor(login = login),
        dao.observeViewersFor(login = login)
    ) { videos, likes, comments, viewers ->
        OwnerTotals(
            videos = videos.size,
            likes = likes,
            comments = comments,
            viewers = viewers
        )

    }
}