package com.sample.tmdb.feature_webview.gecko

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.mozilla.geckoview.GeckoSession

@Composable
fun WebViewScreen(url: String,modifier: Modifier=Modifier) {

    val context = LocalContext.current
    val geckoRuntime = GeckoRuntimeHolder.getOrCreate(context)

    val geckoSession = remember { GeckoSession().apply { open(geckoRuntime) } }

    val geckoView = remember {
//        GeckoView(context).apply {
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//            setSession(geckoSession)
//        }
        GeckoViewWithVirtualCursor(context).apply {
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
        factory = { _ ->
            geckoView
        },
        modifier = modifier.fillMaxSize(),
    )
}