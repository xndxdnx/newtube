package com.newtube.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

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