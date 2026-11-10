package com.newtube.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.newtube.presentation.bottom_bar.BottomBar
import com.newtube.presentation.bottom_bar.Placeholder
import com.newtube.presentation.bottom_bar.bottomBarItems

@Composable
fun AppNav() {

    val navController = rememberNavController()

    val entry by navController.currentBackStackEntryAsState()

    val route = entry?.destination?.route

    val showBar = bottomBarItems.any { item ->
        item.route == route
    } && Routes.RECORD != route

    Scaffold(
        modifier = Modifier,
        bottomBar = {
            if (showBar) {
                BottomBar(
                    navHostController = navController,
                    current = route
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Routes.FEED,
            modifier = Modifier
                .padding(paddingValues)
        ) {
            composable (Routes.FEED){ Placeholder("FEED") }
            composable (Routes.RECORD){ Placeholder("RECORD") }
            composable (Routes.SHORTS){ Placeholder("SHORTS") }
            composable (Routes.PROFILE){ Placeholder("PROFILE") }
        }
    }


}