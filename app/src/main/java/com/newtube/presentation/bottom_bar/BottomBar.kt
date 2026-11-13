package com.newtube.presentation.bottom_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.newtube.navigation.Routes
import com.newtube.navigation.Tab
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds


val bottomBarItems = listOf<Tab>(
    Tab(route = Routes.FEED, title = "Главная", icon = Icons.Filled.Home),
    Tab(route = Routes.SHORTS, title = "Шорты", icon = Icons.Filled.PlayArrow),
    Tab(route = Routes.RECORD, title = "Создать", icon = Icons.Filled.AddCircle),
    Tab(route = Routes.PROFILE, title = "Профиль", icon = Icons.Filled.AccountCircle)
)

@Composable
fun BottomBar(
    navHostController: NavHostController,
    current: String?
) {


    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {

        bottomBarItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == current,
                onClick = {
                    navHostController.navigate(item.route) {
                        popUpTo(route = Routes.FEED) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }

    }


}

@Composable
fun Placeholder(
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(true) {

            while (true) {
                visible = true
                delay(1000.milliseconds)
                visible = false
                delay(1000.milliseconds)
            }
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = title,
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.error

            )

        }
    }
}