package com.newtube.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import java.io.File

@Composable
fun VideoCover (
    coverPath: String,
    modifier: Modifier = Modifier,
    rounded: Boolean = true,
    content : @Composable BoxScope.() -> Unit = {}
) {

    Box(
        modifier = modifier
            .then(
                if (rounded) Modifier.clip(RoundedCornerShape(12.dp)) else Modifier
            )
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (coverPath.isNotBlank()) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = File(coverPath),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        content(

        )
    }

}