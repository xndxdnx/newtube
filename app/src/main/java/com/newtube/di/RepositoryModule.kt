package com.newtube.di

import com.newtube.data.repository.VideoRepository
import com.newtube.data.repository.VideoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(
    SingletonComponent::class
)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsRepository(repository: VideoRepositoryImpl): VideoRepository

}