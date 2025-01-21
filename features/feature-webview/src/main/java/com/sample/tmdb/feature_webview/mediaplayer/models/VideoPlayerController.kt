package com.sample.tmdb.feature_webview.mediaplayer.models

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import javax.inject.Inject

class VideoPlayerController @Inject constructor(
    private val exoPlayer: ExoPlayer
) {
    fun getExoPlayer(): ExoPlayer = exoPlayer

    fun playVideo(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    fun playVideo(exoPlayer: ExoPlayer, url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
    }
}
