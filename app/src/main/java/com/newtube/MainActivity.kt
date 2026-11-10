package com.newtube

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.newtube.navigation.AppNav
import com.newtube.ui.theme.NewtubeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            )
        )
        setContent {
            NewtubeTheme {


                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AppNav()
                }

            }
        }
    }
}


//@Composable
//fun Placeholder () {
//    var visible by remember { mutableStateOf(false) }
//
//    LaunchedEffect(true) {
//
//        while (true) {
//            visible = true
//            delay(1000.milliseconds)
//            visible = false
//            delay(1000.milliseconds)
//        }
//    }
//
//    AnimatedVisibility(
//        visible = visible,
//        enter = fadeIn(
//            tween(delayMillis = 400)
//        ),
//        exit = fadeOut(
//            tween(durationMillis = 400)
//        )
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = "HELLO ANDROID",
//                fontSize = 44.sp,
//                fontWeight = FontWeight.ExtraBold,
//                color = MaterialTheme.colorScheme.error
//
//            )
//        }
//
//    }
//}
