package com.sample.tmdb.feature_webview.mediaplayer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.sample.tmdb.feature_webview.mediaplayer.models.VideoPlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val playerController: VideoPlayerController
) : ViewModel() {

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun getExoPlayer(): ExoPlayer {
        return playerController.getExoPlayer()
    }

    fun playVideo(url: String) {
        playerController.playVideo(url)
        _isPlaying.value = true
    }

    fun playVideo(exoPlayer: ExoPlayer, url: String) {
        playerController.playVideo(exoPlayer, url)
    }

    fun stopVideo() {
        playerController.releasePlayer()
        _isPlaying.value = false
    }

    override fun onCleared() {
        super.onCleared()
        playerController.releasePlayer()
    }
}
