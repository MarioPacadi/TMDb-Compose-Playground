package com.android.sample.tmdb.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.sample.tmdb.ui.DetailScreens.Companion.TMDb_ITEM
import com.android.sample.tmdb.ui.detail.DetailScreenContent
import com.android.sample.tmdb.ui.feed.FeedMovieScreen
import com.android.sample.tmdb.ui.theme.TmdbPagingComposeTheme
import com.android.sample.tmdb.utils.TMDbItemNavType
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TmdbPagingComposeTheme {
                HomeScreen()
            }
        }
    }

    @Composable
    private fun HomeScreen(navController: NavHostController = rememberNavController()) {
        Scaffold(
            bottomBar = { BottomBar(navController = navController) }
        ) {
            NavigationGraph(navController = navController, it.calculateBottomPadding())
        }
    }

    @Composable
    private fun BottomBar(navController: NavHostController) {
        val screens = listOf(
            BottomNavScreens.MovieNavItem,
            BottomNavScreens.TVShowNavItem
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val bottomBarDestination = screens.any { it.route == currentDestination?.route }
        if (bottomBarDestination) {
            BottomNavigation {
                screens.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                }
            }
        }
    }

    @Composable
    private fun RowScope.AddItem(
        screen: BottomNavScreens,
        currentDestination: NavDestination?,
        navController: NavHostController
    ) {
        BottomNavigationItem(
            label = {
                Text(text = screen.title)
            },
            icon = {
                Icon(
                    painterResource(id = screen.icon),
                    contentDescription = screen.title
                )
            },
            selected = currentDestination?.hierarchy?.any {
                it.route == screen.route
            } == true,
            unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
            onClick = {
                navController.navigate(screen.route) {
                    navController.graph.startDestinationRoute?.let { screen_route ->
                        popUpTo(screen_route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }

    @Composable
    private fun NavigationGraph(navController: NavHostController, bottomPadding: Dp) {
        NavHost(
            navController = navController,
            startDestination = BottomNavScreens.MovieNavItem.route,
            Modifier.padding(bottom = bottomPadding)
        ) {
            composable(route = BottomNavScreens.MovieNavItem.route) {
                FeedMovieScreen( onClick = {
                    val json = Uri.encode(Gson().toJson(it))
                    navController.navigate(
                        DetailScreens.MovieDetails.route.replace(
                            "{${TMDb_ITEM}}",
                            json
                        )
                    )
                })
            }
            movieDetailsNavGraph()
        }
    }

    private fun NavGraphBuilder.movieDetailsNavGraph() {
        composable(route = DetailScreens.MovieDetails.route, arguments = listOf(
            navArgument(TMDb_ITEM) {
                type = TMDbItemNavType()
            }
        )) {
            DetailScreenContent()
        }
    }
}