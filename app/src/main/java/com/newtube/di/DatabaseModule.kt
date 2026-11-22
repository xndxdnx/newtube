package com.newtube.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.newtube.NewTubeApp
import com.newtube.data.local.Database
import com.newtube.data.local.NewTubeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(
    SingletonComponent::class
)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): Database {
        return Room.databaseBuilder(
            context = context,
            name = "newtube_table",
            klass = Database::class.java
        ).build()

    }

    @Provides
    @Singleton
    fun provideDao(
        db: Database
    ): NewTubeDao {
        return db.dao()
    }


}