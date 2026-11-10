package com.newtube.navigation

import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.vector.ImageVector

data class Tab (
    val icon: ImageVector,
    val title: String,
    val route: String
)