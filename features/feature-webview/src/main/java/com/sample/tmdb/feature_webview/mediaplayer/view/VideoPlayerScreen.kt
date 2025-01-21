package com.sample.tmdb.feature_webview.mediaplayer.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.ui.PlayerView
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.sample.tmdb.feature_webview.mediaplayer.viewmodel.VideoPlayerViewModel

@Composable
fun VideoPlayerScreen(
    url: String,
    viewModel: VideoPlayerViewModel = hiltViewModel(),
    modifier: Modifier=Modifier
) {
//    val context = LocalContext.current
    val exoPlayer = remember { viewModel.getExoPlayer() } // Use singleton from ViewModel
    val isPlaying = viewModel.isPlaying.collectAsState()

    DisposableEffect(Unit) {
        // Play video
        viewModel.playVideo(url)

        // Clean up when leaving the screen
        onDispose {
            viewModel.stopVideo()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = modifier.fillMaxSize()
    )

    if (!isPlaying.value) {
        Text("Loading...", style = MaterialTheme.typography.bodyMedium)
    }
}
