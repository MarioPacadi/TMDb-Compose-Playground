package hr.fourp.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import com.sample.tmdb.common.ui.theme.AlphaNavigationBar

@Composable
fun NavDrawer(tabs: Array<HomeSections>, currentRoute: String?, navigateToRoute: (String) -> Unit, content: @Composable () -> Unit){
//    val currentSection = tabs.first { it.route == currentRoute }
    val currentSection = if (currentRoute == null) {
        // Select the first tab if currentRoute is null
        tabs.first()
    } else {
        // Otherwise, find the tab matching the currentRoute
        tabs.first { it.route == currentRoute }
    }

    NavigationDrawer(
        drawerContent = {
            Column(
                Modifier.background(MaterialTheme.colors.background.copy(alpha = AlphaNavigationBar)).fillMaxHeight().padding(12.dp).selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
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
                            )
                        }
                    ) {
                        Text(text = stringResource(id = section.title))
                    }
                }
            }
        }
    ) {
        content()
    }
}