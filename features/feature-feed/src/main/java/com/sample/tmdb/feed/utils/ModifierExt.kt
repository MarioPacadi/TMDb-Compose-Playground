package com.sample.tmdb.feed.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

fun Modifier.pagerTransition(pagerState: PagerState, page: Int) = graphicsLayer {
    val pageOffset = pagerState.calculatePageOffset(page)

    lerp(
        start = 0.85f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f),
    ).also { scale ->
        scaleX = scale
        scaleY = scale
    }

    alpha =
        lerp(
            start = 0.5f,
            stop = 1f,
            fraction = 1f - pageOffset.coerceIn(0f, 1f),
        )
}

@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.calculatePageOffset(page: Int) = ((currentPage - page) + currentPageOffsetFraction).absoluteValue


@Composable
fun Modifier.onFirstGainingVisibility(onGainingVisibility: () -> Unit): Modifier {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) { if (isVisible) onGainingVisibility() }

    return onPlaced { isVisible = true }
}

@Composable
fun Modifier.requestFocusOnFirstGainingVisibility(): Modifier {
    val focusRequester = remember { FocusRequester() }
    return focusRequester(focusRequester).onFirstGainingVisibility {
        focusRequester.requestFocus()
    }
}