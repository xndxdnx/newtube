package com.newtube.presentation.feed

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.newtube.domain.model.Video
import com.newtube.ui.components.DurationBadge
import com.newtube.ui.components.Initial
import com.newtube.ui.components.VideoCover
import java.util.Locale

@Composable
fun FeedScreen(
    onVideoClick: (String) -> Unit = {},
    viewModel: FeedViewModel = hiltViewModel()
) {

    val videos = viewModel.videos.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text(
            text = "NEW_TUBE",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp)
        )

        FeedList(
            videos = videos,
            onVideoClick = onVideoClick
        )


    }


}

@Composable
private fun FeedList(
    videos: LazyPagingItems<Video>,
    onVideoClick: (String) -> Unit = {}
) {

    val refresh = videos.loadState.refresh

    when {

        refresh is LoadState.Loading && videos.itemCount == 0 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        videos.itemCount == 0 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "Видео пока нет. Загрузите свой ролик во вкладке «Профиль» или снимите его во вкладке «Создать».",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (refresh is LoadState.Loading) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }

                items(videos.itemCount, key = videos.itemKey { key -> key.id }) { index ->
                    videos[index]?.let { video ->
                        VideoCard(
                            video = video,
                            onClick = { onVideoClick(video.id) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun VideoCard(
    video: Video,
    onClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        VideoCover(
            coverPath = video.coverPath,
            rounded = false,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
        ) {
            DurationBadge(
                durationMillis = video.durationMs,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp)

        ) {
            Initial(
                size = 36.dp,
                name = video.ownerName
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = video.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${video.ownerName} · ${formatCount(video.views)} просмотров · ${
                        formatRelative(
                            video.createdAt
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

        }

    }

}

// 1 234 -> "1,2 тыс.". */
fun formatCount(value: Long): String = when {
    value >= 1_000_000 -> String.format(Locale.US, "%.1f млн", value / 1_000_000f)
    value >= 1_000 -> String.format(Locale.US, "%.1f тыс.", value / 1_000f)
    else -> value.toString()
}

// "3 дня назад", "2 часа назад" and so on, in the device language. */
fun formatRelative(timeMs: Long): String {
    val diff = System.currentTimeMillis() - timeMs
    if (diff < DateUtils.MINUTE_IN_MILLIS) return "только что"
    return DateUtils.getRelativeTimeSpanString(timeMs, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString()
}
