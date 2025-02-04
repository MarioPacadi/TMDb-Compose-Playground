package com.sample.tmdb.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private val LocalCustomFontSizes = staticCompositionLocalOf { FontSizes() }

object TmdbPagingComposeTheme {
    val fontSizes: FontSizes
        @Composable
        get() = LocalCustomFontSizes.current
}

@Immutable
data class FontSizes(val sp_11: TextUnit = 11.sp)

private val DarkColorPalette =
    darkColors(
        primary = AshGray,
        primaryVariant = DarkGray,
        secondary = Teal200,
        background= Color.Black
    )

private val LightColorPalette =
    lightColors(
        primary = GRAY,
        primaryVariant = DarkGray,
        secondary = Teal200,
        background = Color.Black
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
     */
    )

@Composable
fun TmdbPagingComposeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors =
        if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
