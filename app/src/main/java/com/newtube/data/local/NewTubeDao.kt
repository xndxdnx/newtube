package com.newtube.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface NewTubeDao {

    // получение видео с пагинацией (paging 3)
    @Query("SELECT * FROM videos WHERE isShort = 0 ORDER BY createdAt DESC")
    suspend fun longVideoPaged(): PagingSource<Int, VideoEntity>

    @Query("SELECT * FROM videos WHERE isShort = 1 ORDER BY createdAt DESC")
    suspend fun shortVideoPaged(): PagingSource<Int, VideoEntity>

    // получение отдельных списков и видео

    @Query("SELECT * FROM videos WHERE id = :id")
    fun observeById(id: String): Flow<VideoEntity>?

    @Query("SELECT * FROM videos WHERE isShort = 0 AND id != :excludeId ORDER BY createdAt DESC LIMIT :limit")
    fun observeRelated(excludeId: String, limit: Int): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE ownerLogin = :login ORDER BY createdAt DESC")
    fun observeByOwner(login: String): Flow<List<VideoEntity>>

    @Query(
        """
        SELECT v.* FROM videos AS v
        INNER JOIN reactions AS r ON r.videoId = v.id
        WHERE r.login = :login AND r.saved = 1
        ORDER BY v.createdAt DESC
        """
    )
    fun observeSaved(login: String): Flow<List<VideoEntity>>

    @Query("SELECT COUNT(*) FROM videos")
    suspend fun countAll(): Int

    @Query("UPDATE videos SET views = views + 1 WHERE id =:id")
    suspend fun incrementViewers(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: VideoEntity)

    // comments

    @Query("SELECT * FROM comments WHERE id =:id ORDER BY createdAt DESC")
    fun observeComments(id: String): Flow<List<CommentEntity>>

    @Query("SELECT COUNT(*) FROM comments WHERE id = :id")
    fun observeCommentsCount(id: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)


    //edit videos and getting statistics

    @Query(
        """
        UPDATE videos SET
        title = :title,
        description = :description,
        coverPath = :coverPath
        WHERE id =:id
    """
    )
    fun updateMeta(
        id: String,
        title: String,
        description: String,
        coverPath: String
    )

    @Query("SELECT COALESCE(SUM (views),0) FROM videos WHERE ownerLogin = :login")
    fun observeViewersFor(login: String): Flow<Long>

    @Query(
        """
        SELECT COUNT(*) FROM reactions AS r
        INNER JOIN videos AS v ON r.videoId = v.id
        WHERE ownerLogin = :login and liked = 1
    """
    )
    fun observeLikesFor(login: String): Flow<Long>


    @Query(
        """
        SELECT COUNT(*) FROM comments AS c
        INNER JOIN videos AS v ON c.videoId = v.id
        WHERE ownerLogin = :login
    """
    )
    fun observeCommentsFor(login: String): Flow<Long>

    @Query(
        """
        UPDATE videos SET
        ownerName = :name,
        ownerLogin = :newLogin
        WHERE ownerLogin =:oldLogin
    """
    )
    suspend fun renameOwner(
        oldLogin: String,
        newLogin: String,
        name: String
    )

    @Query(
        """
        UPDATE comments SET
        authorName = :name,
        authorLogin = :newLogin
        WHERE authorLogin =:oldLogin
    """
    )
    suspend fun renameCommentAuthor(
        oldLogin: String,
        newLogin: String,
        name: String
    )


    @Query(
        """
        UPDATE reactions SET
        login = :newLogin
        WHERE login =:oldLogin
    """
    )
    suspend fun renameReactions(
        oldLogin: String,
        newLogin: String,
    )


    // total editing profile data white editing login or name

    @Transaction
    suspend fun renameAccount(
        oldLogin: String,
        newLogin: String,
        name: String
    ) {
        renameOwner(
            oldLogin = oldLogin,
            newLogin = newLogin,
            name= name
        )
        renameCommentAuthor(
            oldLogin = oldLogin,
            newLogin = newLogin,
            name= name
        )
        renameReactions(
            oldLogin = oldLogin,
            newLogin = newLogin,
        )

    }





}