package hr.fourp.tv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.tv.material3.ExperimentalTvMaterial3Api

import com.sample.tmdb.common.ui.theme.AlphaNavigationBar
import com.sample.tmdb.common.ui.theme.AlphaNearOpaque
import com.sample.tmdb.common.ui.theme.TmdbPagingComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, true)

//        registerOnBackPress {
//
//        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        enableEdgeToEdge()
        setContent {
            TmdbPagingComposeTheme(darkTheme = true) {
                ChangeSystemBarsTheme(true)
                TMDbApp()
            }
        }
    }

    @Composable
    private fun ChangeSystemBarsTheme(darkTheme: Boolean) {
        val statusBarColor = MaterialTheme.colors.background.copy(alpha = AlphaNearOpaque).toArgb()
        val navigationBarColor = MaterialTheme.colors.background.copy(alpha = AlphaNavigationBar).toArgb()
        LaunchedEffect(darkTheme) {
            if (!darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(statusBarColor, statusBarColor),
                    navigationBarStyle = SystemBarStyle.light(navigationBarColor, navigationBarColor),
                )
            } else {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(statusBarColor),
                    navigationBarStyle = SystemBarStyle.dark(navigationBarColor),
                )
            }
        }
    }
}
