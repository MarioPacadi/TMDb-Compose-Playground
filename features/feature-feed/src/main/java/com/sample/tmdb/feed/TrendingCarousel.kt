package com.sample.tmdb.feed

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import androidx.tv.material3.rememberCarouselState
import coil.compose.AsyncImage
import com.sample.tmdb.common.model.TMDbItem
import com.sample.tmdb.common.ui.Dimens.TMDb_12_dp
import com.sample.tmdb.common.ui.Dimens.TMDb_4_dp
import com.sample.tmdb.common.ui.Dimens.TMDb_6_dp
import com.sample.tmdb.domain.model.FeedWrapper

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TrendingCarousel(
    pagerState: PagerState, contentPadding: PaddingValues, item: FeedWrapper, onClick: (TMDbItem) -> Unit,
    modifier: Modifier=Modifier) {
    var carouselFocused by remember { mutableStateOf(false) }
    val carouselState = rememberCarouselState()
    val context=LocalContext.current
    Carousel(
        itemCount = pagerState.pageCount,
        carouselState = carouselState,
        modifier =
        Modifier
            .height(250.dp)
            .fillMaxWidth()
            .onFocusChanged {
                carouselFocused = it.isFocused
            },
        contentTransformEndToStart = fadeIn(tween(1000)).togetherWith(fadeOut(tween(1000))),
        contentTransformStartToEnd = fadeIn(tween(1000)).togetherWith(fadeOut(tween(1000))),
        carouselIndicator = {
            CarouselDefaults.IndicatorRow(
                itemCount = pagerState.pageCount,
                activeItemIndex = carouselState.activeItemIndex,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp))
        },
    ) { page ->
        with(item.feeds[page]) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(TMDb_4_dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(10.dp))
//                    .animateEnterExit(
//                        enter = slideInHorizontally(animationSpec = tween(1000)) { it / 2 },
//                        exit = slideOutHorizontally(animationSpec = tween(1000))
//                    )
                    .then(Modifier.clickable(onClick = {
                        onClick(this)
//                        Toast.makeText(context,"ImageUrl: $backdropUrl", Toast.LENGTH_LONG).show()
                    })),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = backdropUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                    )

                    Column(
                        modifier =
                        Modifier
                            .padding(
                                start = TMDb_12_dp,
                                bottom = TMDb_6_dp,
                            )
                            .align(Alignment.BottomStart),
                    ) {
                        Text(
                            text = name,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(TMDb_6_dp))
                        releaseDate?.let { releaseDate ->
                            Text(
                                text = releaseDate,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}