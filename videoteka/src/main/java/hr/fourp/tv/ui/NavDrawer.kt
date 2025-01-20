package hr.fourp.tv.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.DrawerState
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import com.sample.tmdb.common.ui.theme.AlphaNavigationBar

@Composable
fun NavDrawer(
    tabs: Array<HomeSections>,
    currentRoute: String?,
    navigateToRoute: (String) -> Unit,
    content: @Composable () -> Unit,
) {
//    val currentSection = tabs.first { it.route == currentRoute }
    val currentSection = if (currentRoute == null) {
        // Select the first tab if currentRoute is null
        tabs.first()
    } else {
        // Otherwise, find the tab matching the currentRoute
        tabs.first { it.route == currentRoute }
    }

    NavigationDrawer(
        drawerState = DrawerState(DrawerValue.Closed),
        drawerContent = {
            Column(
                Modifier
                    .background(MaterialTheme.colors.background.copy(alpha = AlphaNavigationBar))
                    .fillMaxHeight()
                    .padding(20.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                tabs.forEach { section ->
                    val selected = section == currentSection

                    NavigationDrawerItem(
                        selected = selected,
                        onClick = { navigateToRoute(section.route) },
                        leadingContent = {
                            Icon(
                                imageVector = if (selected) section.selectedIcon else section.unselectedIcon,
                                contentDescription = stringResource(id = section.title),
                                tint = MaterialTheme.colors.primary,
                            )
                        },
                    ) {
                        Text(text = stringResource(id = section.title), color = MaterialTheme.colors.primary)
                    }
                }
            }
        },
    ) {
        content()
    }
}