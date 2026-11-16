package com.newtube.data.library

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.newtube.data.local.NewTubeDao
import com.newtube.data.local.entity.VideoEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject


//

data class Owner(
    val login: String,
    val name: String
)


class VideoLibrary @Inject constructor(
    private val dao: NewTubeDao,
    @ApplicationContext
    private val context: Context
) {
    val videosDir: File by lazy {
        File(context.filesDir, "library/videos").apply { mkdirs() }
    }

    val coversDir: File by lazy {
        File(context.filesDir, "library/covers").apply { mkdirs() }
    }

    fun newVideoFile(
        id: String = UUID.randomUUID().toString()
    ): File = File(videosDir, "$id.mp4")

    suspend fun copyIntoLibrary(
        source: Uri,
        id: String
    ): File = withContext(Dispatchers.IO) {

        val target = newVideoFile(id = id)

        context.contentResolver.openInputStream(source).use { input ->
            requireNotNull(input) { "Не удалось открыть файл" }
            FileOutputStream(target).use { output ->
                input.copyTo(output)
            }
            target
        }
    }

    suspend fun register(
        owner: Owner,
        id: String,
        file: File,
        title: String,
        description: String,
        isShortOverride: Boolean? = null
    ) : VideoEntity {

        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(file.absolutePath)

            val duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
                    ?: 0L

            var width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                ?.toIntOrNull() ?: 0

            var height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                ?.toIntOrNull() ?: 0

            val rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
                ?.toIntOrNull() ?: 0

            if (rotation == 90 || rotation == 270) {
                val tmp = width
                width = height
                height = tmp
            }


            val cover = File(coversDir, "$id.jpg")

            val frame = retriever.getFrameAtTime(
                (duration * 1000L / 3).coerceAtLeast(0L),
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )

            if (frame != null) {
                FileOutputStream(cover).use {
                    frame.compress(
                        Bitmap.CompressFormat.JPEG,
                        88,
                        it
                    )
                }
                frame.recycle()
            }

            val entity = VideoEntity(
                id = id,
                title = title.ifBlank { "Без названия" },
                description = description,
                ownerLogin = owner.login,
                ownerName = owner.name,
                filePath = file.absolutePath,
                coverPath = if (frame != null) cover.absolutePath else "",
                durationMs = duration,
                width = width,
                height = height,
                isShort = isShortOverride ?: (height > width),
                views = 0,
                createdAt = System.currentTimeMillis(),
            )

            dao.insert(entity)

            return entity

        }finally {
            retriever.release()
        }

    }

    suspend fun copyImage(
        source: Uri,
        target: File
    ) : String  = withContext(Dispatchers.IO) {
        target.parentFile?.mkdirs()
        context.contentResolver.openInputStream(source).use { inputStream ->
            requireNotNull(inputStream) {"Не удалось открыть картинку"}
            FileOutputStream(target).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        target.absolutePath
    }

    fun coverFile(id: String) : File = File(coversDir, "${id}_id_${System.currentTimeMillis()}.jpg")

    fun avatarFile(login: String) : File = File(context.filesDir, "library/avatars").apply { mkdirs() }.let {
        File(it, "${login}_${System.currentTimeMillis()}.jpg")
    }




}