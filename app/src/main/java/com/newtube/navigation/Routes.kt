package com.newtube.navigation

object Routes {
    const val FEED = "feed"
    const val SHORTS = "shorts"
    const val RECORD = "record"
    const val PROFILE = "profile"
    const val EDITOR = "editor"
    const val PLAYER = "player/{videoId}"
    const val EDIT = "edit/{videoId}"
    fun player(videoId: String) = "player/$videoId"
    fun edit(videoId: String) = "edit/$videoId"
    
}