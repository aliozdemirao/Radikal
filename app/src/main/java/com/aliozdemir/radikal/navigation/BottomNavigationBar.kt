package com.aliozdemir.radikal.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.aliozdemir.radikal.R
import com.aliozdemir.radikal.navigation.Screen.Bookmark
import com.aliozdemir.radikal.navigation.Screen.Home
import com.aliozdemir.radikal.navigation.Screen.Search

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.ic_home, Home),
        BottomNavItem("Search", R.drawable.ic_search, Search),
        BottomNavItem("Bookmark", R.drawable.ic_bookmark, Bookmark),
    )

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.route == item.route.routeName

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                    )
                },
                label = { Text(text = item.label) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )
        }
    }
}