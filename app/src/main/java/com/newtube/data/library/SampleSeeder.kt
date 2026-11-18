package com.newtube.data.library

import android.content.Context
import com.newtube.data.local.NewTubeDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// отвечает за автоматическое заполнение видеотеки при запуске нашего приложения когда нету данных

@Singleton
class SampleSeeder @Inject constructor(
    @ApplicationContext
    val context: Context,
    val dao : NewTubeDao,
    val videoLibrary: VideoLibrary
){

    val owner = Owner(
        login = SAMPLE_LOGIN,
        name = "media_studio"
    )


    suspend fun seedIfEmpty() = withContext(Dispatchers.IO) {
        if (dao.countAll() > 0) {
            return@withContext
        }

        val names = context.assets.list("samples").orEmpty().filter { names ->
            names.endsWith(".mp4")
        }.sorted()

        names.forEach { asset ->

            val base = asset.removeSuffix(".mp4")

            val id = "sample_$base"

            val target = videoLibrary.newVideoFile(id = id)

            context.assets.open("samples/$asset").use { inputStream ->
                target.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            val (title, description) = SAMPLE_TEXT[base] ?: base to "Test video"

            videoLibrary.register(
                id = id,
                file = target,
                title = title,
                description = description,
                owner = owner
            )

        }

    }
    companion object {
        const val SAMPLE_LOGIN = "media_studio"

        private val SAMPLE_TEXT = mapOf(
            "clip_bridge" to ("Мост на закате" to "Пример горизонтального видео для занятия по Media3."),
            "clip_city" to ("Ночной город" to "Пример длинного ролика с плавным движением."),
            "clip_ocean" to ("Океан и ветер" to "Пример ролика, на котором удобно тестировать перемотку."),
            "clip_forest" to ("Лесная тропа" to "Короткий ролик для проверки скорости воспроизведения."),
            "clip_desert" to ("Песчаные дюны" to "Ролик для проверки кнопок и сохранения."),
            "clip_night" to ("Звёзды над полем" to "Ролик для проверки двойного тапа."),
            "short_neon" to ("Неон" to "Вертикальный ролик для раздела «Шорты»."),
            "short_sunrise" to ("Рассвет" to "Вертикальный ролик для раздела «Шорты»."),
            "short_mint" to ("Мятный градиент" to "Вертикальный ролик для раздела «Шорты»."),
        )
    }
}



