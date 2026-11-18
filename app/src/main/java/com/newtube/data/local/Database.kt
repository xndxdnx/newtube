package com.newtube.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.newtube.data.local.entity.CommentEntity
import com.newtube.data.local.entity.ReactionEntity
import com.newtube.data.local.entity.VideoEntity

@Database(
    entities = [
        VideoEntity::class,
        CommentEntity::class,
        ReactionEntity::class
    ],
    version = 1
)
abstract class Database() : RoomDatabase() {
    abstract fun dao () : NewTubeDao
}