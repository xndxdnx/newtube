package com.newtube.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp


@Composable
fun Initial(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp
) {

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.trim().take(1).uppercase(),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = (size.value * 0.42f).sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}