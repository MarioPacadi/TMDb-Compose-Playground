package com.sample.tmdb.feature_webview

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

@Composable
fun WebViewScreen(url: String,modifier: Modifier=Modifier) {

    val context = LocalContext.current
    val geckoRuntime = GeckoRuntimeHolder.getOrCreate(context)

    val geckoSession = remember { GeckoSession().apply { open(geckoRuntime) } }

    val geckoView = remember {
        GeckoView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setSession(geckoSession)
        }
    }

    DisposableEffect(Unit) {
        // Load the URL into the GeckoSession
        geckoSession.loadUri(url)

        onDispose {
            // Cleanup when the composable is disposed
            geckoSession.close() // Close session to release resources
        }
    }


    AndroidView(
        factory = { factory_context ->
            geckoView
        },
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
fun DemoPlayer(navController: NavController,movieId: Int,modifier: Modifier=Modifier) {
    BackHandler {
//        WebViewSettings.closeSession()
        navController.popBackStack() // Navigate back to the previous screen
    }

    SetHorizontalOrientation()

    val visibility = remember { mutableStateOf(true) }

    AnimatedVisibility(
        modifier = modifier.fillMaxSize(),
        visible = visibility.value,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        Box(
            modifier = modifier.fillMaxWidth().fillMaxHeight(0.5f),
            contentAlignment = Alignment.Center
        ) {
            WebViewScreen(url = "https://vidsrc.xyz/embed/movie?tmdb=${movieId}",modifier)
        }
    }
}

@Composable
fun SetHorizontalOrientation() {
    val activity = LocalContext.current as? Activity

    // Set orientation to landscape when this screen is displayed
    DisposableEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onDispose {
            // Restore the orientation when leaving the screen
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}