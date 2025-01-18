package hr.fourp.tv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme

import com.sample.tmdb.common.ui.theme.AlphaNavigationBar
import com.sample.tmdb.common.ui.theme.AlphaNearOpaque
import com.sample.tmdb.common.ui.theme.TmdbPagingComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

//        registerOnBackPress {
//
//        }

        enableEdgeToEdge()
        setContent {
            TmdbPagingComposeTheme {
                ChangeSystemBarsTheme(!isSystemInDarkTheme())
                TMDbApp()
            }
        }
    }

    @Composable
    private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
        val statusBarColor = MaterialTheme.colorScheme.background.copy(alpha = AlphaNearOpaque).toArgb()
        val navigationBarColor = MaterialTheme.colorScheme.background.copy(alpha = AlphaNavigationBar).toArgb()
        LaunchedEffect(lightTheme) {
            if (lightTheme) {
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
